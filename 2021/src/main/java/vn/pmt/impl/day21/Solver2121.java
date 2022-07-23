package vn.pmt.impl.day21;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import lombok.AllArgsConstructor;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 21/12/2021
 */
@Puzzle(year = 2021, day = 21)
public class Solver2121 extends AbstractPuzzleSolver<Solver2121.Input, Solver2121.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 21;
    }

    @Override
    protected Input parseInput(List<String> lines) {

        return new Input(Integer.parseInt(String.valueOf(lines.get(0).charAt(lines.get(0).length() - 1))),
            Integer.parseInt(String.valueOf(lines.get(1).charAt(lines.get(1).length() - 1))));
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        int player1Score = 0;
        int player2Score = 0;

        boolean player1Turn = true;
        int currentDiceNum = 0;
        int rolledCount = 0;
        while (player1Score < 1000 && player2Score < 1000) {
            var nextSteps = nextNumberOnDice(currentDiceNum, 3);
            if (player1Turn) {
                input.player1Pos = positionAfterRoll(input.player1Pos, nextSteps.getRight());
                player1Score += input.player1Pos;
                player1Turn = false;
            } else {
                input.player2Pos = positionAfterRoll(input.player2Pos, nextSteps.getRight());
                player2Score += input.player2Pos;
                player1Turn = true;
            }
            currentDiceNum = nextSteps.getLeft();
            rolledCount += 3;
        }

        int loserScore = player1Score > player2Score ? player2Score : player1Score;
        result.multiplication = loserScore * rolledCount;
        return result;
    }

    static Pair<Integer, Integer> nextNumberOnDice(int current, int times) {
        int[] arr = new int[times + 1];
        arr[0] = current;
        for (int i = 1; i < times + 1; i++) {
            arr[i] = arr[i - 1] + 1;
        }
        int result = 0;
        for (int i = 1; i < times + 1; i++) {
            result += arr[i];
        }
        return Pair.of(arr[times], result);
    }

    static int positionAfterRoll(int current, int rollScore) {
        int position = (rollScore + current) % 10;
        return position == 0 ? 10 : position;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        nextPlay(input.player1Pos, input.player2Pos, 0, 0, true, result);
        return result;
    }

    void nextPlay(int currentPos1, int currentPos2, int currentScore1, int currentScore2, boolean is1Turn, Result result) {
        if (currentScore1 >= 21) {
            result.player1WinCount++;
        } else if (currentScore2 >= 21) {
            result.player2WinCount++;
        } else {
            int score1 = currentScore1;
            int score2 = currentScore2;
            while (score1 < 21 && score2 < 21) {
                for (int i = 1; i <= 6; i++) {
                    if (is1Turn) {
                        int pos1 = positionAfterRoll(currentPos1, i);
                        score1 = currentScore1 + pos1;
                        nextPlay(pos1, currentPos2, score1, currentScore2, false, result);
                    } else {
                        int pos2 = positionAfterRoll(currentPos2, 7 - i);
                        score2 = currentScore2 + pos2;
                        nextPlay(currentPos1, pos2, currentScore1, score2, true, result);
                    }
                }

            }
        }
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.multiplication == 739785;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.player1WinCount == 444_356_092_776_315L && result.player2WinCount == 341_960_390_180_808L;
    }

    @AllArgsConstructor
    static class Input implements PuzzleInput {
        int player1Pos;
        int player2Pos;
    }
    static class Result implements PuzzleResult {
        long multiplication;
        long player1WinCount;
        long player2WinCount;

        @Override
        public String toString() {
            return "Result: " + (multiplication != 0 ? multiplication : "P1: " + player1WinCount + ", P2: " + player2WinCount);
        }
    }
}
