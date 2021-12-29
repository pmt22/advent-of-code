package vn.pmt.year2021.day12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import vn.pmt.common.Constant;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 12/12/2021
 */
@SuppressWarnings("java:S1854")
@Puzzle(year = 2021, day = 12)
public class Solver2112 extends AbstractPuzzleSolver<Solver2112.Input, Solver2112.Result> {
    static final String START = "start";
    static final String END = "end";

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 12;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        Map<Cave, Cave> positionsMap = new HashMap<>();

        lines.stream()
            .map(str -> str.split(Constant.HYPHEN_DELIMITER))
            .forEach(arr -> {
                var firstEnd = new Cave(arr[0]);
                var secondEnd = new Cave(arr[1]);
                if (!positionsMap.containsKey(firstEnd)) positionsMap.put(firstEnd, firstEnd);
                if (!positionsMap.containsKey(secondEnd)) positionsMap.put(secondEnd, secondEnd);
                positionsMap.get(firstEnd).linkedCaveSet.add(positionsMap.get(secondEnd));
                positionsMap.get(secondEnd).linkedCaveSet.add(positionsMap.get(firstEnd));
            });

        input.caves.addAll(positionsMap.keySet());
        input.caves.forEach(Cave::makeList);
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        Cave start = input.caves.stream().filter(Cave::isStart).findFirst().get();
        Cave end = input.caves.stream().filter(Cave::isEnd).findFirst().get();

        count(start, end, result, input, (in, concernedCave) -> concernedCave.passedCount < 1);

        return result;
    }

    void count(Cave start, Cave end, Result result, Input input, BiPredicate<Input, Cave> caseSpecificPredicate) {
        if (start == end) {
            result.pathCount++;
        } else {
            for (int i = 0; i < start.linkedCaveList.size(); i++) {
                Cave cave = start.linkedCaveList.get(i);
                if (!cave.isStart && (!cave.isSmallCave || caseSpecificPredicate.test(input, cave))) {
                    cave.passedCount++;
                    count(cave, end, result, input, caseSpecificPredicate);
                    cave.passedCount--;
                }
            }
        }
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();

        var startCave = input.caves.stream().filter(Cave::isStart).findFirst().get();
        var endCave = input.caves.stream().filter(Cave::isEnd).findFirst().get();

        count(startCave, endCave, result, input, (in, concernedCave) -> noCaveIsPassedMoreThanOnce(input)
            || existASmallCaveIsPassedTwiceAndThatOneIsNotSameAsConcernedCaveWhichIsAlsoNotPassed(input, concernedCave));
        return result;
    }

    boolean noCaveIsPassedMoreThanOnce(Input input) {
        return input.caves.stream().filter(Cave::isSmallCave).noneMatch(p -> p.passedCount > 1);
    }

    boolean existASmallCaveIsPassedTwiceAndThatOneIsNotSameAsConcernedCaveWhichIsAlsoNotPassed(Input input, Cave cave) {
        return cave.passedCount < 1
            && input.caves.stream().filter(Cave::isSmallCave).anyMatch(p -> p.passedCount > 1)
            && input.caves.stream().filter(Cave::isSmallCave).filter(p -> p.passedCount > 1).findFirst().map(c -> !c.equals(cave)).orElse(false);
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.pathCount == 10;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.pathCount == 36;
    }

    static class Input implements PuzzleInput {
        Set<Cave> caves = new HashSet<>();
    }

    static class Result implements PuzzleResult {
        int pathCount;

        @Override
        public String toString() {
            return "Result: " + pathCount;
        }
    }

    @Getter
    @EqualsAndHashCode(of = "val")
    static class Cave {
        String val;
        boolean isSmallCave;
        boolean isStart;
        boolean isEnd;
        Set<Cave> linkedCaveSet = new HashSet<>();
        List<Cave> linkedCaveList = new ArrayList<>();

        int passedCount;

        public Cave(String val) {
            this.val = val;
            isStart = START.equals(val);
            isEnd = END.equals(val);
            if (!isStart && !isEnd) isSmallCave = StringUtils.isAllLowerCase(val);
        }

        void makeList() {
            linkedCaveList = linkedCaveSet.stream().toList();
        }
    }
}
