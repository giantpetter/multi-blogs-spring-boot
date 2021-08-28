package MySpring.entity;

import java.util.List;

public class BlogListResult extends Result<List<Blog>> {
    private Integer total;
    private Integer page;
    private Integer totalPage;

    public static BlogListResult success(String msg, Integer total, Integer page, Integer totalPage, List<Blog> blogs) {
        return new BlogListResult("ok", msg, blogs, total, page, totalPage);
    }

    public static BlogListResult failure(String msg) {
        return new BlogListResult("fail", msg, null, null, null, null);
    }

    private BlogListResult(String status, String msg, List<Blog> data, Integer total, Integer page, Integer totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }
}
