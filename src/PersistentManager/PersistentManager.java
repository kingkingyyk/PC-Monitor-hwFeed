package PersistentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import hwFeed.Hardware;
import hwFeed.Sensor;

public class PersistentManager {

	private static ConcurrentHashMap<String,String> HardwareMap;
	private static ConcurrentHashMap<String,String> SensorMap;
	private static boolean HardwareMapToWrite=false;
	private static boolean SensorMapToWrite=false;
	private static boolean DisplayListToWrite=false;
	private static File HardwareFile=new File("hardware.hwf");
	private static File SensorFile=new File("sensor.hwf");
	private static File DisplayListFile=new File("display.hwf");
	
	public static void restoreHardwareAttributes(Hardware hw) {
		hw.setDisplayName(HardwareMap.getOrDefault(hw.getId()+"_DisplayName",hw.getDisplayName()));
		hw.setDisplay(Boolean.parseBoolean(HardwareMap.getOrDefault(hw.getId()+"_IsDisplay",String.valueOf(hw.isDisplay()))));
	}
	
	public static void storeHardwareAttributes(Hardware hw) {
		HardwareMap.put(hw.getId()+"_DisplayName",hw.getDisplayName());
		HardwareMap.put(hw.getId()+"_IsDisplay", String.valueOf(hw.isDisplay()));
		HardwareMapToWrite=true;
	}
	
	public static void restoreSensorAttributes(Sensor s) {
		s.setDisplayName(SensorMap.getOrDefault(s.getId()+"_DisplayName",s.getDisplayName()));
		s.setDisplay(Boolean.parseBoolean(SensorMap.getOrDefault(s.getId()+"_IsDisplay",s.isDisplay()+"")));
		s.setUnit(SensorMap.getOrDefault(s.getId()+"_Unit",s.getUnit()));
	}
	
	public static void storeSensorAttributes(Sensor s) {
		SensorMap.put(s.getId()+"_DisplayName",s.getDisplayName());
		SensorMap.put(s.getId()+"_IsDisplay", String.valueOf(s.isDisplay()));
		SensorMap.put(s.getId()+"_Unit", s.getUnit());
		SensorMapToWrite=true;
	}
	
	public static void storeDisplayList() {
		DisplayListToWrite=true;
	}
	
	public static List<String> loadDisplayList() {
		try {
			if (DisplayListFile.exists()) {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream(DisplayListFile));
				List<String> list=Collections.synchronizedList((List<String>)ois.readObject());
				ois.close();
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	private static ConcurrentHashMap<String,String> loadHashMapFromFile(File f) {
		try {
			if (f.exists()) {
				ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f));
				ConcurrentHashMap<String,String> hashmap=(ConcurrentHashMap)ois.readObject();
				ois.close();
				return hashmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ConcurrentHashMap<String,String>();
	}
	
	private static void saveHashMapToFile(ConcurrentHashMap map, File f) {
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(map);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void saveDisplayListToFile() {
		try {
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(DisplayListFile));
			oos.writeObject(hwFeed.hwFeed.DisplayList);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initialize() {
		HardwareMap=loadHashMapFromFile(HardwareFile);
		SensorMap=loadHashMapFromFile(SensorFile);
		hwFeed.hwFeed.DisplayList=loadDisplayList();
		new Thread() {
			public void run() {
				while (true) {
					if (HardwareMapToWrite) {
						saveHashMapToFile(HardwareMap,HardwareFile);
						HardwareMapToWrite=false;
					}
					if (SensorMapToWrite) {
						saveHashMapToFile(SensorMap,SensorFile);
						SensorMapToWrite=false;
					}
					if (DisplayListToWrite) {
						saveDisplayListToFile();
						DisplayListToWrite=false;
					}
					try { Thread.sleep(2000); } catch (Exception e) {}
				}
			}
		}.start();
	}
	
}
