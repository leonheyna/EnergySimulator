package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartController extends SimulationComponent {
    public SmartController() {
    }

    public SmartController(String name, double latency, List<Consumer> consumerList, Logger logger) {
        this.componentName = name;
        this.latency = latency;
        this.consumerList = consumerList;
        this.logger = logger;
    }

    Logger logger;

    /**
     * latency in s
     */
    private double latency;

    private ConsumptionBean consumptionBean = new ConsumptionBean();
    private List<Consumer> consumerList;
    private Map<Double, BalanceIndicator> balanceIndicatorMap = new HashMap<>();
    private BalanceIndicator activeBalanceIndicator = new BalanceIndicator(0, 0);

    int getConsumes() {
        return consumptionBean.getConsumes();
    }

    double getConsumesInterval() {
        return consumptionBean.getConsumesInterval();
    }

    void testNames() {
        System.out.println(componentName);
        consumerList.forEach(Consumer::testNames);
    }

    void receiveIndicator(BalanceIndicator newBalanceIndicator) {
        double effectTime = newBalanceIndicator.timestamp + latency;
        balanceIndicatorMap.put(effectTime, newBalanceIndicator);
        for (Map.Entry<Double, BalanceIndicator> bI : balanceIndicatorMap.entrySet()) {
            effectTime = bI.getKey();
            BalanceIndicator balanceIndicator = bI.getValue();
            if (activeBalanceIndicator.timestamp < balanceIndicator.timestamp && effectTime < (double) time + 1) {
                activeBalanceIndicator = balanceIndicator;
                handleIndicator(balanceIndicator, effectTime);
                balanceIndicatorMap.remove(effectTime, activeBalanceIndicator);
            }
        }
        refreshConsumers();
    }

    private void handleIndicator(BalanceIndicator balanceIndicator, double effectTime) {
        for (Consumer consumer : consumerList) {
            consumer.receiveIndicator(balanceIndicator, effectTime);
        }
    }

    void refreshConsumers() {
        consumptionBean.setConsumesInterval(0);
        String loggerString = ""+activeBalanceIndicator.indicator;
        for (Consumer consumer : consumerList) {
            consumer.refreshConsumer();
            double consuming = consumer.getConsumesInterval();
            double fillstand = consumer.getFillStand();
            consumptionBean.setConsumesInterval(consumptionBean.getConsumesInterval() + consuming);
            loggerString = loggerString + ";" + consuming + ";" + fillstand;
        }
        consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
        loggerString = loggerString + ";" + consumptionBean.getConsumesInterval();
        loggerString = loggerString.replaceAll("\\.", ",");
        logger.info(loggerString);
        time++;
    }

    void initiateSimulation() {
        String loggerHeader= "BalanceIndicator";
        for (int i = 0; i < consumerList.size(); i++) {
            loggerHeader = loggerHeader + ";consumedInterval;Fillstand";
        }
        loggerHeader = loggerHeader + ";SmaCoConsumedInterval";
        logger.info(loggerHeader);
        time = 0;
        consumptionBean.setConsumes(0);
        for (Consumer consumer : consumerList) {
            consumer.initiateSimulation();
            consumptionBean.setConsumes(consumptionBean.getConsumes() + consumer.getConsumes());
        }
    }
}
