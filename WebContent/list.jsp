<%@page import="java.io.PrintWriter"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*"%>
<%@ page import="myPackage.ConnectDB"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>게시판</title>
</head>
<body>
	<%
		
		try {
			ConnectDB db = new ConnectDB();
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/test";
			String username = "root";
			String password = "1234";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement pstmt = conn.prepareStatement("Select * FROM board");
			ResultSet rs = pstmt.executeQuery();
	%>
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr height="5">
			<td width="5"></td>
		</tr>
		<tr
			style="background: url('img/table_mid.gif') repeat-x; text-align: center;">
			<td width="5"><img src="img/table_left.gif" width="5"
				height="30" /></td>
			<td width="73">번호</td>
			<td width="379">제목</td>
			<td width="73">작성자</td>
			<td width="164">작성일</td>
			<td width="7"><img src="img/table_right.gif" width="5"
				height="30" /></td>
		</tr>
		<%
			while (rs.next()) {
				int idx = rs.getInt(1);
					String title = rs.getString("title");
					String writer = rs.getString("writer");
					String date = rs.getString("reg_date");
		%>
		<tr height="25" align="center">
			<td>&nbsp;</td>
			<td><%=idx %></td>                                           
			<td align="left"><a href="view.jsp?idx=<%=idx%>"><%=title%></td> <!-- 제목에 링크거는 태그 -->
			<td align="center"><%=writer%></td>
			<td align="center"><%=date%></td>
			<td>&nbsp;</td>
		</tr>
		<%
			}
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				out.println(e.toString());
			}
		%>
		<tr height="1" bgcolor="#82B5DF">
			<td colspan="6" width="752"></td>
		</tr>
	</table>
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td colspan="4" height="5"></td>
		</tr>
		<tr align="center">
			<td><input type=button value="조회" OnClick="post()"></td>
		</tr>
	</table>
</body>
</html>

