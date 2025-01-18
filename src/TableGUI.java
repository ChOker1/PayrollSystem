import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class TableGUI {
    private boolean isUpdating = false;
    private static JFrame createFrame = new JFrame("Payroll");// Declare the flag at the class level

    public TableGUI(ArrayList<Employees> employeeList) {
        // Create the frame

        System.out.println("gui");
        createFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createFrame.setSize(1200, 400); // Adjust the size as needed

        // Create the table model
        String[] columnNames = new String[13];
        String[] bar = {"NAME", "RATE", "DAYS", "SALARY", "COMMISSIONS", "GROSS INCOME", "LOANS", "SSS", "PHILHEALTH","CASH ADVANCED", "OTHERS", "DEDUCTION", "NET INCOME"};
        System.arraycopy(bar, 0, columnNames, 0, 13);

        int i =0;

        Object[][] data = new Object[employeeList.size()+i][13];
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
                // Make only specific columns editable
                return column != 3 && column != 5 && column != 12;
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
                    cellComponent.setBackground(Color.orange);
                } else {
                    // Set default background for editable and non-editable cells
                    if (table.isCellEditable(row, column)) {
                        cellComponent.setBackground(Color.WHITE);  // Editable cells
                    } else {
                        cellComponent.setBackground(Color.YELLOW);  // Non-editable cells
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
                    double philhealth = model.getValueAt(row, 8).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row, 8).toString());
                    double cashAdvanced = model.getValueAt(row,9).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row,9).toString());
                    double others = model.getValueAt(row,10).toString().isEmpty() ? 0 : Double.parseDouble(model.getValueAt(row,10).toString());


                    Deduction deduction= new Deduction(loans,sss,philhealth,cashAdvanced,others);
                    employeeList.get(row).getPayroll().setDeduction(deduction);

                    double totalDeduction = employeeList.get(row).getPayroll().getDeduction().getTotal();
                    model.setValueAt(totalDeduction, row, 11); // Update DEDUCTION

                    double netIncome = employeeList.get(row).getnet();
                    model.setValueAt(netIncome, row, 12); // Update NET INCOME
                } catch (NumberFormatException ex) {
                    // Handle invalid input gracefully
                    System.err.println("Invalid input in table cell: " + ex.getMessage());
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                finally {
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
                JOptionPane.showMessageDialog(createFrame, "Please ensure all fields are filled before saving.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Proceed with saving
                Head.saveOrigin(employeeList);
                Head.save(employeeList);
                System.out.println("Saved");
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
                            "0",
                            newEmployee.getSalary(),
                            "0",
                            newEmployee.getPayroll().getGrossic(),
                            newEmployee.getPayroll().getDeduction().getLoans(),
                            newEmployee.getPayroll().getDeduction().getSss(),
                            newEmployee.getPayroll().getDeduction().getPhilhealth(),
                            "0",
                            "0",
                            newEmployee.getPayroll().getDeduction().getTotal(),
                            newEmployee.getPayroll().getNetic()
                    };
                    model.addRow(newRow);

                    // Save the data
                    Head.add(employeeList);




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






        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            // Ask for the employee's name to delete
            String nameToDelete = JOptionPane.showInputDialog(createFrame, "Enter the name of the employee to delete:", "Delete Employee", JOptionPane.PLAIN_MESSAGE);

            if (nameToDelete != null && !nameToDelete.trim().isEmpty()) {
                boolean found = false;

                // Iterate through the employeeList to find and delete the employee
                for (int row = 0; row < employeeList.size(); row++) {
                    if (employeeList.get(row).getName().equalsIgnoreCase(nameToDelete.trim())) {
                        found = true;

                        Head.delete(employeeList.get(row));

                        // Remove from the employeeList
                        employeeList.remove(row);

                        // Remove from the table model
                        model.removeRow(row);

                        //Updates the list and origin



                        JOptionPane.showMessageDialog(createFrame, "Employee \"" + nameToDelete + "\" has been deleted.", "Delete Success", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }
                }

                // If the name was not found
                if (!found) {
                    JOptionPane.showMessageDialog(createFrame, "Employee \"" + nameToDelete + "\" not found.", "Delete Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(createFrame, "No name entered. Deletion canceled.", "Delete Canceled", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton ok = new JButton("Ok");
        ok.addActionListener(e -> {
            createFrame.dispose();
            run();
        });

        bottomPanel.add(add);
        bottomPanel.add(delete);
        bottomPanel.add(save);
        bottomPanel.add(ok);



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


    //vew methods
    public static void createMultiDatePicker() {
        JFrame frame = new JFrame("Multi-Date Picker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);

        JPanel calendarPanel = new JPanel(new BorderLayout());
        JLabel selectedDatesLabel = createSelectedDatesLabel();
        JPanel footerPanel = createFooterPanel(frame, selectedDatesLabel);

        // Shared state across months
        Set<LocalDate> selectedDates = new HashSet<>();
        final LocalDate[] range = new LocalDate[2]; // Stores the range (start, end)

        // Initialize the calendar with the current month
        updateCalendarPanel(calendarPanel, LocalDate.now(), selectedDates, range, selectedDatesLabel);

        frame.add(calendarPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static JPanel headerPanel = null;

    private static void updateCalendarPanel(JPanel calendarPanel, LocalDate currentMonth, Set<LocalDate> selectedDates,
                                            LocalDate[] range, JLabel selectedDatesLabel) {
        calendarPanel.removeAll();  // Clear the panel

        // Create header panel if it doesn't exist yet
        if (headerPanel == null) {
            headerPanel = createHeaderPanel(currentMonth, selectedDates, range, selectedDatesLabel);
        } else {
            // Update the existing header panel
            JLabel monthLabel = (JLabel) headerPanel.getComponent(1); // Get the month label
            monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
        }

        // Create and add the grid panel
        JPanel gridPanel = createCalendarGrid(currentMonth, selectedDates, range, selectedDatesLabel);
        calendarPanel.add(headerPanel, BorderLayout.NORTH);
        calendarPanel.add(gridPanel, BorderLayout.CENTER);

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private static JPanel createHeaderPanel(LocalDate currentMonth, Set<LocalDate> selectedDates,
                                            LocalDate[] range, JLabel selectedDatesLabel) {
        JPanel headerPanel = new JPanel(new BorderLayout());

        JButton prevButton = new JButton("<<");
        JButton nextButton = new JButton(">>");
        JLabel monthLabel = new JLabel(currentMonth.getMonth() + " " + currentMonth.getYear(), SwingConstants.CENTER);

        prevButton.addActionListener(e -> updateCalendarPanel((JPanel) headerPanel.getParent(), currentMonth.minusMonths(1),
                selectedDates, range, selectedDatesLabel));
        nextButton.addActionListener(e -> updateCalendarPanel((JPanel) headerPanel.getParent(), currentMonth.plusMonths(1),
                selectedDates, range, selectedDatesLabel));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        return headerPanel;
    }

    private static JPanel createCalendarGrid(LocalDate currentMonth, Set<LocalDate> selectedDates,
                                             LocalDate[] range, JLabel selectedDatesLabel) {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new BorderLayout());

        // Add Day of the Week Indicator (This will not be duplicated)
        JPanel weekHeaderPanel = new JPanel(new GridLayout(1, 7));
        String[] dayOfWeekNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : dayOfWeekNames) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            weekHeaderPanel.add(dayLabel);
        }
        gridPanel.add(weekHeaderPanel, BorderLayout.NORTH);

        // Create the main calendar grid with 7 rows and 7 columns
        JPanel calendarGrid = new JPanel(new GridLayout(7, 7));  // 7 rows and 7 columns
        LocalDate firstDayOfMonth = currentMonth.withDayOfMonth(1);
        int startDay = firstDayOfMonth.getDayOfWeek().getValue() % 7; // Adjust for Sunday=0
        int daysInMonth = currentMonth.lengthOfMonth();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Clear the calendar grid before adding new days
        calendarGrid.removeAll();

        // Add empty placeholders for days before the start of the month
        for (int i = 0; i < startDay; i++) {
            calendarGrid.add(new JLabel()); // Empty cell for days before the month starts
        }

        // Add buttons for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.withDayOfMonth(day);
            JButton dayButton = new JButton(String.valueOf(day));

            // Reset the button color to default for non-selected days
            dayButton.setBackground(null);

            // Highlight if date is in the selected range
            if (selectedDates.contains(date)) {
                dayButton.setBackground(Color.CYAN);
            }

            dayButton.addActionListener(e -> {
                if (range[0] == null || range[1] != null) {
                    // Reset the range and select the first date
                    range[0] = date;
                    range[1] = null;
                    selectedDates.clear();
                    selectedDates.add(date);
                } else {
                    // Select the second date and highlight the range
                    range[1] = date;

                    LocalDate start = range[0].isBefore(range[1]) ? range[0] : range[1];
                    LocalDate end = range[0].isBefore(range[1]) ? range[1] : range[0];

                    selectedDates.clear();
                    while (!start.isAfter(end)) {
                        selectedDates.add(start);
                        start = start.plusDays(1);
                    }
                }

                // Refresh the grid to update button highlights
                updateCalendarPanel((JPanel) calendarGrid.getParent(), currentMonth, selectedDates, range, selectedDatesLabel);
                updateSelectedDatesLabel(selectedDates, selectedDatesLabel, dateFormatter);
            });

            calendarGrid.add(dayButton);
        }

        // Add empty placeholders after the last day to ensure 7 rows
        int remainingCells = (startDay + daysInMonth) % 7;
        for (int i = remainingCells; i < 7 && i != 0; i++) {
            calendarGrid.add(new JLabel());
        }

        // Ensure the grid has exactly 7 rows (49 cells)
        while (calendarGrid.getComponentCount() < 49) {
            calendarGrid.add(new JLabel()); // Add empty labels to fill up the grid
        }

        // Add the calendar grid to the panel
        gridPanel.add(calendarGrid, BorderLayout.CENTER);
        return gridPanel;
    }



    private static JLabel createSelectedDatesLabel() {
        JLabel selectedDatesLabel = new JLabel("Selected Dates: None");
        selectedDatesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return selectedDatesLabel;
    }

    private static JPanel createFooterPanel(JFrame frame, JLabel selectedDatesLabel) {
        JPanel footerPanel = new JPanel(new BorderLayout());

        // Place selected dates label on the left side
        footerPanel.add(selectedDatesLabel, BorderLayout.CENTER);

        // Create the OK button on the right side
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(80, 30));

        // Action listener for OK button
        okButton.addActionListener(e -> {
            LocalDate start = null, end = null;
            // Extract the selected start and end dates
            if (!selectedDatesLabel.getText().equals("Selected Dates: None")) {
                String selectedText = selectedDatesLabel.getText().replace("Selected Dates: ", "");
                String[] dates = selectedText.split(" to ");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                if (dates.length == 2) {
                    start = LocalDate.parse(dates[0], formatter);
                    end = LocalDate.parse(dates[1], formatter);
                }
            }

            // Check if the dates were parsed correctly and assign them
            if (start != null && end != null) {
                System.out.println("Start: " + start);
                System.out.println("End: " + end);
                Head.view(start,end);
            } else {
                JOptionPane.showMessageDialog(frame, "No valid date range selected!");
            }
            frame.dispose(); // Close the application after selecting dates
        });

        // Add OK button to the right side
        footerPanel.add(okButton, BorderLayout.EAST);

        return footerPanel;
    }

    private static void updateSelectedDatesLabel(Set<LocalDate> selectedDates, JLabel selectedDatesLabel,
                                                 DateTimeFormatter dateFormatter) {
        if (selectedDates.isEmpty()) {
            selectedDatesLabel.setText("Selected Dates: None");
        } else {
            LocalDate start = selectedDates.stream().min(LocalDate::compareTo).orElse(null);
            LocalDate end = selectedDates.stream().max(LocalDate::compareTo).orElse(null);

            if (start != null && end != null) {
                selectedDatesLabel.setText("Selected Dates: " + start.format(dateFormatter) + " to " + end.format(dateFormatter));
            }
        }
    }








    //Main
    public static void run() {
        ArrayList<Employees>employee = new ArrayList<>();

        JFrame sframe = new JFrame();

        sframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sframe.setSize(400,300);
        sframe.setLocationRelativeTo(null);


        JPanel main = new JPanel();
        JPanel start = new JPanel();
        JLabel label = new JLabel("Welcome to Yamban Payroll System");
        JButton create = new JButton("Create");
        create.addActionListener(e ->{

            sframe.dispose();
            Head.run(employee);


        });

        create.setVisible(true);

        JButton viewr = new JButton("View Recent");
        viewr.addActionListener(e -> {
            sframe.dispose();
            SwingUtilities.invokeLater(() -> createMultiDatePicker());


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

    }

    public static void main(String[] args) {
        run();
    }
}