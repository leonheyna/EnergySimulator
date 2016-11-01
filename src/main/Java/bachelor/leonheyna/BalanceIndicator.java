package bachelor.leonheyna;

public class BalanceIndicator {
    public BalanceIndicator() {
    }

    public BalanceIndicator(int timestamp, double indicator) {
        this.timestamp = timestamp;
        this.indicator = indicator;
    }

    int timestamp;
    double indicator;
}
