package cn.believeus.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;

import cn.believeus.dao.DBUtil;
import cn.believeus.entity.Tmenu;

public class GetMenuController extends HttpServlet {

	private static final long serialVersionUID = 5527296803150332973L;

@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
int id = Integer.parseInt(req.getParameter("id"));
List<Tmenu> menuBox =(List<Tmenu>) DBUtil.find(Tmenu.class,"pid",id);//��ø������������id�������ŵ�һ��������
Object jsondata = JSONArray.toJSON(menuBox);
//����˷�����json��ʽ���ַ���
resp.getWriter().print(jsondata);

}
}
