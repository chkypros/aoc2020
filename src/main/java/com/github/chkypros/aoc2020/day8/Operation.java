package com.github.chkypros.aoc2020.day8;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public abstract class Operation {
    private static final Pattern PATTERN = Pattern.compile("(?<command>(acc|jmp|nop)) (?<argument>.+)");
    private static final Map<String, BiFunction<String, Integer, Operation>> PROVIDERS = new HashMap<>();
    private static final Map<String, UnaryOperator<Operation>> FLIPPERS = new HashMap<>();

    static {
        PROVIDERS.put("acc", Accumulator::new);
        PROVIDERS.put("jmp", Jump::new);
        PROVIDERS.put("nop", NoOperation::new);

        FLIPPERS.put("acc", o -> o);
        FLIPPERS.put("jmp", o -> new NoOperation(o.command, o.argument));
        FLIPPERS.put("nop", o -> new Jump(o.command, o.argument));
    }

    protected final String command;
    protected final int argument;

    private Operation(String command, int argument) {
        this.command = command;
        this.argument = argument;
    }

    public abstract int execute(ProcessingContext context);

    public static Operation of(String operationStr) {
        final Matcher matcher = PATTERN.matcher(operationStr);
        matcher.find();

        final String command = matcher.group("command");
        final int argument = Integer.parseInt(matcher.group("argument"));
        return PROVIDERS.get(command).apply(command, argument);
    }

    public static Operation flip(Operation operation) {
        return FLIPPERS.get(operation.command).apply(operation);
    }

    private static class Accumulator extends Operation{
        public Accumulator(String command, int argument) {
            super(command, argument);
        }

        @Override
        public int execute(ProcessingContext context) {
            context.setAccumulator(context.getAccumulator() + argument);
            return context.getCurrentLine() + 1;
        }
    }

    private static class Jump extends Operation{
        public Jump(String command, int argument) {
            super(command, argument);
        }

        @Override
        public int execute(ProcessingContext context) {
            return context.getCurrentLine() + argument;
        }
    }

    private static class NoOperation extends Operation{
        public NoOperation(String command, int argument) {
            super(command, argument);
        }

        @Override
        public int execute(ProcessingContext context) {
            return context.getCurrentLine() + 1;
        }
    }

}
