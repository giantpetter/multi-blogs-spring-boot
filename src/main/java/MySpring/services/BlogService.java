package MySpring.services;

import MySpring.dao.BlogDao;
import MySpring.entity.Blog;
import MySpring.entity.BlogListResult;
import MySpring.entity.BlogResult;
import MySpring.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class BlogService {
    private final BlogDao blogDao;

    @Inject
    public BlogService(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public BlogListResult getBlogs(Integer userId, Integer page, Integer pageSize, boolean atIndex) {
        try {
            List<Blog> blogs = blogDao.getBlogs(userId, page, pageSize, atIndex);
            int total = blogDao.countBlogs();
            int totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
            return BlogListResult.success("获取成功", total, page, totalPage, blogs);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogListResult.failure("系统异常");
        }
    }

    public BlogResult getBlogByBlogId(Integer blogId) {
        try {
            return BlogResult.success("获取成功", blogDao.selectBlogById(blogId));
        } catch (Exception e) {
            return BlogResult.fail(e);
        }
    }

    public BlogResult insertBlog(Blog newBlog) {
        try {
            return BlogResult.success("创建成功", blogDao.insertBlog(newBlog));
        } catch (Exception e) {
            return BlogResult.fail(e);
        }
    }

    public BlogResult updateBlogById(Integer blogId, Blog modifiedBlog) {
        Blog blog = blogDao.selectBlogById(blogId);
        if (blog == null) {
            return BlogResult.fail("博客不存在");
        }

        Integer currentUserId = modifiedBlog.getUserId();
        Integer ownerId = blog.getUserId();
        if (!ownerId.equals(currentUserId)) {
            return BlogResult.fail("无法修改别人的博客");
        }

        try {
            modifiedBlog.setId(blogId);
            Blog newBlog = blogDao.updateBlogByBlogId(modifiedBlog);
            return BlogResult.success("修改成功", newBlog);
        } catch (Exception e) {
            e.printStackTrace();
            return BlogResult.fail(e);
        }
    }

    public BlogResult deleteBlogById(Integer blogId, User currentUser) {
        Blog toBeDeletedBlog = blogDao.selectBlogById(blogId);
        if (toBeDeletedBlog == null) {
            return BlogResult.fail("博客不存在");
        }

        Integer BlogUserId = toBeDeletedBlog.getUserId();
        if (!BlogUserId.equals(currentUser.getId())) {
            return BlogResult.fail("无法删除别人的博客");
        }
        try {
            blogDao.deleteBlogById(blogId);
            return BlogResult.success("删除成功", null);
        } catch (Exception e) {
            return BlogResult.fail(e);
        }
    }
}
