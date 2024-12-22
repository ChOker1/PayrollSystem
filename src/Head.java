import java.sql.*;
import java.util.ArrayList;
import  java.util.Date;
import java.util.Calendar;

public class Head {
    static Connection conn;
    static ResultSet rs;

    public static void run() {
        System.out.println("head");

        ArrayList<Employees> employee = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.WEEK_OF_MONTH));


        //Sql access
        String url = "jdbc:mysql://localhost:3306/Payroll";  // Replace with your DB details
        String user = "root";  // Replace with your MySQL username
        String password = "722811@";  // Replace with your MySQL password

        try {
            // Load MySQL JDBC driver (optional in newer versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to MySQL database!");

            rs = conn.createStatement().executeQuery("SELECT * FROM EMPLOYEE");
            while (rs.next()) {
                Deduction deduction = new Deduction(rs.getDouble("Loans"), rs.getDouble("SSS"),rs.getDouble("PhilHealth"),rs.getDouble("CashAdvanced"),rs.getDouble("Others") );
                Payroll payroll = new Payroll(rs.getDouble("Gross"),rs.getDouble("NetIncome"),deduction);
                employee.add(new Employees(rs.getInt("EmpId"),rs.getString("Name"),rs.getDouble("Rate"),rs.getDouble("NoOfDays"),rs.getDouble("Salary"),rs.getDouble("Commissions"),payroll));


            }
        }catch (Exception e){
            e.printStackTrace();
        }



        try {
            int i = 1;

            // Create the TableGUI with the list of employees
            new TableGUI(employee);

            // Output the employee details for verification
            for (Employees e : employee) {
                System.out.println(e.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void saveOrigin(ArrayList<Employees> employee) {
        String sql = "UPDATE EMPLOYEE SET " +
                "Name = ?, Rate = ?, NoOfDays = ?, Salary = ?, Commissions = ?, " +
                "Gross = ?, Loans = ?, SSS = ?, PhilHealth = ?, CashAdvanced = ?, " +
                "Others = ?, TotalDeduction = ?, NetIncome = ? " +
                "WHERE EmpId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (Employees payroll : employee) {
                // Set parameters for each column
                pstmt.setString(1, payroll.getName());
                pstmt.setDouble(2, payroll.getRate());
                pstmt.setDouble(3, payroll.getDays());
                pstmt.setDouble(4, payroll.getSalary());
                pstmt.setDouble(5, payroll.getCommission());
                pstmt.setDouble(6, payroll.getPayroll().getGrossic());
                pstmt.setDouble(7, payroll.getPayroll().getDeduction().getLoans());
                pstmt.setDouble(8, payroll.getPayroll().getDeduction().getSss());
                pstmt.setDouble(9, payroll.getPayroll().getDeduction().getPhilhealth());
                pstmt.setDouble(10, payroll.getPayroll().getDeduction().getCashAdvanced());
                pstmt.setDouble(11, payroll.getPayroll().getDeduction().getOthers());
                pstmt.setDouble(12, payroll.getPayroll().getDeduction().getTotal());
                pstmt.setDouble(13, payroll.getPayroll().getNetic());

                // Set the EmpId for the WHERE clause
                pstmt.setInt(14, i);

                System.out.println(i);
                i++;

                // Add to batch
                pstmt.addBatch();
            }

            // Execute batch update
            pstmt.executeUpdate();
            System.out.println("Payroll list updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void save(ArrayList<Employees> employee) {

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.WEEK_OF_MONTH));
        String sql = "UPDATE EMPLOYEE SET " +
                "Name = ?, Rate = ?, NoOfDays = ?, Salary = ?, Commissions = ?, " +
                "Gross = ?, Loans = ?, SSS = ?, PhilHealth = ?, CashAdvanced = ?, " +
                "Others = ?, TotalDeduction = ?, NetIncome = ?, Date = ? " +
                "WHERE EmpId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (Employees payroll : employee) {
                // Set parameters for each column
                pstmt.setString(1, payroll.getName());
                pstmt.setDouble(2, payroll.getRate());
                pstmt.setDouble(3, payroll.getDays());
                pstmt.setDouble(4, payroll.getSalary());
                pstmt.setDouble(5, payroll.getCommission());
                pstmt.setDouble(6, payroll.getPayroll().getGrossic());
                pstmt.setDouble(7, payroll.getPayroll().getDeduction().getLoans());
                pstmt.setDouble(8, payroll.getPayroll().getDeduction().getSss());
                pstmt.setDouble(9, payroll.getPayroll().getDeduction().getPhilhealth());
                pstmt.setDouble(10, payroll.getPayroll().getDeduction().getCashAdvanced());
                pstmt.setDouble(11, payroll.getPayroll().getDeduction().getOthers());
                pstmt.setDouble(12, payroll.getPayroll().getDeduction().getTotal());
                pstmt.setDouble(13, payroll.getPayroll().getNetic());


                // Set the EmpId for the WHERE clause
                pstmt.setInt(14, i);

                System.out.println(i);
                i++;

                // Add to batch
                pstmt.addBatch();
            }

            // Execute batch update
            pstmt.executeUpdate();
            System.out.println("Payroll list updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add(ArrayList<Employees> employee){

        String sql = "INSERT EMPLOYEE SET " +
                "Name = ?, Rate = ?, NoOfDays = ?, Salary = ?, Commissions = ?, " +
                "Gross = ?, Loans = ?, SSS = ?, PhilHealth = ?, CashAdvanced = ?, " +
                "Others = ?, TotalDeduction = ?, NetIncome = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i = 1;
                // Set parameters for each column
                pstmt.setString(1, employee.getLast().getName());
                pstmt.setDouble(2, employee.getLast().getRate());
                pstmt.setDouble(3, employee.getLast().getDays());
                pstmt.setDouble(4, employee.getLast().getSalary());
                pstmt.setDouble(5, employee.getLast().getCommission());
                pstmt.setDouble(6, employee.getLast().getPayroll().getGrossic());
                pstmt.setDouble(7, employee.getLast().getPayroll().getDeduction().getLoans());
                pstmt.setDouble(8, employee.getLast().getPayroll().getDeduction().getSss());
                pstmt.setDouble(9, employee.getLast().getPayroll().getDeduction().getPhilhealth());
                pstmt.setDouble(10, employee.getLast().getPayroll().getDeduction().getCashAdvanced());
                pstmt.setDouble(11, employee.getLast().getPayroll().getDeduction().getOthers());
                pstmt.setDouble(12, employee.getLast().getPayroll().getDeduction().getTotal());
                pstmt.setDouble(13, employee.getLast().getPayroll().getNetic());

                System.out.println(i);
                i++;

                // Add to batch
                pstmt.addBatch();

            // Execute batch update
            pstmt.executeUpdate();
            System.out.println("Payroll list updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void delete(Employees employee){
        String sql = "DELETE FROM EMPLOYEE WHERE EmpId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the EmpId parameter
             // Replace with the actual EmpId to delete
            pstmt.setInt(1, employee.getEmpid());

            // Execute the DELETE query
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Row deleted successfully!");
            } else {
                System.out.println("No row found with the given EmpId.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}
