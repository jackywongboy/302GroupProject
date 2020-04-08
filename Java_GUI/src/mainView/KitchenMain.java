package mainView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.EmpModel;
import test.TTServer;
import tools.myFont;

public class KitchenMain extends JFrame implements ActionListener, Runnable{
	private static final long serialVersionUID = 1L;
	SwingWorker<Void,Void> worker = null;
	int uid = 0;
	int loopFlag = 0;
	boolean FLAG = true;
	String sname = "";
	String sid = "";
	
	JPanel mp,up,bp,bp1,bp2,bp3;
	JLabel staff,time,hint1,hint2;
	JButton language, logout;
	Timer t;
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	JPanel[] item = new JPanel[10];
	JTable[] jt = new JTable[10];
	JScrollPane[] js = new JScrollPane[10];
	JButton[] jb = new JButton[10];
	JLabel[] jl = new JLabel[10];
	DefaultTableModel[] tm = new DefaultTableModel[10];
	
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
	
	public KitchenMain(int locale,int staffid,String staffname,String shopid) {
		localeCurrent = locale;
		uid = staffid;
		sname = staffname;
		sid = shopid;
		initLocale();
		
		bpPanel();
		mainPanel();
		
		up = new JPanel(new BorderLayout());
		hint1 = new JLabel("   "+rb.getString("km_hint1"),JLabel.CENTER);
		hint1.setFont(myFont.f1);
		hint1.setForeground(Color.blue);
		hint2 = new JLabel(rb.getString("km_hint2")+"   ",JLabel.CENTER);
		hint2.setFont(myFont.f1);
		hint2.setForeground(new Color(3, 128, 23));
		up.add(hint1,"West");up.add(hint2,"East");
		this.add(up,"North");
		this.add(mp,"Center");
		this.add(bp,"South");
		
		start();
		
		try {
			titleIcon = ImageIO.read(new File("images/logo/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setTitle(rb.getString("kit_tl")+"  shop"+sid);
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
		mp = new JPanel(new GridBagLayout());
		
		updateTable();
	}
	
	public void updateTable() {
		EmpModel emp = new EmpModel();
		String sql = "select count(*) from ce_orderitem where oi_sid='"+sid+"' ";
		emp.runSql(sql);
		int nflag = Integer.parseInt(emp.getValueAt(0, 0).toString());
		
		if(loopFlag!=nflag) {
			loopFlag=nflag;
			sql = "select o_id from ce_order where o_f='2' and o_sid='"+sid+"' order by o_id";
			emp.runSql(sql);
			
			mp.removeAll();
			
			GridBagConstraints gbc = new GridBagConstraints();
			int x = 0;
			int u = 0;
			int n = 0;

			int loop=0;
			if(emp.getRowCount()>6) {
				loop=6;
			}else {
				loop=emp.getRowCount();
			}

			for(int i=0;i<loop;i++) {
				item[i] = new JPanel(new BorderLayout());
				
				String oid = emp.getValueAt(i, 0).toString();
				
				jl[i] = new JLabel("Order ID: "+oid,JLabel.CENTER);
				jl[i].setFont(myFont.f1);
				
				if(localeCurrent==0) {
					sql = "select oi_a,f_n,oi_dt,oi_id from ( " + 
							"select oi.*,ff.* from ce_orderitem oi join ce_food ff on oi.oi_fid=ff.f_id ) " + 
							" where oi_oid = '"+oid+"' ";
				}else {
					sql = "select oi_a,f_nzh,oi_dt,oi_id from ( " + 
							"select oi.*,ff.* from ce_orderitem oi join ce_food ff on oi.oi_fid=ff.f_id ) " + 
							" where oi_oid = '"+oid+"' ";
				}
				EmpModel emp22 = new EmpModel();
				emp22.runSql(sql);
				tm[i] = new DefaultTableModel(){
					private static final long serialVersionUID = 1L;

					@Override
				    public boolean isCellEditable(int row, int column) {
				       return false;
				    }
				};
				tm[i].addColumn("Area");
				tm[i].addColumn("Name");
				tm[i].addColumn("Time");
				tm[i].addColumn("Special");
				
				for(int y=0;y<emp22.getRowCount();y++) {
					Vector<String> vv = new Vector<String>();
					for(int q=0;q<emp22.getColumnCount()-1;q++) {
						if(q==2) {
							try {
								vv.addElement(emp22.getValueAt(y, q).toString().substring(11,19));
							}catch (Exception e) {
								vv.addElement(emp22.getValueAt(y, q).toString());
							}
							
						}else {
							vv.addElement(emp22.getValueAt(y, q).toString());
						}
					}
					String oiid = emp22.getValueAt(y, 3).toString();
					String sn = "";
					EmpModel emp2 = new EmpModel();
					String sql2 = "";
					if(localeCurrent==0) {
						sql2 = "select s_n from ( " + 
								"select ss.*,sf.* from ce_sitem ss join ce_special sf on ss.si_sid=sf.s_id ) " + 
								"where si_oiid = '"+oiid+"' ";
					}else {
						sql2 = "select s_nzh from ( " + 
								"select ss.*,sf.* from ce_sitem ss join ce_special sf on ss.si_sid=sf.s_id ) " + 
								"where si_oiid = '"+oiid+"' ";
					}
					emp2.runSql(sql2);
					for(int s=0;s<emp2.getRowCount();s++) {
						if(s==0) {
							sn = emp2.getValueAt(s, 0).toString();
						}else {
							sn = sn + "," + emp2.getValueAt(s, 0).toString();
						}
					}
					vv.addElement(sn);
					tm[i].addRow(vv);
				}
				jt[i] = new JTable(tm[i]){
					private static final long serialVersionUID = 1L;

					@Override
				    public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
				        Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
				        
				        String area = getModel().getValueAt(rowIndex,0).toString();
				        if(area.equals("0")) {
				        	componenet.setBackground(new Color(156, 255, 164));
				            componenet.setForeground(Color.BLACK);
				            
				        }else if(area.equals("1")) {
				        	componenet.setBackground(new Color(135, 191, 255));
				            componenet.setForeground(Color.BLACK);
				        }else {
				        	componenet.setBackground(Color.white);
				            componenet.setForeground(Color.BLACK);
				        }
				        return componenet;
				    }
				};
				jt[i].setAutoCreateRowSorter(true);
				jt[i].setRowHeight(30);
				jt[i].setFont(myFont.f3);
				
				jt[i].getColumnModel().getColumn(0).setPreferredWidth(0);
				jt[i].getColumnModel().getColumn(1).setPreferredWidth(500);
				jt[i].getColumnModel().getColumn(2).setPreferredWidth(100);
				jt[i].getColumnModel().getColumn(3).setPreferredWidth(200);
				
				js[i] = new JScrollPane(jt[i]);
				
				jb[i] = new JButton(rb.getString("km_ok"));
				jb[i].setFont(myFont.f1);
				jb[i].setName(i+","+oid);
				jb[i].addActionListener(this);
				
				item[i].add(jl[i],"North");
				item[i].add(js[i],"Center");
				item[i].add(jb[i],"South");
				
				item[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
				
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weightx = 1;
				gbc.weighty = 1;
				gbc.gridy = x;
				gbc.gridx = u;
				mp.add(item[i],gbc);
				u++;
				if(u==3) {
					u=0;
				}
				if(i==2) {
					n=i;
					x+=1;
				}
				if(i-n==3) {
					n=i;
					x+=1;
				}
			}
		}
		mp.validate();
		mp.repaint();
	}
	
	@Override
	public void run() {
		while(FLAG) {
			try {
				Thread.sleep(1000);
				updateTable();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			KitchenMain km = new KitchenMain(x,uid,sname,sid);
			Thread t = new Thread(km);
			t.start();
		}else if(e.getSource() == logout) {
			FLAG = false;
			this.dispose();
			new Login(localeCurrent);
		}else {
			try {
				JButton b = (JButton)e.getSource();
				String[] buff = b.getName().split(",");
				int jname = Integer.parseInt(buff[0]);
			    String oid = buff[1];
			    try {
			    	int noid = Integer.parseInt(oid);
			    	try {
			    		EmpModel emp = new EmpModel();
			    		String sql = "update ce_orderitem set oi_f=? where oi_oid=? ";
			    		String[] paras = {"3",noid+""};
			    		emp.updInfo(sql, paras);
			    		
			    		sql = "update ce_order set o_f=? where o_id=? ";
			    		String[] paras2 = {"3",noid+""};
			    		emp.updInfo(sql, paras2);
			    		
			    		mp.remove(item[jname]);
			    		mp.validate();
			    		mp.repaint();
			    		
			    	}catch (Exception e3) {
			    		e3.printStackTrace();
			    		JLabel w2 = new JLabel(rb.getString("w_int"));
						w2.setFont(myFont.f1);
						JOptionPane.showMessageDialog(
							this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
					}
			    }catch (Exception e3) {
			    	e3.printStackTrace();
			    }
			}catch (ClassCastException e2) {
			}catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private void start() {
		worker = new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
				try {
					Thread.sleep(1000);
					if(FLAG) {
						new TTServer();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		worker.execute();
	}
	
	public static void main(String[] args) {
		KitchenMain km = new KitchenMain(0,2,"jacky","1");
		Thread t = new Thread(km);
		t.start();
	}
}
