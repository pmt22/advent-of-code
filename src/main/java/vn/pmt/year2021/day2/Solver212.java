package vn.pmt.year2021.day2;

import static vn.pmt.common.Constant.SPACE_DELIMITER;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 11/12/2021
 */
@Puzzle(year = 2021, day = 2)
public class Solver212 extends AbstractPuzzleSolver<Solver212.Input212, Solver212.Result212> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 2;
    }

    @Override
    protected Input212 parseInput(List<String> rawInput) {
        var input = new Input212();
        input.actions.addAll(
            rawInput.stream()
                .map(in -> in.split(SPACE_DELIMITER))
                .map(arr -> new Action(Direction.valueOf(arr[0]), Integer.parseInt(arr[1])))
                .toList()
        );
        return input;
    }

    @Override
    protected Result212 proposeSolutionPart1(Input212 parsedInput) {
        var result = new Result212();
        int horizontal = 0;
        int depth = 0;

        for (var action : parsedInput.actions) {
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
    protected Result212 proposeSolutionPart2(Input212 parsedInput) {
        var result = new Result212();
        int horizontal = 0;
        int depth = 0;
        int aim = 0;

        for (var action : parsedInput.actions) {
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
    protected boolean testPart1(Result212 result) {
        return result.multiplication == 150;
    }

    @Override
    protected boolean testPart2(Result212 result) {
        return result.multiplication == 900;
    }

    @Override
    protected void displayResult(Result212 result) {
        System.out.println("Result: " + result.multiplication);
    }

    static class Input212 implements PuzzleInput {
        List<Action> actions = new ArrayList<>();
    }

    static class Result212 implements PuzzleResult {
        long multiplication;
    }

    @AllArgsConstructor
    static class Action {
        Direction direction;
        int value;
    }

    enum Direction {
        forward, down, up
    }
}
