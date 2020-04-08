package mainView;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import foodView.FoodAddPanel;
import foodView.FoodSpecialDialog;
import model.EmpModel;
import test.TTServer;
import tools.myFont;

public class CommonMain extends JFrame implements ActionListener, MouseListener, Runnable{
	private static final long serialVersionUID = 1L;
	int af = 0;
	int uid = 0;
	int oid = 0;
	String sname = "";
	String sid = "";
	boolean FLAG = true;
	int upflag = 0;
	
	JFrame parent;
	JPanel mp,rp,rbp;
	JPanel lp,lp1,lp2,lp3,lp4,lm;
	JPanel bp,bp1,bp2,bp3;
	CardLayout cardl;
	JLabel staff,time;
	JButton language, logout, area, reset, bill;
	JTable ot;
	JScrollPane js;
	JLabel l1,l2,l3,l4;
	DefaultTableModel tm;
	
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
	
	public void bpPnael() {
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
	
	public void lpPnael() {
		cardl = new CardLayout();
		lm = new JPanel(this.cardl);
		
		l1 = new JLabel(rb.getString("ao_drink"),new ImageIcon("images/logo/cof.png"),JLabel.CENTER);
		l2 = new JLabel(rb.getString("ao_set"),new ImageIcon("images/logo/set.jpg"),JLabel.CENTER);
		l3 = new JLabel(rb.getString("ao_sand"),new ImageIcon("images/logo/sandwich.png"),JLabel.CENTER);
		l4 = new JLabel(rb.getString("ao_salad"),new ImageIcon("images/logo/salad.jpg"),JLabel.CENTER);
		l1.setFont(myFont.f1);
		l2.setFont(myFont.f1);
		l3.setFont(myFont.f1);
		l4.setFont(myFont.f1);
		l1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		l2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		l3.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		l4.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		l1.setVerticalTextPosition(JLabel.TOP);
		l2.setVerticalTextPosition(JLabel.TOP);
		l3.setVerticalTextPosition(JLabel.TOP);
		l4.setVerticalTextPosition(JLabel.TOP);
		l1.setHorizontalTextPosition(JLabel.CENTER);
		l2.setHorizontalTextPosition(JLabel.CENTER);
		l3.setHorizontalTextPosition(JLabel.CENTER);
		l4.setHorizontalTextPosition(JLabel.CENTER);
		l1.addMouseListener(this);
		l2.addMouseListener(this);
		l3.addMouseListener(this);
		l4.addMouseListener(this);
		
		lp.add(l1);
		lp.add(l2);
		lp.add(l3);
		lp.add(l4);
		
		lm.add(lp,"0");
		
		FoodAddPanel fp1= new FoodAddPanel(localeCurrent,1,oid,sid,af);
		lm.add(fp1,"1");
		
		FoodAddPanel fp2= new FoodAddPanel(localeCurrent,2,oid,sid,af);
		lm.add(fp2,"2");
		
		FoodAddPanel fp3= new FoodAddPanel(localeCurrent,3,oid,sid,af);
		lm.add(fp3,"3");
		
		FoodAddPanel fp4= new FoodAddPanel(localeCurrent,4,oid,sid,af);
		lm.add(fp4,"4");
	}
	
	public void rpPnael() {
		rp = new JPanel(new BorderLayout());
		
		area = new JButton(rb.getString("ao_dine"));
		area.setForeground(Color.red);
		area.setBackground(Color.YELLOW);
		area.setFont(myFont.f1);
		area.addActionListener(this);
		
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
		tm.addColumn("cid");
		tm.addColumn("Special");
		tm.addColumn("Delete");
		
		ot = new JTable(tm){
			private static final long serialVersionUID = 1L;

			@Override
		    public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
		        Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
		        
		        if(columnIndex == 4) {
		        	componenet.setBackground(Color.GREEN);
		        	componenet.setForeground(Color.RED);
		        }else if(columnIndex == 5){
		        	componenet.setBackground(Color.YELLOW);
		        	componenet.setForeground(Color.RED);
		        }else {
		        	componenet.setBackground(Color.white);
		        	componenet.setForeground(Color.black);
		        }
		        return componenet;
		    }
		};
		ot.setAutoCreateRowSorter(true);
		ot.setRowHeight(30);
		ot.setFont(myFont.f1);
		ot.addMouseListener(this);
		setTable();
		js = new JScrollPane(ot);
		
		rbp = new JPanel(new GridLayout(2,0));
		
		bill = new JButton(rb.getString("ao_bill"));
		bill.setFont(myFont.f1);
		bill.addActionListener(this);
		
		reset = new JButton(rb.getString("ao_reset"));
		reset.setForeground(Color.red);
		reset.setBackground(Color.YELLOW);
		reset.setFont(myFont.f1);
		reset.addActionListener(this);
		
		rbp.add(bill);rbp.add(reset);
		
		rp.add(area,"North");
		rp.add(js,"Center");
		rp.add(rbp,"South");
	}
	
