import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import  java.sql.Date;
import java.time.LocalDate;

public class Head {
    static Connection conn;
    static ResultSet employeers;

    public static void run(ArrayList<Employees> employee) {
        System.out.println("head");


        LocalDate now = LocalDate.now();
        System.out.println(now);
        LocalDate mon = now.with(DayOfWeek.MONDAY);
        LocalDate sat = now.with(DayOfWeek.SATURDAY);
        System.out.println(mon);
        System.out.println(sat);


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

            employeers = conn.createStatement().executeQuery("SELECT * FROM EMPLOYEE");
            while (employeers.next()) {
                Deduction deduction = new Deduction(employeers.getDouble("Loans"), employeers.getDouble("SSS"), employeers.getDouble("PhilHealth"), employeers.getDouble("CashAdvanced"), employeers.getDouble("Others") );
                Payroll payroll = new Payroll(employeers.getDouble("Gross"), employeers.getDouble("NetIncome"),deduction);
                employee.add(new Employees(employeers.getInt("EmpId"), employeers.getString("Name"), employeers.getDouble("Rate"), employeers.getDouble("NoOfDays"), employeers.getDouble("Salary"), employeers.getDouble("Commissions"),payroll));


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(employee.size());




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
            for (Employees payroll : employee) {
                System.out.println(payroll.getName());
                System.out.println(payroll.toString());
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
                pstmt.setInt(14, payroll.getEmpid());


                // Add to batch
                pstmt.addBatch();
                pstmt.executeUpdate();

            }


            // Execute batch update
            System.out.println("Payroll list updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void save(ArrayList<Employees> employee) {
        LocalDate now = LocalDate.now();
        Date mon = Date.valueOf(now.with(DayOfWeek.MONDAY));
        Date sat = Date.valueOf(now.with(DayOfWeek.SATURDAY));

        int id = 0;
        String datee = "INSERT INTO DATE (DateMon, DateSat) VALUES (?, ?)";

        try {
            // Query to check if the DateMon already exists
            String query = "SELECT * FROM DATE WHERE DateMon = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setDate(1, mon);
                ResultSet rs = pstmt.executeQuery();

                // If a match is found, get the DateId
                if (rs.next()) {
                    id = rs.getInt("DateId");
                }
            }

            // If no existing record was found, insert a new one
            if (id == 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(datee, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setDate(1, mon);
                    pstmt.setDate(2, sat);
                    pstmt.executeUpdate();

                    // Retrieve the generated DateId
                    ResultSet rs = pstmt.getGeneratedKeys();
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    System.out.println("Error inserting date: ");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking existing dates: ");
            e.printStackTrace();
        }

        // SQL for checking if a record with the same EmpId and DateId already exists
        String checkExistenceSQL = "SELECT COUNT(*) FROM EMPLOYEEARCHIVE WHERE EmpId = ? AND DateId = ?";

        // SQL for updating or inserting employee archive
        String updateSQL = "UPDATE EMPLOYEEARCHIVE SET " +
                "Name = ?, Rate = ?, NoOfDays = ?, Salary = ?, Commissions = ?, Gross = ?, Loans = ?, " +
                "SSS = ?, PhilHealth = ?, CashAdvanced = ?, Others = ?, TotalDeduction = ?, NetIncome = ? " +
                "WHERE EmpId = ? AND DateId = ?";

        String insertSQL = "INSERT INTO EMPLOYEEARCHIVE (" +
                "Name, Rate, NoOfDays, Salary, Commissions, Gross, Loans, SSS, PhilHealth, CashAdvanced, " +
                "Others, TotalDeduction, NetIncome, DateId, EmpId" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkExistenceSQL);
             PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {


            employee = update(employee);

            for (Employees payroll : employee) {

                System.out.println(employee.size());
                // Check if the record already exists
                checkStmt.setInt(1, employee.indexOf(payroll)+1);
                checkStmt.setInt(2, id);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getInt(1) > 0) {
                    // If the record exists, update it
                    updateStmt.setString(1, payroll.getName());
                    updateStmt.setDouble(2, payroll.getRate());
                    updateStmt.setDouble(3, payroll.getDays());
                    updateStmt.setDouble(4, payroll.getSalary());
                    updateStmt.setDouble(5, payroll.getCommission());
                    updateStmt.setDouble(6, payroll.getPayroll().getGrossic());
                    updateStmt.setDouble(7, payroll.getPayroll().getDeduction().getLoans());
                    updateStmt.setDouble(8, payroll.getPayroll().getDeduction().getSss());
                    updateStmt.setDouble(9, payroll.getPayroll().getDeduction().getPhilhealth());
                    updateStmt.setDouble(10, payroll.getPayroll().getDeduction().getCashAdvanced());
                    updateStmt.setDouble(11, payroll.getPayroll().getDeduction().getOthers());
                    updateStmt.setDouble(12, payroll.getPayroll().getDeduction().getTotal());
                    updateStmt.setDouble(13, payroll.getPayroll().getNetic());
                    updateStmt.setInt(14, payroll.getEmpid());
                    updateStmt.setInt(15, id);

                    updateStmt.executeUpdate();
                    System.out.println("Record updated");
                } else {
                    // If the record doesn't exist, insert a new one
                    insertStmt.setString(1, payroll.getName());
                    insertStmt.setDouble(2, payroll.getRate());
                    insertStmt.setDouble(3, payroll.getDays());
                    insertStmt.setDouble(4, payroll.getSalary());
                    insertStmt.setDouble(5, payroll.getCommission());
                    insertStmt.setDouble(6, payroll.getPayroll().getGrossic());
                    insertStmt.setDouble(7, payroll.getPayroll().getDeduction().getLoans());
                    insertStmt.setDouble(8, payroll.getPayroll().getDeduction().getSss());
                    insertStmt.setDouble(9, payroll.getPayroll().getDeduction().getPhilhealth());
                    insertStmt.setDouble(10, payroll.getPayroll().getDeduction().getCashAdvanced());
                    insertStmt.setDouble(11, payroll.getPayroll().getDeduction().getOthers());
                    insertStmt.setDouble(12, payroll.getPayroll().getDeduction().getTotal());
                    insertStmt.setDouble(13, payroll.getPayroll().getNetic());
                    insertStmt.setInt(14, id); // Use the found or generated DateId
                    insertStmt.setInt(15, payroll.getEmpid()); // Use the employee ID from the object

                    insertStmt.executeUpdate(); // Execute insert immediately
                    System.out.println("Record inserted");
                }
            }

            System.out.println("Archive Updated or Inserted");

        } catch (SQLException e) {
            System.out.println("Save error");
            e.printStackTrace();
        }
    }





    public static void add(ArrayList<Employees> employee){

        String sql = "INSERT EMPLOYEE SET " +
                "Name = ?, Rate = ?, NoOfDays = ?, Salary = ?, Commissions = ?, " +
                "Gross = ?, Loans = ?, SSS = ?, PhilHealth = ?, CashAdvanced = ?, " +
                "Others = ?, TotalDeduction = ?, NetIncome = ? ";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

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


                // Add to batch
                pstmt.addBatch();

            System.out.println("add");

            // Execute batch update
            pstmt.executeUpdate();
            System.out.println("Payroll list updated successfully!");

        } catch (SQLException e) {
            System.out.println("Add error");
            e.printStackTrace();
        }

    }

