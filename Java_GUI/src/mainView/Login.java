package mainView;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import model.EmpModel;
import tools.myFont;

public class Login extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	JTabbedPane jtp;
	JPanel bp1,p1,p2,p3;
	Border margin;
	JButton lan,logout,go,go2,go3;
	JComboBox<Item> shop,shop2;
	JLabel id,pw,id2,pw2,id3,pw3;
	JTextField idf,id2f,id3f;
	JPasswordField pwf,pw2f,pw3f;
	
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
	
	public void bp1Panel() {
		bp1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		lan = new JButton(rb.getString("com_lan"));
		lan.setFont(myFont.f3);
		lan.addActionListener(this);
		
		logout = new JButton(rb.getString("com_logout"));
		logout.setFont(myFont.f3);
		logout.addActionListener(this);
		
		bp1.add(lan);//bp1.add(logout);
	}
	
	public void p1Panel() {
		p1 = new JPanel(new GridLayout(8,0));
		margin = new EmptyBorder(10, 50, 10, 50);
		
		id = new JLabel(rb.getString("ln_id"));
		id.setFont(myFont.f3);
		idf = new JTextField(20);
		idf.setFont(myFont.f3);
		pw = new JLabel(rb.getString("ln_pw"));
		pw.setFont(myFont.f3);
		pwf = new JPasswordField(20);
		pwf.setFont(myFont.f3);
		
		Vector<Item> model = new Vector<Item>();
		String sql="";
		if(localeCurrent==0) {
			sql = "select s_id, s_a from ce_shop order by s_id";
		}else {
			sql = "select s_id, s_azh from ce_shop order by s_id";
		}
		EmpModel emp = new EmpModel();
		emp.runSql(sql);
		for(int i = 0; i<emp.getRowCount(); i++){
			model.addElement( new Item(emp.getValueAt(i, 0).toString(), emp.getValueAt(i, 1).toString()));
		}
		shop = new JComboBox<Item>(model);
		shop.setFont(myFont.f3);
		shop.setSelectedIndex(0);
		
		go = new JButton(rb.getString("com_go"));
		go.setFont(myFont.f3);
		go.addActionListener(this);
		
		JLabel sp = new JLabel(" ");
		sp.setFont(myFont.f1);
		JLabel sp2 = new JLabel(" ");
		sp2.setFont(myFont.f1);
		
		p1.add(id);p1.add(idf);

		p1.add(pw);p1.add(pwf);
		p1.add(sp);
		p1.add(shop);
		p1.add(sp2);
		p1.add(go);
		p1.setBorder(margin);
	}
	
	public void p2Panel() {
		p2 = new JPanel(new GridLayout(8,0));
		margin = new EmptyBorder(10, 50, 10, 50);
		
		id2 = new JLabel(rb.getString("ln_id")+" - "+rb.getString("ln_kit"));
		id2.setFont(myFont.f3);
		id2f = new JTextField(20);
		id2f.setFont(myFont.f3);
		pw2 = new JLabel(rb.getString("ln_pw"));
		pw2.setFont(myFont.f3);
		pw2f = new JPasswordField(20);
		pw2f.setFont(myFont.f3);
		
		Vector<Item> model = new Vector<Item>();
		String sql="";
		if(localeCurrent==0) {
			sql = "select s_id, s_a from ce_shop order by s_id";
		}else {
			sql = "select s_id, s_azh from ce_shop order by s_id";
		}
		EmpModel emp = new EmpModel();
		emp.runSql(sql);
		for(int i = 0; i<emp.getRowCount(); i++){
			model.addElement( new Item(emp.getValueAt(i, 0).toString(), emp.getValueAt(i, 1).toString()));
		}
		shop2 = new JComboBox<Item>(model);
		shop2.setFont(myFont.f3);
		shop2.setSelectedIndex(0);
		
		go2 = new JButton(rb.getString("com_go"));
		go2.setFont(myFont.f3);
		go2.addActionListener(this);
		
		JLabel sp = new JLabel(" ");
		sp.setFont(myFont.f1);
		JLabel sp2 = new JLabel(" ");
		sp2.setFont(myFont.f1);
		
		p2.add(id2);p2.add(id2f);
		p2.add(pw2);p2.add(pw2f);
		p2.add(sp);
		p2.add(shop2);
		p2.add(sp2);
		p2.add(go2);
		p2.setBorder(margin);
	}
	
	public void p3Panel() {
		p3 = new JPanel(new GridLayout(6,0));
		margin = new EmptyBorder(50, 50, 50, 50);
		
		id3 = new JLabel(rb.getString("ln_id"));
		id3.setFont(myFont.f3);
		id3f = new JTextField(20);
		id3f.setFont(myFont.f3);
		pw3 = new JLabel(rb.getString("ln_pw"));
		pw3.setFont(myFont.f3);
		pw3f = new JPasswordField(20);
		pw3f.setFont(myFont.f3);
		
		go3 = new JButton(rb.getString("com_go"));
		go3.setFont(myFont.f3);
		go3.addActionListener(this);
		
		JLabel sp = new JLabel(" ");
		sp.setFont(myFont.f1);
		
		p3.add(id3);p3.add(id3f);
		p3.add(pw3);p3.add(pw3f);
		p3.add(sp);
		p3.add(go3);
		p3.setBorder(margin);
	}
    
	public Login(int locale) {
		localeCurrent = locale;
		initLocale();
		
		bp1Panel();
		p1Panel();
		p2Panel();
		p3Panel();
		
		jtp = new JTabbedPane();
		jtp.add(p1,rb.getString("ln_com"));
		jtp.add(p2,rb.getString("ln_kit"));
		jtp.add(p3,rb.getString("ln_man"));
		jtp.setFont(myFont.f3);
		
		this.add(jtp,"Center");
		this.add(bp1,"South");
		
		try {
			titleIcon = ImageIO.read(new File("images/logo/logo.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setTitle(rb.getString("login_tl"));
		this.setSize(600,400);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation(width/2-300, height/2-200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(titleIcon);
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == lan) {
			int x = 0;
			if(localeCurrent == 0) {
				x = 1;
			}else {
				x = 0;
			}
			ResourceBundle.clearCache();
			this.dispose();
			new Login(x);
		}else if(e.getSource() == go) {
			int uid = 0;
			try {
				Item item = (Item)shop.getSelectedItem();
				String sid = item.getId();
				uid = Integer.parseInt(idf.getText());
				String pw= String.valueOf(pwf.getPassword());
				
				EmpModel emp = new EmpModel();
				String sql = "";
				if(localeCurrent==0) {
					sql = "select s_pw,s_n from ce_staff where s_id='"+uid+"' ";
				}else {
					sql = "select s_pw,s_nzh from ce_staff where s_id='"+uid+"' ";
				}
				emp.runSql(sql);
				
				try {
					String getpw = emp.getValueAt(0, 0).toString();
					String name = emp.getValueAt(0, 1).toString();
					if(pw.equals(getpw)) {
						CommonMain cm = new CommonMain(localeCurrent,uid,name,sid);
						Thread t = new Thread(cm);
						t.start();
						this.dispose();
					}else {
						JLabel w2 = new JLabel(rb.getString("w_upw"));
						w2.setFont(myFont.f1);
						JOptionPane.showMessageDialog(
							this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
					}
				}catch (Exception e3) {
					JLabel w2 = new JLabel(rb.getString("w_un"));
					w2.setFont(myFont.f1);
					JOptionPane.showMessageDialog(
						this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
				}
			}catch (Exception e2) {
				JLabel w2 = new JLabel(rb.getString("w_input"));
				w2.setFont(myFont.f1);
				JOptionPane.showMessageDialog(
					this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == go2) {
			int uid = 0;
			try {
				Item item = (Item)shop2.getSelectedItem();
				String sid = item.getId();
				uid = Integer.parseInt(id2f.getText());
				String pw= String.valueOf(pw2f.getPassword());
				
				EmpModel emp = new EmpModel();
				String sql = "";
				if(localeCurrent==0) {
					sql = "select s_pw,s_n from ce_staff where s_id='"+uid+"' ";
				}else {
					sql = "select s_pw,s_nzh from ce_staff where s_id='"+uid+"' ";
				}
				emp.runSql(sql);
				
				try {
					String getpw = emp.getValueAt(0, 0).toString();
					String name = emp.getValueAt(0, 1).toString();
					if(pw.equals(getpw)) {
						KitchenMain km = new KitchenMain(localeCurrent,uid,name,sid);
						Thread t = new Thread(km);
						t.start();
						this.dispose();
					}else {
						JLabel w2 = new JLabel(rb.getString("w_upw"));
						w2.setFont(myFont.f1);
						JOptionPane.showMessageDialog(
							this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
					}
				}catch (Exception e3) {
					e3.printStackTrace();
					JLabel w2 = new JLabel(rb.getString("w_un"));
					w2.setFont(myFont.f1);
					JOptionPane.showMessageDialog(
						this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
				}
			}catch (Exception e2) {
				JLabel w2 = new JLabel(rb.getString("w_input"));
				w2.setFont(myFont.f1);
				JOptionPane.showMessageDialog(
					this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
			}
		}else if(e.getSource() == go3) {
			int uid = 0;
			try {
				uid = Integer.parseInt(id3f.getText());
				String pw= String.valueOf(pw3f.getPassword());
				EmpModel emp = new EmpModel();
				String sql = "";
				if(localeCurrent==0) {
					sql = "select s_pw,s_n from ce_staff where s_id='"+uid+"' ";
				}else {
					sql = "select s_pw,s_nzh from ce_staff where s_id='"+uid+"' ";
				}
				emp.runSql(sql);
				
				try {
					String getpw = emp.getValueAt(0, 0).toString();
					String name = emp.getValueAt(0, 1).toString();
					if(pw.equals(getpw)) {
						new MangerMain(localeCurrent,uid,name);
						this.dispose();
					}else {
						JLabel w2 = new JLabel(rb.getString("w_upw"));
						w2.setFont(myFont.f1);
						JOptionPane.showMessageDialog(
							this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
					}
				}catch (Exception e3) {
					JLabel w2 = new JLabel(rb.getString("w_un"));
					w2.setFont(myFont.f1);
					JOptionPane.showMessageDialog(
						this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
				}
			}catch (Exception e2) {
				JLabel w2 = new JLabel(rb.getString("w_input"));
				w2.setFont(myFont.f1);
				JOptionPane.showMessageDialog(
					this,w2,rb.getString("w_s"),JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public static void main(String[] args) {
		new Login(0);
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
