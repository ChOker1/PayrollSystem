import  java.io.BufferedReader;
import  java.io.BufferedWriter;
import  java.io.FileReader;
import  java.io.FileWriter;
import  java.io.File;

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

        String textFilePath = newDirectoryPath + File.separator + "testfile.txt";
        File textFile = new File(textFilePath);



        try{
            if (textFile.createNewFile()) {
                System.out.println("File created: " + textFile.getAbsolutePath());
            } else {
                System.out.println("File already exists: " + textFile.getAbsolutePath());
            }
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(textFile));


        } catch (Exception e){
            e.printStackTrace();
        }

        Employees employees = new Employees();


    }
}