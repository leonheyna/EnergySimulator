package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

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
    Logger logger = LoggerCreator.createConsumerLogger();

    private double activateAt;
    private double currentIndicator;
    private int minRuntime;
    private int runtime;

    private boolean isActive = false;
    private boolean indicatorChangedState = false;

    private double fillStand = 0.5;
    private double changeInterval;
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
        if(runtime != 0){logger.debug("FIXED STATE - runtime is not 0, consumer cannot change state");}
        if(empties()){logger.debug("FIXED STATE - consumers fillstand would drop below 0 if not activated");}
        if(overflows()){logger.debug("DIXED STATE - consumers fillstand would go over 1 if stays active");}
        return runtime == 0 && !empties() && !overflows();
    }

    int getConsumes() {
        return consumptionBean.getConsumes();
    }

    double getConsumesInterval() {
        return consumptionBean.getConsumesInterval();
    }

    void handleIndicator(BalanceIndicator balanceIndicator, double effectTime) {
        logger.info("handling Balance Indicator with effectTime "+effectTime+" , timestamp "+balanceIndicator.timestamp+" and indicator "+balanceIndicator.indicator);
        if (acceptsBalanceIndicators()) {
            logger.info("Consumer is accepting new BalanceIndicator");
            currentIndicator = balanceIndicator.indicator;
            logger.info("new indicator is "+currentIndicator);
            if (currentIndicator <= activateAt) {
                logger.info("indicator "+currentIndicator+" is low enough for activity, need is "+activateAt);
                if (isActive) {
                    logger.info("consumer already active - state not changed");
                    indicatorChangedState = false;
                } else {
                    logger.info("consume is not active and gets activated at "+effectTime);
                    isActive = true;
                    indicatorChangedState = true;
                    consumptionBean.setConsumesInterval((consumptionBean.getConsumes() * (effectTime - time) / 3600));
                    logger.info("consumesInterval is therefore "+consumptionBean.getConsumesInterval());
                    fillInterval = fillRate * (effectTime - time) / 3600;
                    logger.info("fillstand will therefore increase for "+fillInterval);
                    emptyInterval = emptyRate * (1 - (effectTime - time) / 3600);
                    logger.info("fillstand will therefore decrease for "+emptyInterval);
                    changeInterval = fillInterval - emptyInterval;
                    logger.info("fillstand will change by "+changeInterval);
                    runtime = minRuntime;
                    logger.info("consumer must run for "+runtime+" seconds");
                }
            } else if (currentIndicator > activateAt) {
                logger.info("indicator "+currentIndicator+" is too high for activity, need is "+activateAt);
                if (isActive) {
                    logger.info("consumer is active and gets deactivated at "+effectTime);
                    isActive = false;
                    indicatorChangedState = true;
                    consumptionBean.setConsumesInterval((consumptionBean.getConsumes() * (1 - (effectTime - time)) / 3600));
                    logger.info("consumes Interval is therefore "+consumptionBean.getConsumesInterval());
                    fillInterval = fillRate * (1 - (effectTime - time)) / 3600;
                    logger.info("fillstand will therefore increase for "+fillInterval);
                    emptyInterval = emptyRate * (effectTime - time) / 3600;
                    logger.info("fillstand will therefore decrease for "+emptyInterval);
                    changeInterval = fillInterval - emptyInterval;
                    logger.info("fillstand will change by "+changeInterval);

                } else {
                    logger.info("consumer already deactivated - state not changed");
                    indicatorChangedState = false;
                }
            }
        }
        logger.info("done handling new Indicator");
        logger.info("");
    }

    void refreshConsumer() {
        logger.info("refreshing consumer");
        if (isActive) {
            logger.info("consumer is active");
            if (!indicatorChangedState) {
                logger.info("state was not changed by a new indicator");
                consumptionBean.setConsumesInterval( ((double) consumptionBean.getConsumes()) /3600);
                logger.info("consumesInterval is therefore "+consumptionBean.getConsumesInterval());
                fillInterval = fillRate / 3600;
                logger.info("fillinterval is therefore "+fillInterval);
                changeInterval = fillInterval;
                logger.info("changeInterval is therefore "+changeInterval);
            }
            if (overflows()) {
                logger.debug("ERROR - consumer would overflow!");
                isActive = false;
                logger.info("consumer deactivated!");
                runtime = 0;
                logger.info("runtime set to 0 and Consumer will refresh again!");
                refreshConsumer();
            } else {
                logger.info("consumer is okay");
                consumptionBean.setConsumesTotal(consumptionBean.getConsumesTotal() + consumptionBean.getConsumesInterval());
                logger.info("consumesTotal summed to "+consumptionBean.getConsumesTotal());
                changeInterval = fillInterval - emptyInterval;
                logger.info("fillstand with change by "+changeInterval);
                fillStand += changeInterval;
                logger.info("fillstand is now "+fillStand);
                activateAt = fillStand;
                logger.info("new need is set, consumer will activate at "+activateAt);
                indicatorChangedState = false;
                logger.info("changedstate resetted to false");
                time++;
                logger.info("next time is "+time);
                logger.info("");
            }
        } else {
            logger.info("consumer is not active");
            if (!indicatorChangedState) {
                logger.info("state was not changed by a new indicator");
                consumptionBean.setConsumesInterval(0);
                logger.info("consumesInterval is therefore 0");
                emptyInterval = emptyRate / 3600;
                logger.info("emptyInterval is therefore "+emptyInterval);
                changeInterval = emptyInterval;
                logger.info("changeInterval is therefore "+changeInterval);
            }
            if (empties()) {
                logger.debug("ERROR - consumer would run empty!");
                isActive = true;
                logger.info("consumer activated!");
                runtime = minRuntime;
                logger.info("runtime set to "+runtime+" and consumer will refresh again!");
                refreshConsumer();
            } else {
                logger.info("consumer is oaky");
                fillStand -= changeInterval;
                logger.info("fillstand will therefore change by "+changeInterval);
                activateAt = fillStand;
                logger.info("new need is set, consumer will activate at "+activateAt);
                indicatorChangedState = false;
                logger.info("changedstate resetted to false");
                time++;
                logger.info("next time is "+time);
                logger.info("");
            }
        }
    }

    void initiateSimulation() {
        logger.info("initiate Consumer, time set 0");
        time = 0;
        logger.info("done initiating");
        logger.info("");
    }
}
