package vn.pmt.impl.day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 11/12/2021
 */
@Puzzle(year = 2021, day = 5)
public class Solver215 extends AbstractPuzzleSolver<Solver215.Input, Solver215.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 5;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        Pattern pattern = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int x1 = Integer.parseInt(matcher.group(1));
                int y1 = Integer.parseInt(matcher.group(2));
                int x2 = Integer.parseInt(matcher.group(3));
                int y2 = Integer.parseInt(matcher.group(4));
                var vent = new Vent(x1, x2, y1, y2);
                input.allVents.add(vent);
                if (x1 == x2 || y1 == y2) {
                    input.notDiagonalVents.add(vent);
                }
            }
        }
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        Map<Pair<Integer, Integer>, Integer> map = new HashMap<>();
        for (var vent : input.notDiagonalVents) {
            if (vent.isVertical()) {
                for (int x = Math.min(vent.x1, vent.x2); x <= Math.max(vent.x1, vent.x2); x++) {
                    map.merge(Pair.of(x, vent.y1), 1, Integer::sum);
                }
            } else if (vent.isHorizontal()) {
                for (int y = Math.min(vent.y1, vent.y2); y <= Math.max(vent.y1, vent.y2); y++) {
                    map.merge(Pair.of(vent.x1, y), 1, Integer::sum);
                }
            }
        }

        result.count = map.values().stream().filter(i -> i > 1).count();
        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        Map<Pair<Integer, Integer>, Integer> map = new HashMap<>();

        for (var vent : input.allVents) {
            if (vent.isVertical()) {
                for (int x = Math.min(vent.x1, vent.x2); x <= Math.max(vent.x1, vent.x2); x++) {
                    map.merge(Pair.of(x, vent.y1), 1, Integer::sum);
                }
            } else if (vent.isHorizontal()) {
                for (int y = Math.min(vent.y1, vent.y2); y <= Math.max(vent.y1, vent.y2); y++) {
                    map.merge(Pair.of(vent.x1, y), 1, Integer::sum);
                }
            } else if (vent.hassXYIncreasing()) {
                for (int x = Math.min(vent.x1, vent.x2), y = Math.min(vent.y1, vent.y2);
                     x <= Math.max(vent.x1, vent.x2);
                     x++, y++) {
                    map.merge(Pair.of(x, y), 1, Integer::sum);
                }
            } else if (vent.hasXIncreasingYDecreasing()) {
                for (int x = Math.min(vent.x1, vent.x2), y = Math.max(vent.y1, vent.y2);
                     x <= Math.max(vent.x1, vent.x2);
                     x++, y--) {
                    map.merge(Pair.of(x, y), 1, Integer::sum);
                }
            }
        }

        result.count = map.values().stream().filter(i -> i > 1).count();
        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.count == 5;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.count == 12;
    }

    static class Input implements PuzzleInput {
        List<Vent> notDiagonalVents = new ArrayList<>();
        List<Vent> allVents = new ArrayList<>();
    }

    static class Result implements PuzzleResult {
        long count;

        @Override
        public String toString() {
            return "Result: " + count;
        }
    }

    @AllArgsConstructor
    static class Vent {
        int x1;
        int x2;
        int y1;
        int y2;

        boolean isVertical() {
            return y1 == y2;
        }

        boolean isHorizontal() {
            return x1 == x2;
        }

        boolean hassXYIncreasing() {
            return (x1 > x2 && y1 > y2) || (x2 > x1 && y2 > y1);
        }

        boolean hasXIncreasingYDecreasing() {
            return (x1 > x2 && y2 > y1) || (x2 > x1 && y1 > y2);
        }
    }
}
