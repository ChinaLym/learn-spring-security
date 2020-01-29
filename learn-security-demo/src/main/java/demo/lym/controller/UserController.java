package demo.lym.controller;

import com.fasterxml.jackson.annotation.JsonView;
import demo.lym.dto.UserDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示 {@link com.fasterxml.jackson.annotation.JsonView} 的使用
 * @author lym
 * @since 1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    private UserDTO demoUser = new UserDTO().setUsername("name").setPassword("password").setInfo("info");

    @JsonView(UserDTO.BASE.class)
    @RequestMapping("base")
    public UserDTO base(){
        return demoUser;
    }

    @RequestMapping("normal")
    public UserDTO normal(){
        return demoUser;
    }

    @JsonView(UserDTO.DETAIL.class)
    @RequestMapping("detail")
    public UserDTO detail(){
        return demoUser;
    }
}
