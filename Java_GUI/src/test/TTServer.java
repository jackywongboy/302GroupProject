package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

import model.EmpModel;
import tools.myFont;
 
public class TTServer {
	
	public TTServer() {
		try {
			System.out.println("Server Start...");
			ServerSocket serverSocket = new ServerSocket(5010);
			
			try {
				while(true) {
					Socket socket = serverSocket.accept();
					System.out.println("...Server accepted...");
					startHandler(socket);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new TTServer();
	}
	
	private static void startHandler(final Socket socket) throws IOException{
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
					String line = reader.readLine();

					JSONObject jsonObject = new JSONObject(line);

					//System.out.println("JSON : "+jsonObject.toString());
					int type = Integer.parseInt(jsonObject.get("type").toString());
					
					switch(type) {
					case 1:
						String sid = jsonObject.get("shop").toString();
						String area = jsonObject.get("area").toString();
						
						int oid = 0;
						
						// Add Data to DataBase
		                
						try {
							EmpModel emp = new EmpModel();
							String sql = "select oid_increase.nextval from dual ";
							emp.runSql(sql);
							oid = Integer.parseInt(emp.getValueAt(0, 0).toString());
							LocalDateTime time = LocalDateTime.now();
							sql = "insert into ce_order values (?,?,?,TO_DATE(SYSDATE),?,?,?) ";
							String [] paras = {oid+"",sid,area,time.toString(),"0","2"};
							emp.updInfo(sql, paras);
						}catch (Exception e2) {
							e2.printStackTrace();
						}
		                
		                // End
						
						System.out.println("Type: "+jsonObject.get("type"));
						System.out.println("Shopid: "+jsonObject.get("shop"));
						System.out.println("Area: "+jsonObject.get("area"));
						
						try {
							JSONObject food = new JSONObject(jsonObject.get("food").toString());
							for(int i=0;i<food.names().length();i++) {					
								//System.out.print(food.names().get(i)+" : "+food.get(food.names().get(i).toString()));
								
								JSONArray t = new JSONArray(food.get(food.names().get(i).toString()).toString());
								for (int j = 0; j < t.length(); j++) {
					                JSONObject jsonObject2 = t.getJSONObject(j);
					                
					                String oiid = "";
					                // Add Data to DataBase
					                try {
					                	EmpModel emp = new EmpModel();
						                String sql = "select oiid_increase.nextval from dual ";
						                emp.runSql(sql);
						                oiid = emp.getValueAt(0, 0).toString();
						                
						                String fid = jsonObject2.getInt("fid")+"";
						                sql = "select f_p from ce_food where f_id='"+fid+"' ";
						                emp.runSql(sql);
						                String fp = emp.getValueAt(0, 0).toString();
						                
						                sql = "insert into ce_orderitem values (?,?,?,?,?,?,?,?,?,?) ";
						                String[] paras = {oiid,oid+"",sid,area,jsonObject2.getString("time"),
						                		fp,"2",fid,"1","na"};
						                emp.updInfo(sql, paras);
					                }catch (Exception e) {
										e.printStackTrace();
									}
					                
					                
					                // End
					                
					                System.out.print("FoodID: "+jsonObject2.getInt("fid"));
					                System.out.print("  OT: "+jsonObject2.getString("time"));
					                if(jsonObject2.getString("sid").toString().equals("na")) {
					                	; // No Special requirement
					                }else {
					                	String[] buff = jsonObject2.getString("sid").toString().split("_");
					                	System.out.print("  SpecialID: ");
					                	for(int b=0;b<buff.length;b++) {
					                		
					                		// Add Data to DataBase
							                try {
							                	EmpModel emp = new EmpModel();
							                	String sql = "";
							                	sql = "select s_p from ce_special where s_id = '"+buff[b]+"' ";
						                		emp.runSql(sql);
						                		String sp = emp.getValueAt(0, 0).toString();
						                		
						                		sql = "insert into ce_sitem values (sitem_increase.nextval,?,?,?) ";
						                		String[] paras2 = {buff[b],oiid,sp};
						                		emp.updInfo(sql, paras2);
							                }catch (Exception e) {
												e.printStackTrace();
											}
					                		
					                		
					                		// End
					                		System.out.print(buff[b]+",");
					                	}
					                }
					                System.out.println();
								}
							}
						}catch (Exception e) {
							System.out.println("No food");
						}
						
						try {
							JSONObject combo = new JSONObject(jsonObject.get("com").toString());
							for(int i=0;i<combo.names().length();i++) {					
								//System.out.print(food.names().get(i)+" : "+food.get(food.names().get(i).toString()));
								
								JSONArray t = new JSONArray(combo.get(combo.names().get(i).toString()).toString());
								for (int j = 0; j < t.length(); j++) {
					                JSONObject jsonObject2 = t.getJSONObject(j);
					                System.out.print("ComboID: "+jsonObject2.getInt("cid"));
					                
					                String cid= "";
					                // Add to DataBase
					                try {
					                	EmpModel emp = new EmpModel();
						                String sql = "select setCall_increase.nextval from dual ";
						                emp.runSql(sql);
						                cid = jsonObject2.getInt("cid")+"_"+emp.getValueAt(0, 0).toString();
						                
						                sql = "select s_p from ce_set where s_id='"+jsonObject2.getInt("cid")+"' ";
						                emp.runSql(sql);
						                String cp = emp.getValueAt(0, 0).toString();
						                System.out.println(sql);
						                
						                sql = "insert into ce_orderitem values "
						                		+ "(oiid_increase.nextval,?,?,?,?,?,?,?,?,?) ";
						                String[] paras = {oid+"",sid,area,jsonObject2.getString("time"),
						                		cp,"2","0","2",cid};
						                emp.updInfo(sql, paras);
					                }catch (Exception e) {
										e.printStackTrace();
									}
					                
					                // End
					                
					                String[] buff = jsonObject2.getString("fid").toString().split(",");
					                System.out.print("  FoodID: ");
				                	for(int b=0;b<buff.length;b++) {
				                		
				                		if(buff[b].contains(":")) {
				                			String[] buff2 = buff[b].split(":");
				                			String fid = buff2[0];
				                			
				                			String oiid = "";
				                			// Add DataBase
							                try {
							                	EmpModel emp = new EmpModel();
							                	String sql = "";
							                	sql = "select oiid_increase.nextval from dual ";
								                emp.runSql(sql);
								                oiid = emp.getValueAt(0, 0).toString();
								                
					                			sql = "insert into ce_orderitem values "
								                		+ "(?,?,?,?,?,?,?,?,?,?) ";
								                String[] paras2 = {oiid,oid+"",sid,area,jsonObject2.getString("time"),
								                		"0","2",fid,"1",cid};
								                emp.updInfo(sql, paras2);
							                }catch (Exception e) {
												e.printStackTrace();
											}
				                			// End
				                			
				                			String[] buff3 = buff2[1].split("_");
				                			System.out.print(" "+fid+"(sid: ");
				                			for(int e=0;e<buff3.length;e++) {
				                				System.out.print(buff3[e]+",");
				                				
				                				// Add to DataBase
				                				try {
				                					EmpModel emp = new EmpModel();
				                					String sql = "";
									                
				                					sql = "select s_p from ce_special where s_id = '"+buff3[e]+"' ";
							                		emp.runSql(sql);
							                		String sp = emp.getValueAt(0, 0).toString();
							                		
							                		sql = "insert into ce_sitem values (sitem_increase.nextval,?,?,?) ";
							                		String[] paras3 = {buff3[e],oiid,sp};
							                		emp.updInfo(sql, paras3);
				                				}catch (Exception e2) {
													e2.printStackTrace();
												}
				                				
				                				// End
				                			}
				                			System.out.print(" ), ");
				                			
				                		}else {
				                			System.out.print(buff[b]+",");
			                				
			                				// Add to DataBase
			                				try {
			                					String oiid = "";
			                					
			                					EmpModel emp = new EmpModel();
			                					String sql = "";
			                					
			                					sql = "select oiid_increase.nextval from dual ";
								                emp.runSql(sql);
								                oiid = emp.getValueAt(0, 0).toString();
								                
								                sql = "insert into ce_orderitem values "
								                		+ "(?,?,?,?,?,?,?,?,?,?) ";
								                String[] paras2 = {oiid,oid+"",sid,area,jsonObject2.getString("time"),
								                		"0","2",buff[b],"1",cid};
								                emp.updInfo(sql, paras2);
			                				}catch (Exception e2) {
												e2.printStackTrace();
											}
			                				
			                				// End
				                		}
				                	}
					                System.out.print("  OT: "+jsonObject2.getString("time"));
					                
					                System.out.println();
								}
							}	
						}catch(Exception e) {
							System.out.println("No combo");
						}
						
						
						PrintWriter out = new PrintWriter(socket.getOutputStream()); //message ready to send to Python
						out.println("Your Order ID is "+oid); 
						out.flush(); // send to Python
						
						System.out.println("---Order added---");
						break;
					case 2:
						break;
					case 3:
						break;
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					closeSocket();
				}
			}
			private void closeSocket() {
				try {
					socket.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
}
