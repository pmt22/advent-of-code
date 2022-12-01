package vn.pmt.impl.day04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import vn.pmt.common.Constant;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 11/12/2021
 */
@Puzzle(year = 2021, day = 4)
public class Solver214 extends AbstractPuzzleSolver<Solver214.Input, Solver214.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 4;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        input.calls = Arrays.stream(lines.get(0).split(Constant.COMMA_DELIMITER)).map(Integer::parseInt).toList();

        Board concernedBoard = new Board();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isBlank(line)) {
                concernedBoard = new Board();
                input.boards.add(concernedBoard);
            } else {
                concernedBoard.addRow(line);
            }
        }
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        for (Integer call : input.calls) {
            for (Board board : input.boards) {
                board.applyCall(call);
                if (board.hasWon()) {
                    result.score = board.unmarkedValue() * call;
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        Set<Board> wonBoards = new HashSet<>();
        for (Integer call : input.calls) {
            for (Board board : input.boards) {
                if (!wonBoards.contains(board)) {
                    board.applyCall(call);
                    if (board.hasWon()) {
                        wonBoards.add(board);
                        result.score = board.unmarkedValue() * call;
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.score == 4512;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.score == 1924;
    }

    static class Input implements PuzzleInput {
        List<Integer> calls;
        List<Board> boards = new ArrayList<>();
    }

    static class Result implements PuzzleResult {
        int score;

        @Override
        public String toString() {
            return "Result: " + score;
        }
    }

    static class Board {
        Cell[][] cells = new Cell[5][5];
        int filledRows = -1;

        void addRow(String line) {
            Cell[] cellRow = Stream.of(line.trim().split(Constant.SPACE_DELIMITER))
                .filter(StringUtils::isNotBlank)
                .map(Cell::new)
                .toArray(Cell[]::new);
            System.arraycopy(cellRow, 0, cells[++filledRows], 0, 5);
        }

        void applyCall(int number) {
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (cells[row][col].number == number) {
                        cells[row][col].isMarked = true;
                    }
                }
            }
        }

        boolean hasWon() {
            for (int row = 0; row < 5; row++) {
                if (Stream.of(cells[row]).allMatch(Cell::isMarked)) {
                    return true;
                }
            }

            for (int col = 0; col < 5; col++) {
                int finalCol = col;
                boolean bingo = IntStream.range(0, 5)
                    .boxed()
                    .map(i -> cells[i][finalCol])
                    .allMatch(Cell::isMarked);
                if (bingo) {
                    return true;
                }
            }

            return false;
        }

        int unmarkedValue() {
            int sum = 0;
            for (int row = 0; row < 5; row++) {
                for (int col = 0; col < 5; col++) {
                    if (cells[row][col].isNotMarked()) {
                        sum += cells[row][col].number;
                    }
                }
            }
            return sum;
        }
    }

    @Getter
    static class Cell {
        int number;
        boolean isMarked;

        public Cell(String number) {
            this.number = Integer.parseInt(number);
        }

        boolean isNotMarked() {
            return !isMarked;
        }
    }
}
