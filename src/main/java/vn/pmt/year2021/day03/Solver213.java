package vn.pmt.year2021.day03;

import java.util.List;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 11/12/2021
 */
@Puzzle(year = 2021, day = 3)
public class Solver213 extends AbstractPuzzleSolver<Solver213.Input, Solver213.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 3;
    }

    @Override
    protected Input parseInput(List<String> lines) {
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

    static class Input implements PuzzleInput {

    }

    static class Result implements PuzzleResult {

        @Override
        public String toString() {
            return "Result: ";
        }
    }
}
