public class Payroll {
    private double grossic;
    private double netic;
    private Deduction deduction;

    public Payroll(){
        grossic = 0;
        netic = 0;
        deduction = new Deduction();
    }


    //setters
    public void setGrossic(double grossic) {
        this.grossic = grossic;
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


    public void computeNet(){
        netic = grossic - deduction.getTotal();
    }




}
