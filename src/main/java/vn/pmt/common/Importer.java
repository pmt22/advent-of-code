package vn.pmt.common;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

import com.google.common.reflect.ClassPath;

/**
 * @author Mai Thiên Phú
 * @since 10/12/2021
 */
public class Importer {
    private static final String TEST = "test.txt";
    private static final String ACTUAL = "actual.txt";

    public List<String> importTest(int year, int day) throws Exception {
        return readAllLines(year, day, TEST);
    }

    public List<String> importActual(int year, int day) throws Exception {
        return readAllLines(year, day, ACTUAL);
    }

    private List<String> readAllLines(int year, int day, String fileName) throws Exception {
        var resourceName = String.format("input/year%d/day%02d/%s", year, day, fileName);
        Optional<Path> path = ClassPath.from(ClassLoader.getSystemClassLoader())
            .getResources().stream()
            .filter(res -> StringUtils.equals(res.getResourceName(), resourceName))
            .findFirst()
            .map(ClassPath.ResourceInfo::url)
            .map(URL::getPath)
            .map(s -> s.substring(1))
            .map(Path::of);

        if (path.isPresent()) {
            return Files.readAllLines(path.get());
        }

        return Collections.emptyList();
    }
}
