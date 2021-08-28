package MySpring.entity;

public class LoginResult extends Result<User> {
    private boolean isLogin;

    public static LoginResult success(boolean isLogin, String msg, User user) {
        return new LoginResult("ok", msg, user, isLogin);
    }

    public static LoginResult failure(String msg) {
        return new LoginResult("fail", msg, null, false);
    }

    private LoginResult(String status, String msg, User user, boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public boolean isIsLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
