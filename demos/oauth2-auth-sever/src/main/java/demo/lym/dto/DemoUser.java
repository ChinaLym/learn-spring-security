package demo.lym.dto;

/**
 * demoUser
 * @author lym
 */
public class DemoUser {
    String userName = "userName";

    String nickName = "nickName";

    String info = "info";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
