package com.github.chkypros.aoc2020.day3;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class TobogganTrajectory extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new TobogganTrajectory().solve(3);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());

        long treesEncountered = 0;
        int lineLength = lines.get(0).length();
        for (int i = 1; i < lines.size(); i++) {
            int charIndex = (i * 3) % lineLength;
            if ('#' == lines.get(i).charAt(charIndex)) {
                treesEncountered++;
            }
        }

        return treesEncountered;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        class Pair { int first, second; Pair(int f, int s) {first = f; second = s;} public String toString() {return first + "," + second; } }
        Map<Pair, Long> treesEncountered = new HashMap<>();
        treesEncountered.put(new Pair(1, 1), 0L);
        treesEncountered.put(new Pair(3, 1), 0L);
        treesEncountered.put(new Pair(5, 1), 0L);
        treesEncountered.put(new Pair(7, 1), 0L);
        treesEncountered.put(new Pair(1, 2), 0L);

        final List<String> lines = stream.collect(Collectors.toList());

        int lineLength = lines.get(0).length();
        for (int i = 1; i < lines.size(); i++) {
            for (Map.Entry<Pair, Long> e : treesEncountered.entrySet()) {
                int charIndex = ((i / e.getKey().second) * e.getKey().first) % lineLength;
                if (i % e.getKey().second == 0 && '#' == lines.get(i).charAt(charIndex)) {
                    e.setValue(e.getValue() + 1);
                }
            }
        }

        return treesEncountered.values().stream().reduce(1L, (x, y) -> x * y);
    }
}
