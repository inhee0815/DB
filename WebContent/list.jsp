<%@page import="java.io.PrintWriter"%>
<%@ page import="java.io.*,java.util.*"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.sql.*"%>
<%@ page import="myPackage.ConnectDB"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<br />

	<div class="timer" align="center" style="text-align: center">

		<br /> <label id="hours"> 00</label>:<label id="minutes">00</label>:<label
			id="seconds">00</label> <br /> <br /> <label
			id="totalTime"> </label>

	</div>
	<br />


	<script language="javascript">
		//자바스크립트 시작
		var hoursLabel = document.getElementById("hours");
		var minutesLabel = document.getElementById("minutes");
		var secondsLabel = document.getElementById("seconds");
		var totalTime = document.getElementById("totalTime");
		var totalSeconds = 0;
		var totalMinutes = 0;
		var totalHours = 0;
		var counter;
		var timerOn;
		var htmlResets;
		var totalMills = 0;
		var refresh;
		(function startTimer() { //즉시 실행 함수, 실행하자마자 타이머가 돌아가도록
			refresh = setTimeout("location.reload()",60000*5);
			if (timerOn == 1) {
				return;
			} else {
				counter = setInterval(setTime, 10);
				//setTime마다 함수 실행, milliseconds의 시간이 지날때마다 함수 실행
				timerOn = 1;
				htmlResets = 0;
			}
		})();

		/* function pauseTimer() {

			if (timerOn == 1) {
				clearInterval(counter);
				timerOn = 0;
			}
			if (htmlResets == 1) {
				hoursLabel.innerHTML = "00";
				minutesLabel.innerHTML = "00";
				secondsLabel.innerHTML = "00";
				totalMills = 0;
				totalSeconds = 0;
				totalMinutes = 0;
				totalHours = 0;
			} else {
				htmlResets = 1;
			}

		} */

		/* function stopTimer() {
			totalTime.innerHTML = "Time Recorded: " + hoursLabel.innerHTML
					+ ":" + minutesLabel.innerHTML + ":"
					+ secondsLabel.innerHTML;
			hoursLabel.innerHTML = "00";
			minutesLabel.innerHTML = "00";
			secondsLabel.innerHTML = "00";
			totalMills = 0;
			totalSeconds = 0;
			totalMinutes = 0;
			totalHours = 0;
			clearInterval(counter);
			clear
			timerOn = 0;

		} */

		function setTime() {
			++totalMills;
			if (totalHours == 99 & totalMinutes == 59 & totalSeconds == 60) {
				totalHours = 0;
				totalMinutes = 0;
				totalSeconds = 0;
				hoursLabel.innerHTML = "00";
				minutesLabel.innerHTML = "00";
				secondsLabel.innerHTML = "00";
				clearInterval(counter);

			}

			if (totalMills == 100) { //100이 00:00:01
				totalSeconds++;
				
				secondsLabel.innerHTML = pad(totalSeconds % 60);
				totalMills = 0;

			}

			if (totalSeconds == 60) {

				totalMinutes++;
				minutesLabel.innerHTML = pad(totalMinutes % 60);
				totalSeconds = 0;

			}

			if (totalMinutes == 60) {
				totalHours++;
				hoursLabel.innerHTML = pad(totalHours);
				totalMinutes = 0;

			}
			if(totalMinutes==5) {
				clearInterval(counter);
			}

		}

		function pad(val) {

			var valString = val + "";
			if (valString.length < 2) {
				return "0" + valString;
			} else {
				return valString;

			}

		}
	</script>

	<%
		//response.setIntHeader("Refresh", 10);
		try {
			ConnectDB db = new ConnectDB();
			db.post();
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/test";
			String username = "root";
			String password = "1234";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
			PreparedStatement pstmt = conn.prepareStatement("Select * FROM ssu");
			ResultSet rs = pstmt.executeQuery();
	%>
	<table width="70%" cellpadding="0" cellspacing="0" border="0">

		<tr>
			<td width="40">번호</td>
			<td width="379">제목</td>
			<td width="73">작성자</td>
			<td width="164">작성일</td>

		</tr>
		<%
			while (rs.next()) {
					int idx = rs.getInt(1);
					String title = rs.getString("title");
					String writer = rs.getString("writer");
					String date = rs.getString("reg_date");
					String link = rs.getString("url");
		%>
		<tr height="30" align="center">
			<td><%=idx%></td>
			<td align="left" style="cursor: pointer"
				OnClick="location.href='<%=link%>'"><%=title%></td>
			<!-- 제목에 링크거는 태그 -->
			<td align="center"><%=writer%></td>
			<td align="center"><%=date%></td>

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

		</tr>
	</table>
</body>
</html>