	public void updateOrder(int ord) {
		EmpModel emp = new EmpModel();
		String sql = "select count(*) from ce_orderitem where oi_oid='"+ord+"' and oi_sid='"+sid+"' ";
		emp.runSql(sql);
		int nowc = Integer.parseInt(emp.getValueAt(0, 0).toString());
		if(nowc!=upflag) {
			upflag = nowc;
			sql = "select oi_id,f_n,oi_p,oi_cid from "
					+ "( select ceo.* , cef.* from ce_orderitem ceo join ce_food cef on ceo.oi_fid=cef.f_id) "
					+ "where oi_oid='"+ord+"' and oi_sid='"+sid+"' order by oi_id DESC";
			emp.runSql(sql);
			tm.setRowCount(0);
			for(int i=0;i<emp.getRowCount();i++) {
					Vector<String> vv = new Vector<String>();
					vv.addElement(emp.getValueAt(i, 0).toString());
					vv.addElement(emp.getValueAt(i, 1).toString());
					vv.addElement(emp.getValueAt(i, 2).toString());
					vv.addElement(emp.getValueAt(i, 3).toString());
					vv.addElement(rb.getString("ao_spe"));
					vv.addElement(rb.getString("ao_del"));
					tm.addRow(vv);
			}
		}
	}
	
	public void setTable() {
		ot.getColumnModel().getColumn(0).setPreferredWidth(25);
		ot.getColumnModel().getColumn(1).setPreferredWidth(500);
		ot.getColumnModel().getColumn(2).setPreferredWidth(100);
		ot.getColumnModel().getColumn(3).setPreferredWidth(50);
		ot.getColumnModel().getColumn(4).setPreferredWidth(80);
		ot.getColumnModel().getColumn(5).setPreferredWidth(80);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		ot.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		ot.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
	}
	
