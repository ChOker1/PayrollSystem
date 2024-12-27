import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ViewGUI {
    public  ViewGUI(ArrayList<Employees> employeeList) {
        // Create the frame
        JFrame createFrame = new JFrame("Employee Payroll");
        createFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createFrame.setSize(1200, 400); // Adjust the size as needed

        // Create the table model
        String[] columnNames = {"NAME", "RATE", "DAYS", "SALARY", "COMMISSIONS", "GROSS INCOME", "LOANS", "SSS", "PHILHEALTH", "CASH ADVANCED", "OTHERS", "DEDUCTION", "NET INCOME"};

        Object[][] data = new Object[employeeList.size()][13];
        for (int row = 0; row < employeeList.size(); row++) {
            data[row][0] = employeeList.get(row).getName();
            data[row][1] = employeeList.get(row).getRate();
            data[row][2] = employeeList.get(row).getDays();
            data[row][3] = employeeList.get(row).getRate() * employeeList.get(row).getDays();
            data[row][4] = employeeList.get(row).getCommission();
            data[row][5] = employeeList.get(row).getPayroll().getGrossic();
            data[row][6] = employeeList.get(row).getPayroll().getDeduction().getLoans();
            data[row][7] = employeeList.get(row).getPayroll().getDeduction().getSss();
            data[row][8] = employeeList.get(row).getPayroll().getDeduction().getPhilhealth();
            data[row][9] = employeeList.get(row).getPayroll().getDeduction().getCashAdvanced();
            data[row][10] = employeeList.get(row).getPayroll().getDeduction().getOthers();
            data[row][11] = employeeList.get(row).getPayroll().getDeduction().getTotal();
            data[row][12] = employeeList.get(row).getPayroll().getNetic();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };

        JTable table = new JTable(model);

        // Remove the color customization
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());

        // Customize table appearance
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);

        // Create a panel for the button and set its layout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setSize(400, 200);

        // Create the OK button and add an action listener
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            TableGUI.run();
            createFrame.dispose();  // Close the frame when OK is clicked
        });

        bottomPanel.add(okButton);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the scroll pane to the frame
        createFrame.add(mainPanel);
        // Set the frame to be visible
        createFrame.setVisible(true);
        createFrame.setLocationRelativeTo(null);
    }
}
