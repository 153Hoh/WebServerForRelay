public class Device {

    private String DeviceId;
    private String DeviceIp;
    private String DeviceType;
    private boolean isOnline;

    public Device(String deviceId, String deviceIp, String deviceType, boolean isOnline) {
        DeviceId = deviceId;
        DeviceIp = deviceIp;
        DeviceType = deviceType;
        this.isOnline = isOnline;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getDeviceIp() {
        return DeviceIp;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setIsOnline(boolean v){
        isOnline = v;
    }

    public boolean getIsOnline(){
        return isOnline;
    }
}
