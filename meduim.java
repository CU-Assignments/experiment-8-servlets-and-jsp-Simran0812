indx.html
<!DOCTYPE html>
<html>
<head>
    <title>Employee Search</title>
</head>
<body>
    <h2>Search Employee by ID</h2>
    <form action="EmployeeServlet" method="get">
        <label>Enter Employee ID:</label>
        <input type="text" name="empId" required>
        <input type="submit" value="Search">
    </form>

    <h2>All Employees</h2>
    <a href="EmployeeServlet">View Employee List</a>
</body>
</html>

employee sevlet.java
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);

            String empId = request.getParameter("empId");

            if (empId != null && !empId.isEmpty()) {
                // Search employee by ID
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee WHERE id=?");
                ps.setInt(1, Integer.parseInt(empId));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    out.println("<h2>Employee Details</h2>");
                    out.println("ID: " + rs.getInt("id") + "<br>");
                    out.println("Name: " + rs.getString("name") + "<br>");
                    out.println("Department: " + rs.getString("department") + "<br>");
                    out.println("Salary: $" + rs.getDouble("salary") + "<br>");
                } else {
                    out.println("<h2>No employee found with ID: " + empId + "</h2>");
                }
            } else {
                // Display all employees
                out.println("<h2>Employee List</h2>");
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Employee");
                ResultSet rs = ps.executeQuery();

                out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Department</th><th>Salary</th></tr>");
                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>"
                            + rs.getString("department") + "</td><td>" + rs.getDouble("salary") + "</td></tr>");
                }
                out.println("</table>");
            }

            conn.close();
        } catch (Exception e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }
}

sql table
CREATE DATABASE your_database;
USE your_database;

CREATE TABLE Employee (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    department VARCHAR(100),
    salary DOUBLE
);

INSERT INTO Employee VALUES (1, 'Alice', 'IT', 50000);
INSERT INTO Employee VALUES (2, 'Bob', 'HR', 45000);
INSERT INTO Employee VALUES (3, 'Charlie', 'Finance', 55000);
