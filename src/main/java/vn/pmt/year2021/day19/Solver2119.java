package vn.pmt.year2021.day19;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 19/12/2021
 */
@Puzzle(year = 2021, day = 19)
public class Solver2119 extends AbstractPuzzleSolver<Solver2119.Input, Solver2119.Result> {
    static final Pattern SCANNER_PATTERN = Pattern.compile("--- scanner (\\d+) ---");
    static final Pattern COORDINATE_PATTERN = Pattern.compile("^(-*\\d+),(-*\\d+),(-*\\d+)$");

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 19;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        int concernedScanner = -1;
        for (var line : lines) {
            var scannerMatcher = SCANNER_PATTERN.matcher(line);
            var coordinateMatcher = COORDINATE_PATTERN.matcher(line);
            if (scannerMatcher.matches()) {
                concernedScanner = Integer.parseInt(scannerMatcher.group(1));
                input.scanners.put(concernedScanner, new Scanner());
            } else if (coordinateMatcher.find()) {
                input.scanners.get(concernedScanner)
                    .beaconCoordinates
                    .add(new Coordinate(coordinateMatcher.group(1), coordinateMatcher.group(2), coordinateMatcher.group(3)));
            }
        }
        input.scanners.get(0).coordinate = new Coordinate(0, 0, 0);
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        locatedAllScannersAndBeacons(input);

        result.totalBeacons = input.identified.size();
        return result;
    }

    Set<Scanner> locatedAllScannersAndBeacons(Input input) {
        Set<Scanner> locatedScanners = new HashSet<>();
        Scanner scanner0 = input.scanners.get(0);
        locatedScanners.add(scanner0);
        input.identified.addAll(scanner0.beaconCoordinates);
        while (locatedScanners.size() < input.scanners.size()) {
            for (int i = 1; i < input.scanners.size(); i++) {
                Scanner unlocatedScanner = input.scanners.get(i);
                if (locatedScanners.contains(unlocatedScanner)) {
                    continue;
                }
                unlocatedScanner.allOrientations()
                    .map(sc -> overlapsWithIdentifiedCoordinates(input, sc))
                    .flatMap(Optional::stream)
                    .findFirst()
                    .ifPresent(offset -> {
                        addToIdentifiedSetWithOffset(input.identified, unlocatedScanner, offset.x, offset.y, offset.z);
                        locatedScanners.add(unlocatedScanner);
                    });
            }
        }
        return locatedScanners;
    }

    Optional<Coordinate> overlapsWithIdentifiedCoordinates(Input input, Scanner scanner) {
        return input.identified.stream()
            .flatMap(mapPoint -> scanner.beaconCoordinates.stream().map(scannerPoint -> subtract(mapPoint, scannerPoint)))
            .collect(groupingBy(Function.identity(), counting()))
            .entrySet()
            .stream()
            .filter(e -> e.getValue() >= 12)
            .findFirst()
            .map(Map.Entry::getKey);
    }

    Coordinate subtract(Coordinate a, Coordinate b) {
        return new Coordinate(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    void addToIdentifiedSetWithOffset(Set<Coordinate> identified, Scanner scanner, int offsetX, int offsetY, int offsetZ) {
        scanner.coordinate = Coordinate.builder().x(offsetX).y(offsetY).z(offsetZ).build();
        identified.addAll(scanner.beaconCoordinates.stream().map(co -> Coordinate.builder().x(co.x + offsetX).y(co.y + offsetY).z(co.z + offsetZ).build()).toList());
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();

        Set<Scanner> scanners = locatedAllScannersAndBeacons(input);
        result.largestDistance = scanners.stream()
            .flatMap(scanner1 -> scanners.stream().map(scanner2 -> calculateDistance(scanner1.coordinate, scanner2.coordinate)))
            .max(Comparator.comparing(Function.identity()))
            .get();
        return result;
    }

    int calculateDistance(Coordinate a, Coordinate b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.totalBeacons == 79;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.largestDistance == 3621;
    }

    static class Input implements PuzzleInput {
        Map<Integer, Scanner> scanners = new HashMap<>();
        Set<Coordinate> identified = new HashSet<>();
    }

    static class Result implements PuzzleResult {
        int totalBeacons;
        int largestDistance;

        @Override
        public String toString() {
            return "Result: " + (largestDistance == 0 ? totalBeacons : largestDistance);
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    static class Coordinate {
        int x;
        int y;
        int z;

        Coordinate(String x, String y, String z) {
            this.x = Integer.parseInt(x);
            this.y = Integer.parseInt(y);
            this.z = Integer.parseInt(z);
        }

        Coordinate roll() {
            var oldY = y;

            y = z;
            z = -oldY;
            return this;
        }

        Coordinate turn() {
            var oldX = x;
            x = -y;
            y = oldX;
            return this;
        }

        Coordinate reverseTurn() {
            var oldX = x;
            x = y;
            y = -oldX;
            return this;
        }

        @Override
        public String toString() {
            return x +
                "," + y +
                "," + z;
        }
    }

    static class Scanner {
        Coordinate coordinate;
        List<Coordinate> beaconCoordinates = new ArrayList<>();

        Scanner roll() {
            this.beaconCoordinates.forEach(Coordinate::roll);
            return this;
        }

        Scanner turn() {
            this.beaconCoordinates.forEach(Coordinate::turn);
            return this;
        }

        Scanner reverseTurn() {
            this.beaconCoordinates.forEach(Coordinate::reverseTurn);
            return this;
        }

        Stream<Scanner> allOrientations() {
            return IntStream.range(0, 6)
                .boxed()
                .flatMap(rollIndex -> Stream.concat(Stream.of(this.roll()),
                    IntStream.range(0, 3)
                        .boxed()
                        .map(turnIndex -> rollIndex % 2 == 0 ? this.turn() : this.reverseTurn())));
        }
    }
}
