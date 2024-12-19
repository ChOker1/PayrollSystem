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

        try{
            int i = 1;
            BufferedReader reader = new BufferedReader(new FileReader(FileRead));
            String line = reader.readLine();
            while (line != null){
                String[] read = line.split("`",5);
                Deduction deduction = new Deduction(Integer.parseInt(read[2]),Integer.parseInt(read[3]),Integer.parseInt(read[4]));
                employee.add(new Employees(read[0],Integer.parseInt(read[1]),deduction));
                line = reader.readLine();
                System.out.println(i);
                i++;
            }
            new TableGUI(employee);

            for(Employees e : employee){
                System.out.println(e.toString());
            }


        } catch (Exception e){
            e.printStackTrace();
        }
        Employees employees = new Employees();


    }

    public static void Write(ArrayList<Employees> employees){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
        LocalDate date = LocalDate.now();
        String sdate = date.format(formatter);
        String filedate = sdate + ".txt";
        String TextFileWrite = newDirectoryPath + File.separator + filedate;
        File FileOut = new File(TextFileWrite);

        System.out.println("write");
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
            e.printStackTrace();
        }
    }

    public static void saveOrigin(ArrayList<Employees> employees){
        String FileOut = newDirectoryPath + File.separator + "testfile.txt";

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(FileOut));

            for (int i = 0; i < employees.size(); i++) {
                writer.write(employees.get(i).toSaveOrigin());
                writer.newLine();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}