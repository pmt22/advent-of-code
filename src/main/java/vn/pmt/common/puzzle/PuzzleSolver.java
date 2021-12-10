package vn.pmt.common.puzzle;

import vn.pmt.common.parser.InputParser;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
public abstract class PuzzleSolver {
    protected InputParser parser;

    protected Object parsedInput;

    public void test(String input) {

    }

    public void solvePart1(String input) {
        parsedInput = parser.parse(input);

    }
}
