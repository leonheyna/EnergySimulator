package bachelor.leonheyna;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartController extends SimulationComponent {
    public SmartController() {
    }

    public SmartController(double latency, List<Consumer> consumerList) {
        this.latency = latency;
        this.consumerList = consumerList;
    }

    /**
     * latency in s
     */
    private double latency;

    private ConsumptionBean consumptionBean = new ConsumptionBean();
    private List<Consumer> consumerList;
    private Map<Double, BalanceIndicator> balanceIndicatorMap = new HashMap<>();
    private BalanceIndicator activeBalanceIndicator = new BalanceIndicator(0,0);

    int getConsumes() {
        return consumptionBean.getConsumes();
    }

    double getConsumesInterval() {
        return consumptionBean.getConsumesInterval();
    }

    void receiveIndicator(BalanceIndicator newBalanceIndicator) {
        double effectTime = newBalanceIndicator.timestamp + latency;
        balanceIndicatorMap.put(effectTime, newBalanceIndicator);

        System.out.println("received new Balance Indicator at " +effectTime);

        for (Map.Entry<Double, BalanceIndicator> bI : balanceIndicatorMap.entrySet()) {
            effectTime = bI.getKey();
            BalanceIndicator balanceIndicator = bI.getValue();

            if (time <= effectTime && activeBalanceIndicator.timestamp <= balanceIndicator.timestamp) {
                activeBalanceIndicator = balanceIndicator;
                System.out.println("using indicator with effectTime "+effectTime);
                handleIndicator(balanceIndicator, effectTime);
            }
        }
    }

    private void handleIndicator(BalanceIndicator balanceIndicator, double effectTime) {
        for (Consumer consumer : consumerList) {
            consumer.handleIndicator(balanceIndicator, effectTime);
        }
        refreshConsumers();
    }

    void refreshConsumers() {
        consumptionBean.setConsumesInterval(0);
        for (Consumer consumer : consumerList) {
            consumer.refreshConsumer();
            consumptionBean.setConsumesInterval(consumptionBean.getConsumesInterval() + consumer.getConsumesInterval());
        }
        consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
        System.out.println("CONTROLLER (CONSUMES; INTERVAL; TOTAL)"+ +consumptionBean.getConsumes()+" ; "+consumptionBean.getConsumesInterval()+" ; "+consumptionBean.getConsumesTotal());
        time++;
    }

    void initiateSimulation() {
        time = 0;
        consumptionBean.setConsumes(0);
        for (Consumer consumer : consumerList) {
            consumer.initiateSimulation();
            consumptionBean.setConsumes(consumptionBean.getConsumes() + consumer.getConsumes());
        }
    }
}
