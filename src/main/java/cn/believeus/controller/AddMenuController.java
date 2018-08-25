package cn.believeus.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.believeus.dao.DBUtil;
import cn.believeus.dao.MysqlDao;
import cn.believeus.entity.Tmenu;

public class AddMenuController  extends HttpServlet{

	private static final long serialVersionUID = -8732436682361055603L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String mid = req.getParameter("mid");
		int locid=Integer.parseInt(req.getParameter("locid"));
		String title=req.getParameter("title");
		int pid=Integer.parseInt(req.getParameter("pid")); //pid
		//±£´æ
		if(mid==null){
			Tmenu m=new Tmenu(title,locid,pid);
			int id = DBUtil.save(m);
			resp.getWriter().print(id);
		//¸üÐÂ
		}else {
			 int id=Integer.parseInt(mid);
			Tmenu m=new Tmenu(id,title,locid,pid);
			resp.getWriter().println(DBUtil.update(m));
		}
	}
}
