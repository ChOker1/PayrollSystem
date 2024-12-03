import  java.io.BufferedReader;
import  java.io.BufferedWriter;
import  java.io.FileReader;
import  java.io.FileWriter;
import  java.io.File;
import  java.util.ArrayList;
public class Main {
    public static void main(String[] args) {

        String userHome = System.getProperty("user.home");
        String documentsPath = userHome + File.separator + "Documents";
        String newDirectoryName = "Payroll Archives";
        String newDirectoryPath = documentsPath + File.separator + newDirectoryName;
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

        String textFilePath = newDirectoryPath + File.separator + "Test.txt";
        File textFile = new File(textFilePath);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFilePath));
            ArrayList<Employees> employees = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] content = line.split("`");
                    if (content.length >= 12) { // Ensure sufficient columns
                        Payroll payroll = new Payroll(Double.parseDouble(content[5]),
                                Double.parseDouble(content[11]),
                                Double.parseDouble(content[4]));
                        Deduction deduction = new Deduction(Double.parseDouble(content[7]),
                                Double.parseDouble(content[9]),
                                Double.parseDouble(content[6]),
                                Double.parseDouble(content[10]));
                        employees.add(new Employees(content[0],
                                Float.parseFloat(content[1]),
                                Float.parseFloat(content[2]),
                                payroll, deduction));
                    } else {
                        System.out.println("Skipping malformed line: " + line);
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Error parsing line: " + line);
                    ex.printStackTrace();
                }

            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Employees employees = new Employees();


    }
}