import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;



public class TableGUI {
    private boolean isUpdating = false;
    private static JFrame frame = new JFrame("Payroll");// Declare the flag at the class level

    public TableGUI(ArrayList<Employees> employeeList) {
        // Create the frame

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
            data[row][2] = employeeList.get(row).getDays();
            data[row][3] = employeeList.get(row).getRate() * employeeList.get(row).getDays();
            data[row][4] = employeeList.get(row).getCommission();
            data[row][5] = employeeList.get(row).getPayroll().getGrossic();
            data[row][6] = employeeList.get(row).getPayroll().getDeduction().getLoans();
            data[row][7] = employeeList.get(row).getPayroll().getDeduction().getSss();
            data[row][8] = "0";
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




        // Set colors to the adjustable cell and dead rows
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Check if the "Days" column (index 2) value is 0
                if (table.getValueAt(row, 2) != null && Double.parseDouble(table.getValueAt(row, 2).toString()) == 0) {
                    // Set the entire row's background color to red
                    cellComponent.setBackground(Color.red);
                } else {
                    // Set default background for editable and non-editable cells
                    if (table.isCellEditable(row, column)) {
                        cellComponent.setBackground(Color.YELLOW);  // Editable cells
                    } else {
                        cellComponent.setBackground(Color.WHITE);  // Non-editable cells
                    }
                }

                return cellComponent;
            }
        });






        // Add a listener to update calculations based on changes
        model.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            // Prevent updates during an update to avoid infinite recursion
            if (row >= 0 && column >= 0 && !isUpdating) {
                try {
                    isUpdating = true;  // Set the flag to indicate an update is in progress

                    if (column == 2 || column == 4 || column == 8 ) {
                        Object newValue = model.getValueAt(row, column);
                        if (newValue != null && !newValue.toString().trim().isEmpty()) {
                            // Replace the "0" with the entered value
                            if ("0".equals(model.getValueAt(row, column).toString())) {
                                model.setValueAt(newValue, row, column);
                            }
                        }
                    }

                    double days = Double.parseDouble(model.getValueAt(row, 2).toString());
                    employeeList.get(row).setDays(days);
                    double salary = employeeList.get(row).getSalary();
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

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();

                // Check if the selected cell is editable
                char in = e.getKeyChar();
                if (row >= 0 && column >= 0 && model.isCellEditable(row, column)&& Character.isDigit(in)) {
                    model.setValueAt("", row, column);  // Clear the cell value
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
            boolean hasEmptyFields = false;

            // Check for empty fields in the table
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (model.getValueAt(row, col) == null || model.getValueAt(row, col).toString().trim().isEmpty()) {
                        hasEmptyFields = true;
                        break;
                    }
                }
                if (hasEmptyFields) break; // Exit loop if an empty cell is found
            }

            // Show a popup if any field is empty
            if (hasEmptyFields) {
                JOptionPane.showMessageDialog(frame, "Please ensure all fields are filled before saving.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Proceed with saving
                System.out.println("Saved");
                Head.Write(employeeList);
                Head.saveOrigin(employeeList);
            }
        });



        add.addActionListener(e -> {
            // Create a new row in the table model

            // Create a new JFrame for input
            JFrame input = new JFrame("Input Details");
            input.setSize(400, 300);
            input.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            input.setLocationRelativeTo(null);

            // Create a JPanel for the form
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 2, 5, 5)); // 6 rows, 2 columns, spacing of 5px

            // Add labels and text fields to the panel
            JLabel nameLabel = new JLabel("Name:");
            JTextField nameField = new JTextField();
            panel.add(nameLabel);
            panel.add(nameField);

            JLabel rateLabel = new JLabel("Rate:");
            JTextField rateField = new JTextField();
            rateField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    try {
                        Double.parseDouble(text);
                        return true;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(input, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            });
            panel.add(rateLabel);
            panel.add(rateField);

            JLabel loansLabel = new JLabel("Loans:");
            JTextField loansField = new JTextField();
            loansField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    try {
                        Double.parseDouble(text);
                        return true;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(input, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            });
            panel.add(loansLabel);
            panel.add(loansField);

            JLabel sssLabel = new JLabel("SSS:");
            JTextField sssField = new JTextField();
            sssField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    try {
                        Double.parseDouble(text);
                        return true;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(input, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            });
            panel.add(sssLabel);
            panel.add(sssField);

            JLabel philhealthLabel = new JLabel("PhilHealth:");
            JTextField philhealthField = new JTextField();
            philhealthField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    try {
                        Double.parseDouble(text);
                        return true;
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(input, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            });
            panel.add(philhealthLabel);
            panel.add(philhealthField);

            // Add buttons to save or cancel
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            panel.add(saveButton);
            panel.add(cancelButton);

            // Add action listeners for buttons
            saveButton.addActionListener(ev -> {
                try {
                    String name = nameField.getText();
                    double rate = Double.parseDouble(rateField.getText());
                    double loans = Double.parseDouble(loansField.getText());
                    double sss = Double.parseDouble(sssField.getText());
                    double philhealth = Double.parseDouble(philhealthField.getText());

                    // Add input data to the table row
                    Deduction deduction = new Deduction(loans, sss, philhealth);
                    Employees newEmployee = new Employees(name.toUpperCase().trim(), rate, deduction);
                    employeeList.add(newEmployee);

                    // Add a new row to the table model
                    Object[] newRow = {
                            newEmployee.getName(),
                            newEmployee.getRate(),
                            "",
                            newEmployee.getSalary(),
                            "",
                            newEmployee.getPayroll().getGrossic(),
                            newEmployee.getPayroll().getDeduction().getLoans(),
                            newEmployee.getPayroll().getDeduction().getSss(),
                            "",
                            newEmployee.getPayroll().getDeduction().getPhilhealth(),
                            newEmployee.getPayroll().getDeduction().getTotal(),
                            newEmployee.getPayroll().getNetic()
                    };
                    model.addRow(newRow);

                    // Save the data
                    Head.saveOrigin(employeeList);

                    input.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(input, "Please enter valid numeric values for rate, loans, SSS, and PhilHealth.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(ev -> input.dispose());

            // Add the panel to the JFrame and make it visible
            input.add(panel);
            input.setVisible(true);
        });



        bottomPanel.add(add);
        bottomPanel.add(save);


        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            // Ask for the employee's name to delete
            String nameToDelete = JOptionPane.showInputDialog(frame, "Enter the name of the employee to delete:", "Delete Employee", JOptionPane.PLAIN_MESSAGE);

            if (nameToDelete != null && !nameToDelete.trim().isEmpty()) {
                boolean found = false;

                // Iterate through the employeeList to find and delete the employee
                for (int row = 0; row < employeeList.size(); row++) {
                    if (employeeList.get(row).getName().equalsIgnoreCase(nameToDelete.trim())) {
                        found = true;

                        // Remove from the employeeList
                        employeeList.remove(row);

                        // Remove from the table model
                        model.removeRow(row);

                        //Updates the list and origin

                        Head.saveOrigin(employeeList);

                        JOptionPane.showMessageDialog(frame, "Employee \"" + nameToDelete + "\" has been deleted.", "Delete Success", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }

                // If the name was not found
                if (!found) {
                    JOptionPane.showMessageDialog(frame, "Employee \"" + nameToDelete + "\" not found.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No name entered. Deletion canceled.", "Delete Canceled", JOptionPane.WARNING_MESSAGE);
            }
        });


        bottomPanel.add(delete);



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

        frame.setLocationRelativeTo(null);


    }


    public static void main(String[] args) {
        JFrame sframe = new JFrame();
        JPanel main = new JPanel();
        JPanel start = new JPanel();
        JLabel label = new JLabel("Welcome to Yamban Payroll System");
        JButton create = new JButton("Create");
        create.addActionListener(e ->{

            sframe.dispose();
            Head.run();


        });

        create.setVisible(true);

        JButton viewr = new JButton("View Recent");
        viewr.addActionListener(e -> {

        });

        start.add(label);

        JPanel bpannel = new JPanel();
        bpannel.add(create);
        bpannel.add(viewr);
        JScrollPane pane = new JScrollPane(start);
        main.setLayout(new BorderLayout());
        main.add(start,BorderLayout.NORTH);
        main.add(bpannel,BorderLayout.CENTER);


        sframe.add(main);



        sframe.setVisible(true);
        sframe.setLocationRelativeTo(null);
        sframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sframe.setSize(700,500);






    }


}
