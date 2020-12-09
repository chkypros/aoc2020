package com.github.chkypros.aoc2020.day9;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class EncodingError extends SolutionTemplate {
    private final static int PREAMBLE_SIZE = 25;

    public static void main(String[] args) throws Exception {
        new EncodingError().solve(9);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<Long> numbers = stream.map(Long::parseLong).collect(Collectors.toList());

        Long invalidNumber = -1L;
        for (int i = PREAMBLE_SIZE; i < numbers.size(); i++) {
            if (!isSumOfPrevious(i, numbers)) {
                invalidNumber = numbers.get(i);
                break;
            }
        }

        return invalidNumber;
    }

    private boolean isSumOfPrevious(int index, List<Long> numbers) {
        for (int i = index - PREAMBLE_SIZE; i < index - 1; i++) {
            for (int j = i + 1; j < index; j++) {
                if (numbers.get(index) == numbers.get(i) + numbers.get(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> numberStrings = stream.collect(Collectors.toList());
        final Long invalidNumber = solvePartOne(numberStrings.stream());
        final List<Long> numbers = numberStrings.stream().map(Long::parseLong).collect(Collectors.toList());

        Long encryptionWeakness = null;
        int start = 0;
        int end = 1;

        while (end < numbers.size()) {
            Long sum = calculateContiguousSetSum(numbers, start, end);
            if (sum.equals(invalidNumber)) {
                break;
            } else if (sum < invalidNumber) {
                end++;
            } else {
                start++;
            }
        }

        encryptionWeakness = addMinAndMaxFromSubList(numbers, start, end);

        return encryptionWeakness;
    }

    private Long calculateContiguousSetSum(List<Long> numbers, int start, int end) {
        return numbers.subList(start, end + 1).stream().mapToLong(v -> v).sum();
    }

    private Long addMinAndMaxFromSubList(List<Long> numbers, int start, int end) {
        final List<Long> sortedNumbers = numbers.subList(start, end + 1).stream().sorted().collect(Collectors.toList());
        return sortedNumbers.get(0) + sortedNumbers.get(sortedNumbers.size() - 1);
    }
}
