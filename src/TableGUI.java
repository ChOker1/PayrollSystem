import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;



public class TableGUI {
    private boolean isUpdating = false;  // Declare the flag at the class level

    public TableGUI(ArrayList<Employees> employeeList) {
        // Create the frame
        JFrame frame = new JFrame("Payroll");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 400); // Adjust the size as needed

        // Create the table model
        String[] columnNames = new String[12];
        String[] bar = {"NAME", "RATE", "DAYS", "SALARY", "COMMISSIONS", "GROSS INCOME", "LOANS", "SSS", "OTHERS", "PHILHEALTH", "DEDUCTION", "NET INCOME"};
        System.arraycopy(bar, 0, columnNames, 0, 12);
        int i =0;

        Object[][] data = new Object[employeeList.size()+i][12];
        for (int row = 0; row < employeeList.size(); row++) {
            data[row][0] = employeeList.get(row).getName();
            data[row][1] = employeeList.get(row).getRate();
            data[row][2] = "";
            data[row][3] = employeeList.get(row).getRate() * employeeList.get(row).getDays();
            data[row][4] = "";
            data[row][5] = employeeList.get(row).getPayroll().getGrossic();
            data[row][6] = employeeList.get(row).getPayroll().getDeduction().getLoans();
            data[row][7] = employeeList.get(row).getPayroll().getDeduction().getSss();
            data[row][8] = "";
            data[row][9] = employeeList.get(row).getPayroll().getDeduction().getPhilhealth();
            data[row][10] = employeeList.get(row).getPayroll().getDeduction().getTotal();
            data[row][11] = employeeList.get(row).getPayroll().getNetic();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make only specific columns editable
                return column != 0 && column != 1 && column != 3 && column != 5&& column != 6 && column != 7 && column != 9 && column != 10 && column != 11;
            }
        };

        JTable table = new JTable(model);

        // Add a listener to update calculations based on changes
        model.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Prevent updates during an update to avoid infinite recursion
            if (row >= 0 && column >= 0 && !isUpdating) {
                try {
                    isUpdating = true;  // Set the flag to indicate an update is in progress

                    double days = Double.parseDouble(model.getValueAt(row, 2).toString());
                    employeeList.get(row).setDays(days);
                    double salary = employeeList.getFirst().getSalary();
                    model.setValueAt(salary, row, 3); // Update SALARY

                    double commissions = model.getValueAt(row, 4).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row, 4).toString());
                    employeeList.get(row).setCommission(commissions);
                    employeeList.get(row).computeGross();
                    double grossIncome = employeeList.get(row).getPayroll().getGrossic();
                    model.setValueAt(grossIncome, row, 5); // Update GROSS INCOME

                    double loans = model.getValueAt(row, 6).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row, 6).toString());
                    double sss = model.getValueAt(row, 7).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row, 7).toString());
                    double others = model.getValueAt(row, 8).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row, 8).toString());
                    double philhealth = model.getValueAt(row, 9).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row, 9).toString());

                    Deduction deduction= new Deduction(loans,sss,others,philhealth);
                    employeeList.get(row).getPayroll().setDeduction(deduction);

                    double totalDeduction = employeeList.get(row).getPayroll().getDeduction().getTotal();
                    model.setValueAt(totalDeduction, row, 10); // Update DEDUCTION

                    double netIncome = employeeList.get(row).getnet();
                    model.setValueAt(netIncome, row, 11); // Update NET INCOME
                } catch (NumberFormatException ex) {
                    // Handle invalid input gracefully
                    System.err.println("Invalid input in table cell: " + ex.getMessage());
                } finally {
                    isUpdating = false;  // Reset the flag after the update is done
                }
            }
        });

        // Customize table appearance
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        // Create a panel for the button and set its layout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        bottomPanel.setSize(400,200);

        // Create the button and add an action listener (you can customize the action here)
        JButton save = new JButton("Save");
        JButton add = new JButton("Add");
        save.addActionListener(e -> {
            System.out.println("saved");
            Head.Write(employeeList);
            Head.saveOrigin(employeeList);

        });

        add.addActionListener(e ->{
            model.addRow(new Object[]{});

            int row = model.getRowCount()-1;
            if (model.getValueAt(row,0) != null){
                String n = model.getValueAt(row,0).toString();
                double r =  Double.parseDouble(model.getValueAt(row,1).toString());
                double l = Double.parseDouble(model.getValueAt(row,6).toString());
                double s = Double.parseDouble(model.getValueAt(row,7).toString());
                double p = Double.parseDouble(model.getValueAt(row,9).toString());
                Deduction deduction = new Deduction(l,s,p);
                employeeList.add(new Employees(n,r,deduction));
            }

        });


        bottomPanel.add(add);
        bottomPanel.add(save);


        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        // Add the scroll pane to the frame
        frame.add(mainPanel);
        // Set the frame to be visible
        frame.setVisible(true);


    }


    public static void main(String[] args) {
        Head.run();
    }
}
