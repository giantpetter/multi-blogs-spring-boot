package MySpring.controller;

import MySpring.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private MockMvc mvc;
//    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new AuthController(userService, authenticationManager))
                .build();
//        session = new MockHttpSession();
    }

    @Test
    public void returnNotLoginByDefault() throws Exception {
        ResultActions resultActions = mvc.perform(get("/auth"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(result -> Assertions.assertTrue(result.getResponse()
                .getContentAsString().contains("用户没有登录")));

    }

    @Test
    public void testLogin() throws Exception {
        //未登录时返回未登录状态
        ResultActions resultActions = mvc.perform(get("/auth"));
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(result -> Assertions.assertTrue(result.getResponse()
                .getContentAsString().contains("用户没有登录")));
        //登录后返回User信息
        Map<String, String> userPassword = new HashMap<>();
        userPassword.put("username", "MyUser");
        userPassword.put("password", "MyPassword");

        Mockito.when(userService.loadUserByUsername("MyUser")).thenReturn(
                new User("MyUser", encoder.encode("MyPassword"), Collections.emptyList())
        );
        Mockito.when(userService.getUserByUsername("MyUser")).thenReturn(
                new MySpring.entity.User(123, "MyUser", encoder.encode("MyPassword"), null)
        );

        String json = new ObjectMapper().writeValueAsString(userPassword);
        ResultActions resultAct = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json));
        HttpSession session = resultAct.andReturn().getRequest().getSession();

        MvcResult mvcResult = resultAct.andReturn();
        mvcResult.getResponse().setCharacterEncoding("UTF-8");

        resultAct.andExpect(status().isOk())
                .andExpect(result -> Assertions
                        .assertTrue(result.getResponse()
                                .getContentAsString().contains("登录成功")));

        //检查/auth的登录在线状态
        ResultActions resultActTwo = mvc.perform(post("/auth").session((MockHttpSession) session));
        resultActTwo.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActTwo
                .andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse()
                            .getContentAsString().contains("MyUser"));
                });
    }

}