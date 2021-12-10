package vn.pmt.common;

/**
 * @author Mai Thiên Phú
 * @since 10/12/2021
 */
public class Log {

    public static void info(String message) {
        System.out.println(message);
    }

    public static void error(String message, Exception e) {
        error(message);
        e.printStackTrace();
    }

    public static void error(String message) {
        System.err.println(message);
    }
}
