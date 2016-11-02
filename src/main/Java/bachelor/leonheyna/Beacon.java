package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

import java.util.ArrayList;
import java.util.List;

public class Beacon extends SimulationComponent {
    public Beacon() {
    }

    public Beacon(int simulationRuntime, int balanceIndicatorIntervalTime, List<SmartController> smartControllerList, List<Double> producedEnergyList) {
        this.simulationRuntime = simulationRuntime;
        this.balanceIndicatorIntervalTime = balanceIndicatorIntervalTime;
        this.smartControllerList = smartControllerList;
        this.producedEnergyList = producedEnergyList;
    }
    Logger logger;

    private int timescale = 3600;
    private int simulationRuntime;
    private double maxConsumption;
    private int balanceIndicatorIntervalTime;

    private List<Double> producedEnergyList;

    private double producedEnergy() {
        return (maxConsumption * 1.5) * producedEnergyList.get(time / timescale);
    }

    private ConsumptionBean consumptionBean = new ConsumptionBean();
    private List<SmartController> smartControllerList;
    private List<BalanceIndicator> balanceIndicatorList = new ArrayList<>();


    private BalanceIndicator calculateBalanceIndicator() {
        int timestamp = time;
        double indicator = (0.5 * (consumptionBean.getConsumesInterval() / producedEnergy()));
        return new BalanceIndicator(timestamp, indicator);
    }

    private void sendBalanceIndicator(BalanceIndicator balanceIndicator) {
        balanceIndicatorList.add(balanceIndicator);
        logger.info("sending BalanceIndicator with Timestamp "+balanceIndicator.timestamp+" and Indicator "+balanceIndicator.indicator);
        for (SmartController smartController : smartControllerList) {
            smartController.receiveIndicator(balanceIndicator);
        }
        logger.info("done sending");
    }

    private void refreshSmartControllers() {
        logger.info("refreshing all SmartControllers");
        smartControllerList.forEach(SmartController::refreshConsumers);
        logger.info("done refreshing");
    }

    void simulateGrid() {
        logger = LoggerCreator.createBeaconLogger();
        logger.info("created BeaconLogger");
        logger.info("going in initiateSimulation()");
        initiateSimulation();
        logger.info("done initiating, starting simulation for runtime of "+simulationRuntime/3600+" virtual hours, or "+simulationRuntime/60+" virtual minutes.");
        for (time = 0; time < simulationRuntime; time++) {
            logger.debug("time is "+time);
            if (time % balanceIndicatorIntervalTime == 0) {
                logger.info("Balance Indicator will be send");
                logger.info("going in sendBalanceIndicator");
                sendBalanceIndicator(calculateBalanceIndicator());
            } else {
                logger.info("SmartControllers will be refreshed");
                logger.info("going in refreshSmartControllers()");
                refreshSmartControllers();
            }
            consumptionBean.setConsumesInterval(0);
            logger.info("consumesInterval set to 0");
            logger.info("summing up consumesInterval");
            for (SmartController smartController : smartControllerList) {
                consumptionBean.setConsumesInterval(consumptionBean.getConsumesInterval() + smartController.getConsumesInterval());
            }
            logger.info("consumesInterval summed up to "+consumptionBean.getConsumesInterval());
            consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
            logger.info("consumesInterval added to consumesTotal to "+consumptionBean.getConsumesTotal());
        }
        logger.info("######################");
        logger.info("simulation ran for "+simulationRuntime+" seconds");
        logger.info("consumed total "+consumptionBean.getConsumesTotal());
        logger.info("");
        logger.info("");
    }

    private void initiateSimulation() {
        time = 0;
        logger.info("time set to 0");
        consumptionBean.setConsumes(0);
        logger.info("consumes set to 0");
        logger.info("going in initiateSimulation() for each SmartController and adding up each consumes");
        for (SmartController smartController : smartControllerList) {
            smartController.initiateSimulation();
            consumptionBean.setConsumes(consumptionBean.getConsumes() + smartController.getConsumes());
        }
        logger.info("summed consumes is "+consumptionBean.getConsumes());
        maxConsumption = ((double) consumptionBean.getConsumes()) / 3600;
        logger.info("maxConsumption is "+maxConsumption);
    }
}
