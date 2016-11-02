package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static int BALANCEINDICATORINTERVAL = 1;
    private static int SIMULTATIONRUNTIME = 60*60*1;

    private static int AMOUNT_SMARTCONTROLLER = 5;
    private static double TEST_LATENCY_0 = 0;
    private static double TEST_LATENCY_1 = 0.01;
    private static double TEST_LATENCY_2 = 0.05;
    private static double TEST_LATENCY_3 = 0.25;
    private static double TEST_LATENCY_4 = 1.25;
    private static double TEST_LATENCY_5 = 6.25;

    private static int AMOUNT_LOW = 5;
    private static int CONSUMPTION_LOW = 50;
    private static double ACTIVATE_LOW = 0.2;
    private static int RUNTIME_LOW = 5;
    private static double FILL_LOW = 0.1;
    private static double EMPTY_LOW = 0.2;

    private static int AMOUNT_MEDIUM = 5;
    private static int CONSUMOTION_MEDIUM = 500;
    private static double ACTIVATE_MEDIUM = 0.1;
    private static int RUNTIME_MEDIUM = 10;
    private static double FILL_MEDIUM = 0.05;
    private static double EMPTY_MEDIUM = 0.1;

    private static int AMOUNT_HIGH = 5;
    private static int CONSUMPTION_HIGH = 3000;
    private static double ACTIVATE_HIGH = 0.01;
    private static int RUNTIME_HIGH = 20;
    private static double FILL_HIGH = 0.025;
    private static double EMPTY_HIGH = 0.5;
    static Logger mainLogger;

    public static void main(String[] args) {
        mainLogger = LoggerCreator.createMainLogger();
        mainLogger.info("mainLogger created");
        mainLogger.info("going in void runGeneratedSimulationEnviroment");
        runGeneratedSimulationEnviroment();
        mainLogger.info("#END");
    }


    private static void runGeneratedSimulationEnviroment() {
        mainLogger.info("creating Beacon with [SIMULATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(), dayEnergyList()]" );
        mainLogger.info("going in List<Smart Controller> generateSmartControllerList()");
        Beacon beacon = new Beacon(SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(), dayEnergyList);
        mainLogger.debug("Created Beacon");
        mainLogger.info("starting Simulation of Beacon");
        beacon.simulateGrid();
    }

    private static List<SmartController> generateSmartControllerList() {
        List<SmartController> smartControllerList = new ArrayList<SmartController>();
        mainLogger.info("created empty List<SmartController> smartControllerList");
        mainLogger.info("going in List<Consumer> generateConsumerList()");
        smartControllerList.add(new SmartController(TEST_LATENCY_1, generateConsumerList()));
        mainLogger.debug("added new SmartController to List");
        return smartControllerList;
    }

    private static List<Consumer> generateConsumerList() {
        List<Consumer> consumerList = new ArrayList<>();
        mainLogger.info("created empty List<Consumer> consumerList");
        for (int i = 0; i < AMOUNT_LOW; i++) {
            consumerList.add(new Consumer(ACTIVATE_LOW, RUNTIME_LOW, CONSUMPTION_LOW, FILL_LOW, EMPTY_LOW));
        }
        mainLogger.debug("added "+AMOUNT_LOW+" LOW Consumers to List");
        for (int i = 0; i < AMOUNT_MEDIUM; i++) {
            consumerList.add(new Consumer(ACTIVATE_MEDIUM, RUNTIME_MEDIUM, CONSUMOTION_MEDIUM, FILL_MEDIUM, EMPTY_MEDIUM));
        }
        mainLogger.debug("added "+AMOUNT_MEDIUM+" MEDIUM Consumers to List");
        for (int i = 0; i < AMOUNT_HIGH; i++) {
            consumerList.add(new Consumer(ACTIVATE_HIGH, RUNTIME_HIGH, CONSUMPTION_HIGH, FILL_HIGH, EMPTY_HIGH));
        }
        mainLogger.debug("added "+AMOUNT_HIGH+" HIGH Consumers to List");
        return consumerList;
    }
    private static List<Double> dayEnergyList = Arrays.asList(
            0.75
            ,0.74
            ,0.74
            ,0.75
            ,0.77
            ,0.78
            ,0.85
            ,0.91
            ,0.94
            ,0.95
            ,0.97
            ,0.99
            ,1.00
            ,0.99
            ,0.97
            ,0.96
            ,0.94
            ,0.93
            ,0.93
            ,0.91
            ,0.88
            ,0.85
            ,0.82
            ,0.78
    );
}
