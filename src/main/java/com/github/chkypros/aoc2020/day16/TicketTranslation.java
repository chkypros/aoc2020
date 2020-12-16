package com.github.chkypros.aoc2020.day16;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
public class TicketTranslation extends SolutionTemplate {
    private static final Pattern FIELD_PATTERN = Pattern.compile("(?<name>.*): (?<min1>\\d+)-(?<max1>\\d+) or (?<min2>\\d+)-(?<max2>\\d+)");

    public static void main(String[] args) throws Exception {
        new TicketTranslation().solve(16);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());
        List<Field> fields = new ArrayList<>();

        parseFieldInfo(lines, fields);
        int i = fields.size() + 5; // Ignore empty lines and "your/nearby ticket" headers

        long errorRate = 0L;
        while (i < lines.size()) {
            final List<Integer> nearbyTicketValues = getTicketValues(lines.get(i));
            for (Integer value : nearbyTicketValues) {
                boolean isValid = false;
                for (Field field : fields) {
                    if (field.isValid(value)) {
                        isValid = true;
                        break;
                    }
                }
                if (!isValid) {
                    errorRate += value;
                }
            }

            i++;
        }

        return errorRate;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());
        List<Field> fields = new ArrayList<>();

        parseFieldInfo(lines, fields);
        int i = fields.size() + 2; // Ignore empty line and "your ticket" header
        final List<Integer> myTicketValues = getTicketValues(lines.get(i));

        i += 3; // Ignore empty line and "nearby tickets" header

        final List<List<Integer>> validTickets = lines.subList(i, lines.size()).stream()
            .map(this::getTicketValues)
            .filter(values -> isTicketValid(values, fields))
            .collect(Collectors.toList());

        Map<Integer, Field> fieldIndex = new HashMap<>();
        List<FieldPositioningMetadata> fieldIndexes = new ArrayList<>();
        findPossibleFieldIndexes(fields, validTickets, fieldIndexes, myTicketValues);
        fieldIndexes.sort(Comparator.comparingInt(fpm -> fpm.validColumns.size()));

        for (FieldPositioningMetadata fpm : fieldIndexes) {
            for (Integer column : fpm.validColumns) {
                if (!fieldIndex.containsKey(column)) {
                    fieldIndex.put(column, fpm.field);
                    break;
                }
            }
        }

        return fieldIndex.entrySet().stream()
            .filter(e -> e.getValue().name.startsWith("departure"))
            .mapToLong(e -> myTicketValues.get(e.getKey()))
            .reduce(1L, (a,b) -> a*b);
    }

    private void findPossibleFieldIndexes(List<Field> fields, List<List<Integer>> validTickets, List<FieldPositioningMetadata> fieldIndexes, List<Integer> myTicketValues) {
        for (Field field : fields) {
            List<Integer> validColumns = new ArrayList<>();
            columnLoop:
            for (int j = 0; j < fields.size(); j++) {
                if (!field.isValid(myTicketValues.get(j))) {
                    continue;
                }

                for (List<Integer> ticket : validTickets) {
                    if (!field.isValid(ticket.get(j))) {
                        continue columnLoop;
                    }
                }

                validColumns.add(j);
            }

            fieldIndexes.add(new FieldPositioningMetadata(field, validColumns));
        }
    }

    private boolean isTicketValid(List<Integer> values, List<Field> fields) {
        for (Integer value : values) {
            boolean isValid = false;
            for (Field field : fields) {
                if (field.isValid(value)) {
                    isValid = true;
                    break;
                }
            }

            if (!isValid) {
                return false;
            }
        }

        return true;
    }

    private List<Integer> getTicketValues(String ticketValues) {
        return Arrays
            .stream(ticketValues.split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    private void parseFieldInfo(List<String> lines, List<Field> fields) {
        Matcher matcher = FIELD_PATTERN.matcher(lines.get(0));
        matcher.find();

        int i = 0;
        do {
            final Field field = new Field(
                matcher.group("name"),
                new Range(Integer.parseInt(matcher.group("min1")), Integer.parseInt(matcher.group("max1"))),
                new Range(Integer.parseInt(matcher.group("min2")), Integer.parseInt(matcher.group("max2")))
            );
            fields.add(field);

            matcher = FIELD_PATTERN.matcher(lines.get(++i));
        } while (matcher.find());
    }

    static class FieldPositioningMetadata {
        final Field field;
        final List<Integer> validColumns;
        public FieldPositioningMetadata(Field field, List<Integer> validColumns) { this.field = field; this.validColumns = validColumns; }
    }

    static class Field {
        final String name;
        final Range lowRange;
        final Range highRange;
        public Field(String name, Range lowRange, Range highRange) { this.name = name; this.lowRange = lowRange; this.highRange = highRange; }
        boolean isValid(int value) { return lowRange.contains(value) || highRange.contains(value); }
    }

    static class Range {
        final int start;
        final int end;
        Range(int start, int end) { this.start = start; this.end = end; }
        boolean contains(int value) { return start <= value && value <= end; }
        @Override public String toString() { return "[" + start + "-" + end + "]"; }
    }
}