    public static void delete(Employees employee){
        String delorg = "DELETE FROM EMPLOYEE WHERE EmpId = ?";

        String delarc = "DELETE FROM EMPLOYEEARCHIVE WHERE EmpId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(delorg);
             PreparedStatement arcStmt = conn.prepareStatement(delarc)) {

            // Set the EmpId parameter
             // Replace with the actual EmpId to delete
            pstmt.setInt(1, employee.getEmpid());
            arcStmt.setInt(1,employee.getEmpid());

            // Execute the DELETE query
            int rowsAffected = pstmt.executeUpdate();
            int rowArc = arcStmt.executeUpdate();
            if (rowsAffected > 0 && rowArc > 0) {
                System.out.println("Row deleted successfully!");
            } else {
                System.out.println("No row found with the given EmpId.");
            }

        } catch (SQLException e) {
            System.out.println(" delete error");
            e.printStackTrace();
        }

    }

    public static ArrayList<Employees> update (ArrayList<Employees> employee){

        employee.removeAll(employee);
        try {
            employeers = conn.createStatement().executeQuery("SELECT * FROM EMPLOYEE");
            while (employeers.next()) {
                Deduction deduction = new Deduction(employeers.getDouble("Loans"), employeers.getDouble("SSS"), employeers.getDouble("PhilHealth"), employeers.getDouble("CashAdvanced"), employeers.getDouble("Others") );
                Payroll payroll = new Payroll(employeers.getDouble("Gross"), employeers.getDouble("NetIncome"),deduction);
                employee.add(new Employees(employeers.getInt("EmpId"), employeers.getString("Name"), employeers.getDouble("Rate"), employeers.getDouble("NoOfDays"), employeers.getDouble("Salary"), employeers.getDouble("Commissions"),payroll));


            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return employee;
    }



    public static void view (LocalDate start,LocalDate end){
        ArrayList<Employees>employee = new ArrayList<>();
        Date mon = Date.valueOf(start.with(DayOfWeek.MONDAY));
        Date sat = Date.valueOf(end.with(DayOfWeek.SATURDAY));

        String url = "jdbc:mysql://localhost:3306/Payroll";  // Replace with your DB details
        String user = "root";  // Replace with your MySQL username
        String password = "722811@";  // Replace with your MySQL password

        try {
            // Load MySQL JDBC driver (optional in newer versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            conn = DriverManager.getConnection(url, user, password);

            System.out.println("Connected to MySQL database!");


            employeers = conn.createStatement().executeQuery("SELECT * FROM EMPLOYEEARCHIVE");
            ResultSet date = conn.createStatement().executeQuery("SELECT * FROM DATE");
            ArrayList<Integer> did = new ArrayList<>();
            while (date.next()) {
                // Check if DateMon is on or after 'mon' and DateSat is on or before 'sat'
                if ((date.getDate("DateMon").after(mon) || date.getDate("DateMon").equals(mon)) &&
                        (date.getDate("DateSat").before(sat) || date.getDate("DateSat").equals(sat))) {
                    did.add(date.getInt("DateId"));
                    System.out.println(did.get(did.size() - 1)); // Print the last added DateId
                }
            }

            try {
                while (employeers.next()) {
                    if (did.contains(employeers.getInt("DateId"))) {
                        boolean found = false;

                        // Iterate over the employees list and check if EmpId matches
                        for (Employees e : new ArrayList<>(employee)) { // Iterate over a copy to prevent modification issues
                            if (e.getEmpid() == employeers.getInt("EmpId")) {
                                // Update the existing employee
                                e.addDays(employeers.getDouble("NoOfDays"));
                                e.addSalary(employeers.getDouble("Salary"));
                                e.addCommission(employeers.getDouble("Commissions"));
                                e.getPayroll().addGrossic(employeers.getDouble("Gross"));

                                e.getPayroll().getDeduction().addLoans(employeers.getDouble("Loans"));
                                e.getPayroll().getDeduction().addSss(employeers.getDouble("SSS"));
                                e.getPayroll().getDeduction().addPhilhealth(employeers.getDouble("PhilHealth"));
                                e.getPayroll().getDeduction().addCashAdvanced(employeers.getDouble("CashAdvanced"));
                                e.getPayroll().getDeduction().addOthers(employeers.getDouble("Others"));

                                e.getPayroll().addNetic(employeers.getDouble("NetIncome"));
                                System.out.println("add");
                                found = true;
                                break;
                            }
                        }

                        // If no matching employee was found, add a new one
                        if (!found) {
                            Deduction deduction = new Deduction(
                                    employeers.getDouble("Loans"),
                                    employeers.getDouble("SSS"),
                                    employeers.getDouble("PhilHealth"),
                                    employeers.getDouble("CashAdvanced"),
                                    employeers.getDouble("Others")
                            );
                            Payroll payroll = new Payroll(
                                    employeers.getDouble("Gross"),
                                    employeers.getDouble("NetIncome"),
                                    deduction
                            );
                            employee.add(new Employees(
                                    employeers.getInt("EmpId"),
                                    employeers.getString("Name"),
                                    employeers.getDouble("Rate"),
                                    employeers.getDouble("NoOfDays"),
                                    employeers.getDouble("Salary"),
                                    employeers.getDouble("Commissions"),
                                    payroll
                            ));
                            System.out.println("insert");
                        }
                    }

                    System.out.println(employee.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            int i = 1;

            // Create the TableGUI with the list of employees
            new ViewGUI(employee);

            // Output the employee details for verification
            for (Employees e : employee) {
                System.out.println(e.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}