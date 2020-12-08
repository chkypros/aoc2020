package com.github.chkypros.aoc2020.day8;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class HandheldHalting extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new HandheldHalting().solve(8);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<Operation> operations = stream.map(Operation::of).collect(Collectors.toList());

        // Luckily our input is small so we can do this
        boolean[] visited = new boolean[operations.size()];

        final ProcessingContext processingContext = new ProcessingContext();

        while (processingContext.getCurrentLine() < operations.size()) {
            if (visited[processingContext.getCurrentLine()]) {
                break;
            }
            visited[processingContext.getCurrentLine()] = true;

            final int nextLine = operations.get(processingContext.getCurrentLine()).execute(processingContext);
            processingContext.setCurrentLine(nextLine);
        }

        return processingContext.getAccumulator();
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<Operation> operations = stream.map(Operation::of).collect(Collectors.toList());

        boolean[] visited = new boolean[operations.size()];
        boolean[] tried = new boolean[operations.size()];
        boolean flipped = false;

        final ProcessingContext processingContext = new ProcessingContext();

        while (processingContext.getCurrentLine() < operations.size()) {
            if (visited[processingContext.getCurrentLine()]) {
                processingContext.setCurrentLine(0);
                processingContext.setAccumulator(0);
                visited = new boolean[operations.size()];
                flipped = false;
                continue;
            }
            visited[processingContext.getCurrentLine()] = true;

            Operation operation = operations.get(processingContext.getCurrentLine());
            if (!flipped && !tried[processingContext.getCurrentLine()]) {
                Operation newOperation = Operation.flip(operation);
                if (operation != newOperation) {
                    operation = newOperation;
                    tried[processingContext.getCurrentLine()] = true;
                    flipped = true;
                }
            }

            final int nextLine = operation.execute(processingContext);
            processingContext.setCurrentLine(nextLine);
        }

        return processingContext.getAccumulator();
    }
}
