public class Employees {

    private String name;
    private double rate;
    private double days;
    private double commission;
    private double salary;
    private  Payroll payroll;

    //constructors
    public Employees(){
        rate = 0;
        name = "";
        days = 0;
        commission = 0;
        payroll = new Payroll();
    }

    public  Employees(String name, double rate, double Days, double commission, Payroll payroll, Deduction deduction, double salary){
        this.name = name;
        this.rate = rate;
        this.days = Days;
        this.commission = commission;
        this.payroll = payroll;
        this.payroll.setDeduction(deduction);
        this.salary = salary;
    }
    public  Employees(String name, float rate){
        this.name = name;
        this.rate = rate;
    }
    public  Employees(String name, double rate,Deduction deduction){
        this.name = name;
        this.rate = rate;
        this.payroll = new Payroll();
        this.payroll.setDeduction(deduction);

    }
    public  Employees(float days, float commission){
        this.days = days;
        this.commission = commission;
    }



    public void setDeduction(Deduction deduction){
        payroll.setDeduction(deduction);
    }

    //setters

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDays(double days) {
        this.days = days;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public void setPayroll(Payroll payroll) {
        this.payroll = payroll;
    }

    public void setSalary(double salary) {
        this.salary = salary;
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
        return name + "`" + rate + "`" + days + "`" +salary+ "`" + commission + "`" + payroll.getGrossic() + "`" +payroll.getDeduction().toString() + "`" + payroll.getNetic();
    }

    public String toSaveOrigin(){
        return name + '`'+ (int)rate  + '`' + (int)payroll.getDeduction().getLoans() + '`' + (int)payroll.getDeduction().getSss() + '`' + (int)payroll.getDeduction().getPhilhealth();
    }

}
