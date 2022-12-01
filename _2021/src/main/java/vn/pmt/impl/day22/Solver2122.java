package vn.pmt.impl.day22;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * @since 22/12/2021
 */
@Puzzle(year = 2021, day = 22)
public class Solver2122 extends AbstractPuzzleSolver<Solver2122.Input, Solver2122.Result> {

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 22;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        var pattern = Pattern.compile("(\\w+) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)");
        input.procedures = lines.stream()
            .map(line -> {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return new Procedure(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4),
                        matcher.group(5), matcher.group(6), matcher.group(7));
                }
                return null;
            })
            .toList();
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        List<Procedure> procedures = input.procedures.stream()
            .filter(Procedure::withinRangeMinus50To50)
            .toList();
        Status[][][] statuses = new Status[101][101][101];

        for (Procedure p : procedures) {
            for (int x = p.xF; x <= p.xT; x++) {
                for (int y = p.yF; y <= p.yT; y++) {
                    for (int z = p.zF; z <= p.zT; z++) {
                        statuses[50 - x][50 - y][50 - z] = p.status;
                    }
                }
            }
        }

        var result = new Result();
        for (int x = 0; x <= 100; x++) {
            for (int y = 0; y <= 100; y++) {
                for (int z = 0; z <= 100; z++) {
                    if (statuses[x][y][z] == Status.on) {
                        result.onCubes++;
                    }
                }
            }
        }
        return result;
    }

    long compute(Cube c) {
        return (long) (c.xT - c.xF + 1) * (c.yT - c.yF + 1) * (c.zT - c.zF + 1);
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();
        var cubes = new CounterCollection();
        for (Procedure p : input.procedures) {
            long nsgn = p.status == Status.on ? 1 : -1;
            var update = new CounterCollection();
            for (var cubeEntry : cubes.counter.entrySet()) {
                var ix0 = Math.max(p.xF, cubeEntry.getKey().xF);
                var iy0 = Math.max(p.yF, cubeEntry.getKey().yF);
                var iz0 = Math.max(p.zF, cubeEntry.getKey().zF);
                var ix1 = Math.min(p.xT, cubeEntry.getKey().xT);
                var iy1 = Math.min(p.yT, cubeEntry.getKey().yT);
                var iz1 = Math.min(p.zT, cubeEntry.getKey().zT);
                if (ix0 <= ix1 && iy0 <= iy1 && iz0 <= iz1) {
                    update.counter.merge(new Cube(ix0, ix1, iy0, iy1, iz0, iz1), -cubeEntry.getValue(), Long::sum);
                }
            }
            if (nsgn > 0) {
                update.counter.merge(new Cube(p.xF, p.xT, p.yF, p.yT, p.zF, p.zT), nsgn, Long::sum);
            }
            cubes.update(update);
        }

        for (var entry : cubes.counter.entrySet()) {
            result.onCubes += compute(entry.getKey()) * entry.getValue();
        }
        return result;
    }

    @Override
    protected boolean testPart1(Result result) {
        return result.onCubes == 474140;
    }

    @Override
    protected boolean testPart2(Result result) {
        return result.onCubes == 2758514936282235L;
    }

    static class Input implements PuzzleInput {
        List<Procedure> procedures;
    }

    static class Result implements PuzzleResult {
        long onCubes;

        @Override
        public String toString() {
            return "Result: " + onCubes;
        }
    }

    @AllArgsConstructor
    static class Procedure {
        int xF;
        int xT;
        int yF;
        int yT;
        int zF;
        int zT;
        Status status;

        public Procedure(String status, String xF, String xT, String yF, String yT, String zF, String zT) {
            this.xF = Integer.parseInt(xF);
            this.xT = Integer.parseInt(xT);
            this.yF = Integer.parseInt(yF);
            this.yT = Integer.parseInt(yT);
            this.zF = Integer.parseInt(zF);
            this.zT = Integer.parseInt(zT);
            this.status = Status.valueOf(status);
        }

        boolean withinRangeMinus50To50() {
            return (xF >= -50 && xT <= 50)
                || (yF >= -50 && yT <= 50)
                || (zF >= -50 && zT <= 50);
        }
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    static class Cube {
        int xF;
        int xT;
        int yF;
        int yT;
        int zF;
        int zT;
    }

    enum Status {
        on, off
    }

    static class CounterCollection {
        Map<Cube, Long> counter = new HashMap<>();

        public void update(CounterCollection update) {
            update.counter.forEach((cube, sgn) -> counter.merge(cube, sgn, Long::sum));
        }
    }
}
