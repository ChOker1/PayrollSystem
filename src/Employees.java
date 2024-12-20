public class Employees {

    private String name;
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
    public  Employees(String name, double rate,double days,double commission,Deduction deduction){
        this.name = name;
        this.rate = rate;
        this.days = days;
        this.commission = commission;
        this.payroll = new Payroll();
        computeGross();
        payroll.computeNet();
        this.payroll.setDeduction(deduction);


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

    public void computeGross(){
        payroll.setGrossic((rate*days)+ commission);
    }
    public double getnet(){
        return payroll.getNetic();
    }


    @Override
    public String toString(){
        return name + "`" + (int)rate + "`" + (int)days + "`" + (int)salary+ "`" + (int)commission + "`" + (int)payroll.getGrossic() + "`" + payroll.getDeduction().toString() + "`" + (int)payroll.getNetic();
    }

    public String toSaveOrigin(){
        return name + "`" + (int)rate + "`" + (int)days + "`" +  (int)commission + "`" + payroll.getDeduction().toStringOrigin();
    }

}
