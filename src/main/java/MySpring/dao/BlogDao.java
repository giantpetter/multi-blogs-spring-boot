package MySpring.dao;

import MySpring.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    private Map<String, Object> ofMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }

    public List<Blog> getBlogs(Integer userId, Integer page, Integer pageSize, boolean atIndex) {
        Map<String, Object> para = ofMap(
                "user_id", userId,
                "offset", (page - 1) * pageSize,
                "limit", pageSize,
                "atIndex", atIndex
        );
        return sqlSession.selectList("selectBlog", para);
    }

    public int countBlogs() {
        return sqlSession.selectOne("countBlog");
    }

    public Blog selectBlogById(Integer blogId) {
        Map<String, Object> param = ofMap("id", blogId);
        return sqlSession.selectOne("selectBlogByBlogId", param);
    }

    public Blog insertBlog(Blog newBlog) {
        sqlSession.insert("insertBlog", newBlog);
        return selectBlogById(newBlog.getId());
    }

    public Blog updateBlogByBlogId(Blog modifiedBlog) {
        sqlSession.update("updateBlog", modifiedBlog);
        return selectBlogById(modifiedBlog.getId());
    }

    public void deleteBlogById(Integer blogId) {
        sqlSession.delete("deleteBlog", blogId);
    }
}
