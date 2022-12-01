package vn.pmt.impl.day02;

import static vn.pmt.common.Constant.SPACE_DELIMITER;

import java.util.ArrayList;
import java.util.List;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 11/12/2021
 */
@Puzzle(year = 2021, day = 2)
public class Solver212 extends AbstractPuzzleSolver<Solver212.Input, Solver212.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 2;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        input.actions.addAll(
            lines.stream()
                .map(in -> in.split(SPACE_DELIMITER))
                .map(arr -> new Action(Direction.valueOf(arr[0]), Integer.parseInt(arr[1])))
                .toList()
        );
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        int horizontal = 0;
        int depth = 0;

        for (var action : input.actions) {
            switch (action.direction) {
                case forward -> horizontal += action.value;
                case up -> depth -= action.value;
                case down -> depth += action.value;
            }
        }

        result.multiplication = (long) horizontal * depth;
        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        int horizontal = 0;
        int depth = 0;
        int aim = 0;

        for (var action : input.actions) {
            switch (action.direction) {
                case forward -> {
                    horizontal += action.value;
                    depth += action.value * aim;
                }
                case up -> aim -= action.value;
                case down -> aim += action.value;
            }
        }

        result.multiplication = (long) horizontal * depth;
        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.multiplication == 150;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.multiplication == 900;
    }

    static class Input implements PuzzleInput {
        List<Action> actions = new ArrayList<>();
    }

    static class Result implements PuzzleResult {
        long multiplication;

        @Override
        public String toString() {
            return "Result: " + multiplication;
        }
    }

    static record Action(Direction direction, int value) {
    }

    enum Direction {
        forward, down, up
    }
}
