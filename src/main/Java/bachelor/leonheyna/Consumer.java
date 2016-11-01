package bachelor.leonheyna;

public class Consumer extends SimulationComponent {
    public Consumer() {
    }

    public Consumer(double activateAt, int minRuntime, int consumes, double fillRate, double emptyRate) {
        this.activateAt = activateAt;
        this.minRuntime = minRuntime;
        this.consumptionBean.setConsumes(consumes);
        this.fillRate = fillRate;
        this.emptyRate = emptyRate;
    }

    private ConsumptionBean consumptionBean = new ConsumptionBean();

    private double activateAt;
    private double currentIndicator;
    private int minRuntime;
    private int runtime;

    private boolean isActive = false;
    private boolean indicatorChangedState = false;

    private double fillStand = 0.5;
    private double fillRate;
    private double fillInterval;
    private double emptyRate;
    private double emptyInterval;

    private boolean empties() {
        return fillStand == 0 || fillStand - emptyRate < 0;
    }

    private boolean overflows() {
        return fillStand + fillRate > 1;
    }

    private boolean acceptsBalanceIndicators() {
        return runtime == 0 && !empties() && !overflows();
    }

    int getConsumes() {
        return consumptionBean.getConsumes();
    }

    double getConsumesInterval() {
        return consumptionBean.getConsumesInterval();
    }

    void handleIndicator(BalanceIndicator balanceIndicator, double effectTime) {
        if (acceptsBalanceIndicators()) {
            currentIndicator = balanceIndicator.indicator;
            if (balanceIndicator.indicator <= activateAt) {
                if (isActive) {
                    indicatorChangedState = false;
                } else {
                    consumptionBean.setConsumesInterval((consumptionBean.getConsumes() * (effectTime - time) / 3600));
                    fillInterval = fillRate * (effectTime - time) / 3600;
                    emptyInterval = emptyRate * (1 - (effectTime - time) / 3600);
                    isActive = true;
                    runtime = minRuntime;
                    indicatorChangedState = true;
                }
            } else if (balanceIndicator.indicator > activateAt) {
                if (isActive) {
                    consumptionBean.setConsumesInterval((consumptionBean.getConsumes() * (1 - (effectTime - time)) / 3600));
                    fillInterval = fillRate * (1 - (effectTime - time)) / 3600;
                    emptyInterval = emptyRate * (effectTime - time) / 3600;
                    isActive = false;
                    indicatorChangedState = true;
                } else {
                    indicatorChangedState = false;
                }
            }
        }
    }

    void refreshConsumer() {
        if (isActive) {
            if (!indicatorChangedState) {
                consumptionBean.setConsumesInterval( ((double) consumptionBean.getConsumes()) /3600);
                fillInterval = fillRate;
            }
            if (overflows()) {
                isActive = false;
                runtime = 0;
                refreshConsumer();

            } else {
                consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
                fillStand += fillInterval;
                time++;
                indicatorChangedState = false;
            }
        } else {
            if (!indicatorChangedState) {
                consumptionBean.setConsumesInterval(0);
                emptyInterval = emptyRate;
            }
            if (empties()) {
                isActive = true;
                runtime = minRuntime;
                refreshConsumer();
            } else {
                fillStand -= emptyInterval;
                time++;
                indicatorChangedState = false;
            }
        }
        System.out.println("CONSUMER (CONSUMES; INTERVAL; TOTAL)"+ +consumptionBean.getConsumes()+" ; "+consumptionBean.getConsumesInterval()+" ; "+consumptionBean.getConsumesTotal());
    }

    void initiateSimulation() {
        time = 0;
    }
}
