package cn.believeus.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.believeus.dao.MysqlDao;
import cn.believeus.entity.Tmenu;

public class ViewController extends HttpServlet {

	private static final long serialVersionUID = 2820339581632744411L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		MysqlDao dao=new MysqlDao();
		List<Tmenu> menubox = dao.find();
		req.setAttribute("menubox", menubox);
		req.getRequestDispatcher("/WEB-INF/view.jsp").forward(req, resp);
	}
	@Override
	protected  void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
		
	}
}
