package amalagraba;

import java.util.*;
import java.util.stream.Collectors;

public class DriverGossipSimulator {

    private static final int MINUTES_BETWEEN_STOPS = 1;
    private static final int MINUTES_IN_WORKING_DAY = 480;
    private static final int MAX_SIMULATED_STEPS = MINUTES_IN_WORKING_DAY / MINUTES_BETWEEN_STOPS;

    private final Map<Integer, Map<Integer, GossipingDriver>> stops = new HashMap<>();
    private final Set<GossipingDriver> gossipingDrivers;

    private int step;
    private boolean gossipFullySpread;

    public DriverGossipSimulator(List<Driver> drivers) {
        this.gossipingDrivers = drivers.stream().map(GossipingDriver::new).collect(Collectors.toSet());
    }

    void simulateStopsUntilGossipSpreads() {
        while (shouldSimulateNextStep())
            simulateNextStep();
    }

    private boolean shouldSimulateNextStep() {
        return step < MAX_SIMULATED_STEPS && !gossipFullySpread;
    }

    private void simulateNextStep() {
        moveDriversToCurrentStop();
        exchangeDriverGossips();
        updateGossipFullySpread();
        increaseStep();
    }

    private void moveDriversToCurrentStop() {
        gossipingDrivers.forEach(this::moveDriverToCurrentStop);
    }

    private void moveDriverToCurrentStop(GossipingDriver driver) {
        removeDriverFromLastStop(driver);
        addDriverToCurrentStop(driver);
    }

    private void removeDriverFromLastStop(GossipingDriver driver) {
        if (step > 0) removeDriverFromStop(driver, driver.getStopAtStep(step - 1));
    }

    private void removeDriverFromStop(GossipingDriver driver, int stop) {
        stops.get(stop).remove(driver.id());
    }

    private void addDriverToCurrentStop(GossipingDriver driver) {
        addDriverToStop(driver, driver.getStopAtStep(step));
    }

    private void addDriverToStop(GossipingDriver driver, int stop) {
        stops.computeIfAbsent(stop, k -> new HashMap<>()).put(driver.id(), driver);
    }

    private void exchangeDriverGossips() {
        stops.values().forEach(this::exchangeDriverGossips);
    }

    private void exchangeDriverGossips(Map<Integer, GossipingDriver> driversAtStop) {
        if (driversAtStop.size() > 1)
            addGossipsToDrivers(driversAtStop, computeGossipsAtStop(driversAtStop));
    }

    private void addGossipsToDrivers(Map<Integer, GossipingDriver> driversAtStop, Set<Integer> gossipsAtStop) {
        driversAtStop.values().forEach(driver -> driver.addGossips(gossipsAtStop));
    }

    private Set<Integer> computeGossipsAtStop(Map<Integer, GossipingDriver> driversAtStop) {
        return driversAtStop.values().stream()
                .map(GossipingDriver::gossips)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private void updateGossipFullySpread() {
        gossipFullySpread = gossipingDrivers.stream().allMatch(this::driverHasAllGossips);
    }

    private boolean driverHasAllGossips(GossipingDriver driver) {
        return driver.gossips().size() == gossipingDrivers.size();
    }

    private void increaseStep() {
        step++;
    }

    boolean isGossipFullySpread() {
        return gossipFullySpread;
    }

    int getSimulatedStops() {
        return step;
    }

    private record GossipingDriver(Driver driver, Set<Integer> gossips) {

        GossipingDriver(Driver driver) {
            this(driver, new HashSet<>(Set.of(driver.id())));
        }

        void addGossips(Set<Integer> gossips) {
            this.gossips.addAll(gossips);
        }

        int getStopAtStep(int step) {
            int[] stops = driver.stops();

            return stops[step % stops.length];
        }

        int id() {
            return driver.id();
        }
    }
}
