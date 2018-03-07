public class Device {

    private String DeviceId;
    private String DeviceIp;
    private String DeviceType;

    public Device(String deviceId, String deviceIp, String deviceType) {
        DeviceId = deviceId;
        DeviceIp = deviceIp;
        DeviceType = deviceType;
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
}
