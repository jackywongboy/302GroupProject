package foodView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import model.EmpModel;
import tools.myFont;

public class FoodSpecialDialog extends JDialog implements MouseListener{
	private static final long serialVersionUID = 1L;
	int oid = 0;
	String cat = "";
	String sid = "";
	
	JPanel m,rp,lp;
	DefaultTableModel tm,tm2;
	JTable jt1,jt2;
	JScrollPane js,js2;
	JLabel ll,rl;
	
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
	
	public FoodSpecialDialog(int locale,int orderid) {
		localeCurrent = locale;
		oid = orderid;
		initLocale();
		
		m = new JPanel(new GridLayout(0,2));
		
		EmpModel emp = new EmpModel();
		String sql = "select oi_fid from ce_orderitem where oi_id = '"+oid+"' ";
		emp.runSql(sql);
		String fid = emp.getValueAt(0, 0).toString();
		sql = "select f_cat from ce_food where f_id = '"+fid+"' ";
		emp.runSql(sql);
		cat = emp.getValueAt(0, 0).toString();
		
		rpPanel();
		lpPanel();
		
		m.add(lp);m.add(rp);
		
		this.add(m,"Center");
		
		try {
			titleIcon = ImageIO.read(new File("images/logo/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setTitle(rb.getString("special_tl"));
		this.setIconImage(titleIcon);
		this.setSize(1000,500);
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(w/2-500, h/2-250);
		this.setVisible(true);
	}
	
	public void lpPanel() {
		lp = new JPanel(new BorderLayout());
		
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
		tm.addColumn("Add");
		
		EmpModel emp = new EmpModel();
		String sql = "";
		if(localeCurrent==0) {
			sql = "select s_id,s_n,s_p from ce_special where s_cat='"+cat+"' or s_cat='com' ";
		}else {
			sql = "select s_id,s_nzh,s_p from ce_special where s_cat='"+cat+"' or s_cat='com' ";
		}
		emp.runSql(sql);
		
		for(int i=0;i<emp.getRowCount();i++) {
			Vector<String> vv = new Vector<String>();
			vv.addElement(emp.getValueAt(i, 0).toString());
			vv.addElement(emp.getValueAt(i, 1).toString());
			vv.addElement(emp.getValueAt(i, 2).toString());
			vv.addElement(rb.getString("ao_add"));
			tm.addRow(vv);
		}
		jt1 = new JTable(tm){
			private static final long serialVersionUID = 1L;

			@Override
		    public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
		        Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
		        
		        if(columnIndex == 3) {
		        	componenet.setBackground(Color.GREEN);
		        	componenet.setForeground(Color.RED);
		        }else {
		        	componenet.setBackground(Color.white);
		        	componenet.setForeground(Color.black);
		        }
		        return componenet;
		    }
		};
		jt1.setAutoCreateRowSorter(true);
		jt1.setRowHeight(30);
		jt1.setFont(myFont.f1);
		jt1.addMouseListener(this);
		js = new JScrollPane(jt1);
		
		ll = new JLabel(rb.getString("as_item"),JLabel.LEFT);
		ll.setFont(myFont.f1);
		
		lp.add(ll,"North");
		lp.add(js,"Center");
		setTable(1);
	}
	
	public void rpPanel() {
		rp = new JPanel(new BorderLayout());
		
		tm2 = new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		tm2.addColumn("ID");
		tm2.addColumn("Name");
		tm2.addColumn("Price");
		tm2.addColumn("Delete");
		
		jt2 = new JTable(tm2){
			private static final long serialVersionUID = 1L;

			@Override
		    public Component prepareRenderer (TableCellRenderer renderer, int rowIndex, int columnIndex){
		        Component componenet = super.prepareRenderer(renderer, rowIndex, columnIndex);
		        
		        if(columnIndex == 3) {
		        	componenet.setBackground(Color.YELLOW);
		        	componenet.setForeground(Color.RED);
		        }else {
		        	componenet.setBackground(Color.white);
		        	componenet.setForeground(Color.black);
		        }
		        return componenet;
		    }
		};
		jt2.setAutoCreateRowSorter(true);
		jt2.setRowHeight(30);
		jt2.setFont(myFont.f1);
		jt2.addMouseListener(this);
		js2 = new JScrollPane(jt2);
		
		rl = new JLabel(rb.getString("as_selected"),JLabel.RIGHT);
		rl.setFont(myFont.f1);
		
		rp.add(rl,"North");
		rp.add(js2,"Center");
		setTable(2);
		updateTable();
	}
	
	public void updateTable() {
		EmpModel emp = new EmpModel();
		String sql = "";
		if(localeCurrent==0) {
			sql = "select si_id,s_n,si_p from ( " + 
				"select ss.*,sf.* from ce_sitem ss join ce_special sf on ss.si_sid=sf.s_id ) " + 
				"ce_sitem where si_oiid = '"+oid+"' ";
		}else {
			sql = "select si_id,s_nzh,si_p from ( " + 
					"select ss.*,sf.* from ce_sitem ss join ce_special sf on ss.si_sid=sf.s_id ) " + 
					"ce_sitem where si_oiid = '"+oid+"' ";
		}
		emp.runSql(sql);
		tm2.setRowCount(0);
		for(int i=0;i<emp.getRowCount();i++) {
			Vector<String> vv = new Vector<String>();
			vv.addElement(emp.getValueAt(i, 0).toString());
			vv.addElement(emp.getValueAt(i, 1).toString());
			vv.addElement(emp.getValueAt(i, 2).toString());
			vv.addElement(rb.getString("ao_del"));
			tm2.addRow(vv);
		}
	}
	
	public void setTable(int flag) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		if(flag==1) {
			jt1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			jt1.getColumnModel().getColumn(0).setPreferredWidth(25);
			jt1.getColumnModel().getColumn(1).setPreferredWidth(200);
			jt1.getColumnModel().getColumn(2).setPreferredWidth(100);
			jt1.getColumnModel().getColumn(3).setPreferredWidth(100);
		}else if(flag==2) {
			jt2.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			jt2.getColumnModel().getColumn(0).setPreferredWidth(25);
			jt2.getColumnModel().getColumn(1).setPreferredWidth(200);
			jt2.getColumnModel().getColumn(2).setPreferredWidth(100);
			jt2.getColumnModel().getColumn(3).setPreferredWidth(100);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == jt1) {
			int col = jt1.columnAtPoint(e.getPoint());
			if(col==3) {
				String sp = jt1.getValueAt(jt1.getSelectedRow(), 2).toString();
				String sid = jt1.getValueAt(jt1.getSelectedRow(), 0).toString();
				EmpModel emp = new EmpModel();
				String sql = "insert into ce_sitem values (sitem_increase.nextval,?,?,?) ";
				String[] paras = {sid,oid+"",sp};
				emp.updInfo(sql, paras);
				updateTable();
			}
			
		}else if(e.getSource() == jt2) {
			int col = jt2.columnAtPoint(e.getPoint());
			if(col==3) {
				String sid = jt2.getValueAt(jt2.getSelectedRow(), 0).toString();
				EmpModel emp = new EmpModel();
				String sql = "delete from ce_sitem where si_id=? ";
				String[] paras = {sid};
				emp.updInfo(sql, paras);
				updateTable();
			}
		}
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
