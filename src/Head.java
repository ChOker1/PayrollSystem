import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Head {
    static String newDirectoryPath;
    public static void run() {


        ArrayList<Employees> employee = new ArrayList<>();


        String userHome = System.getProperty("user.home");
        String documentsPath = userHome + File.separator + "Documents";
        String newDirectoryName = "Payroll Archives";
        newDirectoryPath = documentsPath + File.separator + newDirectoryName;
        File newDirectory = new File(newDirectoryPath);

        System.out.println("User Home: " + userHome);
        System.out.println("Documents Path: " + documentsPath);
        System.out.println("New Directory Path: " + newDirectoryPath);

        if (newDirectory.exists()) {
            System.out.println("Directory already exists: " + newDirectory.getAbsolutePath());
        } else if (newDirectory.mkdirs()) {
            System.out.println("Directory successfully created at: " + newDirectory.getAbsolutePath());
        } else {
            System.out.println("Failed to create directory at: " + newDirectory.getAbsolutePath());
        }

        String textFilePath = newDirectoryPath + File.separator + "testfile.txt";
        File FileRead = new File(textFilePath);

        try {
            int i = 1;
            BufferedReader reader = new BufferedReader(new FileReader(FileRead));
            String line = reader.readLine();

            while (line != null) {
                // Split the line by backticks (`) without limiting the number of parts
                String[] read = line.split("`");

                // Check if there are at least 7 parts in the read array (or handle missing parts gracefully)
                if (read.length >= 7) {
                    // Parse the deduction values
                    int loans = Integer.parseInt(read[4]);
                    int sss = Integer.parseInt(read[5]);
                    int philhealth = Integer.parseInt(read[6]);

                    // Create a new Deduction object
                    Deduction deduction = new Deduction(loans, sss, philhealth);

                    // Create a new Employee object with the parsed data
                    employee.add(new Employees(read[0], Integer.parseInt(read[1]), Integer.parseInt(read[2]), Integer.parseInt(read[3]), deduction));
                } else {
                    // Handle cases where there aren't enough fields (this should ideally not happen if the data is well-formed)
                    System.err.println("Skipping invalid line: " + line);
                }
                System.out.println(i);

                // Read the next line
                line = reader.readLine();
                i++;
            }

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

    public static void Write(ArrayList<Employees> employees){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM");
        LocalDate date = LocalDate.now();
        String sdate = date.format(formatter);
        String[] tdate = sdate.split("-");
        String filedate = sdate + ".txt";
        String TextFileWrite = newDirectoryPath + File.separator + filedate;
        File FileOut = new File(TextFileWrite);

        System.out.println("write file to archived");
        try {
            if (FileOut.createNewFile()) {
                System.out.println("File created: " + FileOut.getAbsolutePath());
            } else {
                System.out.println("File already exists: " + FileOut.getAbsolutePath());
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileOut));

            for(int i = 0 ; i < employees.size(); i++){
                writer.write(employees.get(i).toString());
                writer.newLine();
            }

            writer.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void saveOrigin(ArrayList<Employees> employees){
        String FileOut = newDirectoryPath + File.separator + "testfile.txt";
        System.out.println("updated origin");

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(FileOut));

            for (int i = 0; i < employees.size(); i++) {
                writer.write(employees.get(i).toSaveOrigin());
                writer.newLine();
            }
            writer.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void reada(String loc,String week){
        String filein = newDirectoryPath + File.separator + loc;
    }
}