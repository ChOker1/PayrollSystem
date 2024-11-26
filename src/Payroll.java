public class Payroll {
    private double grossic;
    private double netic;
    private double commission;

    public Payroll(){
        grossic = 0;
        netic = 0;
        commission = 0;
    }

    public  Payroll(double grossic, double netic,double commitions){
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
