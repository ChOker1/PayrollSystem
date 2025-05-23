public class Payroll {
    private double grossic;
    private double netic;
    private Deduction deduction;

    public Payroll(){
        grossic = 0;
        netic = 0;
        deduction = new Deduction();
    }

    public Payroll(double grossic, double netic, Deduction deduction){
        this.grossic = grossic;
        this.netic = netic;
        this.deduction = deduction;
    }


    //setters
    public void setGrossic(double grossic) {
        this.grossic = grossic;
    }

    public void setDeduction(Deduction deduction) {
        this.deduction = deduction;
    }

    public void addGrossic(double grossic) {
        this.grossic = this.grossic + grossic;
    }

    public void addNetic(double netic) {
        this.netic = this.netic + netic;
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
