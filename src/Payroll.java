public class Payroll {
    private double grossic;
    private double netic;
    private double commission;
    private Deduction deduction;

    public Payroll(){
        grossic = 0;
        netic = 0;
        commission = 0;
    }

    public  Payroll(double commitions,double grossic, double netic){
        this.grossic = grossic;
        this.netic = netic;
        this.commission = commitions;
    }


    //setters
    public void setGrossic(double grossic) {
        this.grossic = grossic;
    }
    public void setNetic(double netic) {
        this.netic = netic;
    }
    public void setCommission(double commission) {
        this.commission = commission;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    // getters
    public double getGrossic() {
        return grossic;
    }
    public double getNetic() {
        return netic;
    }
    public double getCommission() {
        return commission;
    }

}
