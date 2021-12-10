package vn.pmt.common.puzzle;

import java.util.List;

import vn.pmt.common.Importer;
import vn.pmt.common.Log;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
public abstract class AbstractPuzzleSolver<I extends PuzzleInput, R extends PuzzleResult> implements Solver {
    private final Importer importer = new Importer();
    private List<String> rawTestInput;
    private List<String> rawActualInput;

    protected abstract int year();
    protected abstract int day();
    protected abstract I parseInput(List<String> rawInput);
    protected abstract boolean testPart1(R result);
    protected abstract boolean testPart2(R result);
    protected abstract void displayResult(R result);

    @Override
    public void solve() throws Exception {
        importResources();
        part1();
        part2();
    }

    private void importResources() throws Exception {
        rawTestInput = importer.importTest(year(), day());
        rawActualInput = importer.importActual(year(), day());
    }

    private void part1() {
        Log.info("----------Part 1----------");
        I parsedTestInput = parseInput(rawTestInput);
        R testResult = proposeSolutionPart1(parsedTestInput);
        displayResult(testResult);
        if (testPart1(testResult)) {
            I parsedInput = parseInput(rawActualInput);
            R result = proposeSolutionPart1(parsedInput);
            displayResult(result);
        }
    }

    private void part2() {
        Log.info("----------Part 2----------");
        I parsedTestInput = parseInput(rawTestInput);
        R testResult = proposeSolutionPart2(parsedTestInput);
        displayResult(testResult);
        if (testPart2(testResult)) {
            I parsedInput = parseInput(rawActualInput);
            R result = proposeSolutionPart2(parsedInput);
            displayResult(result);
        }
    }

    protected R proposeSolutionPart1(I parsedInput) {
        Log.error("Solution for part 1 is not yet implemented!");
        return null;
    }

    protected R proposeSolutionPart2(I parsedInput) {
        Log.error("Solution for part 2 is not yet implemented!");
        return null;
    }
}
