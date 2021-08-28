package MySpring.dao;


import MySpring.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username}")
    User getUserByUsername(@Param("username") String username);

    @Insert("insert into user(username, password, avatar, created_at, updated_at)" +
            "values(#{username},#{encoded_password}," +
            "'https://img0.baidu.com/it/u=4245350061,3599711701&fm=26&fmt=auto&gp=0.jpg',now(),now())")
    void saveUser(@Param("username") String username, @Param("encoded_password") String password);
}