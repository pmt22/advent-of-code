package vn.pmt.year2021.day8;

import static vn.pmt.year2021.day8.Solver218.DAY;
import static vn.pmt.year2021.day8.Solver218.YEAR;

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
@Puzzle(year = YEAR, day = DAY)
public class Solver218 extends AbstractPuzzleSolver<Solver218.Input218, Solver218.Result218> {
    public static final int YEAR = 2021;
    public static final int DAY = 8;

    @Override
    protected int year() {
        return YEAR;
    }

    @Override
    protected int day() {
        return DAY;
    }

    @Override
    protected Input218 parseInput(List<String> rawInput) {
        return null;
    }

    @Override
    protected boolean testPart1(Result218 result) {
        return false;
    }

    @Override
    protected boolean testPart2(Result218 result) {
        return false;
    }

    @Override
    protected void displayResult(Result218 result) {

    }

    static class Input218 implements PuzzleInput {
        List<Integer> depths = new ArrayList<>();
    }

    static class Result218 implements PuzzleResult {
        int count;
    }
}
