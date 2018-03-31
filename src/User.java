import java.util.List;

public class User {

    private String UserName;
    private String UserPass;
    private List<Device> UserDevices;

    public User(String userName, String userPass) {
        UserName = userName;
        UserPass = userPass;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public List<Device> getUserDevices() {
        return UserDevices;
    }

    public void setUserDevices(List<Device> userDevices) {
        UserDevices = userDevices;
    }

    public void addDevice(Device device){
        UserDevices.add(device);
    }
}
