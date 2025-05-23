import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.util.Date;

public class TransferMoney extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		String accountno1 = req.getParameter("accountno1");
		// long accountno1 = Long.parseLong(accountno12);
		String accountno2 = req.getParameter("accountno2");
		// long accountno2 = Long.parseLong(accountno21);
		String amount1 = req.getParameter("money");
		long amount = Long.parseLong(amount1);
		// SimpleDateFormat dtf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		SimpleDateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		Date now = new Date();
		String name = (String) session.getAttribute("name");
		String date = dtf.format(now);

		// java.sql.Timestamp sqlDate = new java.sql.Timestamp(now.getTime());

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "system");
			Statement s1 = con.createStatement();
			// deduct from accountno1
			ResultSet rs = s1.executeQuery("select balance from accountdata where accountno='" + accountno1 + "'");
			while (rs.next()) {

				long balance = Long.parseLong(rs.getString(1));

				long newbalance = balance - amount;
				Statement s = con.createStatement();
				s.executeUpdate(
						"update  accountdata set balance=" + newbalance + " where accountno='" + accountno1 + "'");
			}
			// add to accountno2
			Statement s2 = con.createStatement();
			ResultSet rs1 = s2.executeQuery("select balance from accountdata where accountno='" + accountno2 + "'");
			while (rs1.next()) {

				long balance1 = Long.parseLong(rs1.getString(1));

				long newbalance1 = balance1 + amount;
				Statement s3 = con.createStatement();
				s3.executeUpdate(
						"update accountdata set balance=" + newbalance1 + " where accountno='" + accountno2 + "'");
				out.println("<center>Money Transfered</center>");
			}
			PreparedStatement ps = con.prepareStatement("insert into transaction values(?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, accountno1);
			ps.setString(3, accountno2);
			ps.setString(4, amount1);
			ps.setString(5, date);
			ps.executeUpdate();


			RequestDispatcher disp = req.getRequestDispatcher("welcome.html");
			disp.include(req, res);
			out.println("<center>Money Transfered</center>");
		} catch (Exception e) {
			out.println("<center>" + e + "</center>");
		}
	}
}