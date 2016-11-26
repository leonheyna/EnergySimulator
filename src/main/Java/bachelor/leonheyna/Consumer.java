package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

public class Consumer extends SimulationComponent {
    public Consumer() {
    }

    public double getFillStand() {
        return fillStand;
    }

    public Consumer(String name, int minRuntime, int consumes, double fillRate, double emptyRate) {
        this.componentName = name;
        this.minRuntime = minRuntime;
        this.consumptionBean.setConsumes(consumes);
        this.fillRate = fillRate;
        this.emptyRate = emptyRate;
    }

    private ConsumptionBean consumptionBean = new ConsumptionBean();

    private double currentIndicator = 0;
    private double changeTime = 0;
//    private boolean newBalanceIndicator = false;

    private int minRuntime = 0;
    private double runTime = 0;
    private boolean isActive = false;
    private boolean stateChanged = false;

    private double fillStand = 0;
    private double changeInterval;
    private double fillInterval;
    private double emptyInterval;
    private double fillRate = 0;
    private double emptyRate = 0;

    int getConsumes() {
        return consumptionBean.getConsumes();
    }

    double getConsumesInterval() {
        return consumptionBean.getConsumesInterval();
    }

    private boolean overflows(int timespan) {
        return 1 < fillStand + (fillRate / 3600) * timespan || 1<fillStand;
    }

    private boolean empties() {
        return 0 > fillStand - (emptyRate / 3600) || 0 >fillStand;
    }

    void testNames() {
        System.out.println(componentName);
    }

    void receiveIndicator(BalanceIndicator balanceIndicator, double effectTime) {
        currentIndicator = balanceIndicator.indicator;
        changeTime = effectTime;
//        newBalanceIndicator = true;
    }

    void stayActivated() {
        if (overflows(1)) {
            if (runTime > time) {
                consumptionBean.setConsumesInterval(((double) consumptionBean.getConsumes() / 3600) * (runTime - time));
                fillInterval = (fillRate / 3600) * (runTime - time);
                emptyInterval = (emptyRate / 3600) * (1 - (runTime - time));
            } else {
                consumptionBean.setConsumesInterval(0);
                fillInterval = 0;
                emptyInterval = (emptyRate / 3600);
            }
            isActive = false;
        } else {
            consumptionBean.setConsumesInterval((double) consumptionBean.getConsumes() / 3600);
            fillInterval = fillRate / 3600;
            emptyInterval = 0;
        }
    }
    void stayDeactivated(){
        if (empties()) {
            consumptionBean.setConsumesInterval(consumptionBean.getConsumes() / 3600);
            fillInterval = fillRate / 3600;
            emptyInterval = 0;
            isActive = true;
            runTime = time + minRuntime;
        } else {
            consumptionBean.setConsumesInterval(0);
            fillInterval = 0;
            emptyInterval = (emptyRate / 3600);
        }
    }


    void refreshConsumer() {
        if (runTime < time + 1) {
            if (runTime > changeTime) {
                changeTime = runTime;
            }
            if (changeTime < time + 1) {
                if (currentIndicator <= 1 - fillStand) {
                    if (isActive) {
                        stayActivated();
                    }else{
                        if(overflows(minRuntime)){
                            stayDeactivated();
                        }else{
                            isActive = true;
                            if(changeTime > time){
                                consumptionBean.setConsumesInterval((double)consumptionBean.getConsumes()/3600 * (1-(changeTime-time)));
                                fillInterval = (fillRate / 3600) * (1-(changeTime-time));
                                emptyInterval = (emptyRate / 3600) * (changeTime-time);
                                runTime = changeTime + minRuntime;
                            }else{
                                consumptionBean.setConsumesInterval((double) consumptionBean.getConsumes()/3600);
                                fillInterval = fillRate / 3600;
                                emptyInterval = 0;
                                runTime = time + minRuntime;
                            }
                        }
                    }
                } else {
                    if(isActive){
                        if(empties()){
                            stayActivated();
                        }else{
                            isActive = false;
                            if(changeTime > time){
                                consumptionBean.setConsumesInterval((double)consumptionBean.getConsumes()/3600 * (changeTime-time));
                                fillInterval = fillRate / 3600 * (changeTime-time);
                                emptyInterval = emptyRate / 3600 * (1-(changeTime-time));
                            }
                        }
                    }else{
                        stayDeactivated();
                    }
                }
            } else {
                if (isActive) {
                    stayActivated();
                } else {
                    stayDeactivated();
                }
            }
        } else {
            consumptionBean.setConsumesInterval((double) consumptionBean.getConsumes() / 3600);
            fillInterval = fillRate / 3600;
            emptyInterval = 0;
        }
        changeInterval = fillInterval - emptyInterval;
        fillStand += changeInterval;
        consumptionBean.addIntervallToTotal();
        time++;
    }

    void initiateSimulation() {

        time = 0;
    }
}
