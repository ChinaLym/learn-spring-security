package demo.lym.dto;


import com.fasterxml.jackson.annotation.JsonView;

/**
 * 演示 {@link com.fasterxml.jackson.annotation.JsonView} 的使用
 * @author lym
 * @since 1.0
 */
public class UserDTO {

    public interface BASE {};
    public interface DETAIL extends BASE {};

    private String username;

    private String password;

    private String info;

    @JsonView(BASE.class)
    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    @JsonView(DETAIL.class)
    public String getInfo() {
        return info;
    }

    public UserDTO setInfo(String info) {
        this.info = info;
        return this;
    }
}
