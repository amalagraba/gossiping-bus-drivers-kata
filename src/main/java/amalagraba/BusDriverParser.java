package amalagraba;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;

public class BusDriverParser {

    public List<Driver> parse(String input) {
        if (hasContent(input)) {
            return parseLines(input);
        }
        return emptyList();
    }

    private boolean hasContent(String input) {
        return input != null && !input.isBlank();
    }

    private List<Driver> parseLines(String input) {
        String[] inputLines = input.split("\\R");

        return IntStream.range(0, inputLines.length)
                .mapToObj(index -> parseDriver(index, inputLines[index]))
                .collect(Collectors.toList());
    }

    private Driver parseDriver(int id, String inputLine) {
        return new Driver(id, parseStops(inputLine));
    }

    private int[] parseStops(String inputLine) {
        return inputLine.chars().filter(this::isNotSpace).map(this::parseStop).toArray();
    }

    private boolean isNotSpace(int codePoint) {
        return codePoint != ' ';
    }

    private int parseStop(int codePoint) {
        if (Character.isDigit(codePoint)) {
            return Character.getNumericValue(codePoint);
        }
        throw new IllegalArgumentException("Illegal character '" + (char) codePoint + "' found");
    }
}
