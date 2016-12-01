package bachelor.leonheyna;

import ch.qos.logback.classic.Logger;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Main {
    private static int BALANCEINDICATORINTERVAL = 5;
    private static int SIMULTATIONRUNTIME = 60 * 60 * 24;

    private static int AMOUNT_SMARTCONTROLLER = 5;
    private static double TEST_LATENCY_0 = 0;
    private static double TEST_LATENCY_1 = 0.01;
    private static double TEST_LATENCY_2 = 0.05;
    private static double TEST_LATENCY_3 = 0.25;
    private static double TEST_LATENCY_4 = 1.25;
    private static double TEST_LATENCY_5 = 6.25;

    private static int highConsumption = 750;
    private static int lowConsumption = 250;


    public static void main(String[] args) {
//        System.out.println((6 - 4%5) * 10);
        runGeneratedSimulationEnviroment();
    }


    private static void runGeneratedSimulationEnviroment() {
        String beaconName = "" + 0;
        Logger logger = LoggerCreator.defaultLogger(beaconName);
        Beacon beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(beaconName, TEST_LATENCY_0), dayEnergyList, logger);
        beacon.simulateGrid();
        System.out.println(0);
       /* beaconName = "" + 1;
        logger = LoggerCreator.defaultLogger(beaconName);
        beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(beaconName, TEST_LATENCY_1), dayEnergyList, logger);
        beacon.simulateGrid();
        System.out.println(1);
        beaconName = "" + 2;
        logger = LoggerCreator.defaultLogger(beaconName);
        beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(beaconName, TEST_LATENCY_2), dayEnergyList, logger);
        beacon.simulateGrid();
        System.out.println(2);
        beaconName = "" + 3;
        logger = LoggerCreator.defaultLogger(beaconName);
        beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(beaconName, TEST_LATENCY_3), dayEnergyList, logger);
        beacon.simulateGrid();
        System.out.println(3);
        beaconName = "" + 4;
        logger = LoggerCreator.defaultLogger(beaconName);
        beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(beaconName, TEST_LATENCY_4), dayEnergyList, logger);
        beacon.simulateGrid();
        System.out.println(4);
        beaconName = "" + 5;
        logger = LoggerCreator.defaultLogger(beaconName);
        beacon = new Beacon(beaconName, SIMULTATIONRUNTIME, BALANCEINDICATORINTERVAL, generateSmartControllerList(beaconName, TEST_LATENCY_5), dayEnergyList, logger);
        beacon.simulateGrid();
        System.out.println(5);
        */
//        beacon.testNames();
//        System.out.println(2 + 0.5);
    }

    private static List<SmartController> generateSmartControllerList(String beaconName, double latency) {
        List<SmartController> smartControllerList = new ArrayList<SmartController>();
        for (int i = 0; i < 1; i++) {
            String smaCoName = beaconName + "-" + i;
            Logger logger = LoggerCreator.defaultLogger(smaCoName);
            smartControllerList.add(new SmartController(smaCoName, TEST_LATENCY_0, generateConsumerListTestready(smaCoName), logger));
        }
        return smartControllerList;
    }

    private static List<Consumer> generateConsumerList(String smaCoName) {
        List<Consumer> consumerList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            String consumerName = smaCoName + "-" + i;
            /*int minRuntime = (6 - i % 5) * 10;
            int consumption;
            double fillrate = (double) (i + 1) / 10;
            double emptyrate = fillrate/2;
            consumption = i < 5 ? highConsumption : lowConsumption;
            */
            int minRuntime = 1;
            int consumption = 1000;
            double fillrate = 0.1;
            double emptyrate = 0.1;
            consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        }
        return consumerList;
    }
    private static  List<Consumer> generateConsumerListTestready(String smaCoName){
        List<Consumer> consumerList = new ArrayList<>();
        String consumerName = smaCoName + "-" + 0;
        int minRuntime = 1;
        int consumption = 510;
        double fillrate = 0.51;
        double emptyrate = 0.51;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        consumerName = smaCoName + "-" + 1;
        consumption = 250;
        fillrate = 0.25;
        emptyrate = 0.25;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        consumerName = smaCoName + "-" + 2;
        consumption = 120;
        fillrate = 0.12;
        emptyrate = 0.12;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        consumerName = smaCoName + "-" + 3;
        consumption = 60;
        fillrate = 0.06;
        emptyrate = 0.06;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        consumerName = smaCoName + "-" + 4;
        consumption = 30;
        fillrate = 0.03;
        emptyrate = 0.03;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        consumerName = smaCoName + "-" + 5;
        consumption = 20;
        fillrate = 0.03;
        emptyrate = 0.03;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));
        consumerName = smaCoName + "-" + 6;
        consumption = 10;
        fillrate = 0.01;
        emptyrate = 0.01;
        consumerList.add(new Consumer(consumerName, minRuntime, consumption, fillrate, emptyrate));

        return consumerList;
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
