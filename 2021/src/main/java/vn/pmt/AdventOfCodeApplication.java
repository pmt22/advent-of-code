package vn.pmt;

import java.io.IOException;
import java.util.Optional;

import com.google.common.reflect.ClassPath;
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
        Boolean needTest = null;
        if (args.length == 3) {
            needTest = Boolean.valueOf(args[2]);
        }

        Optional<Solver> solver = findSolver(year, day, needTest);

        if (solver.isPresent()) {
            solver.get().solve();
        } else {
            System.err.println("Cannot find the Solver!!!");
        }
    }

    private static Optional<Solver> findSolver(int year, int day, Boolean needTest) throws IOException {
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
                    Solver solver = (Solver) clazz.getDeclaredConstructor().newInstance();
                    solver.needTest(needTest);
                    return solver;
                } catch (Exception e) {
                    System.err.println("Exception when instantiating a PuzzleSolver");
                    e.printStackTrace();
                    return null;
                }
            });
    }
}
