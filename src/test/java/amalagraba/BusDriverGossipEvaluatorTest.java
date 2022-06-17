package amalagraba;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusDriverGossipEvaluatorTest {

    private final BusDriverGossipEvaluator evaluator = new BusDriverGossipEvaluator();


    @Test
    void given_no_drivers_then_stops_required_to_spread_gossip_is_0() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("");

        assertEquals("0", requiredStops);
    }

    @Test
    void given_one_driver_then_stops_required_to_spread_gossip_is_0() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("1");

        assertEquals("0", requiredStops);
    }

    @Test
    void given_two_drivers_that_start_at_the_same_stop_then_required_stops_to_spread_gossip_is_1() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("""
                12
                13
                """);

        assertEquals("1", requiredStops);
    }

    @Test
    void given_two_drivers_that_coincide_at_the_third_stop_then_required_stops_to_spread_gossip_is_3() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("""
                123
                413
                """);

        assertEquals("3", requiredStops);
    }

    @Test
    void given_two_drivers_that_never_meet_then_required_stops_to_spread_gossip_is_never() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("""
                123
                597
                """);

        assertEquals("never", requiredStops);
    }

    @Test
    void given_example1_input_then_required_stops_to_spread_gossip_is_5() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("""
                3 1 2 3
                3 2 3 1
                4 2 3 4 5
                """);

        assertEquals("5", requiredStops);
    }

    @Test
    void given_example2_input_then_required_stops_to_spread_gossip_is_never() {
        String requiredStops = evaluateRequiredStopsToSpreadGossip("""
                2 1 2
                5 2 8
                """);

        assertEquals("never", requiredStops);
    }

    private String evaluateRequiredStopsToSpreadGossip(String drivers) {
        return evaluator.evaluateRequiredStopsToSpreadGossip(drivers);
    }
}
