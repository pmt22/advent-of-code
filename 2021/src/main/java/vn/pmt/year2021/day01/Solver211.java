package vn.pmt.year2021.day01;

import java.util.ArrayList;
import java.util.List;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
@Puzzle(year = 2021, day = 1)
public class Solver211 extends AbstractPuzzleSolver<Solver211.Input, Solver211.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 1;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        input.depths.addAll(lines.stream().map(Integer::parseInt).toList());
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        for (int i = 1; i < input.depths.size(); i++) {
            if (input.depths.get(i) > input.depths.get(i - 1)) {
                result.count++;
            }
        }
        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.count == 7;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        for (int i = 3; i < input.depths.size(); i++) {
            if (input.depths.get(i) > input.depths.get(i - 3)) {
                result.count++;
            }
        }
        return result;
    }

    @Override
    protected boolean testPart2(Result result) {
         return result.count == 5;
    }

    static class Input implements PuzzleInput {
        List<Integer> depths = new ArrayList<>();
    }

    static class Result implements PuzzleResult {
        int count;

        @Override
        public String toString() {
            return "Result: " + count;
        }
    }
}
