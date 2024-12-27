public class Deduction {
    private double sss;
    private double philhealth;
    private double loans;
    private double others;
    private double cashAdvanced;

    //constructors
    public Deduction() {
        loans = 0; // Default values
        sss = 0;
        philhealth = 0;
        cashAdvanced = 0;
        others = 0;
    }


    public Deduction(double loans, double sss, double philhealth, double cashAdvanced,double others){
        this.sss = sss;
        this.philhealth = philhealth;
        this.loans = loans;
        this.others = others;
        this.cashAdvanced = cashAdvanced;
    }
    public Deduction(double loans, double sss, double philhealth){
        this.sss = sss;
        this.philhealth = philhealth;
        this.loans = loans;
    }

    //setters


    public void addLoans(double loans) {
        this.loans = this.loans + loans;
    }

    public void addSss(double sss) {
        this.sss = this.sss + sss;
    }

    public void addPhilhealth(double philhealth) {
        this.philhealth = this.philhealth + philhealth;
    }

    public void addCashAdvanced(double cashAdvanced) {
        this.cashAdvanced = this.cashAdvanced + cashAdvanced;
    }

    public void addOthers(double others) {
        this.others = this.others + others;
    }

    //getters
    public double getSss() {
        return sss;
    }

    public double getPhilhealth() {
        return philhealth;
    }

    public double getLoans() {
        return loans;
    }

    public double getCashAdvanced() {
        return cashAdvanced;
    }

    public double getOthers() {
        return others;
    }

    //get total
    public double getTotal(){
        return sss+philhealth+loans+others+cashAdvanced;
    }


    public String toString(){
        return (int)loans + "`" +(int)sss + "`" + (int)others + "`" + (int)philhealth + "`" + (int)getTotal();
    }
    public String toStringOrigin(){
        return (int)loans + "`" +(int)sss + "`" + (int)philhealth;
    }
}
