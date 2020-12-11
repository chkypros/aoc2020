package com.github.chkypros.aoc2020.day10;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class AdapterArray extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new AdapterArray().solve(10);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<Integer> sortedAdapters = stream.map(Integer::parseInt).sorted().collect(Collectors.toList());

        long diff1 = 0;
        long diff3 = 1; // Take devices diff into consideration

        // Take outlet jolt of 0 into consideration
        sortedAdapters.add(0, 0);

        for(int i = 0; i < sortedAdapters.size() - 1; i++) {
            int diff = sortedAdapters.get(i + 1) - sortedAdapters.get(i);
            if (1 == diff) {
                diff1++;
            } else if (3 == diff) {
                diff3++;
            }
        }

        return diff1 * diff3;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<Integer> sortedAdapters = stream.map(Integer::parseInt).sorted().collect(Collectors.toList());

        final List<Integer> adapterDiffs = new ArrayList<>();
        adapterDiffs.add(sortedAdapters.get(0));
        for (int i = 1; i < sortedAdapters.size(); i++) {
            adapterDiffs.add(sortedAdapters.get(i) - sortedAdapters.get(i - 1));
        }
        adapterDiffs.add(0, 0); // Consider outlet
        adapterDiffs.add(3); // Consider device

        sortedAdapters.add(0, 0);
        sortedAdapters.add(sortedAdapters.get(sortedAdapters.size() - 1) + 3);

        final List<Range> optionalAdapterRanges = getOptionalAdapterRanges(adapterDiffs);

        return optionalAdapterRanges.stream()
            .map(range -> calculateSubArrangements(adapterDiffs, range))
            .reduce(1L, (a,b) -> a*b);
    }

    private List<Range> getOptionalAdapterRanges(List<Integer> adapterDiffs) {
        final List<Range> optionalAdapterRanges = new ArrayList<>();
        int start = -1;
        for (int i = 1; i < adapterDiffs.size() - 1; i++) {
            if (adapterDiffs.get(i) + adapterDiffs.get(i + 1) <= 3) {
                // Optional adapter
                if (-1 == start) {
                    start = i;
                }
            } else {
                if (-1 != start) {
                    optionalAdapterRanges.add(new Range(start, i));
                    start = -1;
                }
            }
        }
        return optionalAdapterRanges;
    }

    private Long calculateSubArrangements(List<Integer> adapterDiffs, Range range) {
        final long possibleArrangements = 1L << range.end - range.start;
        return LongStream.range(0, possibleArrangements)
            .mapToObj(inclusionMap  -> constructReducedAdapterDiffList(inclusionMap, adapterDiffs.subList(range.start, range.end + 1)))
            .filter(this::isValidArrangement)
            .count();
    }

    private List<Integer> constructReducedAdapterDiffList(long inclusionMap, List<Integer> adapterDiffs) {
        final ArrayList<Integer> reducedAdapterDiffsList = new ArrayList<>();

        int carryOver = 0;
        for (int i = 0; i < adapterDiffs.size() - 1; i++) {
            if (((1L << i) & inclusionMap) != 0) {
                reducedAdapterDiffsList.add(adapterDiffs.get(i) + carryOver);
                carryOver = 0;
            } else {
                carryOver += adapterDiffs.get(i);
            }
        }
        reducedAdapterDiffsList.add(adapterDiffs.get(adapterDiffs.size() - 1) + carryOver);

        return reducedAdapterDiffsList;
    }

    private boolean isValidArrangement(List<Integer> adapterDiffs) {
        return adapterDiffs.stream().noneMatch(d -> d > 3);
    }

    public static class Range {
        final int start;
        final int end;
        Range(int start, int end) { this.start = start; this.end = end; }
        @Override public String toString() { return "[" + start + "-" + end + ")"; }
    }
}
