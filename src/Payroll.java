public class Payroll {
    private double grossic;
    private double netic;
    private Deduction deduction;

    public Payroll(){
        grossic = 0;
        netic = 0;
    }

    public  Payroll(double grossic, double netic){
        this.grossic = grossic;
        this.netic = netic;
    }


    //setters
    public void setGrossic(double grossic) {
        this.grossic = grossic;
    }
    public void setNetic(double netic) {
        this.netic = netic;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    // getters
    public double getGrossic() {
        return grossic;
    }
    public double getNetic() {
        computeNet();
        return netic;
    }

    public Deduction getDeduction() {
        return deduction;
    }

    public void computeGross(float rate, float days, float commission){
        grossic = (rate * days) + commission;
    }
    public void computeNet(){
        netic = grossic - deduction.getTotal();
    }




}
