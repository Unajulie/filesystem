package cn.believeus.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import org.apache.log4j.Logger;

import cn.believeus.entity.Tmenu;

public class DBUtil {
	private static final Logger LOGGER=Logger.getLogger(DBUtil.class);
	public static final Stack<Connection> POOL = new Stack<Connection>();
	public static void close(Connection con){
		DBUtil.POOL.add(con);
	}
	static{
		try {
			String connsize=ResourceBundle.getBundle("project").getString("connsize");
			Class.forName("com.mysql.jdbc.Driver");
			for (int i = 0; i <Integer.parseInt(connsize); i++) {
				String url = "jdbc:mysql://localhost:3306/vbox";
				// �������ݿ�������Ҫ�˷�ʱ��
				Connection conn = DriverManager.getConnection(url, "root","123456");
				DBUtil.POOL.add(conn);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			
		}
	}
	public static int save(Object obj){ // Object obj=new Tuser();
		int id=0;
		try {
			Class<?> clazz = obj.getClass();
			String tablename=clazz.getSimpleName().replace("T", "");
			//ͨ���������м�������
			Field[] fields = clazz.getDeclaredFields();
			//begin :��̬ƴ��sql���
			String sql="insert into "+tablename+"(";
			for (int i = 1; i < fields.length-1; i++) {
				sql+=fields[i].getName()+",";
			}
			sql+=fields[fields.length-1].getName();
			sql+=") values(";
			for (int i = 1; i < fields.length-1; i++) {
				sql+="?,";
			}
			sql+="?)";
			System.out.println(sql);
			//End :��̬ƴ��sql���
			Connection con = POOL.pop();
			PreparedStatement pstm = con.prepareStatement(sql);
			
			for (int i = 1; i < fields.length; i++) {
				fields[i].setAccessible(true);
				pstm.setObject(i,fields[i].get(obj) );
			}
			pstm.execute();
			//��ѯ����id
			sql="select max(id) as id from "+tablename;
			 ResultSet rs=pstm.executeQuery(sql);
			 while(rs.next()){
				 id=rs.getInt("id");
			 }
			 rs.close();
			pstm.close();
			DBUtil.close(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	//����id����
	public static Object find(Class<?> clazz,int id){
		ResultSet rs =null;
		PreparedStatement pstm=null;
		Connection conn=null;
		try {
			String tablename=clazz.getSimpleName().replace("T", "");
			//����ƴ��sql���
			StringBuilder sb=new StringBuilder("select * from ");
			sb.append(tablename).append(" where id=?");
			LOGGER.trace(sb.toString());
			LOGGER.debug(sb.toString());
			LOGGER.warn(sb.toString());
			LOGGER.info(sb.toString());
		
			conn = DBUtil.POOL.pop();
			 pstm = conn.prepareStatement(sb.toString());
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			Field[] fields = clazz.getDeclaredFields();
			Object obj = clazz.newInstance();
			while(rs.next()){ //����ÿһ�е�ÿһ��
				for (Field field : fields) {
					Object v=rs.getObject(field.getName());
					field.setAccessible(true);
					field.set(obj, v);
				}
			}
			
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstm!=null){
				try {
					pstm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(conn!=null){
				DBUtil.close(conn);
			}
		}
		return null;
	}
	public static void del(Class<?> clazz,int id){
		try {
			String tablename=clazz.getSimpleName().replace("T", "");
			StringBuilder sb=new StringBuilder("delete from ");
			sb.append(tablename).append(" where id=?");
			Connection conn = DBUtil.POOL.pop();
			LOGGER.debug(sb.toString());
			PreparedStatement pstm = conn.prepareStatement(sb.toString());
			pstm.setInt(1, id);
			pstm.execute(); //ɾ���Ͳ��� executeUpdate 
			pstm.close();
			DBUtil.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static List<?>  find(Class<?> clazz,Object k,Object v){
		List<Object> box=new ArrayList<Object>();
		try {
			// select * from tablename where username=wuqiwei;
			String tablename=clazz.getSimpleName().replace("T", "");
			StringBuilder sb=new StringBuilder("select * from ").append(tablename);
			sb.append(" where ").append(k).append("=?");
			System.out.println(sb);
			Connection conn = DBUtil.POOL.pop();
			PreparedStatement pstm = conn.prepareStatement(sb.toString());
			pstm.setObject(1, v);
			ResultSet rs = pstm.executeQuery();
			Field[] fields = clazz.getDeclaredFields();
			while(rs.next()){ //ÿ�α���һ�о�ȥ����һ������
				Object obj = clazz.newInstance();
				//�������ÿһ�����Ը�ֵ
				for (Field field : fields) {
					field.setAccessible(true);
					field.set(obj, rs.getObject(field.getName()));
				}
				box.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return box;
	}
	public static void  del(Class<?> clazz,Object k,Object v){
		try {
			String tablename=clazz.getSimpleName().replace("T", "");
			StringBuilder sb=new StringBuilder(" delete from ").append(tablename);
			sb.append(" where ").append(k).append("=?");
			System.out.println(sb);
			Connection conn = DBUtil.POOL.pop();
			PreparedStatement pstm = conn.prepareStatement(sb.toString());
			pstm.setObject(1, v);
			pstm.execute();
			pstm.close();
			DBUtil.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		 DBUtil.find(Tmenu.class, 7);
	}
	public static List<?>  find(Class<?> clazz){
		//select * from tablename
		List<Object> box=new ArrayList<Object>();
		try {
			String tablename=clazz.getSimpleName().replace("T", "");
			//����ƴ��sql���
			StringBuilder sb=new StringBuilder("select * from ");
			sb.append(tablename);
			Connection con = POOL.pop();
			PreparedStatement pstm = con.prepareStatement(sb.toString());
			ResultSet rs = pstm.executeQuery();
			Field[] fi = clazz.getDeclaredFields();
		
			//���Ĵ���
			while(rs.next()){ //�������ÿһ��
				Object o = clazz.newInstance();
				//�������ÿһ�����Ը�ֵ
				for (Field f : fi) {
					f.setAccessible(true);// ��������
					Object v = rs.getObject(f.getName()); //���Ĵ��� rs.getObject("id");  id title locid
					f.set(o, v);// ��������ָ�����Ե�set������ֵ
				}
				box.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return box;
	}
	//����
	public static  int update(Object obj){
		// update tablename set xx=xx where id= 
		try {
			Class<?> clazz = obj.getClass();
			String tablename=clazz.getSimpleName().replace("T", "");
			//����ƴ��sql���
			StringBuilder sb=new StringBuilder("update ");
			sb.append(tablename);
			sb.append(" set ");
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 1; i < fields.length-1; i++) {
				sb.append(fields[i].getName()).append("=?,");
			}
			sb.append(fields[ fields.length-1].getName()).append("=? ");
			fields[0].setAccessible(true);
			sb.append("where id=?");
			System.out.println(sb.toString());
			Connection con = POOL.pop();
			PreparedStatement pstm = con.prepareStatement(sb.toString());
			// id title locid
			for (int i = 1; i < fields.length; i++) { //1 2
				fields[i].setAccessible(true);
				pstm.setObject(i,fields[i].get(obj) );
			}
			pstm.setObject(fields.length, fields[0].get(obj));
			pstm.executeUpdate();
			pstm.close();
			DBUtil.close(con);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update(obj);
	}
	
}
