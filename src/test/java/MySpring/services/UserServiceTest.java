package MySpring.services;

import MySpring.entity.User;
import MySpring.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserMapper userMapper;
    @Mock
    BCryptPasswordEncoder encoder;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        when(encoder.encode("MyPassword")).thenReturn("MyEncodedPassword");
        userService.save("MyUser", "MyPassword");
        verify(userMapper).saveUser("MyUser", "MyEncodedPassword");
    }

    @Test
    public void testGetUserByUserName() {
        userService.getUserByUsername("MyUser");
        verify(userMapper).getUserByUsername("MyUser");
    }

    @Test
    public void testNoExistingUser() {
        Assertions.assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername("MyUser"));
    }

    @Test
    public void returnUserDetailsWhenUserFound() {
        when(userMapper.getUserByUsername("MyUser")).
                thenReturn(new User(123, "MyUser", "encodedPassword", null));
        UserDetails userDetails = userService.loadUserByUsername("MyUser");
        Assertions.assertEquals("MyUser", userDetails.getUsername());
    }


}