package MySpring.controller;

import MySpring.entity.LoginResult;
import MySpring.entity.User;
import MySpring.services.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import MySpring.entity.Result;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    UserService userService;
    AuthenticationManager authenticationManager;

    @Inject
    public AuthController(
            UserService userService,
            AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @RequestMapping("/auth")
    @ResponseBody
    public LoginResult isLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication == null ? null : authentication.getName());
        if (user == null) {
            return LoginResult.success(false, "用户没有登录", null);
        } else {
            return LoginResult.success(true, "用户在线", user);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public LoginResult register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (username == null || password == null) {
            return LoginResult.failure("用户名或密码为空");
        }
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("无效的用户名");
        }
        if (password.length() < 1 || password.length() > 15) {
            return LoginResult.failure("无效的密码");
        }
        userService.getUserByUsername(username);
        try {
            userService.save(username, password);
            login(usernameAndPassword);
            return LoginResult.success(true, "注册成功", userService.getUserByUsername(username));
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("用户已存在");
        }

    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        UserDetails userDetails = null;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("用户不存在");

        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
//            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            User loggedInUser = userService.getUserByUsername(username);
            return LoginResult.success(true, "登录成功", loggedInUser);
        } catch (BadCredentialsException e) {
            return LoginResult.failure("密码不正确");
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (username == null) {
            return LoginResult.failure("用户未登录");
        } else {
            SecurityContextHolder.clearContext();
            return LoginResult.success(true, "注销成功", null);
        }
    }


}
