package amalagraba;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BusDriverParserTest {

    private final BusDriverParser parser = new BusDriverParser();

    @Test
    void parse_returns_empty_list_when_input_is_null() {
        List<Driver> drivers = parse(null);

        assertTrue(drivers.isEmpty());
    }

    @Test
    void parse_return_empty_list_when_input_is_blank() {
        List<Driver> drivers = parse("");

        assertTrue(drivers.isEmpty());
    }

    @Test
    void parse_throws_IllegalArgumentException_when_input_has_non_numeric_character() {
        IllegalArgumentException exception = parseAndExpectException("a");

        assertEquals("Illegal character 'a' found", exception.getMessage());
    }

    @Test
    void parse_exception_message_contains_invalid_character_when_input_has_non_numeric_character() {
        IllegalArgumentException exception = parseAndExpectException("124b");

        assertEquals("Illegal character 'b' found", exception.getMessage());
    }

    @Test
    void parse_returns_one_driver_per_input_line() {
        List<Driver> drivers = parse("""
                123
                4322
                """);

        assertEquals(2, drivers.size());
        assertEquals(0, drivers.get(0).id());
        assertEquals(1, drivers.get(1).id());
        assertDriverHasStops(drivers.get(0), 1, 2, 3);
        assertDriverHasStops(drivers.get(1), 4, 3, 2, 2);
    }

    @Test
    void parse_returns_one_driver_stop_per_digit() {
        List<Driver> drivers = parse("123");

        assertEquals(1, drivers.size());
        assertEquals(0, drivers.get(0).id());
        assertDriverHasStops(drivers.get(0), 1, 2, 3);
    }

    @Test
    void parse_must_ignore_spaces() {
        List<Driver> drivers = parse("1 2 3");

        assertEquals(1, drivers.size());
        assertDriverHasStops(drivers.get(0), 1, 2, 3);
    }

    private void assertDriverHasStops(Driver driver, int... stops) {
        assertArrayEquals(stops, driver.stops());
    }

    IllegalArgumentException parseAndExpectException(String input) {
        Executable executable = () -> parse(input);

        return assertThrows(IllegalArgumentException.class, executable);
    }

    private List<Driver> parse(String input) {
        return parser.parse(input);
    }

}
