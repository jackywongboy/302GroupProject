package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JOptionPane;

public class SqlHelper {
    java.util.Properties prop = new java.util.Properties();
    
    String DbSID     = "xe";
    String HostName  = "144.214.177.102";
    String username  = "G1_team05";
    String userpwd   = "cityu123";
    
    String url = "jdbc:oracle:thin:@"+ HostName + ":1521:" + DbSID;     
    Connection conDB = null;
    
    PreparedStatement ps=null;
	ResultSet rs=null;
 
    public Connection getConn(){
        try{
            prop.put("user",         username);
            prop.put("password",     userpwd);
            prop.put("CHARSET",     "eucksc");
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            conDB = DriverManager.getConnection(url, username, userpwd);            
            //System.out.println("DataBase connected : " + conDB.toString());
            conDB.setAutoCommit(false);
        }
        catch(ClassNotFoundException cnfe){
            System.out.println("Driver didn't be load : " + cnfe.toString());
        }
        catch(SQLException sqle){
            System.out.println("DataBase didn't connected : " + sqle.toString());
        }
        return conDB;
    }
    
	public ResultSet queryExectue(String sql){
		try {
			getConn();
			//System.out.println(sql);
			Statement stmt=conDB.createStatement();  
			rs=stmt.executeQuery(sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean updExecute(String sql,String []paras){
		boolean b=true;
		try {
			getConn();

			ps=conDB.prepareStatement(sql);
			for(int i=0;i<paras.length;i++){
				ps.setString(i+1, paras[i]);
			}
			if(ps.executeUpdate()!=1){
				b=false;
			}
			
		} catch (Exception e) {
			b=false;
			JOptionPane.showMessageDialog(null, "Error on connecting to DataBase", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}finally{
			this.close();
		}
		return b;
	}
	
	public void close() {
		try {
			conDB.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
