public class Employees {

    private String name;
    private float rate;
    private float attendance;
    private  Payroll payroll;
    private Deduction deduction;

    //constructors
    public Employees(){
        rate = 0;
        name = "";
        attendance = 0;
        payroll = new Payroll();
    }

    public  Employees(String name, float rate, float attendance, Payroll payroll, Deduction deduction){
        this.name = name;
        this.rate = rate;
        this.attendance = attendance;
        this.payroll = payroll;
        this.deduction = deduction;
    }
    public  Employees(String name, float rate, float attendance){
        this.name = name;
        this.rate = rate;
        this.attendance = attendance;
    }


}
