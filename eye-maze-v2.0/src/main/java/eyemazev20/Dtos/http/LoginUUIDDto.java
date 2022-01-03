package eyemazev20.Dtos.http;

public class LoginUUIDDto {
    private String loginUUID;

    public LoginUUIDDto() {}

    public String getLoginUUID() {
        return loginUUID;
    }

    public void setLoginUUID(String loginUUID) {
        this.loginUUID = loginUUID;
    }

    public LoginUUIDDto(String loginUUID) {
        this.loginUUID = loginUUID;
    }
}
