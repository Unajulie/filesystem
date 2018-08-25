package cn.believeus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.believeus.entity.Tmenu;

public class MysqlDao {
	public int save(Tmenu m) {
		int id = 0;
		try {
			Connection conn = DBUtil.POOL.pop();
			String sql = "insert into menu(title,locid) values (?,?)";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setString(1, m.getTitle());
			stm.setInt(2, 12);
			stm.execute();//完成插入
			sql = "select max(id)  id from menu";
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			stm.close();
			DBUtil.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	public int updata(Tmenu m) {
		try {
			Connection conn = DBUtil.POOL.pop();
			String sql = "update menu set title=? where id=?";
			PreparedStatement stm =(PreparedStatement) conn.prepareStatement(sql);
			stm.setString(1, m.getTitle());
			stm.setInt(2, m.getId());
			stm.executeUpdate();
			stm.close();
			DBUtil.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m.getId();
	}
	public List<Tmenu> find() {
		List<Tmenu> box = new ArrayList<Tmenu>();
		try {
			Connection conn = DBUtil.POOL.pop();
			String sql = "select id,title,locid from menu";
			PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String locid = rs.getString("locid");
				Tmenu m = new Tmenu();
				m.setId(id);
				m.setTitle(title);
				m.setLocid(Integer.parseInt(locid));
				box.add(m);
			}
			DBUtil.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return box;
	}
	public boolean delete(int id) {
		   int del=0;
		  boolean delete= false;
		  try {
				Connection conn = DBUtil.POOL.pop();
		    String sql="delete  from menu where id=?";
		    PreparedStatement stm=conn.prepareStatement(sql); 
		    stm.setInt(1, id);
		    //返回你删除了几行
			del = stm.executeUpdate();
			//判断
		    if(del>0){
		     delete= true; 
		    }
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		//返回Boolean值
		  return delete;
		 }
}
