import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
public class CreditHistory extends HttpServlet
{
	public void service(HttpServletRequest req , HttpServletResponse res)throws IOException , ServletException
	{
		res.setContentType("text/html");
		PrintWriter out=res.getWriter();
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
			PreparedStatement ps=con.prepareStatement("select * from credit");
	        ResultSet rs=ps.executeQuery();
			
				out.println("<html><body>");
				out.println("<table border='2'>");
				out.println("<tr><th>ser no.</th> <th>NAME</th><th>AMOUNT</th><th>DATE</th></tr>");
				int serno=1;
				while(rs.next()){
					out.println("<tr><td>"+serno+"</td><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><tr>");
					serno++;
				}
				
			
		}
		catch(Exception e){}
	}
}