
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BbsServlet extends HttpServlet {

	// get 방식으로 요청되었을 때 호출됨
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * String d1 = Integer.toString((int) ((Math.random() * 6) + 1)); String
		 * d2 = Integer.toString((int) ((Math.random() * 6) + 1)); PrintWriter
		 * out = response.getWriter();
		 * 
		 * out.println("<html> < body>" +
		 * "<h1 align=center>HF\'s Chap 2 Dice Roller</h1>" + "<p>" + d1 +
		 * " and " + d2 + " were rolled " + "</body> </html>");
		 */
		doPost(request, response);
	}

	// post방식으로 요청되었을 때 호출됨
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out= response.getWriter();
		String title = request.getParameter("title");
		String writer= request.getParameter("writer");
		String date = request.getParameter("reg_date");		
		try{
			ConnectDB conn = new ConnectDB();
			conn.getConnection();
			conn.post();
			conn.showArticles(out);
		} catch (Exception e){
			System.err.println("Error : " + e);
		}
	
		
		//addArticle(title, writer, date);
		//showArticles(out);
	}
// 데이터 관련 작업 처리 명령은 반드시 예외처리
}
