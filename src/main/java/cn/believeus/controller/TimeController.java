package cn.believeus.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimeController extends HttpServlet{

	private static final long serialVersionUID = -6833706315274516211L;

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	String time=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	resp.getWriter().println(time);
}
}
