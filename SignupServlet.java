import javax.servlet.*;
import java.io.*;
import java.sql.*;
public class SignupServlet implements Servlet{
public void init(ServletConfig h){}
public void service(ServletRequest req,ServletResponse res)throws ServletException,IOException
{
res.setContentType("text/html");
PrintWriter pw=res.getWriter();
String name=req.getParameter("name");
String pass=req.getParameter("password");
String email=req.getParameter("email");
String contact=req.getParameter("phone");
try{
Class.forName("oracle.jdbc.driver.OracleDriver");
Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
PreparedStatement ps=con.prepareStatement("insert into employee values(?,?,?,?)");
ps.setString(1,name);
ps.setString(2,pass);
ps.setString(3,email);
ps.setString(4,contact);
ps.executeUpdate();
RequestDispatcher dispatcher=req.getRequestDispatcher("Login.html");
dispatcher.include(req,res);
pw.println("<br>Hello "+name+" you are registered successfully");
}
catch(Exception e){}
pw.close();
}
public void destroy(){}
public String getServletInfo()
{
return(null);
}
public ServletConfig getServletConfig()
{
return(null);
}
}