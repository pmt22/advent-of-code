package vn.pmt.year2021.day14;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import vn.pmt.common.Constant;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 14/12/2021
 */
@Puzzle(year = 2021, day = 14)
public class Solver2114 extends AbstractPuzzleSolver<Solver2114.Input, Solver2114.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 14;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();

        boolean polyPart = true;
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                polyPart = false;
                continue;
            }
            if (polyPart) {
                for (int i = 0; i < line.length() - 1; i++) {
                    input.countMap.merge(Pair.of(String.valueOf(line.charAt(i)), String.valueOf(line.charAt(i + 1))), 1L, Long::sum);
                }

                input.lastChar = String.valueOf(line.charAt(line.length() - 1));
            } else {
                var mainSplit = line.split(Constant.ARROW_SPACE_DELIMITER);
                var firstHalf = mainSplit[0].split(StringUtils.EMPTY);

                input.rules.put(Pair.of(firstHalf[0], firstHalf[1]),
                    List.of(Pair.of(firstHalf[0], mainSplit[1]), Pair.of(mainSplit[1], firstHalf[1])));
            }
        }

        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        solve(input, result, 10);

        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();

        solve(input, result, 40);

        return result;
    }

    private void solve(Input input, Result result, int steps) {
        for (int i = 0; i < steps; i++) {
            Map<Pair<String, String>, Long> tempMap = new LinkedHashMap<>();
            Set<Pair<String, String>> keySet = input.countMap.keySet();
            for (Pair<String, String> key : keySet) {
                Long count = input.countMap.get(key);
                input.rules.get(key).forEach(pair -> tempMap.merge(pair, count, Long::sum));
            }

            input.countMap = tempMap;
        }

        Map<String, Long> firstCharacterCountMap = new HashMap<>();
        input.countMap.forEach((key, value) -> {
            String first = key.getLeft();
            firstCharacterCountMap.merge(first, value, Long::sum);
        });

        firstCharacterCountMap.merge(input.lastChar, 1L, Long::sum);

        long max = firstCharacterCountMap.values().stream().max(Comparator.comparing(Function.identity())).get();
        long min = firstCharacterCountMap.values().stream().min(Comparator.comparing(Function.identity())).get();

        result.subtraction = (max - min);
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.subtraction == 1588;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.subtraction == 2188189693529L;
    }

    static class Input implements PuzzleInput {
        Map<Pair<String, String>, List<Pair<String, String>>> rules = new HashMap<>();
        Map<Pair<String, String>, Long> countMap = new LinkedHashMap<>();
        String lastChar;
    }

    static class Result implements PuzzleResult {
        long subtraction;

        @Override
        public String toString() {
            return "Result: " + subtraction;
        }
    }
}
