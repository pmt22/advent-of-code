package vn.pmt.common.parser;

/**
 * @author Mai Thiên Phú
 * @since 08/12/2021
 */
public interface InputParser<R> {

    R parse(String str);
}
