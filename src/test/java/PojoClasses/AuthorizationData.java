package PojoClasses;

public class AuthorizationData {
    private String username;
    private String password;

    public AuthorizationData(){
        username="default";
        password="default";
    }
    public AuthorizationData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
