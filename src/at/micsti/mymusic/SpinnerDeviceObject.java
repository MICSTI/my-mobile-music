package at.micsti.mymusic;

public class SpinnerDeviceObject {
	
	private int deviceId;
	private String deviceName;
	
	public SpinnerDeviceObject (int deviceId, String deviceName) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
	}
	
	public int getDeviceId ()  {
		return this.deviceId;
	}
	
	public String getDeviceName () {
		return this.deviceName;
	}
	
	@Override
	public String toString () {
		return this.getDeviceName();
	}
}
