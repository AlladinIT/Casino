package main.java.ee.playtech.casino;

import main.java.ee.playtech.casino.services.BettingDataProcessorService;

public class ConsoleApp {
    public static void main(String[] args) {
        BettingDataProcessorService service = new BettingDataProcessorService();
        service.run();
    }
}
