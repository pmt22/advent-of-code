package vn.pmt;

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import vn.pmt.common.puzzle.Puzzle;
import vn.pmt.common.puzzle.PuzzleSolver;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
@Slf4j
public class AdventOfCodeApplication {

    public static void main(String[] args) throws Exception {
        final int year = 2021;
        final int day = 8;

        PuzzleSolver puzzleSolver = ClassPath.from(ClassLoader.getSystemClassLoader())
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
                    return ((PuzzleSolver) clazz.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    log.error("Exception when instantiating a PuzzleSolver", e);
                    return null;
                }
            })
            .orElse(null);

        puzzleSolver.solvePart1()
    }
}
