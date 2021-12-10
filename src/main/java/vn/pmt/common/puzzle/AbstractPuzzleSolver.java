package vn.pmt.common.puzzle;

import java.io.IOException;
import java.util.List;

import vn.pmt.common.Importer;
import vn.pmt.common.Log;
import vn.pmt.common.parser.InputParser;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
public abstract class AbstractPuzzleSolver<R> implements Solver {
    private final Importer importer = new Importer();
    private List<String> rawTestInput;
    private List<String> rawActualInput;

    protected InputParser<R> parser;

    protected abstract int year();
    protected abstract int day();

    @Override
    public void solve() throws Exception {
        importResources();
        Log.info("Solved: " + rawTestInput.size() + ", " + rawActualInput.size());
    }

    private void importResources() throws Exception {
        rawTestInput = importer.importTest(year(), day());
        rawActualInput = importer.importActual(year(), day());
    }

    public void test(String input) {

    }

    public void solvePart1(String input) {

    }
}
