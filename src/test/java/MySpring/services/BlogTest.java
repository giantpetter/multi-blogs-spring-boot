package MySpring.services;

import MySpring.dao.BlogDao;
import MySpring.entity.BlogListResult;
import MySpring.entity.BlogResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BlogTest {
    @InjectMocks
    BlogService blogService;
    @Mock
    BlogDao blogDao;

    @Test
    public void getBlogFromDb() {
        blogService.getBlogs(null, 2, 10, false);
        Mockito.verify(blogDao).getBlogs(null, 2, 10, false);
    }

    @Test
    public void returnFailureWhenExceptionThrown() {
        Mockito.when(blogDao.getBlogs(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenThrow(new RuntimeException());
        BlogListResult result = blogService.getBlogs(null, 2, 10, false);
        Assertions.assertEquals("系统异常", result.getMsg());
        Assertions.assertEquals("fail", result.getStatus());

    }

}
