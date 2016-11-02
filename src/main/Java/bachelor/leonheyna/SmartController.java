package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

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

    Logger logger = LoggerCreator.createSmartControllerLogger();

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

    void receiveIndicator(BalanceIndicator newBalanceIndicator) {
        logger.info("###Received BalanceIndicator " + newBalanceIndicator.indicator + " with timeStamp " + newBalanceIndicator.timestamp);
        double effectTime = newBalanceIndicator.timestamp + latency;
        logger.info("BalanceIndicator effectTime is " + effectTime);
        balanceIndicatorMap.put(effectTime, newBalanceIndicator);
        logger.info("put BalanceIndicator to map");
        for (Map.Entry<Double, BalanceIndicator> bI : balanceIndicatorMap.entrySet()) {
            effectTime = bI.getKey();
            BalanceIndicator balanceIndicator = bI.getValue();

            if (activeBalanceIndicator.timestamp < balanceIndicator.timestamp) {
                if (((double)(time + 1)) > effectTime) {
                    logger.info("new active BalanceIndicator found with effectTime " + effectTime + " with timestamp " + balanceIndicator.timestamp + " and indicator " + balanceIndicator.indicator);
                    activeBalanceIndicator = balanceIndicator;
                    logger.info("going in handleIndicator()");
                    handleIndicator(balanceIndicator, effectTime);
                }
            } else {
                logger.info("no new active BalanceIndicator found, going on with old one and in refreshController()");
                refreshConsumers();
            }
        }
        balanceIndicatorMap.remove(effectTime, activeBalanceIndicator);
        logger.info("removing old BalanceIndicator from Map");
        logger.info("done receiving and handling new Indicator");
    }

    private void handleIndicator(BalanceIndicator balanceIndicator, double effectTime) {
        logger.info("sending new Balance Indicator to all Consumers");
        for (Consumer consumer : consumerList) {
            consumer.handleIndicator(balanceIndicator, effectTime);
        }
        logger.info("done sending and refreshing going in refreshConsumers()");
        refreshConsumers();
        logger.info("### done handling the Indicator");
    }

    void refreshConsumers() {
        logger.info("resetting ConsumesInterval");
        consumptionBean.setConsumesInterval(0);
        logger.info("refreshing all Consumers and summing up Consumes Interval");
        for (Consumer consumer : consumerList) {
            consumer.refreshConsumer();
            consumptionBean.setConsumesInterval(consumptionBean.getConsumesInterval() + consumer.getConsumesInterval());
        }
        logger.info("done refreshing and summing, ConsumesInterval is " + consumptionBean.getConsumesInterval());
        consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
        time++;
        logger.info("next time is " + time);
        logger.info("");
    }

    void initiateSimulation() {
        time = 0;
        logger.info("time set to 0");
        consumptionBean.setConsumes(0);
        logger.info("consumes set to 0");
        logger.info("initiating connected consumers and summing consumes");
        for (Consumer consumer : consumerList) {
            consumer.initiateSimulation();
            consumptionBean.setConsumes(consumptionBean.getConsumes() + consumer.getConsumes());
        }
        logger.info("done initiating and summing, consumes is " + consumptionBean.getConsumes());
        logger.info("");
    }
}
