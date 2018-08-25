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
			//读取配置文件(以后根据连接数量来相应创建数据库连接数量)
			String connsize = ResourceBundle.getBundle("project").getString("connsize");
			Class.forName("com.mysql.jdbc.Driver");
			// 程序已启动就创建5个连接，开始慢一点，使用时就快
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
        String tablename =clazz.getSimpleName().replace("T", "");//通过反射获取到表名并将开头字母去掉
        Field[] fields = clazz.getDeclaredFields();//获得表内的所有属性(id,title,locid)
        String sql="insert into "+tablename+"(";
        for (int i = 1; i < fields.length-1; i++) {//不需要id故从1开始遍历（顺序是由Tmenu表格里排序得来）
			sql+=fields[i].getName()+",";
		}
        sql+=fields[fields.length-1].getName(); //获得最后一个字段(就不会有逗号)
        sql+=") values(";
        for (int i = 1; i < fields.length-1; i++) {
        	sql+="?,";
        }
        sql+="?)";
        System.out.println(sql);
        Connection con = POOL.pop();//获得连接
        PreparedStatement pstm = con.prepareStatement(sql);
        for (int i = 1; i < fields.length; i++) {
        	fields[i].setAccessible(true);//强制访问私有属性
        	pstm.setObject(i, fields[i].get(obj));//fields[i].get(obj)通过获取user表获取里面每个对象
		}
        pstm.execute();
        pstm.close();
        DBUtil.close(con);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查找
	public static List<?> find(Class<?> clazz){
		List<Object> box=new ArrayList<Object>();
		try {
			String tablename = clazz.getSimpleName().replace("T", "");
			//用来拼接sql语句
			StringBuilder sb=new StringBuilder("select * from");
			sb.append(tablename);
			Connection con=POOL.pop();
			PreparedStatement pstm=con.prepareStatement(sb.toString());
			ResultSet rs=pstm.executeQuery();
			Field[] fi = clazz.getDeclaredFields();
			
			//核心代码
			while (rs.next()) {
				Object o=clazz.newInstance();//创建实例对象
				for (Field f :fi ) {//给遍历到的每个对象赋值
					f.setAccessible(true);
					Object v = rs.getObject(f.getName());
					f.set(o, v);//调用类中属性的set方法赋值
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
	//更新
	public static void update(Object obj){
		try {
		Class<?> clazz=obj.getClass();
			String tablename=clazz.getSimpleName().replace("T", "");
			//用来凭借sql语句
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
