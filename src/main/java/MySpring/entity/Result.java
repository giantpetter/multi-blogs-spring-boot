package MySpring.entity;

public class Result {
    String status;
    boolean isLogin;
    String msg;
    Object data;

    public static Result failure(String msg) {
        return new Result("fail", false, msg, null);
    }

    public static Result success(String msg, Object data) {
        return new Result("ok", true, msg, data);
    }

    public static Result success(boolean isLogin, String msg, Object data) {
        return new Result("ok", isLogin, msg, data);
    }

    private Result(String status, boolean isLogin, String msg, Object data) {
        this.status = status;
        this.isLogin = isLogin;
        this.msg = msg;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}