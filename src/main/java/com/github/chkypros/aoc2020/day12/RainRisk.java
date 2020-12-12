package com.github.chkypros.aoc2020.day12;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class RainRisk extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new RainRisk().solve(12);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> instructions = stream.collect(Collectors.toList());
        long xPosition = 0;
        long yPosition = 0;

        Direction shipDirection = Direction.E;
        for (String instruction : instructions) {
            String action = instruction.substring(0, 1);
            int value = Integer.parseInt(instruction.substring(1));

            if ("L".equals(action) || "R".equals(action)) {
                if ("L".equals(action)) {
                    shipDirection = Direction.of((360 + shipDirection.degrees - value) % 360);
                } else {
                    shipDirection = Direction.of((shipDirection.degrees + value) % 360);
                }
            } else if ("F".equals(action)) {
                xPosition += shipDirection.xMultiplier * value;
                yPosition += shipDirection.yMultiplier * value;
            } else {
                final Direction direction = Direction.valueOf(action);
                xPosition += direction.xMultiplier * value;
                yPosition += direction.yMultiplier * value;
            }
        }

        return Math.abs(xPosition) + Math.abs(yPosition);
    }

    enum Direction {
        N(0, 1, 270),
        E(1, 0, 0),
        S(0, -1, 90),
        W(-1, 0, 180);

        long yMultiplier;
        long xMultiplier;
        int degrees;

        Direction(long yMultiplier, long xMultiplier, int degrees) {
            this.yMultiplier = yMultiplier;
            this.xMultiplier = xMultiplier;
            this.degrees = degrees;
        }

        static Direction of(int degrees) {
            for (Direction value : values()) {
                if (value.degrees == degrees) {
                    return value;
                }
            }

            throw new IllegalArgumentException("Only 0, 90, 180, 270 degrees are supported");
        }
    }
}
