package com.github.chkypros.aoc2020;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public abstract class SolutionTemplate {
    public void solve(int day) throws Exception {
        final String resource = "day" + day + "/aoc-day-" + day + "-input.txt";
        final Path inputFile = Paths.get(ClassLoader.getSystemResource(resource).getPath());
        Optional.ofNullable(solvePartOne(inputFile))
            .ifPresent(partOneAnswer -> System.out.println("partOneAnswer = " + partOneAnswer));

        Optional.ofNullable(solvePartTwo(inputFile))
            .ifPresent(partTwoAnswer -> System.out.println("partTwoAnswer = " + partTwoAnswer));

        try (Stream<String> stream = Files.lines(inputFile)) {
            Optional.ofNullable(solvePartOne(stream))
                .ifPresent(partOneAnswer -> System.out.println("partOneAnswer = " + partOneAnswer));
        }

        try (Stream<String> stream = Files.lines(inputFile)) {
            Optional.ofNullable(solvePartTwo(stream))
                .ifPresent(partTwoAnswer -> System.out.println("partTwoAnswer = " + partTwoAnswer));
        }
    }

    protected Long solvePartOne(Path inputFile) throws Exception {
        return null;
    }

    protected Long solvePartOne(Stream<String> stream) throws Exception {
        return null;
    }

    protected Long solvePartTwo(Path inputFile) throws Exception {
        return null;
    }

    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        return null;
    }
}
