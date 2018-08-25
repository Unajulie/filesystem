package cn.believeus.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.believeus.dao.MysqlDao;

public class DelController extends HttpServlet {

	private static final long serialVersionUID = -7576390278617389709L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MysqlDao dao = new MysqlDao();
		String mid = req.getParameter("mid");
		dao.delete(Integer.parseInt(mid));
		resp.getWriter().print("success");

	}
}
