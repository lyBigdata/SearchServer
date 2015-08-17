package cn.hadoop.liuyu.search;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
/**
 * @function  返回关键词搜索记录
 * @author liuyu
 *
 */
public class Suggest extends HttpServlet {
	// 处理客户端发送的GET请求 　　
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 向客户端发送的内容格式和采用的字符编码
		response.setContentType("text/html;charset=UTF-8");
		// 获取前台发送的关键字term
		String term = request.getParameter("term").trim();
		//设置term的字符集编码
		term = new String(term.getBytes("ISO-8859-1"), "utf-8");
		// 利用PrintWriter对象的方法将数据发送给客户端
		PrintWriter out = response.getWriter();

		if (!term.isEmpty()) {
			DBConnection db = new DBConnection();
			String sql = "select keyword from dictionary where keyword like '%"
					+ term + "%' order by frequency desc limit 0,5";
			/** 数据查询 **/
			ArrayList<String> list = db.getList(sql);
			if (list == null) {
				list = new ArrayList<String>();
				list.add(term);
			}
			//返回json格式的数据
			out.println(JSONArray.fromObject(list).toString());
		} else {
			out.println("");
		}
		out.close();
	}

	// 处理客户端发送的POST请求
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);// 这条语句的作用是，当客户端发送POST请求时，调用doGet()方法进行处理 　　
	}
}