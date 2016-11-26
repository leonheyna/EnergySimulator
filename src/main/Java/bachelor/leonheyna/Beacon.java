package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

import java.util.ArrayList;
import java.util.List;

public class Beacon extends SimulationComponent {
    public Beacon() {
    }

    public Beacon(String name, int simulationRuntime, int balanceIndicatorIntervalTime, List<SmartController> smartControllerList, List<Double> producedEnergyList, Logger logger) {
        this.componentName = name;
        this.simulationRuntime = simulationRuntime;
        this.balanceIndicatorIntervalTime = balanceIndicatorIntervalTime;
        this.smartControllerList = smartControllerList;
        this.producedEnergyList = producedEnergyList;
        this.logger = logger;
    }

    Logger logger;

    private int timescale = 3600;
    private int simulationRuntime;
    private double maxConsumption;
    private int balanceIndicatorIntervalTime;
    private double tolerance = 0.1;

    private List<Double> producedEnergyList;

    private double producesIntervall() {
        return (maxConsumption / 3600) * producedEnergyList.get(time / timescale);
    }

    private ConsumptionBean consumptionBean = new ConsumptionBean();
    private List<SmartController> smartControllerList;
    private List<BalanceIndicator> balanceIndicatorList = new ArrayList<>();
    private BalanceIndicator activeBalanceIndicator;


    private BalanceIndicator calculateBalanceIndicator() {
        int timestamp = time;
        double consumes = consumptionBean.getConsumesInterval();
        double produces = producesIntervall();
        double indicator = (consumes - produces * ((double) 1 - tolerance)) / (produces * ((double) 1 + tolerance) - produces * ((double) 1 - tolerance));
        if (indicator > 1) indicator = 1;
        if (indicator < 0) indicator = 0;
        return new BalanceIndicator(timestamp, indicator);
    }

    private void sendBalanceIndicator(BalanceIndicator balanceIndicator) {
        balanceIndicatorList.add(balanceIndicator);
        for (SmartController smartController : smartControllerList) {
            smartController.receiveIndicator(balanceIndicator);
        }
    }

    private void refreshSmartControllers() {
        smartControllerList.forEach(SmartController::refreshConsumers);
    }

    void testNames() {
        System.out.println(componentName);
        smartControllerList.forEach(SmartController::testNames);
    }

    void simulateGrid() {
        initiateSimulation();
        logger.info("time;consumesIntervall;producesInterval;balanceIndicator");
        for (time = 0; time < simulationRuntime; time++) {
            if (time % balanceIndicatorIntervalTime == 0) {
                activeBalanceIndicator = calculateBalanceIndicator();
                sendBalanceIndicator(activeBalanceIndicator);
            } else {
                refreshSmartControllers();
            }
            consumptionBean.setConsumesInterval(0);
            for (SmartController smartController : smartControllerList) {
                consumptionBean.setConsumesInterval(consumptionBean.getConsumesInterval() + smartController.getConsumesInterval());
            }
            consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
            String loggerString = time + ";" + consumptionBean.getConsumesInterval() + ";" + producesIntervall() + ";" + activeBalanceIndicator.indicator;
            loggerString = loggerString.replaceAll("\\.", ",");
            logger.info(loggerString);
        }
    }

    private void initiateSimulation() {
        time = 0;
        consumptionBean.setConsumes(0);
        for (SmartController smartController : smartControllerList) {
            smartController.initiateSimulation();
            consumptionBean.addConsumes(smartController.getConsumes());
        }
        maxConsumption = ((double) consumptionBean.getConsumes());
    }
}
