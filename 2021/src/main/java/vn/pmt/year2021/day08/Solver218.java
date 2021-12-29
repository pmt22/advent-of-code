package vn.pmt.year2021.day08;

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
@Puzzle(year = 2021, day = 8)
public class Solver218 extends AbstractPuzzleSolver<Solver218.Input, Solver218.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 8;
    }

    @Override
    protected Input parseInput(List<String> rawInput) {
        return null;
    }

    @Override
    protected boolean testPart1(Result result) {
        return false;
    }

    @Override
    protected boolean testPart2(Result result) {
        return false;
    }

    @Override
    protected void displayResult(Result result) {

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
