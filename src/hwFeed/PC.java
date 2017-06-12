package hwFeed;

import java.util.ArrayList;

public class PC implements Comparable<PC> {
	private ArrayList<Hardware> hardwares;
	
	public PC () {
		this.hardwares=new ArrayList<>();
	}
	
	public void addHardware(Hardware hw) {
		this.hardwares.add(hw);
	}
	
	public void populateHardware() throws Exception {
		this.addHardware(Utility.retrieveHardwareList());
	}
	
	public void addHardware(ArrayList<Hardware> list) {
		this.hardwares.addAll(list);
	}
	
	public ArrayList<Hardware> getHardwareList() {
		return this.hardwares;
	}
	
	public PC copy() {
		PC p=new PC();
		for (Hardware hw : this.hardwares) p.addHardware(hw.copy());
		return p;
	}

	@Override
	public int compareTo(PC p) {
		return this.hardwares.toString().compareTo(p.hardwares.toString());
	}
	
}
