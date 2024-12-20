public class Deduction {
    private double sss;
    private double philhealth;
    private double loans;
    private double others;

    //constructors
    public Deduction(){
        sss = 0;
        philhealth = 0;
        loans = 0;
        others = 0;
    }

    public Deduction(double loans, double sss, double others, double philhealth){
        this.sss = sss;
        this.philhealth = philhealth;
        this.loans = loans;
        this.others = others;
    }
    public Deduction(double loans, double sss, double philhealth){
        this.sss = sss;
        this.philhealth = philhealth;
        this.loans = loans;
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
   //get total
    public double getTotal(){
        return sss+philhealth+loans+others;
    }


    public String toString(){
        return (int)loans + "`" +(int)sss + "`" + (int)others + "`" + (int)philhealth + "`" + (int)getTotal();
    }
    public String toStringOrigin(){
        return (int)loans + "`" +(int)sss + "`" + (int)philhealth;
    }
}
