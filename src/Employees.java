public class Employees {

    private String name;
    private int empid;
    private double rate;
    private double days;
    private double commission;
    private double salary;
    private  Payroll payroll;

    //constructors

    public  Employees(String name, double rate,Deduction deduction){
        this.name = name;
        this.rate = rate;
        this.payroll = new Payroll();
        this.payroll.setDeduction(deduction);

    }
    public  Employees(int empid,String name, double rate,double days,double salary,double commission,Payroll payroll){
        this.empid = empid;
        this.name = name;
        this.rate = rate;
        this.days = days;
        this.commission = commission;
        this.salary = salary;
        this.payroll = payroll;

    }


    //setters

    public void setDays(double days) {
        this.days = days;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    //getters

    public double getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public double getDays() {
        return days;
    }

    public double getCommission() {
        return commission;
    }

    public Payroll getPayroll() {
        return payroll;
    }

    public double getSalary() {
        salary = rate*days;
        return salary;
    }

    public int getEmpid() {
        return empid;
    }

    public void computeGross(){
        payroll.setGrossic(salary+ commission);
    }
    public double getnet(){
        return payroll.getNetic();
    }


    @Override
    public String toString(){
        return empid + " " + name + "`" + (int)rate + "`" + (int)days + "`" + (int)salary+ "`" + (int)commission + "`" + (int)payroll.getGrossic() + "`" + payroll.getDeduction().toString() + "`" + (int)payroll.getNetic();
    }

}
