package hwFeed;

import java.util.ArrayList;

public class Hardware implements Comparable<Hardware> {
	private ArrayList<Sensor> sensors=new ArrayList<>();
	private String type;
	private String name;
	private String identifier;
	private String displayName;
	private boolean isDisplay;
	
	public Hardware() {}
	
	public Hardware(String t, String n, String id) {
		this.type=t;
		this.name=n;
		this.identifier=id;
		this.displayName=n;
	}
	
	public void setType(String t) {
		this.type=t;
	}
	
	public void setName(String n) {
		this.name=n;
		if (this.displayName.isEmpty() || this.displayName.equals(this.name)) this.displayName=n; 
	}
	
	public void setId(String id) {
		this.identifier=id;
	}
	
	public void setDisplayName(String dn) {
		this.displayName=dn;
	}
	
	public void setDisplay(boolean flag) {
		this.isDisplay=flag;
	}
	
	public String getType () {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getId() {
		return this.identifier;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public ArrayList<Sensor> getSensorList() {
		return this.sensors;
	}
	
	public void populateSensors() {
		this.sensors.clear();
		try {
			this.sensors.addAll(Utility.retrieveSensorList(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int compareTo(Hardware hw) {
		return this.identifier.compareTo(hw.identifier);
	}
	
	public boolean isDisplay() {
		return this.isDisplay;
	}
	
	public Hardware copy () {
		Hardware hw=new Hardware();
		hw.type=this.type;
		hw.name=this.name;
		hw.identifier=this.identifier;
		hw.displayName=this.displayName;
		hw.isDisplay=this.isDisplay;
		for (Sensor s : this.getSensorList()) {
			Sensor clonedS=s.copy();
			clonedS.setHardware(hw);
			hw.getSensorList().add(clonedS);
		}
		return hw;
	}
	
	public String toString() {
		return this.type+";"+this.name+";"+this.identifier+";"+this.displayName+";"+this.isDisplay+";"+this.getSensorList().toString();
	}
}
