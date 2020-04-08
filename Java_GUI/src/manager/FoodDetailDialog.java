package manager;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import model.EmpModel;
import tools.myFont;

public class FoodDetailDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	String fid = "";
	int flag = 0;
	int host = 5001;
	
	JPanel mp,p1,p2,p3;
	JButton add,up;
	JLabel id,n,nzh,p;
	JTextField idf,nf,nzhf,pf;
	JComboBox<Item> catf;
	JComboBox<String> ff;
	JCheckBox cb;
	
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
	
	public FoodDetailDialog(Frame owner,String title,boolean modal,int locale,int Flag,String foodid) {
		super(owner,title,modal);
		localeCurrent = locale;
		fid = foodid;
		flag = Flag;
		initLocale();
		
		mainPanel();
		
		add = new JButton(rb.getString("fj_add"));
		add.setFont(myFont.f1);
		add.addActionListener(this);
		
		up = new JButton(rb.getString("fj_up"));
		up.setFont(myFont.f1);
		up.addActionListener(this);
		
		this.add(mp,"Center");
		if(flag==0) {
			this.add(add,"South");
		}else {
			updateDetail();
			this.add(up,"South");
		}
		
		try {
			titleIcon = ImageIO.read(new File("images/logo/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setIconImage(titleIcon);
		int w = Toolkit.getDefaultToolkit().getScreenSize().width;
		int h = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(w/2-400, h/2-200);
		this.setSize(800, 400);
		this.setVisible(true);
	}
	
	public void mainPanel() {
		mp = new JPanel(new GridBagLayout());
		p1 = new JPanel();p2 = new JPanel();p3 = new JPanel();
		
		id = new JLabel("ID: ");
		id.setFont(myFont.f1);
		idf = new JTextField(5);
		idf.setEditable(false);
		idf.setFont(myFont.f1);
		
		EmpModel emp = new EmpModel();
		String sql = "";
		if(localeCurrent==0) {
			sql = "select c_id , c_n from ce_cat order by length(c_id),c_id ";
		}else {
			sql = "select c_id , c_nzh from ce_cat order by length(c_id),c_id ";
		}
		emp.runSql(sql);
		Vector<Item> model = new Vector<Item>();
		for(int i = 0; i<emp.getRowCount(); i++){
			model.addElement( new Item(emp.getValueAt(i, 0).toString(), emp.getValueAt(i, 1).toString()));
		}
		catf = new JComboBox<Item>(model);
		catf.setFont(myFont.f1);
		catf.setSelectedIndex(0);
		
		cb = new JCheckBox("  "+rb.getString("fj_sup"));  
		cb.setFont(myFont.f3);
		
		p1.add(id);p1.add(idf);p1.add(catf);p1.add(cb);
		
		n = new JLabel(rb.getString("fj_n"));
		n.setFont(myFont.f1);
		nf = new JTextField(10);
		nf.setFont(myFont.f1);
		
		nzh = new JLabel("  "+rb.getString("fj_nzh"));
		nzh.setFont(myFont.f1);
		nzhf = new JTextField(10);
		nzhf.setFont(myFont.f1);
		
		p2.add(n);p2.add(nf);p2.add(nzh);p2.add(nzhf);
		
		p = new JLabel(rb.getString("fj_p"));
		p.setFont(myFont.f1);
		pf = new JTextField(10);
		pf.setFont(myFont.f1);
		
		String[] list = {rb.getString("fj_f1"),rb.getString("fj_f2")};
		ff = new JComboBox<String>(list);
		ff.setFont(myFont.f1);
		ff.setSelectedIndex(0);
		
		p3.add(p);p3.add(pf);p3.add(ff);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		mp.add(p1,gbc);
		gbc.gridy = 1;
		mp.add(p2,gbc);
		gbc.gridy = 2;
		mp.add(p3,gbc);
	}
	
	public void updateDetail() {
		EmpModel emp = new EmpModel();
		String sql = "select * from ce_food where f_id='"+fid+"' ";
		emp.runSql(sql);
		
		idf.setText(emp.getValueAt(0, 0).toString());
		String cat = emp.getValueAt(0, 1).toString();
		nf.setText(emp.getValueAt(0, 2).toString());
		nzhf.setText(emp.getValueAt(0, 3).toString());
		pf.setText(emp.getValueAt(0, 4).toString());
		int y = Integer.parseInt(emp.getValueAt(0, 5).toString());
		
		sql = "select c_id from ce_cat order by length(c_id), c_id";
		emp.runSql(sql);
		
		int x=0;
		for(int s=0;s<emp.getRowCount();s++) {
			if(emp.getValueAt(s, 0).toString().equals(cat)) {
				x = s;
			}
		}
		catf.setSelectedIndex(x);
		ff.setSelectedIndex(y-1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add) {
			EmpModel emp = new EmpModel();
			String sql = "select fid_increase.nextval from dual ";
			emp.runSql(sql);
			String fid = emp.getValueAt(0, 0).toString();
			
			sql = "insert into ce_food values (?,?,?,?,?,?) ";
			Item item = (Item)catf.getSelectedItem();
			String cat = item.getId();
			String [] paras = {fid,cat,nf.getText(),nzhf.getText(),pf.getText(),(ff.getSelectedIndex()+1)+""};
			emp.updInfo(sql, paras);
				
			if(cb.isSelected()) {
				System.out.println("Socket:Add");
				
				Socket socket = null;
				try {
					socket = new Socket("localhost", host);
					System.out.println("Socket:Add, Server Connected!");
					
					OutputStream outputStream = null;
					DataOutputStream dataOutputStream;
					try {
						outputStream = socket.getOutputStream();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					dataOutputStream = new DataOutputStream(outputStream);
					
					try {
						String s = "{'cid':'pc','type':'1',"
								+ "'fid':'"+fid+"','cat':'"+cat+"','n':'"+nf.getText()+
								"','nzh':'"+nzhf.getText()+"',"+
								"'p':'"+pf.getText()+"','f':'"+(ff.getSelectedIndex()+1)+"'}";
						
						dataOutputStream.writeUTF(s);
						dataOutputStream.flush();
						dataOutputStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						socket.close();
						System.out.println("Closing socket and terminating program.");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JLabel w2 = new JLabel(rb.getString("w_soc"));
					w2.setFont(myFont.f1);
					JOptionPane.showMessageDialog(
						this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
			this.dispose();
		}else if(e.getSource() == up) {
			EmpModel emp = new EmpModel();
			String sql = "update ce_food set f_id=?,f_cat=?,f_n=?,f_nzh=?,f_p=?,f_f=? where f_id=? ";
			Item item = (Item)catf.getSelectedItem();
			String cat = item.getId();
			String [] paras = {idf.getText(),cat,nf.getText(),nzhf.getText(),pf.getText(),
					(ff.getSelectedIndex()+1)+"",idf.getText()};
			emp.updInfo(sql, paras);
			
			if(cb.isSelected()) {
				
				Socket socket = null;
				try {
					socket = new Socket("localhost", host);
					System.out.println("Socket:update, server Connected!");
					
					OutputStream outputStream = null;
					DataOutputStream dataOutputStream;
					try {
						outputStream = socket.getOutputStream();
					}catch (Exception e1) {
						e1.printStackTrace();
					}
					dataOutputStream = new DataOutputStream(outputStream);
					
					
					try {
						String s = "{'cid':'pc','type':'2',"
								+ "'fid':'"+idf.getText()+"','cat':'"+cat+"','n':'"+nf.getText()+
								"','nzh':'"+nzhf.getText()+"',"+
								"'p':'"+pf.getText()+"','f':'"+(ff.getSelectedIndex()+1)+"'}";
						
						dataOutputStream.writeUTF(s);
						dataOutputStream.flush();
						dataOutputStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					try {
						socket.close();
						System.out.println("Closing socket and terminating program.");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JLabel w2 = new JLabel(rb.getString("w_soc"));
					w2.setFont(myFont.f1);
					JOptionPane.showMessageDialog(
						this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
				}
			}
			this.dispose();
		}
	}
	
    class ItemRenderer extends BasicComboBoxRenderer{
		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus){
            super.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
 
            if (value != null){
                Item item = (Item)value;
                setText( item.getDescription().toUpperCase() );
            }
 
            if (index == -1){
                Item item = (Item)value;
                setText( "" + item.getId());
            }
            return this;
        }
    }
 
    class Item{
        private String id;
        private String description;
 
        public Item(String id, String description){
            this.id = id;
            this.description = description;
        }
 
        public String getId(){
            return id;
        }
 
        public String getDescription(){
            return description;
        }
 
        public String toString(){
            return description;
        }
    }
}
