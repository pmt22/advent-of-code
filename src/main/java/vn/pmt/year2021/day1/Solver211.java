package vn.pmt.year2021.day1;

import static vn.pmt.year2021.day1.Solver211.DAY;
import static vn.pmt.year2021.day1.Solver211.YEAR;

import java.util.List;

import vn.pmt.common.Log;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
@Puzzle(year = YEAR, day = DAY)
public class Solver211 extends AbstractPuzzleSolver<Input211, Result211> {
    public static final int YEAR = 2021;
    public static final int DAY = 1;

    @Override
    protected int year() {
        return YEAR;
    }

    @Override
    protected int day() {
        return DAY;
    }

    @Override
    protected Input211 parseInput(List<String> rawInput) {
        var input = new Input211();
        input.depths.addAll(rawInput.stream().map(Integer::parseInt).toList());
        return input;
    }

    @Override
    protected Result211 proposeSolutionPart1(Input211 parsedInput) {
        var result = new Result211();
        for (int i = 1; i < parsedInput.depths.size(); i++) {
            if (parsedInput.depths.get(i) > parsedInput.depths.get(i - 1)) {
                result.count++;
            }
        }
        return result;
    }

    @Override
    protected boolean testPart1(Result211 result) {
        return result.count == 7;
    }

    @Override
    protected Result211 proposeSolutionPart2(Input211 parsedInput) {
        var result = new Result211();
        for (int i = 0; i < parsedInput.depths.size() - 3; i++) {
            var firstSlidingWindow = parsedInput.depths.get(i) + parsedInput.depths.get(i + 1) + parsedInput.depths.get(i + 2);
            var secondSlidingWindow = parsedInput.depths.get(i + 1) + parsedInput.depths.get(i + 2) + parsedInput.depths.get(i + 3);

            if (secondSlidingWindow > firstSlidingWindow) {
                result.count++;
            }
        }
        return result;
    }

    @Override
    protected boolean testPart2(Result211 result) {
         return result.count == 5;
    }

    @Override
    protected void displayResult(Result211 result) {
        Log.info("Result: " + result.count);
    }
}
