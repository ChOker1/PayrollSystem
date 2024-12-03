public class Deduction {
    private double sss;
    private double philhealth;
    private double loans;
    private double others;
    private double total;
    //constructors
    public Deduction(){
        sss = 0;
        philhealth = 0;
        loans = 0;
        others = 0;
    }

    public Deduction(double sss,double philhealth, double loans,double others){
        this.sss = sss;
        this.philhealth = philhealth;
        this.loans = loans;
        this.others = others;
    }

    //setters
    public void setSss(double sss) {
        this.sss = sss;
    }

    public void setPhilhealth(double philhealth) {
        this.philhealth = philhealth;
    }

    public void setLoans(double loans) {
        this.loans = loans;
    }

    public void setOthers(double others) {
        this.others = others;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public double getOthers() {
        return others;
    }

    public double getTotal(){
        if(total!=0){
            return total;
        }
        return sss+philhealth+loans+others;
    }


}
