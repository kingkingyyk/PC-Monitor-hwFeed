package hwFeed;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import ui.MainUI;
import ui.WaitUI;

public class Tray {
	
	private static TrayIcon trayIcon=null;
	
	public static void addTray() throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
        
        PopupMenu menu=new PopupMenu();
        
        MenuItem menuOption=new MenuItem("Modify");
        menuOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hideTray();
				WaitUI diag=new WaitUI();
				Thread t=new Thread() {
					public void run () {
						for (Hardware hw : hwFeed.DetectedPC.getHardwareList()) {
							diag.setInformation("Loading "+hw.getId());
							try {
								Map<Sensor,Double> reading=Utility.retrieveSensorReading(hw.getSensorList());
								for (Sensor s : hw.getSensorList()) s.setReading(reading.get(s));
							} catch (Exception e) {}
						}
						diag.dispose();
						hwFeed.UIPC=hwFeed.WorkingPC.copy();
						MainUI ui=new MainUI();
						ui.addWindowListener(new WindowListener() {
							@Override
							public void windowClosed(WindowEvent arg0) {
								showTray();
							}

							@Override public void windowActivated(WindowEvent arg0) {}
							@Override public void windowClosing(WindowEvent arg0) {}
							@Override public void windowDeactivated(WindowEvent arg0) {}
							@Override public void windowDeiconified(WindowEvent arg0) {}
							@Override public void windowIconified(WindowEvent arg0) {}
							@Override public void windowOpened(WindowEvent arg0) {}
						});
						ui.setLocationRelativeTo(null);
						ui.setVisible(true);
					}
				};
				t.start();
				diag.setVisible(true);
			}
        });
        menu.add(menuOption);
        
        MenuItem menuExit=new MenuItem("Exit");
        menuExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
        });
        menu.add(menuExit);
        
        trayIcon=new TrayIcon(Utility.resizeImageIcon(new ImageIcon(Utility.class.getClassLoader().getResource("iconx.png")),16,16).getImage(),"hwFeed",menu);
        showTray();
	}
	
	public static void hideTray() {
		if (trayIcon!=null) SystemTray.getSystemTray().remove(trayIcon);
	}
	
	public static void showTray() {
		if (trayIcon!=null) {
			try {
				SystemTray.getSystemTray().add(trayIcon);
			} catch (Exception e) {}
		}
	}
}
