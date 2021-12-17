package vn.pmt.year2021.day17;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 17/12/2021
 */
@Puzzle(year = 2021, day = 17)
public class Solver2117 extends AbstractPuzzleSolver<Solver2117.Input, Solver2117.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 17;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        Pattern xPattern = Pattern.compile("x=(-*\\d+)..(-*\\d+)");
        Pattern yPattern = Pattern.compile("y=(-*\\d+)..(-*\\d+)");
        String in = lines.get(0);
        Matcher matcher = xPattern.matcher(in);
        if (matcher.find()) {
            input.targetXFrom = Integer.parseInt(matcher.group(1));
            input.targetXTo = Integer.parseInt(matcher.group(2));
        }

        matcher = yPattern.matcher(in);
        if (matcher.find()) {
            input.targetYFrom = Integer.parseInt(matcher.group(1));
            input.targetYTo = Integer.parseInt(matcher.group(2));
        }

        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        result.highestY = (Math.abs(input.targetYFrom) - 1) * Math.abs(input.targetYFrom) / 2;

        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();

        Set<Pair<Integer, Integer>> set = new HashSet<>();

        for (int vx = 1; vx <= input.targetXTo; vx++) {
            for (int vy = input.targetYFrom; vy <= Math.abs(input.targetYFrom); vy++) {
                if (velocitiesWillResultProbeInTargetArea(vx, vy, input)) {
                    set.add(Pair.of(vx, vy));
                }
            }
        }

        result.distinctInitialVelocity = set.size();

        return result;
    }

    static boolean velocitiesWillResultProbeInTargetArea(int vx, int vy, Input input) {
        int x = 0;
        int y = 0;

        while (true) {
            y += vy--;
            x += vx;
            if (vx != 0) {
                vx--;
            }

            if (x >= input.targetXFrom && x <= input.targetXTo && y >= input.targetYFrom && y <= input.targetYTo) {
                return true;
            }

            if (x > input.targetXTo || y < input.targetYFrom) {
                return false;
            }
        }
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.highestY == 45;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.distinctInitialVelocity == 112;
    }

    static class Input implements PuzzleInput {
        int targetXFrom;
        int targetXTo;
        int targetYFrom;
        int targetYTo;
    }

    static class Result implements PuzzleResult {
        int highestY;

        int distinctInitialVelocity;

        @Override
        public String toString() {
            return "Result: " + (distinctInitialVelocity == 0 ? highestY : distinctInitialVelocity);
        }
    }
}
