package mainView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import manager.FoodDetailDialog;
import model.EmpModel;
import tools.myFont;

public class MangerMain extends JFrame implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	int uid = 0;
	String sname = "";
	
	JFrame parent;
	JPanel m;
	JButton add;
	JTable jt;
	JScrollPane js;
	DefaultTableModel tm;
	JPanel bp,bp1,bp2,bp3;
	JLabel staff,time;
	JButton language, logout;
	Timer t;
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Image titleIcon;
	ResourceBundle rb;
	int localeCurrent = 0;
	public void initLocale(){
    	if(localeCurrent == 0) {
    		this.setLocale(Locale.getDefault());
    		rb = ResourceBundle.getBundle("translation/my", Locale.getDefault());
    	}else {
    		this.setLocale(new Locale("zh", "TW"));
    		rb = ResourceBundle.getBundle("translation/my", new Locale("zh", "TW"));
    	}
	}
	
	public MangerMain(int locale,int staffid,String staffname) {
		localeCurrent=locale;
		uid = staffid;
		sname = staffname;
		initLocale();
		
		m = new JPanel(new BorderLayout());
		mainPanel();
		bpPanel();
		m.add(js,"Center");
		m.add(add,"South");
		
		this.add(m,"Center");
		this.add(bp,"South");
		
		try {
			titleIcon = ImageIO.read(new File("images/logo/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setTitle(rb.getString("man_tl"));
		this.setIconImage(titleIcon);
		this.setSize(1500,800);
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(w/2-750, h/2-400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setVisible(true);
	}
	
	public void bpPanel() {
		bp = new JPanel(new BorderLayout());
		
		bp1 = new JPanel();
		bp2 = new JPanel();
		bp3 = new JPanel();
		
		staff = new JLabel(" ID:"+uid+" - "+sname);
		staff.setFont(myFont.f1);
		
		t = new Timer(1000,this); //1000 = 1 second
		t.start();
		time = new JLabel(rb.getString("com_time")+
				format1.format(Calendar.getInstance().getTime()).toString()+"  ");
		time.setFont(myFont.f1);
		
		language = new JButton(rb.getString("com_lan"));
		language.setFont(myFont.f1);
		language.addActionListener(this);
		
		logout = new JButton(rb.getString("com_logout"));
		logout.setFont(myFont.f1);
		logout.addActionListener(this);
		
		bp1.add(staff);
		bp2.add(language);bp2.add(logout);
		bp3.add(time);
		
		bp.add(bp1,"West");bp.add(bp2,"Center");bp.add(bp3,"East");
	}
	
	public void mainPanel() {
		EmpModel emp = new EmpModel();
		String sql = "";
		if(localeCurrent==0) {
			sql = "select f_id,f_n,f_p,f_f from ce_food order by f_id ";
		}else {
			sql = "select f_id,f_nzh,f_p,f_f from ce_food order by f_id ";
		}
		emp.runSql(sql);
		
		tm = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		tm.addColumn("ID");
		tm.addColumn("Name");
		tm.addColumn("Price");
		tm.addColumn("Flag");
		tm.addColumn("Delete");
		tm.addColumn("Update");
		
		for(int i=0;i<emp.getRowCount();i++) {
			Vector<String> vv = new Vector<String>();
			for(int ii=0;ii<emp.getColumnCount();ii++) {
				vv.addElement(emp.getValueAt(i, ii).toString());
			}
			vv.addElement(rb.getString("mm_del"));
			vv.addElement(rb.getString("mm_up"));
			tm.addRow(vv);
		}
		
		jt = new JTable(tm){
			private static final long serialVersionUID = 1L;

			@Override
		    public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
		        Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
		        
		        int flag = Integer.parseInt(getModel().getValueAt(rowIndex,3).toString());
		        
		        if(columnIndex==3) {
		        	if(flag == 1) {
		        		componenet.setBackground(new Color(119, 252, 250));
			            componenet.setForeground(Color.BLACK);
		        	}else if(flag == 2) {
		        		componenet.setBackground(new Color(252, 191, 121));
			            componenet.setForeground(Color.BLACK);
		        	}else {
		        		componenet.setBackground(new Color(252, 252, 121));
			            componenet.setForeground(Color.BLACK);
		        	}
		        }else if(columnIndex==4) { // Delete
		        	componenet.setBackground(new Color(252, 123, 121));
		            componenet.setForeground(Color.BLACK);
		            
		        }else if(columnIndex==5) { // update
		        	componenet.setBackground(new Color(121, 252, 125));
		            componenet.setForeground(Color.BLACK);
		        }else {
		        	componenet.setBackground(Color.white);
		            componenet.setForeground(Color.BLACK);
		        }
		        return componenet;
		    }
		};
		jt.setAutoCreateRowSorter(true);
		jt.setRowHeight(30);
		jt.setFont(myFont.f1);
		jt.addMouseListener(this);
		js = new JScrollPane(jt);
		setTable();
		
		add = new JButton(rb.getString("mm_add"));
		add.setFont(myFont.f1);
		add.addActionListener(this);
	}
	
	public void updateTable() {
		EmpModel emp = new EmpModel();
		String sql = "";
		if(localeCurrent==0) {
			sql = "select f_id,f_n,f_p,f_f from ce_food order by f_id ";
		}else {
			sql = "select f_id,f_nzh,f_p,f_f from ce_food order by f_id ";
		}
		emp.runSql(sql);
		
		tm.setRowCount(0);
		
		for(int i=0;i<emp.getRowCount();i++) {
			Vector<String> vv = new Vector<String>();
			for(int ii=0;ii<emp.getColumnCount();ii++) {
				vv.addElement(emp.getValueAt(i, ii).toString());
			}
			vv.addElement(rb.getString("mm_del"));
			vv.addElement(rb.getString("mm_up"));
			tm.addRow(vv);
		}
		jt.setModel(tm);
		setTable();
	}
	
	public void setTable() {
		jt.getColumnModel().getColumn(0).setPreferredWidth(25);
		jt.getColumnModel().getColumn(1).setPreferredWidth(600);
		jt.getColumnModel().getColumn(2).setPreferredWidth(25);
		jt.getColumnModel().getColumn(3).setPreferredWidth(25);
		jt.getColumnModel().getColumn(4).setPreferredWidth(50);
		jt.getColumnModel().getColumn(5).setPreferredWidth(50);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		jt.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		jt.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == jt) {
			int col = jt.columnAtPoint(e.getPoint());
			if(col==4) { //Delete
				String fid = jt.getValueAt(jt.getSelectedRow(), 0).toString();
				int reply = JOptionPane.showConfirmDialog(null,rb.getString("w_confirm"),
						rb.getString("w_confirmTitle"),JOptionPane.YES_NO_OPTION);
		        if (reply == JOptionPane.YES_OPTION) {
		        	EmpModel emp = new EmpModel();
		        	String sql = "delete from ce_food where f_id=? ";
		        	String[] paras = {fid};
		        	emp.updInfo(sql, paras);
		        	updateTable();
		        }
			}else if(col==5) { //Update
				String fid = jt.getValueAt(jt.getSelectedRow(), 0).toString();
				new FoodDetailDialog(parent,rb.getString("fj_tl"),true,localeCurrent,1,fid);
				updateTable();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.time.setText((rb.getString("com_time")+
				format1.format(Calendar.getInstance().getTime()).toString())+"  ");
		if(e.getSource() == add) {
			new FoodDetailDialog(parent,rb.getString("fj_tl"),true,localeCurrent,0,"0");
			updateTable();
		}else if(e.getSource() == language) {
			int x = 0;
			if(localeCurrent == 0) {
				x = 1;
			}else {
				x = 0;
			}
			ResourceBundle.clearCache();
			this.dispose();
			new MangerMain(x,uid,sname);
		}else if(e.getSource() == logout) {
			this.dispose();
			new Login(localeCurrent);
		}
	}
	
	public static void main(String[] args) {
		new MangerMain(0,2,"jacky");
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
