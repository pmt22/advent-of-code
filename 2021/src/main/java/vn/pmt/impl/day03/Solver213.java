package vn.pmt.impl.day03;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        var input = new Input();
        input.maxNumber = lines.size();
        input.numberLength = lines.get(0).length();
        input.arr = new char[input.maxNumber][input.numberLength];
        for (int i = 0; i < lines.size(); i++) {
            System.arraycopy(lines.get(i).toCharArray(), 0, input.arr[i], 0, input.numberLength);
        }
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        StringBuilder gammaBuilder = new StringBuilder();
        StringBuilder epsilonBuilder = new StringBuilder();

        for (int col = 0; col < input.numberLength; col++) {
            int sum = 0;
            for (int row = 0; row < input.maxNumber; row++) {
                sum += input.arr[row][col] - '0';
            }
            if (sum > input.maxNumber/2) {
                gammaBuilder.append(1);
                epsilonBuilder.append(0);
            } else {
                gammaBuilder.append(0);
                epsilonBuilder.append(1);
            }
        }

        return new Result(Integer.parseInt(gammaBuilder.toString(), 2) * Integer.parseInt(epsilonBuilder.toString(), 2), null);
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        Set<Integer> rowsForOxygen = identifyRating(input, true);

        Set<Integer> rowsForCO2 = identifyRating(input, false);

        Integer oxygen = Integer.parseInt(String.valueOf(input.arr[rowsForOxygen.iterator().next()]), 2);
        Integer co2 = Integer.parseInt(String.valueOf(input.arr[rowsForCO2.iterator().next()]), 2);

        return new Result(null, oxygen * co2);
    }

    private Set<Integer> identifyRating(Input input, boolean isOxygen) {
        Set<Integer> remainingRows = IntStream.range(0, input.maxNumber).boxed().collect(Collectors.toSet());
        for (int col = 0; col < input.numberLength; col++) {
            if (remainingRows.size() == 1) {
                break;
            }
            Set<Integer> zeros = new HashSet<>();
            Set<Integer> ones = new HashSet<>();
            for (int row = 0; row < input.maxNumber; row++) {
                if (remainingRows.contains(row)) {
                    if (input.arr[row][col] == '0') {
                        zeros.add(row);
                    } else {
                        ones.add(row);
                    }
                }
            }

            if (ones.size() >= zeros.size()) {
                remainingRows.removeAll(isOxygen ? zeros : ones);
            } else {
                remainingRows.removeAll(isOxygen ? ones : zeros);
            }
        }
        return remainingRows;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.powerConsumption == 198;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.lifeSupportRating == 230;
    }

    static class Input implements PuzzleInput {
        int maxNumber;
        int numberLength;
        char[][] arr;
    }

    static class Result implements PuzzleResult {
        Integer powerConsumption;
        Integer lifeSupportRating;

        public Result(Integer powerConsumption, Integer lifeSupportRating) {
            this.powerConsumption = powerConsumption;
            this.lifeSupportRating = lifeSupportRating;
        }

        @Override
        public String toString() {
            return "Result: " + (lifeSupportRating != null ? lifeSupportRating : powerConsumption);
        }
    }
}
