package MySpring.mapper;


import MySpring.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    public User findById(@Param("id") Integer id);


    @Select("select * from user where username = #{username}")
    User getUserByUsername(@Param("username") String username);

    @Insert("insert into user(username, password, avatar, created_at, updated_at)" +
            "values(#{username},#{encoded_password},null,now(),now())")
    void saveUser(@Param("username") String username, @Param("encoded_password") String password);
}