package amalagraba;

import java.util.List;

public class BusDriverGossipEvaluator {

    private static final int MIN_DRIVERS_TO_SIMULATE = 1;

    private final BusDriverParser parser = new BusDriverParser();


    public String evaluateRequiredStopsToSpreadGossip(String input) {
        return evaluateRequiredStopsToSpreadGossip(parser.parse(input));
    }

    private String evaluateRequiredStopsToSpreadGossip(List<Driver> drivers) {
        if (hasEnoughDrivers(drivers)) {
            return runSimulation(drivers);
        }
        return "0";
    }

    private String runSimulation(List<Driver> drivers) {
        DriverGossipSimulator simulator = new DriverGossipSimulator(drivers);
        simulator.simulateStopsUntilGossipSpreads();

        return getSimulationResults(simulator);
    }

    private String getSimulationResults(DriverGossipSimulator simulator) {
        if (simulator.isGossipFullySpread()) {
            return String.valueOf(simulator.getSimulatedStops());
        }
        return "never";
    }

    private boolean hasEnoughDrivers(List<Driver> drivers) {
        return drivers.size() > MIN_DRIVERS_TO_SIMULATE;
    }
}
