package vn.pmt.year2021.day25;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 25/12/2021
 */
@Puzzle(year = 2021, day = 25)
public class Solver2125 extends AbstractPuzzleSolver<Solver2125.Input, Solver2125.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 25;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        input.maxRight = lines.get(0).length();
        input.maxDown = lines.size();
        input.floor = new SeaCucumber[input.maxDown][input.maxRight];

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            SeaCucumber[] cucumbers = new SeaCucumber[line.length()];
            for (int col = 0; col < line.toCharArray().length; col++) {
                char c = line.toCharArray()[col];
                if (c != '.') {
                    SeaCucumber seaCucumber = new SeaCucumber(row, col, Direction.of(c));
                    cucumbers[col] = seaCucumber;
                    if (seaCucumber.dir == Direction.RIGHT) {
                        input.rightOnes.add(seaCucumber);
                    } else {
                        input.downOnes.add(seaCucumber);
                    }
                }
            }
            System.arraycopy(cucumbers, 0, input.floor[row], 0, line.length());
        }
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        List<SeaCucumber> canMoveRights;
        List<SeaCucumber> canMoveDowns;
        int step = 1;
        while (true) {
            canMoveRights = input.rightOnes.stream()
                .filter(s -> s.canMove(input.floor, input.maxRight))
                .toList();

            canMoveRights.forEach(s -> s.move(input.floor, input.maxRight));

            canMoveDowns = input.downOnes.stream()
                .filter(s -> s.canMove(input.floor, input.maxDown))
                .toList();

            canMoveDowns.forEach(s -> s.move(input.floor, input.maxDown));

            if (canMoveRights.isEmpty() && canMoveDowns.isEmpty()) {
                break;
            }
            step++;
        }

        result.steps = step;
        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        return super.proposeSolutionPart2(input);
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.steps == 58;
    }

    @Override
    protected boolean testPart2(Result result) {
        return false;
    }

    static class Input implements PuzzleInput {
        int maxDown;
        int maxRight;
        SeaCucumber[][] floor;
        List<SeaCucumber> rightOnes = new ArrayList<>();
        List<SeaCucumber> downOnes = new ArrayList<>();
    }

    static class Result implements PuzzleResult {
        int steps;

        @Override
        public String toString() {
            return "Result: " +  steps;
        }
    }

    @AllArgsConstructor
    static class SeaCucumber {
        int row;
        int col;
        Direction dir;

        boolean canMove(SeaCucumber[][] floor, int maxEnd) {
            if (dir == Direction.RIGHT) {
                int nextPos = col + 1;
                if (nextPos == maxEnd) {
                    return floor[row][0] == null;
                } else {
                    return floor[row][nextPos] == null;
                }
            } else {
                int nextPos = row + 1;
                if (nextPos == maxEnd) {
                    return floor[0][col] == null;
                } else {
                    return floor[nextPos][col] == null;
                }
            }
        }

        void move(SeaCucumber[][] floor, int maxEnd) {
            if (dir == Direction.RIGHT) {
                int nextPos = col + 1;
                if (nextPos == maxEnd) {
                    floor[row][0] = floor[row][col];
                    floor[row][col] = null;
                    col = 0;
                } else {
                    floor[row][nextPos] = floor[row][col];
                    floor[row][col] = null;
                    col = nextPos;
                }
            } else {
                int nextPos = row + 1;
                if (nextPos == maxEnd) {
                    floor[0][col] = floor[row][col];
                    floor[row][col] = null;
                    row = 0;
                } else {
                    floor[nextPos][col] = floor[row][col];
                    floor[row][col] = null;
                    row = nextPos;
                }
            }
        }

        @Override
        public String toString() {
            return dir == Direction.RIGHT ? ">" : "v";
        }
    }

    enum Direction {
        RIGHT, DOWN;

        static Direction of(char c) {
            if ('>' == c) {
                return RIGHT;
            } else {
                return DOWN;
            }
        }
    }
}
