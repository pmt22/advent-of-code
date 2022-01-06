package vn.pmt.impl.day06;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import vn.pmt.common.Constant;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 04/01/2022
 */
@Puzzle(year = 2021, day = 6)
public class Solver216 extends AbstractPuzzleSolver<Solver216.Input, Solver216.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 6;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        Arrays.stream(lines.iterator().next().split(Constant.COMMA_DELIMITER))
            .map(Integer::parseInt)
            .map(LanternFish::new)
            .forEach(fish -> input.schoolMap.merge(fish, 1L, Long::sum));
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        return solve(input, 80);
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        return solve(input, 256);
    }

    Result solve(Input input, int days) {
        for (int i = 0; i < days; i++) {
            Map<LanternFish, Long> schoolMapTemp = new HashMap<>();
            input.schoolMap.forEach((lanternFish, count) -> {
                lanternFish.nextState(schoolMapTemp, count);
                schoolMapTemp.merge(lanternFish, count, Long::sum);
            });
            input.schoolMap = schoolMapTemp;
        }
        return new Result(input.schoolMap.values().stream().reduce(0L, Long::sum));
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.count == 5934;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.count == 26_984_457_539L;
    }

    static class Input implements PuzzleInput {
        Map<LanternFish, Long> schoolMap = new HashMap<>();
    }

    @AllArgsConstructor
    static class Result implements PuzzleResult {
        long count;

        @Override
        public String toString() {
            return "Result: " + count;
        }
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    static class LanternFish {
        int state = 8;

        void nextState(Map<LanternFish, Long> schoolMap, long originCount) {
            if (state == 0) {
                giveBirth(schoolMap, originCount);
                state = 6;
            } else {
                state--;
            }
        }

        void giveBirth(Map<LanternFish, Long> schoolMap, long count) {
            schoolMap.merge(new LanternFish(), count, Long::sum);
        }
    }
}
