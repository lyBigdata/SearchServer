package cn.hadoop.liuyu.search;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @function 显示检索结果
 * @author 
 *
 */
public class search extends HttpServlet {
	// 处理客户端发送的GET请求 　　
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 这条语句指明了向客户端发送的内容格式和采用的字符编码
		response.setContentType("text/html;charset=UTF-8");
		String wd = request.getParameter("wd").trim();
		wd = new String(wd.getBytes("ISO-8859-1"), "utf-8");

		/** 这里是查询代码块 **/
		// 利用PrintWriter对象的方法将数据发送给客户端
		PrintWriter out = response.getWriter();
		out.println("<font color=red>" + wd + "</font> 的检索结果如下：<hr/>");

		out.println("<h3><a href='http://cn.bing.com/'>ALL   IN</a></h3>");
		out.println("<p>ALL  IN 全情投入。。。。</p>");
		
		out.println("<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>");
		out.println("<p>你想要的这里都有。。。。</p>");

		out.println("<h3><a href='http://www.haosou.com'>好搜一下</a></h3>");
		out.println("<p>世界很大，你要就来。。。。</p>");
		
		out.close();
	}

	// 处理客户端发送的POST请求
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);// 当客户端发送POST请求时，调用doGet()方法进行处理 　　
	}

}
