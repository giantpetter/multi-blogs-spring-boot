package MySpring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public class User {
    private Integer id;
    private String username;
    @JsonIgnore
    private String encodedPassword;
    private String avatar;
    Instant createdAt;
    Instant updatedAt;


    public User(Integer id, String username, String encodedPassword, String avatar) {
        this.id = id;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.avatar = avatar;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
