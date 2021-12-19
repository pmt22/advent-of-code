package vn.pmt.year2021.day13;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 13/12/2021
 */
@Puzzle(year = 2021, day = 13)
public class Solver2113 extends AbstractPuzzleSolver<Solver2113.Input, Solver2113.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 13;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();

        boolean foldPart = false;
        List<Pair<Integer, Integer>> coordinateList = new ArrayList<>();
        for (var line : lines) {
            if (StringUtils.isBlank(line) && !foldPart) {
                foldPart = true;
                continue;
            }

            if (foldPart) {
                var split = line.split("=");
                input.actions.add(new Action(Integer.parseInt(split[1]), split[0].charAt(split[0].length() - 1)));
            } else {
                var split = line.split(",");
                var pair = Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                if (pair.getLeft() > input.maxX) {
                    input.maxX = pair.getLeft();
                }
                if (pair.getRight() > input.maxY) {
                    input.maxY = pair.getRight();
                }
                coordinateList.add(pair);
            }
        }

        input.arr = new int[input.maxY + 1][input.maxX + 1];

        coordinateList.forEach(pair -> input.arr[pair.getRight()][pair.getLeft()] = 1);

        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        fold(input, result, input.actions.get(0));

        return result;
    }

    private void fold(Input input, Result result, Action action) {
        if (action.direction == 'y') {
            for (int row = input.maxY; row > action.foldPosition; row--) {
                for (int col = 0; col <= input.maxX; col++) {
                    input.arr[(input.maxY % 2 != 0 ? input.maxY + 1 : input.maxY) - row][col] += input.arr[row][col];
                }
            }

            for (int row = 0; row < action.foldPosition; row++) {
                for (int col = 0; col <= input.maxX; col++) {
                    if (input.arr[row][col] > 0) result.visibleDots++;
                }
            }
            input.maxY = action.foldPosition - 1;
        } else {
            for (int col = input.maxX; col > action.foldPosition; col--) {
                for (int row = 0; row <= input.maxY; row++) {
                    input.arr[row][(input.maxX % 2 != 0 ? input.maxX + 1 : input.maxX) - col] += input.arr[row][col];
                }
            }

            for (int row = 0; row <= input.maxY; row++) {
                for (int col = 0; col < action.foldPosition; col++) {
                    if (input.arr[row][col] > 0) result.visibleDots++;
                }
            }
            input.maxX = action.foldPosition - 1;
        }
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        for (int i = 0; i < input.actions.size(); i++) {
            var action = input.actions.get(i);
            fold(input, result, action);
            if (i == input.actions.size() - 1) print(input);
        }
        return result;
    }

    private void print(Input input) {
        for (int row = 0; row <= input.maxY; row++) {
            for (int col = 0; col <= input.maxX; col++) {
                int num = input.arr[row][col];
                if (num != 0) {
                    System.out.print("# ");
                } else {
                    System.out.print(". ");
                }
                if ((col - 4) % 5 == 0) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        System.out.println("-----");
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.visibleDots == 17;
    }

    @Override
    protected boolean testPart2(Result result) {
        return true;
    }

    static class Input implements PuzzleInput {
        List<Action> actions = new ArrayList<>();

        int maxX;
        int maxY;
        int[][] arr;
    }

    static class Result implements PuzzleResult {
        int visibleDots;

        @Override
        public String toString() {
            return "Result: " + visibleDots;
        }
    }

    static record Action(int foldPosition, char direction) {}
}
