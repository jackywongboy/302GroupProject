package foodView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import mainView.CommonMain;
import model.EmpModel;
import tools.myFont;

public class FoodAddPanel extends JPanel implements ActionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	JPanel m;
	JTable jt;
	JScrollPane js;
	JButton exit;
	DefaultTableModel tm;
	
	int tt = 0;
	int oid = 0;
	String sid = "";
	int af = 0;
	
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
	
	public FoodAddPanel(int locale,int type,int orderid,String shopid,int areaflag) {
		localeCurrent = locale;
		oid = orderid;
		sid = shopid;
		af = areaflag;
		tt = type;
		initLocale();
		
		m = new JPanel(new BorderLayout());
		mainPanel();
		this.setLayout(new BorderLayout());
		this.add(m,"Center");
	}
	
	public void mainPanel() {
		exit = new JButton(rb.getString("com_exit"));
		exit.setFont(myFont.f1);
		exit.addActionListener(this);
		
		EmpModel emp = new EmpModel();
		String sql = "";
		if(localeCurrent==0) {
			switch(tt) {
			case 1:
				sql = "select f_id,f_n,f_p from ce_food where (f_cat='ce' or f_cat='ie') and f_f='1' ";
				break;
			case 2:
				sql = "select s_id,s_n,s_p,s_fl from ce_set where s_f='1' ";
				break;
			case 3:
				sql = "select f_id,f_n,f_p from ce_food where f_cat='sh' and f_f='1' ";
				break;
			case 4:
				sql = "select f_id,f_n,f_p from ce_food where f_cat='sd' and f_f='1' ";
				break;
			default:
				sql = "select f_id,f_n,f_p from ce_food where (f_cat='ce' or f_cat='ie') and f_f='1' ";
				break;
			}
		}else {
			switch(tt) {
			case 1:
				sql = "select f_id,f_nzh,f_p from ce_food where (f_cat='ce' or f_cat='ie') and f_f='1' ";
				break;
			case 2:
				sql = "select s_id,s_nzh,s_p,s_fl from ce_set where s_f='1' ";
				break;
			case 3:
				sql = "select f_id,f_nzh,f_p from ce_food where f_cat='sh' and f_f='1' ";
				break;
			case 4:
				sql = "select f_id,f_nzh,f_p from ce_food where f_cat='sd' and f_f='1' ";
				break;
			default:
				sql = "select f_id,f_nzh,f_p from ce_food where (f_cat='ce' or f_cat='ie') and f_f='1' ";
				break;
			}
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
		tm.addColumn("h");
		tm.addColumn("Add");
		
		for(int y=0;y<emp.getRowCount();y++) {
			Vector<String> vv = new Vector<String>();
			if(tt==2) {
				vv.addElement(emp.getValueAt(y, 0).toString());
				vv.addElement(emp.getValueAt(y, 1).toString());
				vv.addElement(emp.getValueAt(y, 2).toString());
				vv.addElement(emp.getValueAt(y, 3).toString());
				vv.addElement(rb.getString("ao_add"));
			}else {
				vv.addElement(emp.getValueAt(y, 0).toString());
				vv.addElement(emp.getValueAt(y, 1).toString());
				vv.addElement(emp.getValueAt(y, 2).toString());
				vv.addElement("NA");
				vv.addElement(rb.getString("ao_add"));
			}
			tm.addRow(vv);
		}
		jt = new JTable(tm){
			private static final long serialVersionUID = 1L;

			@Override
		    public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
		        Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
		        
		        if(columnIndex == 4) {
		        	componenet.setBackground(Color.GREEN);
		        	componenet.setForeground(Color.RED);
		        }else {
		        	componenet.setBackground(Color.white);
		        	componenet.setForeground(Color.black);
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
		
		m.add(exit,"North");
		m.add(js,"Center");
	}
	
	public void setTable() {
		jt.getColumnModel().getColumn(0).setPreferredWidth(25);
		jt.getColumnModel().getColumn(1).setPreferredWidth(500);
		jt.getColumnModel().getColumn(2).setPreferredWidth(100);
		jt.getColumnModel().getColumn(3).setPreferredWidth(0);
		jt.getColumnModel().getColumn(4).setPreferredWidth(80);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		jt.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == jt) {
			int col = jt.columnAtPoint(e.getPoint());
			if(col==4) {
				String type = jt.getValueAt(jt.getSelectedRow(), 3).toString();
				if(type.equals("NA")) {
					int fid = Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 0).toString());
					String fp = jt.getValueAt(jt.getSelectedRow(), 2).toString();
					
					EmpModel emp = new EmpModel();
					String sql = "insert into ce_orderitem values (oiid_increase.nextval,?,?,?,?,?,?,?,?,?) ";
					LocalDateTime time = LocalDateTime.now();
					String[] paras = {oid+"",sid,af+"",time.toString(),fp,"1",fid+"","1","na"};
					emp.updInfo(sql, paras);
					
				}else {
					int cid = Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 0).toString());
					String fh = type;
					
					EmpModel emp = new EmpModel();
					String sql = "";
					
					String[] buff = fh.split("_");
					
					sql = "select setCall_increase.nextval from dual ";
					emp.runSql(sql);
					String setCall = emp.getValueAt(0, 0).toString();
					
					sql = "select s_p from ce_set where s_id='"+cid+"' ";
					emp.runSql(sql);
					String cp = emp.getValueAt(0, 0).toString();
					
					sql = "insert into ce_orderitem values (oiid_increase.nextval,?,?,?,?,?,?,?,?,?) ";
					LocalDateTime time2 = LocalDateTime.now();
					String[] paras2 = {oid+"",sid,af+"",time2.toString(),
							cp+"","1","0","2",cid+"_"+setCall};
					emp.updInfo(sql, paras2);
					
					for(int i=0;i<buff.length;i++) {
						
						if(localeCurrent==0) {
							sql = "select f_n from ce_food where f_id='"+buff[i]+"' ";
							emp.runSql(sql);
							
							sql = "insert into ce_orderitem values (oiid_increase.nextval,?,?,?,?,?,?,?,?,?) ";
							LocalDateTime time = LocalDateTime.now();
							String[] paras = {oid+"",sid,af+"",time.toString(),
									"0","1",buff[i]+"","1",cid+"_"+setCall};
							emp.updInfo(sql, paras);
							
						}else {
							sql = "select f_nzh from ce_food where f_id='"+buff[i]+"' ";
							emp.runSql(sql);
							
							sql = "insert into ce_orderitem values (oiid_increase.nextval,?,?,?,?,?,?,?,?,?) ";
							LocalDateTime time = LocalDateTime.now();
							String[] paras = {oid+"",sid,af+"",time.toString(),
									"0","1",buff[i]+"","1",cid+"_"+setCall};
							emp.updInfo(sql, paras);
						}
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exit) {
			this.setVisible(false);
		}
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
