public class Employees {

    private String name;
    private float rate;
    private float days;
    private  Payroll payroll;

    //constructors
    public Employees(){
        rate = 0;
        name = "";
        days = 0;
        payroll = new Payroll();
    }

    public  Employees(String name, float rate, float Days, Payroll payroll){
        this.name = name;
        this.rate = rate;
        this.days = Days;
        this.payroll = payroll;

    }
    public  Employees(String name, float rate, float attendance){
        this.name = name;
        this.rate = rate;
        this.days = attendance;
    }

    public void AddDeduction(Deduction deduction){
        payroll.setDeduction(deduction);
    }

    //setters

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDays(float days) {
        this.days = days;
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

    public float getDays() {
        return days;
    }

    public Payroll getPayroll() {
        return payroll;
    }

}
