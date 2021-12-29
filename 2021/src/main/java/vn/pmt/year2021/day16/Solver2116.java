package vn.pmt.year2021.day16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import vn.pmt.common.PuzzleInput;
import vn.pmt.common.PuzzleResult;
import vn.pmt.common.puzzle.AbstractPuzzleSolver;
import vn.pmt.common.puzzle.Puzzle;

/**
 * @author Mai Thiên Phú
 * @since 16/12/2021
 */
@Puzzle(year = 2021, day = 16)
public class Solver2116 extends AbstractPuzzleSolver<Solver2116.Input, Solver2116.Result> {
    static final Map<Character, String> HEXA_MAP = new HashMap<>();
    static {
        HEXA_MAP.put('0', "0000");
        HEXA_MAP.put('1', "0001");
        HEXA_MAP.put('2', "0010");
        HEXA_MAP.put('3', "0011");
        HEXA_MAP.put('4', "0100");
        HEXA_MAP.put('5', "0101");
        HEXA_MAP.put('6', "0110");
        HEXA_MAP.put('7', "0111");
        HEXA_MAP.put('8', "1000");
        HEXA_MAP.put('9', "1001");
        HEXA_MAP.put('A', "1010");
        HEXA_MAP.put('B', "1011");
        HEXA_MAP.put('C', "1100");
        HEXA_MAP.put('D', "1101");
        HEXA_MAP.put('E', "1110");
        HEXA_MAP.put('F', "1111");
    }

    @Override
    protected int year() {
        return 2021;
    }

    @Override
    protected int day() {
        return 16;
    }

    @Override
    protected Input parseInput(List<String> lines) {
        var input = new Input();
        StringBuilder sb = new StringBuilder();
        for (char c : lines.get(0).toCharArray()) {
            sb.append(HEXA_MAP.get(c));
        }
        input.str = sb.toString();
        return input;
    }

    @Override
    protected Result proposeSolutionPart1(Input input) {
        var result = new Result();

        resolveBits(input.str, input.packets, result, true);

        return result;
    }

    @Override
    protected Result proposeSolutionPart2(Input input) {
        var result = new Result();

        resolveBits(input.str, input.packets, result, true);

        result.evaluatedResult = calculatePacket(input.packets.get(0));

        return result;
    }

    long calculatePacket(Packet packet) {
        return switch ((int) packet.type) {
            case 0 -> packet.subPackets.stream().map(this::calculatePacket).reduce(0L, Long::sum);
            case 1 -> packet.subPackets.stream().map(this::calculatePacket).reduce(1L, (aLong, aLong2) -> aLong * aLong2);
            case 2 -> packet.subPackets.stream().map(this::calculatePacket).min(Comparator.comparing(Function.identity())).get();
            case 3 -> packet.subPackets.stream().map(this::calculatePacket).max(Comparator.comparing(Function.identity())).get();
            case 4 -> packet.literalVal;
            case 5 -> calculatePacket(packet.subPackets.get(0)) > calculatePacket(packet.subPackets.get(1)) ? 1 : 0;
            case 6 -> calculatePacket(packet.subPackets.get(0)) < calculatePacket(packet.subPackets.get(1)) ? 1 : 0;
            case 7 -> calculatePacket(packet.subPackets.get(0)) == calculatePacket(packet.subPackets.get(1)) ? 1 : 0;
            default -> throw new IllegalStateException("Unexpected value: " + (int) packet.type);
        };
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
        String str;
        List<Packet> packets = new ArrayList<>();
    }

    static class Result implements PuzzleResult {
        int versionSum;
        long evaluatedResult;

        @Override
        public String toString() {
            return "Result: " + (evaluatedResult == 0 ? versionSum : evaluatedResult);
        }
    }

    static class Packet {
        long version;
        long type;
        long lengthType;
        long literalVal;
        List<Packet> subPackets = new ArrayList<>();

        public Packet(long version, long type, long lengthType) {
            this.version = version;
            this.type = type;
            this.lengthType = lengthType;
        }
    }


    static int resolveBits(String bits, List<Packet> packets, Result result, boolean isNotForLengthType1) {
        if (bits.isBlank() || allZeros(bits)) {
            return 0;
        }

        Packet packet = new Packet(toDecimal(bits.substring(0, 3)), toDecimal(bits.substring(3, 6)), toDecimal(bits.substring(6, 7)));

        result.versionSum += packet.version;
        int resolvedIndex = 7;

        if (packet.type != 4) {
            //operator
            if (packet.lengthType == 0) {
                long totalSubPacketBits = toDecimal(bits.substring(resolvedIndex, resolvedIndex + 15));
                resolvedIndex += 15;
                resolveBits(bits.substring(resolvedIndex, (int) (resolvedIndex + totalSubPacketBits)), packet.subPackets, result, true);
                resolvedIndex += totalSubPacketBits;
            } else if (packet.lengthType == 1) {
                long totalSubPackets = toDecimal(bits.substring(resolvedIndex, resolvedIndex + 11));
                resolvedIndex += 11;
                int totalBitsDone = 0;
                for (int i = 0; i < totalSubPackets; i++) {
                    totalBitsDone += resolveBits(bits.substring(resolvedIndex + totalBitsDone), packet.subPackets, result, false);
                }
                resolvedIndex += totalBitsDone;
            }
        } else {
            //literals packet
            int literalResolvedIndex = resolvedIndex - 1;
            String literalBits;
            StringBuilder literalBuilder = new StringBuilder();
            do {
                literalBits = bits.substring(literalResolvedIndex, literalResolvedIndex + 5);
                literalBuilder.append(bits, literalResolvedIndex + 1, literalResolvedIndex + 5);
                literalResolvedIndex += 5;
            } while (literalBits.charAt(0) == '1');

            packet.literalVal = toDecimal(literalBuilder.toString());

            resolvedIndex = literalResolvedIndex;
        }

        packets.add(packet);
        if (isNotForLengthType1) {
            resolveBits(bits.substring(resolvedIndex), packets, result, true);
        }
        return resolvedIndex;
    }

    static boolean allZeros(String bits) {
        return bits.matches("[0]+");
    }

    static long toDecimal(String bits) {
        return Long.parseLong(bits, 2);
    }
}
