package com.github.chkypros.aoc2020.day1;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class ReportRepair extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new ReportRepair().solve(1);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<Long> sortednNumbers = stream.map(Long::parseLong).collect(Collectors.toList());

        long product = -1;
        boolean productFound = false;
        for (int i = 0; i < sortednNumbers.size() - 1; i++) {
            for (int j = i + 1; j < sortednNumbers.size(); j++) {
                if (sortednNumbers.get(i) + sortednNumbers.get(j) == 2020) {
                    product = sortednNumbers.get(i) * sortednNumbers.get(j);
                    productFound = true;
                    break;
                }
            }

            if (productFound) break;
        }

        return product;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<Long> sortednNumbers = stream.map(Long::parseLong).collect(Collectors.toList());

        long product = -1;
        boolean productFound = false;
        for (int i = 0; i < sortednNumbers.size() - 2; i++) {
            for (int j = i + 1; j < sortednNumbers.size() - 1; j++) {
                for (int k = j + 1; k < sortednNumbers.size(); k++) {
                    if (sortednNumbers.get(i) + sortednNumbers.get(j) + sortednNumbers.get(k) == 2020) {
                        product = sortednNumbers.get(i) * sortednNumbers.get(j) * sortednNumbers.get(k);
                        productFound = true;
                        break;
                    }
                }

                if (productFound) break;
            }

            if (productFound) break;
        }

        return product;
    }
}
