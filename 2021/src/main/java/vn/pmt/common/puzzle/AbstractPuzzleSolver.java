package vn.pmt.common.puzzle;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import vn.pmt.common.Constant;
import vn.pmt.common.Importer;
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
    private I parsedTestInput;
    private I parsedActualInput;
    private boolean needTest = true;

    protected StopWatch stopWatch = StopWatch.create();

    protected abstract int year();

    protected abstract int day();

    protected abstract I parseInput(List<String> lines);

    protected abstract boolean testPart1(R result);

    protected abstract boolean testPart2(R result);

    @Override
    public void solve() throws Exception {
        importResources();

        timing(this::parsing, "Parsing input execution time:");

        part1();

        parsing();

        part2();
    }

    private void importResources() throws Exception {
        rawTestInput = importer.importTest(year(), day());
        rawActualInput = importer.importActual(year(), day());
    }

    private void timing(Runnable task, String title) {
        stopWatch.start();
        task.run();
        stopWatch.stop();
        System.out.println(StringUtils.joinWith(Constant.SPACE_DELIMITER, title, stopWatch.getTime(TimeUnit.MILLISECONDS), Constant.MILLISECONDS));
        stopWatch.reset();
    }

    private void parsing() {
        needTest = CollectionUtils.isNotEmpty(rawTestInput);
        if (needTest) {
            parsedTestInput = parseInput(rawTestInput);
        }
        parsedActualInput = parseInput(rawActualInput);
    }

    private void part1() {
        System.out.println("----------Part 1----------");
        R testResult = null;
        if (needTest) {
            testResult = proposeSolutionPart1(parsedTestInput);
            System.out.print("Test: ");
            displayResult(testResult);
        }
        if (testPart1(testResult)) {
            timing(() -> {
                R result = proposeSolutionPart1(parsedActualInput);
                System.out.print("Actual: ");
                displayResult(result);
            }, "Part 1 execution time:");
        }
    }

    private void part2() {
        System.out.println("----------Part 2----------");
        R testResult = null;
        if (needTest) {
            testResult = proposeSolutionPart2(parsedTestInput);
            System.out.print("Test: ");
            displayResult(testResult);
        }
        if (testPart2(testResult)) {
            timing(() -> {
                R result = proposeSolutionPart2(parsedActualInput);
                System.out.print("Actual: ");
                displayResult(result);
            }, "Part 2 execution time:");
        }
    }

    protected R proposeSolutionPart1(I input) {
        System.err.println("Solution for part 1 is not yet implemented!");
        return null;
    }

    protected R proposeSolutionPart2(I input) {
        System.err.println("Solution for part 2 is not yet implemented!");
        return null;
    }

    protected void displayResult(R result) {
        System.out.println(result);
    }
}
