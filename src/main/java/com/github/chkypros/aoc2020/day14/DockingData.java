package com.github.chkypros.aoc2020.day14;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class DockingData extends SolutionTemplate {
    private static final Pattern MASK_PATTERN = Pattern.compile("mask = (?<mask>.*)");
    private static final Pattern MEM_PATTERN = Pattern.compile("mem\\[(?<index>.*)\\] = (?<value>.*)");

    public static void main(String[] args) throws Exception {
        new DockingData().solve(14);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> instructions = stream.collect(Collectors.toList());
        final Map<Integer, String> mem = new HashMap<>();
        String bitmask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        for (String instruction : instructions) {
            Matcher matcher = MASK_PATTERN.matcher(instruction);
            if (matcher.find()) {
                bitmask = matcher.group("mask");
            } else {
                matcher = MEM_PATTERN.matcher(instruction);
                matcher.find();
                final int index = Integer.parseInt(matcher.group("index"));
                String value = Integer.toBinaryString(Integer.parseInt(matcher.group("value")));
                value = "0".repeat(bitmask.length() - value.length()) + value;

                StringBuilder sb = new StringBuilder(value);
                for (int i = 0; i < bitmask.toCharArray().length; i++) {
                    if ('X' != bitmask.charAt(i)) {
                        sb.setCharAt(i, bitmask.charAt(i));
                    }
                }
                mem.put(index, sb.toString());
            }
        }
        return mem.values().stream().mapToLong(s -> Long.parseLong(s, 2)).sum();
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> instructions = stream.collect(Collectors.toList());
        final Map<String, Long> mem = new HashMap<>();
        String bitmask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

        for (String instruction : instructions) {
            Matcher matcher = MASK_PATTERN.matcher(instruction);
            if (matcher.find()) {
                bitmask = matcher.group("mask");
            } else {
                matcher = MEM_PATTERN.matcher(instruction);
                matcher.find();
                final long value = Long.parseLong(matcher.group("value"));
                String index = Integer.toBinaryString(Integer.parseInt(matcher.group("index")));
                index = "0".repeat(bitmask.length() - index.length()) + index;

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bitmask.toCharArray().length; i++) {
                    if ('0' == bitmask.charAt(i)) {
                        sb.append(index.charAt(i));
                    } else {
                        sb.append(bitmask.charAt(i));
                    }
                }
                writeValue(mem, sb.toString(), value);
            }
        }

        return mem.values().stream().mapToLong(s -> s).sum();
    }

    private void writeValue(Map<String, Long> mem, String indexMap, long value) {
        writeValue(mem, indexMap, value, 0);
    }

    private void writeValue(Map<String, Long> mem, String indexMap, long value, int depth) {
        if (indexMap.length() == depth) {
            mem.put(indexMap, value);
        } else if ('X' != indexMap.charAt(depth)) {
            writeValue(mem, indexMap, value, depth + 1);
        } else {
            StringBuilder sb = new StringBuilder(indexMap);
            sb.setCharAt(depth, '0');
            writeValue(mem, sb.toString(), value, depth + 1);
            sb.setCharAt(depth, '1');
            writeValue(mem, sb.toString(), value, depth + 1);
        }
    }
}
