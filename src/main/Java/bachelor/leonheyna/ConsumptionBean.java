package bachelor.leonheyna;

public class ConsumptionBean {
    public ConsumptionBean() {
    }

    private int consumes = 0;
    private double consumesInterval = 0;
    private double consumesTotal = 0;

    public int getConsumes() {
        return consumes;
    }

    public void addConsumes(int value){
        consumes += value;
    }

    public void setConsumes(int consumes) {
        this.consumes = consumes;
    }

    public double getConsumesInterval() {
        return consumesInterval;
    }

    public void setConsumesInterval(double consumesInterval) {
        this.consumesInterval = consumesInterval;
    }

    public double getConsumesTotal() {
        return consumesTotal;
    }

    public void setConsumesTotal(double consumesTotal) {
        this.consumesTotal = consumesTotal;
    }

    public void addIntervallToTotal(){consumesTotal += consumesInterval;}
}
