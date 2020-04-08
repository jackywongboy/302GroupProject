package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import model.SqlHelper;
 
public class EmpModel extends AbstractTableModel{
    
    Vector <String> colums;
    Vector <Vector> rows;
    SqlHelper sqlHelper=null;
 
    public void runSql(String sql){
        try{
        	rows = new Vector<Vector>();
        	colums = new Vector<String>();
        	
        	sqlHelper=new SqlHelper();
			ResultSet rs=sqlHelper.queryExectue(sql);
        	
        	ResultSetMetaData rsmt = rs.getMetaData();
        	int loop = rsmt.getColumnCount();

        	while(rs.next()) {
        		Vector<String> temp = new Vector<String>();
        		
        		for(int i=0;i<loop;i++) {
            		temp.add(rs.getString(i+1));
            	}
        		
        		rows.add(temp);
        		
        		}
        	for(int i=0;i<rsmt.getColumnCount();i++) {
        		this.colums.add(rsmt.getColumnName(i+1));
        	}
        	//System.out.println(rows);
            }catch(SQLException sqle){
            	System.out.println(sqle);
        	}finally {
        		sqlHelper.close();
        	}
        }
    
	public boolean updInfo(String sql,String []paras){
		SqlHelper sqlHelper=new SqlHelper();
		return sqlHelper.updExecute(sql, paras);
	}
    
    public static void main(String[] args) {
    	EmpModel n = new EmpModel();
    }
	@Override
	public int getColumnCount() {
		return this.colums.size();
	}
	@Override
	public int getRowCount() {
		return this.rows.size();
	}
	@Override
	public Object getValueAt(int arg0, int arg1) {
		return ((Vector)rows.get(arg0)).get(arg1);
	}

	@Override
	public String getColumnName(int column) {
		return this.colums.get(column).toString();
	}
	
}
