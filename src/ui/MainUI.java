package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import hwFeed.Hardware;
import hwFeed.PC;
import hwFeed.Sensor;
import hwFeed.Utility;
import hwFeed.hwFeed;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;

import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.DefaultListModel;
import javax.swing.BoxLayout;

public class MainUI extends JFrame {
	private static final long serialVersionUID = -3539602838955083418L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JScrollPane scrollPane;
	private JXTreeTable tableSelection;
	private JPanel panel;
	private JButton btnOK;
	private JButton btnCancel;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JScrollPane scrollPane_1;
	private JList<DisplayListSensor> listDisplay;
	private JButton btnMoveUp;
	private JButton btnMoveDown;

	public MainUI() {
		setTitle("hwFeed");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		tabbedPane.addTab("Selection", null, scrollPane, null);
		
		tableSelection = new JXTreeTable();
		tableSelection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableSelection.setRootVisible(false);
		tableSelection.setTreeTableModel(new SelectionTableModel(hwFeed.UIPC));
		tableSelection.setClosedIcon(null);
		tableSelection.setOpenIcon(null);
		tableSelection.setLeafIcon(null);
		tableSelection.setTreeCellRenderer(new SelectionTreeCellRenderer());
		tableSelection.setRowHeight(24);
		for (int i=0;i<SelectionTableModel.COLUMN_NAMES.length-1;i++) if (SelectionTableModel.COLUMN_CLASSES[i]!=Boolean.class)
			tableSelection.getColumnModel().getColumn(i).setCellRenderer(new SelectionTreeTableCellRenderer());
		scrollPane.setViewportView(tableSelection);
		tableSelection.getColumnModel().getColumn(0).setPreferredWidth(100);
		tableSelection.getColumnModel().getColumn(1).setPreferredWidth(100);
		tableSelection.getColumnModel().getColumn(2).setPreferredWidth(30);
		tableSelection.getColumnModel().getColumn(3).setPreferredWidth(30);
		tableSelection.getColumnModel().getColumn(4).setPreferredWidth(30);
		
		panel_2 = new JPanel();
		tabbedPane.addTab("Display", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (tabbedPane.getSelectedComponent().equals(panel_2)) {
					if (listDisplay.getModel().getSize()==0) {
						btnMoveUp.setEnabled(false);
						btnMoveDown.setEnabled(false);
						DefaultListModel<DisplayListSensor> model=new DefaultListModel<>();
						listDisplay.setModel(model);
						HashMap<String,Sensor> map=new HashMap<>();
						for (Hardware hw : hwFeed.UIPC.getHardwareList()) for (Sensor s : hw.getSensorList()) map.put(s.getId(),s);
						for (String id : hwFeed.DisplayList) model.addElement(new DisplayListSensor(map.get(id)));
						
						System.out.println("HARLO");
					} else {
						ArrayList<DisplayListSensor> list=new ArrayList<>();
						for (int i=0;i<listDisplay.getModel().getSize();i++) list.add(listDisplay.getModel().getElementAt(i));
						
						boolean hasChange=false;
						for (int i=0;i<list.size();i++) {
							Sensor s=list.get(i).getSensor();
							if (!s.isDisplay()) {
								hasChange=true;
								list.remove(i);
							}
						}
						
						if (hasChange) listDisplay.setListData(list.toArray(new DisplayListSensor[list.size()]));
					}
				}
			}
		});
		
		panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		btnMoveUp = new JButton("Move Up");
		btnMoveUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DisplayListSensor [] data=new DisplayListSensor [listDisplay.getModel().getSize()];
				for (int i=0;i<data.length;i++) data[i]=listDisplay.getModel().getElementAt(i);
				int index=listDisplay.getSelectedIndex();
				DisplayListSensor temp=data[index-1];
				data[index-1]=data[index];
				data[index]=temp;
				listDisplay.setListData(data);
				listDisplay.setSelectedIndex(index-1);
			}
		});
		panel_3.add(btnMoveUp);
		
		btnMoveDown = new JButton("Move Down");
		btnMoveDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplayListSensor [] data=new DisplayListSensor [listDisplay.getModel().getSize()];
				for (int i=0;i<data.length;i++) data[i]=listDisplay.getModel().getElementAt(i);
				int index=listDisplay.getSelectedIndex();
				DisplayListSensor temp=data[index+1];
				data[index+1]=data[index];
				data[index]=temp;
				listDisplay.setListData(data);
				listDisplay.setSelectedIndex(index+1);
			}
		});
		panel_3.add(btnMoveDown);
		
		scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);
		
		listDisplay = new JList<>();
		listDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDisplay.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				btnMoveUp.setEnabled(listDisplay.getSelectedIndex()>0);
				btnMoveDown.setEnabled(listDisplay.getSelectedIndex()!=-1 && listDisplay.getSelectedIndex()<listDisplay.getModel().getSize()-1);
			}
			
		});
		scrollPane_1.setViewportView(listDisplay);
		
		panel_1 = new JPanel();
		tabbedPane.addTab("About", null, panel_1, null);
		
		JLabel lblNewLabel = new JLabel("<html>hwFeed v0.1<br>Written by kingkingyyk</html>");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel, BorderLayout.SOUTH);
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hwFeed.WorkingPC=hwFeed.UIPC;
				for (Hardware hw : hwFeed.WorkingPC.getHardwareList()) {
					PersistentManager.PersistentManager.storeHardwareAttributes(hw);
					for (Sensor s : hw.getSensorList()) {
						PersistentManager.PersistentManager.storeSensorAttributes(s);
					}
				}
				hwFeed.DisplayList.clear();
				for (int i=0;i<listDisplay.getModel().getSize();i++) hwFeed.DisplayList.add(listDisplay.getModel().getElementAt(i).getSensor().getId());
				PersistentManager.PersistentManager.storeDisplayList();
				dispose();
			}
		});
		panel.add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (hwFeed.WorkingPC.compareTo(hwFeed.UIPC)==0 || JOptionPane.showConfirmDialog(MainUI.this,"Confirm to discard changes?","hwFeed",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
		panel.add(btnCancel);
	}
	
	private static class SelectionTableModel extends AbstractTreeTableModel  {
		public final static String[] COLUMN_NAMES = {"Sensor","Display Name","Reading","Unit","Display"};
		@SuppressWarnings("rawtypes")
		public final static Class[] COLUMN_CLASSES={Object.class,String.class,Object.class,String.class,Boolean.class};
		
		public SelectionTableModel(PC root) {
			super(root);
		}

	    
		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}
		
	    @Override
	    public String getColumnName(int column) {
	        return COLUMN_NAMES[column];
	    }
	    
	    @SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
	    public Class getColumnClass(int column) {
	    	return COLUMN_CLASSES[column];
	    }
	    
	    @Override
	    public void setValueAt(Object value, Object arg0, int col) {
			Hardware hw=(arg0 instanceof Hardware) ? (Hardware)arg0 : null;
			Sensor s=(arg0 instanceof Sensor) ? (Sensor)arg0 : null;
	    	switch (COLUMN_NAMES[col])  {
		    	case "Display Name" : {
					if (hw!=null) hw.setDisplayName(value.toString());
					else if (s!=null) s.setDisplayName(value.toString());
					break;
		    	}
		    	case "Unit" : {
		    		if (s!=null) s.setUnit(value.toString());
		    		break;
		    	}
		    	case "Display" : {
					boolean flag=(boolean)value;
					if (hw!=null) {
						hw.setDisplay(flag);
						for (Sensor ss : hw.getSensorList()) if (ss.isDisplay()!=flag) ss.setDisplay(flag);
					} else if (s!=null) {
						s.setDisplay(flag);
						boolean hwDisplay=s.getHardware().isDisplay();
						boolean isAllTrue=true;
						for (Sensor hws : s.getHardware().getSensorList()) isAllTrue&=hws.isDisplay();
						
						if (hwDisplay!=isAllTrue) s.getHardware().setDisplay(isAllTrue);
					}
					break;
				}
	    	}
	    }
	    
		@Override
		public Object getValueAt(Object arg0, int col) {
			Hardware hw=(arg0 instanceof Hardware) ? (Hardware)arg0 : null;
			Sensor s=(arg0 instanceof Sensor) ? (Sensor)arg0 : null;
	    	switch (COLUMN_NAMES[col])  {
		    	case "Display Name" : {
					if (hw!=null) return hw.getDisplayName();
					else if (s!=null) return s.getDisplayName();
					else return "";
		    	}
		    	case "Unit" : {
					if (s!=null) return s.getUnit();
					else return "";
		    	}
		    	case "Display" : {
					if (hw!=null) return hw.isDisplay();
					else if (s!=null) return s.isDisplay();
					break;
				}
	    	}
	    	return arg0;
		}

		@Override
		public boolean isCellEditable(Object arg0, int col) {
			return COLUMN_NAMES[col].equals("Display") || COLUMN_NAMES[col].equals("Display Name") || COLUMN_NAMES[col].equals("Unit");
		}
		
		@Override
		public Object getChild(Object arg0, int arg1) {
			if (arg0 instanceof PC) return ((PC)arg0).getHardwareList().get(arg1);
			else if (arg0 instanceof Hardware) return ((Hardware)arg0).getSensorList().get(arg1);
			return null;
		}

		@Override
		public int getChildCount(Object arg0) {
			if (arg0 instanceof PC) return ((PC)arg0).getHardwareList().size();
			else if (arg0 instanceof Hardware) return ((Hardware)arg0).getSensorList().size();
			return 0;
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			if (arg0 instanceof PC) return ((PC)arg0).getHardwareList().indexOf(arg1);
			else if (arg0 instanceof Hardware) return ((Hardware)arg0).getSensorList().indexOf(arg1);
			return 0;
		}
	}
	
	private static class SelectionTreeCellRenderer extends DefaultTreeCellRenderer {
		private static final long serialVersionUID = -6458742907594180572L;
		private static ImageIcon HardwareIcon=Utility.resizeImageIcon(new ImageIcon(MainUI.class.getClassLoader().getResource("chip.png")),16,16);
		private static ImageIcon SensorIcon=Utility.resizeImageIcon(new ImageIcon(MainUI.class.getClassLoader().getResource("gauge.png")),16,16);

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			Component c=super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			JPanel p=new JPanel();
			if (selected) p.setBackground(UIManager.getColor("Tree.selectionBackground"));
			else p.setBackground(Color.WHITE);
			
			if (value instanceof Hardware) {
				Hardware hw=(Hardware)value;
				this.setText(hw.getName());
				this.setIcon(HardwareIcon);
			}
			else if (value instanceof Sensor) {
				Sensor s=(Sensor)value;
				this.setText(s.getName()+" ["+s.getType()+"]");
				this.setIcon(SensorIcon);
			}
			p.add(c);
			
			this.setBackground(c.getBackground());
			if (selected) this.setForeground(Color.WHITE);
			else this.setForeground(Color.BLACK);
			
			return p;
		}
	}
	
	private static class SelectionTreeTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = -6458742907594180572L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			column=table.convertColumnIndexToModel(column);
			Hardware hw=(value instanceof Hardware) ? (Hardware)value : null;
			Sensor s=(value instanceof Sensor) ? (Sensor)value : null;
			switch (SelectionTableModel.COLUMN_NAMES[column]) {
				case "Sensor" : {
					if (hw!=null) this.setText(hw.getName());
					else if (s!=null) this.setText("["+s.getType()+"]"+s.getName());
					else this.setText("");
					break;
				}
				case "Reading" : {
					if (s!=null) this.setText(Utility.formatDoubleOutput(s.getReading()));
					else this.setText("");
					break;
				}
			}
			
			return this;
		}
	}
	
	private static class DisplayListSensor {
		private Sensor s;
		
		public DisplayListSensor(Sensor s) {
			this.s=s;
		}
		
		public Sensor getSensor() {
			return this.s;
		}
		
		public String toString() {
			return this.s.getHardware().getDisplayName()+"\\"+this.s.getDisplayName();
		}
	}
}
