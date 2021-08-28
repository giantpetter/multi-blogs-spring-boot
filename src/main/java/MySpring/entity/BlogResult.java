package MySpring.entity;

public class BlogResult extends Result<Blog> {


    public static BlogResult fail(String msg) {
        return new BlogResult("fail", msg, null);
    }


    public static BlogResult success(String msg, Blog blog) {
        return new BlogResult("ok", msg, blog);
    }

    private BlogResult(String status, String msg, Blog data) {
        super(status, msg, data);
    }


    public static BlogResult fail(Exception e) {
        return BlogResult.fail(e.getMessage());
    }
}
