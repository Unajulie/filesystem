package cn.believeus.init;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.believeus.dao.DBUtil;

public class PoolInit implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
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

	// ����tomcat�ر�
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			Iterator<Connection> iterator = DBUtil.POOL.iterator();
			while (iterator.hasNext()) {
				Connection con = iterator.next();
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
