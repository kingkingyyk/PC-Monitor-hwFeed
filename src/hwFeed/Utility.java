package hwFeed;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

public class Utility {

	private static List<Map<String,String>> powerShellGetObjectList(String className) throws Exception {
		String[] command={"powershell.exe","-Command","get-wmiobject -namespace \"root/OpenHardwareMonitor\" -Class "+className};
		ProcessBuilder pb=new ProcessBuilder(command);
		Process p=pb.start();
		BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream()));
		List<Map<String,String>> list=new ArrayList<>();
		String s;
		HashMap<String,String> lastMap=new HashMap<>();
		String lastKey="";
		while ((s=br.readLine())!=null) {
			if (s.isEmpty()) {
				if (lastMap.size()>0) list.add(lastMap);
				lastMap=new HashMap<>();
			} else {
				int index=s.indexOf(':');
				if (index!=-1) {
					lastKey=s.substring(0,index).trim();
					lastMap.put(lastKey,s.substring(index+1).trim());
				} else lastMap.put(lastKey,lastMap.get(lastKey)+s.trim());
			}
		}
		if (lastMap.size()>0) list.add(lastMap);
		return list;
	}
	
	private static List<Map<String,String>> powerShellGetHardwareObjectList() throws Exception {
		return Utility.powerShellGetObjectList("Hardware");
	}
	
	public static ArrayList<Hardware> retrieveHardwareList () throws Exception {
		ArrayList<Hardware> hardwareList=new ArrayList<>();
		
		List<Map<String,String>> list=Utility.powerShellGetHardwareObjectList();
		for (Map<String,String> map : list) {
			Hardware hw=new Hardware(map.get("HardwareType"),map.get("Name"),map.get("Identifier"));
			hardwareList.add(hw);
		}
		
		return hardwareList;
	}
	
	private static List<Map<String,String>> powerShellGetSensorObjectList() throws Exception {
		return Utility.powerShellGetObjectList("Sensor");
	}
	
	public static ArrayList<Sensor> retrieveSensorList(Hardware hw) throws Exception {
		ArrayList<Sensor> sensorList=new ArrayList<>();
		
		List<Map<String,String>> list=Utility.powerShellGetSensorObjectList();
		for (Map<String,String> map : list) if (map.get("Parent").equals(hw.getId())) {
			Sensor sensor=new Sensor(hw);
			sensor.setId(map.get("Identifier"));
			sensor.setName(map.get("Name"));
			sensor.setType(map.get("SensorType"));
			sensorList.add(sensor);
		}
		
		return sensorList;
	}
	
	public static double retrieveSensorReading(Sensor s) throws Exception {
		List<Map<String,String>> list=powerShellGetSensorObjectList();
		for (Map<String,String> map : list) if (map.get("Identifier").equals(s.getId())) return Double.parseDouble(map.get("Value"));
		return 0.0;
	}
	
	
	public static Map<Sensor,Double> retrieveSensorReading(ArrayList<Sensor> sensors) throws Exception {
		HashMap<String,Sensor> ids=new HashMap<>();
		for (Sensor s : sensors) ids.put(s.getId(),s);
		
		HashMap<Sensor,Double> result=new HashMap<>();
		List<Map<String,String>> list=Utility.powerShellGetSensorObjectList();
		for (Map<String,String> map : list) if (ids.containsKey(map.get("Identifier"))) result.put(ids.get(map.get("Identifier")),Double.parseDouble(map.get("Value")));
		
		return result;
	}
	
	public static double roundOff(double d) {
		return ((int)(d*100))/100.0;
	}
	
	public static String formatDoubleOutput(double d) {
		d=Utility.roundOff(d);
		String output=String.valueOf(d);
		if (output.endsWith(".0")) output=output.substring(0,output.length()-2);
		else if (output.endsWith(".00")) output=output.substring(0,output.length()-3);
		return output;
	}
	
    public static ImageIcon resizeImageIcon (ImageIcon ic, int width, int height) {
    	return new ImageIcon(ic.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
}
