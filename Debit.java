import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Debit extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		String accountno = req.getParameter("accountno");
		long debit = Long.parseLong(req.getParameter("amount"));

		HttpSession session = req.getSession();
        String name = (String) session.getAttribute("name");

        // Format date as String to store in VARCHAR
        SimpleDateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String date = dtf.format(new Date());

		// SimpleDateFormat dtf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		// Date now = new Date();
		// String name = (String) session.getAttribute("name");
		// String date = dtf.format(now);

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "system");
			Statement s1 = con.createStatement();
			ResultSet rs1 = s1.executeQuery("select balance from accountdata where accountno='" + accountno + "'");
			while (rs1.next()) {

				long amount = Long.parseLong(rs1.getString(1));

				long newbalance = amount - debit;
				Statement s = con.createStatement();
				s.executeUpdate(
						"update  accountdata set balance=" + newbalance + " where accountno='" + accountno + "'");
				out.println("<center><div class=image> new balance is " + newbalance + " </div></center>");
			}

			RequestDispatcher disp = req.getRequestDispatcher("welcome.html");
			disp.include(req, res);

			PreparedStatement ps = con.prepareStatement("insert into debit values (?,?,?)");
			ps.setString(1, name);
			ps.setLong(2, debit);
			ps.setString(3, date);
			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}