package eyemazev20.Dtos.http;

public class UsernamePutReq {
    private String newUsername;
    private int id;

    public UsernamePutReq() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }
}
