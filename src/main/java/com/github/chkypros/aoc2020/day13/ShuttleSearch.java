package com.github.chkypros.aoc2020.day13;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayList;
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
        final List<String> shuttles = stream
            .skip(1)
            .flatMap(s -> Arrays.stream(s.split(",")))
            .collect(Collectors.toList());

        final List<Pair> shuttleInfo = new ArrayList<>();
        for (int i = 0; i < shuttles.size(); i++) {
            final String s = shuttles.get(i);
            if (!"x".equals(s)) {
                shuttleInfo.add(new Pair(Long.parseLong(s), i));
            }
        }

        // After manual analysis of the input, I noticed that the first shuttle (offset +0) is 19
        // also, on offset +19 there is a non-x shuttle, 383
        // Thus for the timestamp x, shuttle 383 will come at timestamp y = x+19 = 383*19*z
        // x = 383*19*z - 19 = 382*19*z
        // TODO ^^^ Still doesn't seem to be fast enough, need to find a way to calculate it
        long z = 102460223649L;
        final long baseProduct = 382 * 19;
        while (true) {
            if (checkTimestamp(shuttleInfo, baseProduct * z)) {
                break;
            }

            z++;
        }

        return super.solvePartTwo(stream);
    }

    private boolean checkTimestamp(List<Pair> shuttleInfo, long timestamp) {
        for (Pair pair : shuttleInfo) {
            if (0 != (timestamp - 19 + pair.second) % pair.first) {
                return false;
            }
        }

        return true;
    }

    public static class Pair {
        final long first;
        final int second;

        public Pair(long first, int second) { this.first = first; this.second = second; }
    }
}
