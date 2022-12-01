package vn.pmt.impl.day20;

import java.util.List;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 20/12/2021
 */
@Puzzle(year = 2021, day = 20)
public class Solver2120 extends AbstractPuzzleSolver<Solver2120.Input, Solver2120.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 20;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        input.enhanceAlgorithm = lines.get(0).toCharArray();
        input.lineSize = lines.size() - 2;
        input.inputImage = new char[input.lineSize][input.lineSize];
        for (int i = 0; i < lines.size() - 2; i++) {
            String line = lines.get(i + 2);
            System.arraycopy(line.toCharArray(), 0, input.inputImage[i], 0, line.length());
        }
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        solve(input, result, 2);
        return result;
    }

    void solve(Input input, Result result, int steps) {
        char[][] outputImage;
        char[][] tempOutputImage;

        boolean firstCharInAlgorithmIsDark = input.enhanceAlgorithm[0] == '.';
        for (int step = 1; step <= steps; step++) {
            int additionalTempOutSize = 4;
            int additionalOutSize = 2;
            tempOutputImage = new char[(input.lineSize + additionalTempOutSize)][(input.lineSize + additionalTempOutSize)];
            outputImage = new char[(input.lineSize + additionalOutSize)][(input.lineSize + additionalOutSize)];

            if (firstCharInAlgorithmIsDark || step == 1) {
                fillArray(tempOutputImage);
            } else if (step % 2 == 0) {
                //here every other pixel is already "enhanced" by enhanceAlgorithm[0] so we fill it with 511 which is "#########"
                fillArray(tempOutputImage, buildEnhanceCharFromIndex(input.enhanceAlgorithm, 511));
            } else {
                //here every other pixel is already "enhanced" by enhanceAlgorithm[511] so we fill it with 511 which is "........."
                fillArray(tempOutputImage, buildEnhanceCharFromIndex(input.enhanceAlgorithm, 0));
            }
            fillArray(outputImage);

            for (int row = 0; row < input.lineSize; row++) {
                System.arraycopy(input.inputImage[row], 0, tempOutputImage[row + 2],
                    2, input.inputImage[row].length);
            }

            for (int row = 1; row < tempOutputImage.length - 1; row++) {
                for (int col = 1; col < tempOutputImage[0].length - 1; col++) {
                    char enhancedChar = evaluate(row, col, tempOutputImage, input.enhanceAlgorithm);

                    outputImage[row - 1][col - 1] = enhancedChar;
                }
            }

            input.inputImage = outputImage;
            input.lineSize = outputImage.length;
        }

        count(input, result);
    }

    char buildEnhanceCharFromIndex(char[] enhanceAlgorithm, int index) {
        return fromEnhancedAlgorithm(enhanceAlgorithm,
            new StringBuilder()
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index]))
            .append(convert(enhanceAlgorithm[index])));
    }

    void count(Input input, Result result) {
        for (int r = 0; r < input.inputImage.length; r++) {
            for (int c = 0; c < input.inputImage[0].length; c++) {
                if (input.inputImage[r][c] == '#') result.litCount++;
            }
        }
    }

    char evaluate(int row, int col, char[][] input, char[] enhanceAlgorithm) {
        StringBuilder sb = new StringBuilder();
        sb
            .append(convert(input[row - 1][col - 1]))
            .append(convert(input[row - 1][col]))
            .append(convert(input[row - 1][col + 1]))
            .append(convert(input[row][col - 1]))
            .append(convert(input[row][col]))
            .append(convert(input[row][col + 1]))
            .append(convert(input[row + 1][col - 1]))
            .append(convert(input[row + 1][col]))
            .append(convert(input[row + 1][col + 1]));

        return fromEnhancedAlgorithm(enhanceAlgorithm, sb);
    }

    char fromEnhancedAlgorithm(char[] enhanceAlgorithm, StringBuilder sb) {
        int index = Integer.parseInt(sb.toString(), 2);
        return enhanceAlgorithm[index];
    }


    int convert(char c) {
        return c == '#' ? 1 : 0;
    }

    void fillArray(char[][] array, char enhanceAlgorithmIndex0) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = enhanceAlgorithmIndex0;
            }
        }
    }

    void fillArray(char[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = '.';

            }
        }
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        solve(input, result, 50);
        return result;
    }



    @Override
    protected boolean testPart1(Result result) {
        return result.litCount == 35;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.litCount == 3351;
    }

    static class Input implements PuzzleInput {
        char[] enhanceAlgorithm;
        char[][] inputImage;
        int lineSize;
    }

    static class Result implements PuzzleResult {
        int litCount;

        @Override
        public String toString() {
            return "Result: " + litCount;
        }
    }
}
