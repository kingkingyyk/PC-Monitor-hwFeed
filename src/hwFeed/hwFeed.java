package hwFeed;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class hwFeed {

	private static ArduinoOutput screen=null;
	public static PC DetectedPC=new PC();
	public static PC WorkingPC=new PC();
	public static PC UIPC;
	public static List<String> DisplayList=Collections.synchronizedList(new ArrayList<>());
	
	public static void main (String [] args) throws Exception {
		if (args.length==1) Thread.sleep(Integer.parseInt(args[0])*1000);
		
		PersistentManager.PersistentManager.initialize();
		screen=ArduinoOutput.instanceOf("COM3",115200);
		screen.clearScreen();
		screen.setTextSizeSmall();
		screen.print("Scanning Hardwares...");
		DetectedPC.populateHardware();

		if (DetectedPC.getHardwareList().size()==0) {
			screen.clearScreen();
			screen.print("Failed to get hardwares.");
		} else {
			screen.clearScreen();
			screen.print("Scanning Sensors...");
			Collections.sort(DetectedPC.getHardwareList());
			for (Hardware hw : DetectedPC.getHardwareList()) {
				PersistentManager.PersistentManager.restoreHardwareAttributes(hw);
				hw.populateSensors();
				Collections.sort(hw.getSensorList());
				for (Sensor s : hw.getSensorList()) PersistentManager.PersistentManager.restoreSensorAttributes(s);
			}
			DisplayList=PersistentManager.PersistentManager.loadDisplayList();
			
			WorkingPC=DetectedPC;
			startDisplay();
		}
		Tray.addTray();
	}
	
	private static void startDisplay() {
		Thread t=new Thread() {
			public void run () {
				try {
					HashMap<String,Sensor> map=new HashMap<>();
					for (Hardware hw : WorkingPC.getHardwareList()) for (Sensor s : hw.getSensorList()) map.put(s.getId(),s);
					while (true) {
						boolean hasDisplay=false;
						for (String id :  DisplayList) {
							Sensor s=map.get(id);
							hasDisplay=true;
							screen.clearScreen();
							screen.setTextSizeSmall();
							screen.print(s.getHardware().getDisplayName()+"\n\n"+(s.getDisplayName())+"\n\n");
							screen.setTextSizeLarge();
							s.populateReading();
							screen.print(Utility.formatDoubleOutput(s.getReading())+s.getUnit());
							Thread.sleep(1000);
						}
						if (!hasDisplay) {
							screen.clearScreen();
							screen.setTextSizeLarge();
							screen.print("No sensor selected");
							Thread.sleep(1000);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
}
