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
						if ((a.getContent().toString().substring(2, 4)).equals("�л�")) {
							try {
								
								PreparedStatement posted = con
										.prepareStatement("INSERT INTO notices (title, writer, reg_date) VALUES ('"
												+ a.getContent().toString() + "','" + writer.getContent().toString()
												+ "','" + date.getContent().toString() + "')");
								// dynamic parsing���� sql���� parsing�� �� �Էµ� �ش� ����
								// parsing�� sql���� binding�Ͽ� ����
								// sql���� ���� parsing�Ͽ� �ԷµǴ� ���� �������� ��� sql���� ������
								// parsing���� �ʰ� parsing�� sql����
								// ���ϴ� ���� binding�Ͽ� ����ϹǷ� �ڿ��� ���� ������ ���ſ�
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
			pstmt = con.prepareStatement("Select * FROM notices");//static parsing ���� sql���� parsing�� �� �Էµ� �ش� ���� sql���� binding�Ͽ� ���� parsing�Ͽ� ����
			 //static parsing�� sql���� �Էµ� ���� �Բ� �Ľ��ϹǷ� �ѹ� parsing�� sql���� �ٸ� �����͸� �Է��Ͽ� ����� �� ���� 
			 //�ش� ���񽺿� ����� ���� ���� ��� �޸� full�� �߻��� �� �ִ�
			 //createStatement ���� statement�� ����Ͽ� sql���� �ʿ��� �����͸� �Է� �ް� ����� �߻��� �����͸� resultset�� ����
			 //�����ٶ�� ������ ������ parsing�� sql�� ���� �Էµ� ���� �ٸ� ��� ������ �� ������ �ڿ��� ���� �߻��� �� �ִ�.
			rs = pstmt.executeQuery(); // SELECT ������ ����� ���ڵ� ������
														// �ϳ��� ��ġ�� �� �ִ� ����� �����ϴ�
														// ��ü
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
