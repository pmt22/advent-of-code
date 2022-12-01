package vn.pmt.impl.day24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 24/12/2021
 */
@Puzzle(year = 2021, day = 24)
public class Solver2124 extends AbstractPuzzleSolver<Solver2124.Input, Solver2124.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 24;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var monad = new MONAD();
        var input = new Input();
        input.monad = monad;

        Pattern inpPattern = Pattern.compile("inp ([w-z])");
        Pattern pattern = Pattern.compile("(\\w+) ([w-z]) (-?\\w+)");
        Pattern divZPattern = Pattern.compile("div z (-?\\d+)");
        Execution execution = null;
        List<Integer> divZList = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                execution.instructions.add(new Instruction(Operation.valueOf(matcher.group(1)), matcher.group(2), matcher.group(3)));
            } else {
                matcher = inpPattern.matcher(line);
                if (matcher.find()) {
                    execution = new Execution();
                    execution.instructions.add(new Instruction(Operation.inp, matcher.group(1)));
                    monad.executions.add(execution);
                }
            }
            matcher = divZPattern.matcher(line);
            if (matcher.find()) {
                divZList.add(Integer.parseInt(matcher.group(1)));
            }
        }

        input.maxZ = new long[divZList.size()];

        for (int i = divZList.size() - 1; i >= 0; i--) {
            int e = divZList.get(i);
            if (arrayAllZeros(input.maxZ)) {
                input.maxZ[i] = 26L;
            } else if (e == 1) {
                input.maxZ[i] = input.maxZ[i + 1];
            } else {
                input.maxZ[i] = input.maxZ[i + 1] * 26;
            }
        }
        return input;
    }

    boolean arrayAllZeros(long[] arr) {
        int n = arr.length;
        while (--n >= 0) {
            if (arr[n] != 0) return false;
        }
        return true;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();
        List<String> strings = new ArrayList<>();
        solve(0, 0, new int[14], input, strings);
        strings.stream()
            .map(Long::parseLong)
            .max(Comparator.comparing(Function.identity()))
            .ifPresent(max -> result.result = max);
        return result;
    }

    long validate(Execution execution, long w, long z) {
        execution.setZ(z);
        return execution.execute(w);
    }

    void solve(int depth, long z, int[] number, Input input, List<String> list) {
        if (depth == 14) {
            if (z == 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 14; i++) {
                    sb.append(number[i]);
                }
                list.add(sb.toString());
            }
            return;
        }
        else if (z >= input.maxZ[depth])
            return;

        for(int w = 1; w <= 9; w++) {
            number[depth] = w;
            solve(depth + 1, validate(input.monad.executions.get(depth), w, z), number, input, list);
        }
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        List<String> numbers = new ArrayList<>();
        solve(0, 0, new int[14], input, numbers);
        numbers.stream()
            .map(Long::parseLong)
            .min(Comparator.comparing(Function.identity()))
            .ifPresent(min -> result.result = min);
        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return true;
    }

    @Override
    protected boolean testPart2(Result result) {
        return true;
    }

    static class Input implements PuzzleInput {
        MONAD monad;
        long[] maxZ;
    }

    static class Result implements PuzzleResult {
        long result;

        @Override
        public String toString() {
            return "Result: " + result ;
        }
    }

    static class ALU {

        static void inp(Operand a, Object b) {
            a.value = (Long) b;
        }

        static void add(Operand a, Object b) {
            if (b instanceof Operand o) {
                a.value += o.value;
            } else {
                a.value += (Long) b;
            }
        }

        static void mul(Operand a, Object b) {
            if (b instanceof Operand o) {
                a.value *= o.value;
            } else {
                a.value *= (Long) b;
            }
        }

        static void div(Operand a, Object b) {
            if (b instanceof Operand o) {
                a.value = a.value/o.value;
            } else {
                a.value = a.value/(Long) b;
            }
        }

        static void mod(Operand a, Object b) {
            if (b instanceof Operand o) {
                a.value = a.value % o.value;
            } else {
                a.value = a.value % (Long) b;
            }

        }

        static void eql(Operand a, Object b) {
            if (b instanceof Operand) {
                a.value = Objects.equals(a, b) ? 1L : 0L;
            } else {
                a.value = Objects.equals(a.value, b) ? 1L : 0L;
            }
        }
    }

    static class MONAD {
        List<Execution> executions = new ArrayList<>();
    }

    static class Execution {
        List<Instruction> instructions = new ArrayList<>();
        OperandW w = new OperandW();
        OperandX x = new OperandX();
        OperandY y = new OperandY();
        OperandZ z = new OperandZ();

        long execute(Long wI) {
            instructions.stream()
                .filter(Objects::nonNull)
                .forEach(instruction -> {
                    instruction.w = w;
                    instruction.x = x;
                    instruction.y = y;
                    instruction.z = z;
                    instruction.execute(wI);
                });

            return z.value;
        }

        void setZ(long z) {
            this.z.value = z;
        }
    }

    @EqualsAndHashCode
    static class Operand {
        long value = 0;
    }

    static class OperandX extends Operand {
    }

    static class OperandY extends Operand {
    }

    static class OperandZ extends Operand {
    }

    static class OperandW extends Operand {
    }

    static class Instruction {
        Operation op;
        String left;
        String right;

        public Instruction(Operation op, String left, String right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        public Instruction(Operation op, String left) {
            this.op = op;
            this.left = left;
        }

        OperandW w;
        OperandX x;
        OperandY y;
        OperandZ z;

        void execute(Long wI) {
            Operand leftOperand = (Operand) chooseOperand(left);
            if (op == Operation.inp) {
                op.func.accept(leftOperand, wI);
            } else {
                Object rightOperand = chooseOperand(right);
                op.func.accept(leftOperand, rightOperand);
            }
        }

        Object chooseOperand(String operand) {
            return switch (operand) {
                case "x" -> x;
                case "y" -> y;
                case "z" -> z;
                case "w" -> w;
                default -> Long.parseLong(operand);
            };
        }
    }

    @AllArgsConstructor
    enum Operation {
        inp(ALU::inp),
        add(ALU::add),
        mul(ALU::mul),
        div(ALU::div),
        mod(ALU::mod),
        eql(ALU::eql);

        BiConsumer<Operand, Object> func;
    }
}
