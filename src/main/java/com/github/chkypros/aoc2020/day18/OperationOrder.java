package com.github.chkypros.aoc2020.day18;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class OperationOrder extends SolutionTemplate {
    private static final Pattern PARENTHESIS_PATTERN = Pattern.compile("[()]");
    private static final Pattern ELEMENT_PATTERN = Pattern.compile("\\d+|[+*(]");

    public static void main(String[] args) throws Exception {
        new OperationOrder().solve(18);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> expressions = stream.collect(Collectors.toList());

        long sum = 0;
        for (String expression : expressions) {
            sum += evaluatePartOne(expression);
        }

        return sum;
    }

    private long evaluatePartOne(String expression) {
        long result = 0;
        int matcherIndex = 0;
        final Matcher matcher = ELEMENT_PATTERN.matcher(expression);
        Operation operation = Operation.ADD;
        while (matcher.find(matcherIndex)) {
            matcherIndex = switch (matcher.group()) {
                case "+" -> {
                    operation = Operation.ADD;
                    yield matcher.end();
                }
                case "*" -> {
                    operation = Operation.MULTIPLY;
                    yield matcher.end();
                }
                case "(" -> {
                    final int parenthesisIndex = matcher.start();
                    final int matchingParenthesisIndex = findMatchingParenthesis(expression, parenthesisIndex);
                    final long value = evaluatePartOne(expression.substring(parenthesisIndex + 1, matchingParenthesisIndex));
                    result = operation.applyAsLong(result, value);
                    yield matchingParenthesisIndex;
                }
                default -> {
                    final long value = Long.parseLong(matcher.group());
                    result = operation.applyAsLong(result, value);
                    yield matcher.end();
                }
            };
        }

        return result;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> expressions = stream.collect(Collectors.toList());

        long sum = 0;
        for (String expression : expressions) {
            sum += evaluatePartTwo(expression);
        }

        return sum;
    }

    private long evaluatePartTwo(String expression) {
        long result = 0;
        int matcherIndex = 0;
        Deque<Long> multiplicationStack = new ArrayDeque<>();
        final Matcher matcher = ELEMENT_PATTERN.matcher(expression);
        while (matcher.find(matcherIndex)) {
            matcherIndex = switch (matcher.group()) {
                case "+" -> {
                    yield matcher.end();
                }
                case "*" -> {
                    multiplicationStack.push(result);
                    result = 0L;
                    yield matcher.end();
                }
                case "(" -> {
                    final int parenthesisIndex = matcher.start();
                    final int matchingParenthesisIndex = findMatchingParenthesis(expression, parenthesisIndex);
                    final long value = evaluatePartTwo(expression.substring(parenthesisIndex + 1, matchingParenthesisIndex));
                    result = Operation.ADD.applyAsLong(result, value);
                    yield matchingParenthesisIndex;
                }
                default -> {
                    final long value = Long.parseLong(matcher.group());
                    result = Operation.ADD.applyAsLong(result, value);
                    yield matcher.end();
                }
            };
        }

        result = Operation.MULTIPLY.applyAsLong(result, multiplicationStack.stream().mapToLong(v -> v).reduce(1L, (a,b) -> a*b));

        return result;
    }


    private int findMatchingParenthesis(String expression, int startIndex) {
        int depth = 0;
        final Matcher matcher = PARENTHESIS_PATTERN.matcher(expression.substring(startIndex));
        while (matcher.find()) {
            final String parenthesis = matcher.group();
            depth += "(".equals(parenthesis) ? 1 : -1;

            if (0 == depth) {
                return matcher.start() + startIndex;
            }
        }

        return -1;
    }

    enum Operation implements LongBinaryOperator {
        ADD(Long::sum),
        MULTIPLY((a,b) -> a * b);

        private final LongBinaryOperator operator;
        Operation(LongBinaryOperator operator) { this.operator = operator; }
        @Override public long applyAsLong(long left, long right) { return operator.applyAsLong(left, right); }
    }
}
