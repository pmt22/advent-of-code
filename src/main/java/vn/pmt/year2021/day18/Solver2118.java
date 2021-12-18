package vn.pmt.year2021.day18;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 18/12/2021
 */
@Puzzle(year = 2021, day = 18)
public class Solver2118 extends AbstractPuzzleSolver<Solver2118.Input, Solver2118.Result> {
    static final Map<Character, RegularNumber> REGULAR_NUMBER_MAP = new HashMap<>();

    static {
        REGULAR_NUMBER_MAP.put('0', new RegularNumber(0));
        REGULAR_NUMBER_MAP.put('1', new RegularNumber(1));
        REGULAR_NUMBER_MAP.put('2', new RegularNumber(2));
        REGULAR_NUMBER_MAP.put('3', new RegularNumber(3));
        REGULAR_NUMBER_MAP.put('4', new RegularNumber(4));
        REGULAR_NUMBER_MAP.put('5', new RegularNumber(5));
        REGULAR_NUMBER_MAP.put('6', new RegularNumber(6));
        REGULAR_NUMBER_MAP.put('7', new RegularNumber(7));
        REGULAR_NUMBER_MAP.put('8', new RegularNumber(8));
        REGULAR_NUMBER_MAP.put('9', new RegularNumber(9));
    }

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 18;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        input.lines = lines;
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        String left = input.lines.get(0);
        for (int i = 1; i < input.lines.size(); i++) {
            String right = input.lines.get(i);
            StringBuilder number = doSnailfishNumberAddition(left, right);
            left = number.toString();
        }
        SnailfishNumber resultNumber = Number.parseSnailfishNumber(left);
        result.magnitude = magnitude(resultNumber);
        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        for (int j = 0; j < input.lines.size(); j++) {
            for (int i = 0; i < input.lines.size(); i++) {
                String left = input.lines.get(j);
                String right = input.lines.get(i);
                StringBuilder number = doSnailfishNumberAddition(left, right);
                SnailfishNumber resultNumber = Number.parseSnailfishNumber(number.toString());
                long magnitude = magnitude(resultNumber);
                if (result.magnitude < magnitude) result.magnitude = magnitude;
            }
        }
        return result;
    }

    StringBuilder doSnailfishNumberAddition(String left, String right) {
        StringBuilder number = new StringBuilder();
        number.append('[')
            .append(left)
            .append(',')
            .append(right)
            .append(']');

        boolean notYetReduced = true;
        while (notYetReduced) {
            explode(number);
            notYetReduced = split(number);
        }
        return number;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.magnitude == 4140;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.magnitude == 3993;
    }

    long magnitude(SnailfishNumber number) {
        long magnitude = 0;
        if (number.left instanceof RegularNumber regularNumber) {
            magnitude += regularNumber.value * 3L;
        } else if (number.left instanceof SnailfishNumber snailfishNumber) {
            magnitude += 3 * magnitude(snailfishNumber);
        }

        if (number.right instanceof RegularNumber regularNumber) {
            magnitude += regularNumber.value * 2L;
        } else if (number.right instanceof SnailfishNumber snailfishNumber) {
            magnitude += 2 * magnitude(snailfishNumber);
        }

        return magnitude;
    }

    boolean split(StringBuilder number) {
        Pair<Integer, Integer> splitable;
        if ((splitable = findSplitable(number)) != null) {
            int left = Math.floorDiv(splitable.getRight(), 2);
            int right = Math.round(splitable.getRight() / 2f);

            number.replace(splitable.getLeft(), splitable.getLeft() + 2, String.format("[%d,%d]", left, right));
        }
        return findExplodable(number) != null || findSplitable(number) != null;
    }

    /**
     * @param number
     * @return Pair.of(startIndex, value)
     */
    Pair<Integer, Integer> findSplitable(StringBuilder number) {
        Pattern pattern = Pattern.compile("\\d{2}");
        Matcher matcher = pattern.matcher(number);
        if (matcher.find()) {
            String num = matcher.group();
            return Pair.of(number.indexOf(num), Integer.parseInt(num));
        }

        return null;
    }

    void explode(StringBuilder number) {
        Pair<Integer, Integer> explodeIndexes;
        while ((explodeIndexes = findExplodable(number)) != null) {
            String explodingBlock = number.substring(explodeIndexes.getLeft(), explodeIndexes.getRight() + 1);
            SnailfishNumber explodeNumber = Number.parseOneSimpleSnailfishNumber(explodingBlock);
            Triple<Integer, Integer, Integer> leftRegularNumber = findLeftRegularNumber(explodeIndexes.getLeft(), number);
            Triple<Integer, Integer, Integer> rightRegularNumber = findRightRegularNumber(explodeIndexes.getRight(), number);

            number.replace(explodeIndexes.getLeft(), explodeIndexes.getRight() + 1, "0");
            int lengthChange = number.length();
            if (leftRegularNumber != null) {
                Integer newValue = leftRegularNumber.getRight();
                if (explodeNumber.left instanceof RegularNumber reg) {
                    newValue += reg.value;
                }
                number.replace(leftRegularNumber.getLeft(), leftRegularNumber.getLeft() + leftRegularNumber.getMiddle(), String.valueOf(newValue));
            }
            lengthChange = number.length() - lengthChange;
            if (rightRegularNumber != null) {
                Integer newValue = rightRegularNumber.getRight();
                if (explodeNumber.right instanceof RegularNumber reg) {
                    newValue += reg.value;
                }
                int start = rightRegularNumber.getLeft() - (explodingBlock.length() - lengthChange);
                number.replace(start, start + rightRegularNumber.getMiddle(), String.valueOf(newValue));
            }
        }
    }

    /**
     * @param number
     * @return Pair.of(fromIndex, toIndex)
     */
    Pair<Integer, Integer> findExplodable(StringBuilder number) {
        Deque<Character> symbolDeque = new ArrayDeque<>();
        int explodeIndexFrom = -1;
        int explodeIndexTo = -1;

        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c == '[') {
                symbolDeque.push(c);
                if (symbolDeque.size() == 5) {
                    explodeIndexFrom = i;
                }
            } else if (c == ']') {
                if (symbolDeque.size() == 5) {
                    explodeIndexTo = i;
                }
                symbolDeque.pop();
            }

            if (explodeIndexFrom != -1 && explodeIndexTo != -1) {
                return Pair.of(explodeIndexFrom, explodeIndexTo);
            }
        }

        return null;
    }

    /**
     * @param explodeIndex
     * @param number
     * @return Triple.of(startIndex, length, numberValue)
     */
    static Triple<Integer, Integer, Integer> findLeftRegularNumber(int explodeIndex, StringBuilder number) {
        boolean found = false;
        StringBuilder returned = new StringBuilder();
        for (int i = explodeIndex; i >= 0; i--) {
            char c = number.charAt(i);
            if (REGULAR_NUMBER_MAP.containsKey(c)) {
                if (!found) {
                    returned.append(c);
                    found = true;
                } else {
                    returned.insert(0, c);
                }
            } else {
                if (found) {
                    return Triple.of(i + 1, returned.length(), Integer.parseInt(returned.toString()));
                }
            }
        }
        return null;
    }

    static Triple<Integer, Integer, Integer> findRightRegularNumber(int explodeIndex, StringBuilder number) {
        boolean found = false;
        StringBuilder returned = new StringBuilder();
        for (int i = explodeIndex; i < number.length(); i++) {
            char c = number.charAt(i);
            if (REGULAR_NUMBER_MAP.containsKey(c)) {
                returned.append(c);
                found = true;
            } else {
                if (found) {
                    return Triple.of(i + 1 - returned.length(), returned.length(), Integer.parseInt(returned.toString()));
                }
            }
        }
        return null;
    }

    static class Input implements PuzzleInput {
        List<String> lines;
    }

    static class Result implements PuzzleResult {
        long magnitude;

        @Override
        public String toString() {
            return "Result: " + magnitude;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    static class SnailfishNumber extends Number {
        Number left;
        Number right;
    }

    @AllArgsConstructor
    static class RegularNumber extends Number {
        int value;
    }

    abstract static class Number {
        static SnailfishNumber parseSnailfishNumber(String str) {
            var numbers = new ArrayDeque<Number>();
            for (char c : str.toCharArray()) {
                if (c == '[') {
                    var number = new SnailfishNumber();
                    numbers.push(number);
                } else if (REGULAR_NUMBER_MAP.containsKey(c)) {
                    numbers.push(REGULAR_NUMBER_MAP.get(c));
                } else if (c == ']') {
                    var rightOne = numbers.pop();
                    var leftOne = numbers.pop();
                    var parent = numbers.pop();
                    if (parent instanceof SnailfishNumber snailfishNumber) {
                        snailfishNumber.left = leftOne;
                        snailfishNumber.right = rightOne;
                        numbers.push(snailfishNumber);
                    } else {
                        throw new IllegalStateException("Something is terribly wrong");
                    }
                }
            }
            return (SnailfishNumber) numbers.pop();
        }

        static SnailfishNumber parseOneSimpleSnailfishNumber(String str) {
            var pattern = Pattern.compile("\\[(\\d+),(\\d+)\\]");
            var matcher = pattern.matcher(str);
            if (matcher.find()) {
                return new SnailfishNumber(new RegularNumber(Integer.parseInt(matcher.group(1))), new RegularNumber(Integer.parseInt(matcher.group(2))));
            }
            throw new IllegalStateException("Something is terribly wrong");
        }
    }
}
