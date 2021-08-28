package MySpring.controller;

import MySpring.Utils.AssertUtils;
import MySpring.entity.Blog;
import MySpring.entity.BlogListResult;
import MySpring.entity.BlogResult;
import MySpring.entity.User;
import MySpring.services.AuthService;
import MySpring.services.BlogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@RestController
public class BlogController {
    private final BlogService blogService;
    private final AuthService authService;

    @Inject
    public BlogController(BlogService blogService, AuthService authService) {
        this.blogService = blogService;
        this.authService = authService;
    }

    @GetMapping("/blog")
    @ResponseBody
    public BlogListResult getBlogs(@RequestParam(defaultValue = "1", name = "page") String page,
                                   @RequestParam(required = false, name = "userId") Integer userId,
                                   @RequestParam(required = false, name = "atIndex") boolean atIndex) {
        Integer DEFAULT_PAGE_SIZE = 10;
        return blogService.getBlogs(userId, Integer.parseInt(page), DEFAULT_PAGE_SIZE, atIndex);
    }

    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult getBlogByBlogId(@PathVariable Integer blogId) {
        return blogService.getBlogByBlogId(blogId);
    }

    @PostMapping(value = "/blog", consumes = "application/json")
    @ResponseBody
    public BlogResult insertBlog(@RequestBody Map<String, Object> param) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.insertBlog(fromParam(param, user)))
                    .orElse(BlogResult.fail("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.fail(e);
        }
    }


    @PatchMapping(value = "/blog/{blogId}", consumes = "application/json;charset=utf-8")
    @ResponseBody
    public BlogResult modifyBlogByBlogId(@PathVariable Integer blogId,
                                         @RequestBody Map<String, Object> param) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.updateBlogById(blogId, fromParam(param, user)))
                    .orElse(BlogResult.fail("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.fail(e);
        }
    }

    @DeleteMapping("/blog/{blogId}")
    @ResponseBody
    public BlogResult deleteBlogByBlogId(@PathVariable Integer blogId) {
        try {
            return authService.getCurrentUser()
                    .map(user -> blogService.deleteBlogById(blogId, user))
                    .orElse(BlogResult.fail("登录后才能操作"));
        } catch (IllegalArgumentException e) {
            return BlogResult.fail(e);
        }
    }

    private Blog fromParam(Map<String, Object> param, User user) {
        Blog blog = new Blog();
        String title = (String) param.get("title");
        String description = (String) param.get("description");
        String content = (String) param.get("content");
        boolean atIndex = param.containsKey("atIndex") && (boolean) param.get("atIndex");

        AssertUtils.assertTrue(StringUtils.isNotBlank(title) && title.length() < 100, "无效的标题");
        AssertUtils.assertTrue(StringUtils.isNotBlank(content) && content.length() < 1_0000, "无效的内容");
        if (StringUtils.isBlank(description)) {
            description = content.substring(0, Math.min(content.length(), 10)) + "...";
        }
        blog.setUser(user);
        blog.setContent(content);
        blog.setDescription(description);
        blog.setTitle(title);
        blog.setAtIndex(atIndex);
        return blog;
    }

}
