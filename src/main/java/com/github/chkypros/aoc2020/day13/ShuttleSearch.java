package com.github.chkypros.aoc2020.day13;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class ShuttleSearch extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new ShuttleSearch().solve(13);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());
        final int earliestTimestamp = Integer.parseInt(lines.get(0));
        final List<Long> shuttles = Arrays.stream(lines.get(1).split(","))
            .filter(s -> !"x".equals(s))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        long earliestShuttle = -1;
        long minimumWait = Long.MAX_VALUE;
        for (Long shuttle : shuttles) {
            final long wait = (earliestTimestamp / shuttle + 1) * shuttle - earliestTimestamp;
            if (wait < minimumWait) {
                minimumWait = wait;
                earliestShuttle = shuttle;
            }
        }

        return earliestShuttle * minimumWait;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> shuttles = stream.skip(1).flatMap(s -> Arrays.stream(s.split(","))).collect(Collectors.toList());

        return super.solvePartTwo(stream);
    }
}
