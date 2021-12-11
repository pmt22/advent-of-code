package vn.pmt.year2021.day11;

import static vn.pmt.year2021.day11.Solver2111.Input;
import static vn.pmt.year2021.day11.Solver2111.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 10/12/2021
 */
@Puzzle(year = 2021, day = 11)
public class Solver2111 extends AbstractPuzzleSolver<Input, Result> {
    private static final int STEPS = 100;
    private static final int MAX_COL = 10;
    private static final int MAX_ROW = 10;

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 11;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        var octopuses = new Octopus[MAX_ROW][MAX_COL];

        for (int i = 0; i < lines.size(); i++) {
            var arr = lines.get(i).split(StringUtils.EMPTY);
            System.arraycopy(Arrays.stream(arr).map(str -> new Octopus(Integer.parseInt(str))).toArray(Octopus[]::new),
                0, octopuses[i], 0, arr.length);
        }
        input.octopus = octopuses;
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input parsedInput) {
        var result = new Result();

        findAdjacent(parsedInput.octopus);

        for (int i = 0; i < STEPS; i++) {
            charging(parsedInput.octopus, result);
            afterStep(parsedInput.octopus);
        }

        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.flashedTimes == 1656;
    }

    @Override
    protected Result proposeSolutionPart2(Input parsedInput) {
        var result = new Result();

        findAdjacent(parsedInput.octopus);

        int step = 0;
        do {
            charging(parsedInput.octopus, result);
            afterStep(parsedInput.octopus);
            step++;
        } while (!allOctopusFlash(parsedInput.octopus));

        result.allFlashStep = step;

        return result;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.allFlashStep == 195;
    }

    @Override
    protected void displayResult(Result result) {
        if (result.allFlashStep == 0) {
            System.out.println("Flash times: " + result.flashedTimes);
        } else {
            System.out.println("All octopuses flash at step: " + result.allFlashStep);
        }
    }

    static class Input implements PuzzleInput {
        Octopus[][] octopus;
    }

    static class Result implements PuzzleResult {
        int flashedTimes;

        int allFlashStep;
    }

    static class Octopus {
        int energy;
        boolean hasFlashed;
        List<Octopus> adjacentOctopuses = new ArrayList<>(8);

        public Octopus(int energy) {
            this.energy = energy;
        }
        
        void charge(Result result) {
            energy++;
            if (energy == 10 && !hasFlashed) {
                flash(result);
            }
        }
        
        void flash(Result result) {
            hasFlashed = true;
            result.flashedTimes++;
            adjacentOctopuses.forEach(o -> o.charge(result));
        }

        @Override
        public String toString() {
            return String.valueOf(energy);
        }
    }

    private boolean allOctopusFlash(Octopus[][] octopuses) {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (octopuses[row][col].energy != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void charging(Octopus[][] octopuses, Result result) {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                octopuses[row][col].charge(result);
            }
        }
    }

    private void afterStep(Octopus[][] octopuses) {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Octopus octopus = octopuses[row][col];
                if (octopus.hasFlashed) {
                    octopus.energy = 0;
                    octopus.hasFlashed = false;
                }
            }
        }
    }

    private void findAdjacent(Octopus[][] octopuses) {
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (row == 0) {
                    if (col == 0) {
                        takeRight(col, row, octopuses);
                        takeBelowRight(col, row, octopuses);
                        takeBelow(col, row, octopuses);
                    } else if (col == MAX_COL - 1) {
                        takeLeft(col, row, octopuses);
                        takeBelowLeft(col, row, octopuses);
                        takeBelow(col, row, octopuses);
                    } else {
                        takeNotAbove(col, row, octopuses);
                    }
                } else if (row == MAX_ROW - 1) {
                    if (col == 0) {
                        takeRight(col, row, octopuses);
                        takeAboveRight(col, row, octopuses);
                        takeAbove(col, row, octopuses);
                    } else if (col == MAX_COL - 1) {
                        takeLeft(col, row, octopuses);
                        takeAboveLeft(col, row, octopuses);
                        takeAbove(col, row, octopuses);
                    } else {
                        takeNotBelow(col, row, octopuses);
                    }
                }  else if (col == 0) {
                    takeNotLeft(col, row, octopuses);
                }  else if (col == MAX_COL - 1) {
                    takeNotRight(col, row, octopuses);
                } else {
                    takeAround(col, row, octopuses);
                }
            }
        }
    }

    private void takeAround(int col, int row, Octopus[][] octopuses) {
        takeRight(col, row, octopuses);
        takeLeft(col, row, octopuses);
        takeAbove(col, row, octopuses);
        takeBelow(col, row, octopuses);

        takeAboveRight(col, row, octopuses);
        takeAboveLeft(col, row, octopuses);
        takeBelowRight(col, row, octopuses);
        takeBelowLeft(col, row, octopuses);
    }

    private void takeNotAbove(int col, int row, Octopus[][] octopuses) {
        takeRight(col, row, octopuses);
        takeLeft(col, row, octopuses);
        takeBelow(col, row, octopuses);
        takeBelowRight(col, row, octopuses);
        takeBelowLeft(col, row, octopuses);
    }

    private void takeNotBelow(int col, int row, Octopus[][] octopuses) {
        takeRight(col, row, octopuses);
        takeLeft(col, row, octopuses);
        takeAbove(col, row, octopuses);
        takeAboveRight(col, row, octopuses);
        takeAboveLeft(col, row, octopuses);
    }

    private void takeNotLeft(int col, int row, Octopus[][] octopuses) {
        takeRight(col, row, octopuses);
        takeAbove(col, row, octopuses);
        takeBelow(col, row, octopuses);
        takeAboveRight(col, row, octopuses);
        takeBelowRight(col, row, octopuses);
    }

    private void takeNotRight(int col, int row, Octopus[][] octopuses) {
        takeLeft(col, row, octopuses);
        takeAbove(col, row, octopuses);
        takeBelow(col, row, octopuses);
        takeAboveLeft(col, row, octopuses);
        takeBelowLeft(col, row, octopuses);
    }

    private void takeRight(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row][col + 1]);
    }

    private void takeLeft(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row][col - 1]);
    }

    private void takeBelow(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row + 1][col]);
    }

    private void takeAbove(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row - 1][col]);
    }

    private void takeAboveRight(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row - 1][col + 1]);
    }

    private void takeAboveLeft(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row - 1][col - 1]);
    }

    private void takeBelowRight(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row + 1][col + 1]);
    }

    private void takeBelowLeft(int col, int row, Octopus[][] octopuses) {
        octopuses[row][col].adjacentOctopuses.add(octopuses[row + 1][col - 1]);
    }
}