	public CommonMain(int locale,int staffid,String staffname,String shopid) {
		localeCurrent = locale;
		uid = staffid;
		sname = staffname;
		sid = shopid;
		initLocale();
		
		try {
			EmpModel emp = new EmpModel();
			String sql = "select oid_increase.nextval from dual ";
			emp.runSql(sql);
			oid = Integer.parseInt(emp.getValueAt(0, 0).toString());
			LocalDateTime time = LocalDateTime.now();
			sql = "insert into ce_order values (?,?,?,TO_DATE(SYSDATE),?,?,?) ";
			String [] paras = {oid+"",sid,af+"",time.toString(),"0","1"};
			emp.updInfo(sql, paras);
		}catch (Exception e) {
			e.printStackTrace();
			JLabel w2 = new JLabel(rb.getString("w_int"));
			w2.setFont(myFont.f1);
			JOptionPane.showMessageDialog(
				this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
		}
		
		mp = new JPanel(new GridLayout(0,2));
		
		lp = new JPanel(new GridLayout(2,2));
		bpPnael();
		lpPnael();
		rpPnael();
		mp.add(lm);mp.add(rp);
		this.add(mp,"Center");
		this.add(bp,"South");
		
		start();
		
		try {
			titleIcon = ImageIO.read(new File("images/logo/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setTitle(rb.getString("com_tl")+"  shop"+sid);
		this.setIconImage(titleIcon);
		this.setSize(1500,800);
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(w/2-750, h/2-400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setVisible(true);
	}
	
	public void reset() {
		af=0;
		area.setText(rb.getString("ao_dine"));
		try {
			EmpModel emp = new EmpModel();
			String sql = "select oid_increase.nextval from dual ";
			emp.runSql(sql);
			oid = Integer.parseInt(emp.getValueAt(0, 0).toString());
			LocalDateTime time = LocalDateTime.now();
			sql = "insert into ce_order values (?,?,?,TO_DATE(SYSDATE),?,?,?) ";
			String [] paras = {oid+"",sid,af+"",time.toString(),"0","1"};
			emp.updInfo(sql, paras);
		}catch (Exception e2) {
			e2.printStackTrace();
			JLabel w2 = new JLabel(rb.getString("w_int"));
			w2.setFont(myFont.f1);
			JOptionPane.showMessageDialog(
				this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
		}
		this.cardl.show(lm, "0");
		FoodAddPanel fp1= new FoodAddPanel(localeCurrent,1,oid,sid,af);
		lm.add(fp1,"1");
		FoodAddPanel fp2= new FoodAddPanel(localeCurrent,2,oid,sid,af);
		lm.add(fp2,"2");
		FoodAddPanel fp3= new FoodAddPanel(localeCurrent,3,oid,sid,af);
		lm.add(fp3,"3");
		FoodAddPanel fp4= new FoodAddPanel(localeCurrent,4,oid,sid,af);
		lm.add(fp4,"4");
	}
	
	@Override
	public void run() {
		while(FLAG) {
			try {
				Thread.sleep(1000);
				updateOrder(oid);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void start() {
		SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				try {
					Thread.sleep(1000);
					new TTServer();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		worker.execute();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.time.setText((rb.getString("com_time")+
				format1.format(Calendar.getInstance().getTime()).toString())+"  ");
		if(e.getSource() == language) {
			int x = 0;
			if(localeCurrent == 0) {
				x = 1;
			}else {
				x = 0;
			}
			ResourceBundle.clearCache();
			FLAG = false;
			this.dispose();
			CommonMain cm = new CommonMain(x,uid,sname,sid);
			Thread t = new Thread(cm);
			t.start();
		}else if(e.getSource() == logout) {
			FLAG = false;
			this.dispose();
			new Login(localeCurrent);
		}else if(e.getSource() == reset) {
			int reply = JOptionPane.showConfirmDialog(null,rb.getString("w_confirm"),
						rb.getString("w_confirmTitle"),JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION) {
	        	EmpModel emp = new EmpModel();
				String sql = "delete from ce_orderitem where oi_oid=? ";
				String[] paras = {oid+""};
				emp.updInfo(sql, paras);
	        }
		}else if(e.getSource() == area) {
			if(af==0) {
				af=1;
				area.setText(rb.getString("ao_tk"));
				EmpModel emp = new EmpModel();
				String sql = "update ce_orderitem set oi_a=? where oi_oid=? ";
				String[] paras = {"1",oid+""};
				emp.updInfo(sql, paras);
				sql = "update ce_order set o_a=? where o_id=? ";
				String[] paras2 = {"1",oid+""};
				emp.updInfo(sql, paras2);
			}else {
				af=0;
				area.setText(rb.getString("ao_dine"));
				EmpModel emp = new EmpModel();
				String sql = "update ce_orderitem set oi_a=? where oi_oid=? ";
				String[] paras = {"0",oid+""};
				emp.updInfo(sql, paras);
				sql = "update ce_order set o_a=? where o_id=? ";
				String[] paras2 = {"0",oid+""};
				emp.updInfo(sql, paras2);
			}
		}else if(e.getSource() == bill) {
			// Bill Method
			
			// Update states after success payment
			try {
				EmpModel emp = new EmpModel();
				String sql = "update ce_orderitem set oi_f=? where oi_oid=? ";
				String[]paras = {"2",oid+""};
				emp.updInfo(sql, paras);
				
				sql = "update ce_order set o_f=? where o_id=? ";
				String[]paras2 = {"2",oid+""};
				emp.updInfo(sql, paras2);
			}catch (Exception e2) {
				e2.printStackTrace();
			}
			
			// Reset Everything to default
			reset();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == l1) {
			this.cardl.show(lm, "1");
		}else if(e.getSource() == l2) {
			this.cardl.show(lm, "2");
		}else if(e.getSource() == l3) {
			this.cardl.show(lm, "3");
		}else if(e.getSource() == l4) {
			this.cardl.show(lm, "4");
		}else if(e.getSource() == ot) {
			int col = ot.columnAtPoint(e.getPoint());
			if(col==4) {
				int orderitemid = Integer.parseInt(ot.getValueAt(ot.getSelectedRow(), 0).toString());
				new FoodSpecialDialog(localeCurrent,orderitemid);
			}else if(col==5) {
				int orderid = Integer.parseInt(ot.getValueAt(ot.getSelectedRow(), 0).toString());
				EmpModel emp = new EmpModel();
				String sql = "delete from ce_orderitem where oi_id=? ";
				String[] paras = {orderid+""};
				emp.updInfo(sql, paras);
			}
		}
	}
	
	public static void main(String[] args) {
		CommonMain cm = new CommonMain(0,2,"jacky","1");
		Thread t = new Thread(cm);
		t.start();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}

}
