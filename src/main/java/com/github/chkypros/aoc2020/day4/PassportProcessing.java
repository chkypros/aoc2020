package com.github.chkypros.aoc2020.day4;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class PassportProcessing extends SolutionTemplate {
    private static final Set<String> mandatoryFields = Set.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");
    private static final Map<String, Pattern> validators = new HashMap<>();

    static {
        /* Validations
         * byr (Birth Year) - four digits; at least 1920 and at most 2002.
         * iyr (Issue Year) - four digits; at least 2010 and at most 2020.
         * eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
         * hgt (Height) - a number followed by either cm or in:
         * If cm, the number must be at least 150 and at most 193.
         * If in, the number must be at least 59 and at most 76.
         * hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
         * ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
         * pid (Passport ID) - a nine-digit number, including leading zeroes.
         */
        validators.put("byr", Pattern.compile("^((19[2-9][0-9])|(200[0-2]))$"));
        validators.put("iyr", Pattern.compile("^20((1[0-9])|(20))$"));
        validators.put("eyr", Pattern.compile("^20((2[0-9])|(30))$"));
        validators.put("hgt", Pattern.compile("^((1(([5-8][0-9])|(9[0-3]))cm)|(((59)|(6[0-9])|(7[0-6]))in))$"));
        validators.put("hcl", Pattern.compile("^#[0-9a-f]{6}$"));
        validators.put("ecl", Pattern.compile("^(amb|blu|brn|gry|grn|hzl|oth)$"));
        validators.put("pid", Pattern.compile("^[0-9]{9}$"));
    }

    public static void main(String[] args) throws Exception {
        new PassportProcessing().solve(4);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());
        List<Batch> batchList = getBatches(lines);

        return batchList.stream().filter(b -> b.fields.keySet().containsAll(mandatoryFields)).count();
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());
        List<Batch> batchList = getBatches(lines);

        return batchList.stream().filter(this::isBatchValid).count();
    }

    private boolean isBatchValid(Batch batch) {
        if (!batch.fields.keySet().containsAll(mandatoryFields)) {
            return false;
        }

        for (String field : mandatoryFields) {
            final Pattern pattern = validators.get(field);
            if (null != pattern) {
                final Matcher matcher = pattern.matcher(batch.fields.get(field));
                if (!matcher.find()) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Batch> getBatches(List<String> lines) {
        List<Batch> batchList = new ArrayList<>();

        Batch batch = new Batch();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                batchList.add(batch);
                batch = new Batch();
                continue;
            }

            String[] words = line.split("\\s+");
            for (String word : words) {
                final String[] wordParts = word.split(":");
                batch.fields.put(wordParts[0], wordParts[1]);
            }
        }
        batchList.add(batch);

        return batchList;
    }

    static class Batch {
        Map<String, String> fields = new HashMap<>();
    }
}
