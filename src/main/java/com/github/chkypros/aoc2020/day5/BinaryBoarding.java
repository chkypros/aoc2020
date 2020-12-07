package com.github.chkypros.aoc2020.day5;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class BinaryBoarding extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new BinaryBoarding().solve(5);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        return stream.map(s -> s.replaceAll("F|L","0").replaceAll("B|R", "1"))
            .mapToLong(s -> Integer.parseInt(s, 2))
            .max()
            .getAsLong();
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<Integer> integerList = stream.map(s -> s.replaceAll("F|L", "0").replaceAll("B|R", "1"))
            .map(s -> Integer.parseInt(s, 2))
            .sorted()
            .collect(Collectors.toList());

        for (int i = 0; i < integerList.size() - 1; i++) {
            if (integerList.get(i + 1) > integerList.get(i) + 1) {
                return integerList.get(i) + 1L;
            }
        }
        return -1L;
    }
}
