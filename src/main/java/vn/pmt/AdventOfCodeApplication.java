package vn.pmt;

import java.io.IOException;
import java.util.Optional;

import com.google.common.reflect.ClassPath;
import vn.pmt.common.Log;
import vn.pmt.common.puzzle.Puzzle;
import vn.pmt.common.puzzle.Solver;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
public class AdventOfCodeApplication {

    public static void main(String[] args) throws Exception {
        final int year = Integer.parseInt(args[0]);
        final int day = Integer.parseInt(args[1]);

        Optional<Solver> solver = findSolver(year, day);

        if (solver.isPresent()) {
            solver.get().solve();
        } else {
            Log.info("Cannot find the Solver!!!");
        }
    }

    private static Optional<Solver> findSolver(int year, int day) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
            .getTopLevelClassesRecursive("vn.pmt")
            .stream()
            .map(ClassPath.ClassInfo::load)
            .filter(clazz -> {
                if (!clazz.isAnnotationPresent(Puzzle.class)) {
                    return false;
                }

                Puzzle puzzle = clazz.getAnnotation(Puzzle.class);
                return puzzle.year() == year && puzzle.day() == day;
            })
            .findFirst()
            .map(clazz -> {
                try {
                    return ((Solver) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    Log.error("Exception when instantiating a PuzzleSolver", e);
                    return null;
                }
            });
    }
}
