package cn.believeus.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;

import com.sun.istack.internal.Pool;
import com.sun.org.apache.bcel.internal.generic.NEW;

import cn.believeus.entity.Tmenu;
import cn.believeus.entity.Tuser;

public class DBUtil1 {
	public static final Stack<Connection> POOL = new Stack<Connection>();
	public static void close(Connection conn){
		DBUtil.POOL.add(conn);
	}
	static{
		try {
			//��ȡ�����ļ�(�Ժ����������������Ӧ�������ݿ���������)
			String connsize = ResourceBundle.getBundle("project").getString("connsize");
			Class.forName("com.mysql.jdbc.Driver");
			// �����������ʹ���5�����ӣ���ʼ��һ�㣬ʹ��ʱ�Ϳ�
			String url = "jdbc:mysql://localhost:3306/vbox";
			for (int i = 0; i < Integer.parseInt(connsize); i++) {
				Connection conn = DriverManager.getConnection(url, "root","123456");
				DBUtil.POOL.add(conn);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void save(Object obj){
		try {
			Class<?>clazz=obj.getClass();
        String tablename =clazz.getSimpleName().replace("T", "");//ͨ�������ȡ������������ͷ��ĸȥ��
        Field[] fields = clazz.getDeclaredFields();//��ñ��ڵ���������(id,title,locid)
        String sql="insert into "+tablename+"(";
        for (int i = 1; i < fields.length-1; i++) {//����Ҫid�ʴ�1��ʼ������˳������Tmenu��������������
			sql+=fields[i].getName()+",";
		}
        sql+=fields[fields.length-1].getName(); //������һ���ֶ�(�Ͳ����ж���)
        sql+=") values(";
        for (int i = 1; i < fields.length-1; i++) {
        	sql+="?,";
        }
        sql+="?)";
        System.out.println(sql);
        Connection con = POOL.pop();//�������
        PreparedStatement pstm = con.prepareStatement(sql);
        for (int i = 1; i < fields.length; i++) {
        	fields[i].setAccessible(true);//ǿ�Ʒ���˽������
        	pstm.setObject(i, fields[i].get(obj));//fields[i].get(obj)ͨ����ȡuser���ȡ����ÿ������
		}
        pstm.execute();
        pstm.close();
        DBUtil.close(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//����
	public static List<?> find(Class<?> clazz){
		List<Object> box=new ArrayList<Object>();
		try {
			String tablename = clazz.getSimpleName().replace("T", "");
			//����ƴ��sql���
			StringBuilder sb=new StringBuilder("select * from");
			sb.append(tablename);
			Connection con=POOL.pop();
			PreparedStatement pstm=con.prepareStatement(sb.toString());
			ResultSet rs=pstm.executeQuery();
			Field[] fi = clazz.getDeclaredFields();
			
			//���Ĵ���
			while (rs.next()) {
				Object o=clazz.newInstance();//����ʵ������
				for (Field f :fi ) {//����������ÿ������ֵ
					f.setAccessible(true);
					Object v = rs.getObject(f.getName());
					f.set(o, v);//�����������Ե�set������ֵ
				}
				box.add(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return box;
	}
	public static Object find(Class<?> clazz,int id){
		List<Object> box=new ArrayList<Object>();
		ResultSet rs=null;
		PreparedStatement pstm=null;
		Connection conn=null;
		try { 
			String tablename = clazz.getSimpleName().replace("T", "");
			StringBuilder sb=new StringBuilder("select * from ");
			sb.append(tablename).append(" where id=?");
			System.out.println(sb.toString());
			 conn = DBUtil.POOL.pop();
			 pstm = conn.prepareStatement(sb.toString());
			pstm.setInt(1, id);
			 rs = pstm.executeQuery();
			Field[] fields = clazz.getDeclaredFields();
			while (rs.next()) {
				Object obj = clazz.getInterfaces();
				for (Field field : fields) {
					Object value = rs.getObject(field.getName());
					field.setAccessible(true);
					field.set(obj, value);
				}
				return obj;
			}
			rs.close();
			pstm.close();
			DBUtil.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if (rs!=null) {
					rs.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			if (pstm!=null) {
				try {
					pstm.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		if (conn!=null) {
			try {
				DBUtil.close(conn);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static void del(Class<?>clazz,int id){
		try {
			String tablename = clazz.getSimpleName().replace("T", "");
			StringBuilder sb=new StringBuilder("delete from");
			sb.append(tablename).append("where id=?");
			Connection conn = DBUtil.POOL.pop();
			PreparedStatement pstm = conn.prepareStatement(sb.toString());
			pstm.setInt(1, id);
			pstm.execute();
			pstm.close();
			DBUtil.close(conn);
			System.out.println(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		DBUtil.find(Tmenu.class,12);
	}
	//����
	public static void update(Object obj){
		try {
		Class<?> clazz=obj.getClass();
			String tablename=clazz.getSimpleName().replace("T", "");
			//����ƾ��sql���
			StringBuilder sb=new StringBuilder("update ");
			sb.append(tablename);
			sb.append(" set ");
			Field[] fields = clazz.getDeclaredFields();
			for (int i = 1; i < fields.length-1; i++) {
				sb.append(fields[i].getName()).append("=?,");
			}
			sb.append(fields[fields.length-1].getName()).append("=?");
			fields[0].setAccessible(true);
			sb.append(" where id=?");
			Connection con=POOL.pop();
			PreparedStatement pstm=con.prepareStatement(sb.toString());
			for (int i = 1; i < fields.length-1; i++) { 
				fields[i].setAccessible(true);
				pstm.setObject(i, fields[i].get(obj));
			}
			pstm.setObject(fields.length, fields[0].get(obj));
			pstm.execute();
			pstm.close();
			DBUtil.close(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

}
