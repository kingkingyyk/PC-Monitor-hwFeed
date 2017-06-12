package hwFeed;

public class Sensor implements Comparable<Sensor> {
	private Hardware hardware;
	private String identifier;
	private String name;
	private String type;
	private String unit;
	private String displayName;
	private double reading;
	private boolean isDisplay;

	public Sensor (Hardware hw) {
		this.hardware=hw;
	}
	
	public void setHardware(Hardware hw) {
		this.hardware=hw;
	}
	
	public Hardware getHardware() {
		return this.hardware;
	}
	
	public void setId (String id) {
		this.identifier=id;
	}
	
	public void setName(String n) {
		this.name=n;
		if (this.displayName==null || this.displayName.isEmpty() || this.displayName.equals(this.name)) this.displayName=n;
	}
	
	public void setType(String t) {
		this.type=t;
		if (this.unit==null || this.unit.isEmpty()) {
			switch (t) {
				case "Control" : {
					this.unit="%";
					break;
				}
				case "Temperature" : {
					this.unit="C";
					break;
				}
				case "SmallData" : {
					this.unit="MB";
					break;
				}
				case "Data" : {
					this.unit="GB";
					break;
				}
				case "Fan" : {
					this.unit="RPM";
					break;
				}
				case "Load" : {
					this.unit="%";
					break;
				}
				case "Clock" : {
					this.unit="MHz";
					break;
				}
				case "Power" : {
					this.unit="W";
				}
			}
		}
	}
	
	public void setUnit(String u) {
		this.unit=u;
	}
	
	public void setDisplayName (String dn) {
		this.displayName=dn;
	}
	
	public void setDisplay(boolean flag) {
		this.isDisplay=flag;
	}
	
	public String getId () {
		return this.identifier;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDisplayName () {
		return this.displayName;
	}
	
	public String getUnit() {
		return this.unit;
	}
	
	public void populateReading() {
		try {
			this.reading=Utility.retrieveSensorReading(this);
		} catch (Exception e) {}
	}
	
	public void setReading(double d) {
		this.reading=d;
	}
	
	public double getReading() {
		return this.reading;
	}
	
	public boolean isDisplay() {
		return this.isDisplay;
	}
	
	public int compareTo(Sensor s) {
		return this.identifier.compareTo(s.identifier);
	}
	
	public Sensor copy() {
		Sensor s=new Sensor(null);
		s.identifier=this.identifier;
		s.name=this.name;
		s.type=this.type;
		s.unit=this.unit;
		s.displayName=this.displayName;
		s.reading=this.reading;
		s.isDisplay=this.isDisplay;
		return s;
	}
	
	public String toString() {
		return this.hardware.getId()+";"+this.identifier+";"+this.name+";"+this.type+";"+this.unit+";"+this.displayName+";"+this.reading+";"+this.isDisplay;
	}
}
