import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class ConnectDB {

	public void post() throws Exception {
		final String SSUurl = "http://ssu.ac.kr/web/kor/plaza_d_01?p_p_id=EXT_MIRRORBBS&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_pos=1&p_p_col_count=2&_EXT_MIRRORBBS_struts_action=%2Fext%2Fmirrorbbs%2Fview";
		Connection con=null;
		Source source = null;
		try

		{
			InputStream is = new URL(SSUurl).openStream();
			source = new Source(new InputStreamReader(is, "utf-8"));
			source.fullSequentialParse();
			con = getConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Element> tableList = source.getAllElements(HTMLElementName.TABLE);

		for (Iterator<Element> tableIter = tableList.iterator(); tableIter.hasNext();) {
			Element table = (Element) tableIter.next();
			String tag = table.getAttributeValue("class");
			if (tag.equals("bbs-list")) {
				Element tbody = (Element) table.getAllElements(HTMLElementName.TBODY).get(0);
				int trcount = tbody.getAllElements(HTMLElementName.TR).size();

				for (int i = 0; i < trcount; i++) {
					try {

						Element tr = (Element) tbody.getAllElements(HTMLElementName.TR).get(i);
						Element td = (Element) tr.getAllElements(HTMLElementName.TD).get(1);
						Element a = (Element) td.getAllElements(HTMLElementName.A).get(0);
						Element writer = (Element) tr.getAllElements(HTMLElementName.TD).get(3);
						Element date = (Element) tr.getAllElements(HTMLElementName.TD).get(4);
						if ((a.getContent().toString().substring(2, 4)).equals("학사")) {
							try {
								
								PreparedStatement posted = con
										.prepareStatement("INSERT INTO notices (title, writer, reg_date) VALUES ('"
												+ a.getContent().toString() + "','" + writer.getContent().toString()
												+ "','" + date.getContent().toString() + "')");
								// dynamic parsing으로 sql문을 parsing한 후 입력된 해당 값을
								// parsing된 sql문에 binding하여 실행
								// sql문을 먼저 parsing하여 입력되는 값이 여러개일 경우 sql문을 여러번
								// parsing하지 않고 parsing된 sql문에
								// 원하는 값을 binding하여 사용하므로 자원의 낭비가 적지만 무거움
								posted.executeUpdate();
							} catch (Exception e) {
								System.out.println(e);
							} finally {
								System.out.println("Insert Completed!");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void showArticles(PrintWriter out) throws Exception  {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Connection con = getConnection();
			pstmt = con.prepareStatement("Select * FROM notices");//static parsing 으로 sql문을 parsing할 때 입력된 해당 값을 sql문에 binding하여 같이 parsing하여 실행
			 //static parsing은 sql문과 입력된 값을 함께 파싱하므로 한번 parsing된 sql문을 다른 데이터를 입력하여 사용할 수 없어 
			 //해당 서비스에 사용자 수가 많은 경우 메모리 full이 발생할 수 있다
			 //createStatement 사용시 statement를 사용하영 sql문에 필요한 데이터를 입력 받고 실행시 발생한 데이터를 resultset에 저장
			 //가볍다라는 장점이 있지만 parsing된 sql문 사용시 입력된 값이 다를 경우 재사용할 수 없으며 자원의 낭비가 발생할 수 있다.
			rs = pstmt.executeQuery(); // SELECT 쿼리의 결과를 레코드 단위로
														// 하나씩 페치할 수 있는 기능을 제공하는
														// 객체
			while (rs.next()) {
				out.println(rs.getString("title") + "	|	"
						+ rs.getString("writer") + "	|	" + rs.getString("reg_date") + "<br>");
			}
		} catch (SQLException e) {
			System.err.println("SQL Error : " + e);
		} catch (Exception e) {
			System.err.println("Error : " + e);
		} finally{
			if(rs!=null)	try{rs.close();} catch(SQLException e){}
			if(pstmt!=null)	try{pstmt.close();} catch(SQLException e){}
		}
			
		
	}

	

	public  Connection getConnection() throws Exception {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/test";
			String username = "root";
			String password = "1234";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("Connected");
			return conn;

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
