package hwFeed;

import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class ArduinoOutput {
	private CommPort commPort;
	private OutputStream output;
	
	public static ArduinoOutput instanceOf (String portname, int baudrate) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portname);
        if ( !portIdentifier.isCurrentlyOwned() ) {
        	ArduinoOutput out=new ArduinoOutput();
        	out.commPort = portIdentifier.open("zzz",2000);
            if ( out.commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) out.commPort;
                serialPort.setSerialPortParams(baudrate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                out.output=serialPort.getOutputStream();
            }
            return out;
        }
        return null;
	}
	
	public void clearScreen() {
		try {
			this.output.write(0);
		} catch (Exception e) {};
	}
	
	public void setTextSizeSmall() {
		try {
			this.output.write(1);
		} catch (Exception e) {};
	}
	
	public void setTextSizeLarge() {
		try {
			this.output.write(2);
		} catch (Exception e) {};
	}
	
	public void print(String s) {
		try {
			this.output.write(s.getBytes());
		} catch (Exception e) {};
	}
	
	public void println(String s) {
		try {
			this.output.write((s+"\n").getBytes());
		} catch (Exception e) {};
	}
}
