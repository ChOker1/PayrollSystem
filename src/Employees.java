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


    //setters

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttendance(float attendance) {
        this.attendance = attendance;
    }
    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    //getters

    public float getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public float getAttendance() {
        return attendance;
    }

    public Payroll getPayroll() {
        return payroll;
    }

    public Deduction getDeduction() {
        return deduction;
    }
}
