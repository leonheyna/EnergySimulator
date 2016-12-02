package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static int SIMULTATIONRUNTIME = 60 * 60 * 24 * 7;

    private static double TEST_LATENCY_0 = 0;
    private static double TEST_LATENCY_1 = 0.05;
    private static double TEST_LATENCY_2 = 0.1;
    private static double TEST_LATENCY_3 = 0.2;
    private static double TEST_LATENCY_4 = 1.5;
    private static double TEST_LATENCY_5 = 300;
    private static double TEST_LATENCY_6 = 600;


    public static void main(String[] args) {
        runGeneratedSimulationEnviroment();
    }


    private static void runGeneratedSimulationEnviroment() {
        String beaconName = "" + 1;
        Logger logger = LoggerCreator.defaultLogger(beaconName);
        Beacon beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, 1, 0.1, generateSmartControllerListTestFinal(beaconName), dayEnergyList, logger);
        beacon.simulateGrid();
    }

    private static List<Consumer> generateConsumerList(String smaCoName) {
        List<Consumer> consumerList = new ArrayList<>();
        String consumerName;
        int minRuntime = 0;
        int consumption = 100;
        double fillstand = 0;
        double fillrate = 0.1;
        double emptyrate = 0.1;
        for (int i = 0; i < 10; i++) {
            consumerName = smaCoName + "-" + i;
            minRuntime = 2 ^ i;
            fillrate = (double) 1 - (0.8 / ((double) i + 1));
            emptyrate = fillrate;
            consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillstand, fillrate, emptyrate));
        }
        return consumerList;
    }


    private static List<SmartController> generateSmartControllerListTestFinal(String beaconname) {
        List<SmartController> smartControllerList = new ArrayList<SmartController>();

        String smaCoName = beaconname + "-" + 0;
        Logger logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_0, generateConsumerList(smaCoName), logger));
        smaCoName = beaconname + "-" + 1;
        logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_1, generateConsumerList(smaCoName), logger));
        smaCoName = beaconname + "-" + 2;
        logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_2, generateConsumerList(smaCoName), logger));
        smaCoName = beaconname + "-" + 3;
        logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_3, generateConsumerList(smaCoName), logger));
        smaCoName = beaconname + "-" + 4;
        logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_4, generateConsumerList(smaCoName), logger));
        smaCoName = beaconname + "-" + 5;
        logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_5, generateConsumerList(smaCoName), logger));
        smaCoName = beaconname + "-" + 6;
        logger = LoggerCreator.defaultLogger(smaCoName);
        smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_6, generateConsumerList(smaCoName), logger));

        return smartControllerList;
    }

    private static List<Double> dayEnergyList = Arrays.asList(
            0.75
            , 0.74
            , 0.74
            , 0.75
            , 0.77
            , 0.78
            , 0.85
            , 0.91
            , 0.94
            , 0.95
            , 0.97
            , 0.99
            , 1.00
            , 0.99
            , 0.97
            , 0.96
            , 0.94
            , 0.93
            , 0.93
            , 0.91
            , 0.88
            , 0.85
            , 0.82
            , 0.78
    );
}
