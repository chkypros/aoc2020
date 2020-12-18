package com.github.chkypros.aoc2020.day17;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class ConwayCubes extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new ConwayCubes().solve(17);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        Map<Coordinates, Boolean> pocketSpace = new HashMap<>();
        final List<String> lines = stream.collect(Collectors.toList());
        for (int i = 0; i < lines.size(); i++) {
            final char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                final Coordinates coordinates = new Coordinates(Arrays.asList(j, i, 0));
                if ('#' == chars[j]) {
                    activate(pocketSpace, coordinates);
                } else {
                    deactivate(pocketSpace, coordinates);
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            pocketSpace = applyCycle(pocketSpace);
        }

        return pocketSpace.values().stream().filter(Boolean.TRUE::equals).count();
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        Map<Coordinates, Boolean> pocketSpace = new HashMap<>();
        final List<String> lines = stream.collect(Collectors.toList());
        for (int i = 0; i < lines.size(); i++) {
            final char[] chars = lines.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                final Coordinates coordinates = new Coordinates(Arrays.asList(j, i, 0, 0));
                if ('#' == chars[j]) {
                    activate(pocketSpace, coordinates);
                } else {
                    deactivate(pocketSpace, coordinates);
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            pocketSpace = applyCycle(pocketSpace);
        }

        return pocketSpace.values().stream().filter(Boolean.TRUE::equals).count();
    }

    private Map<Coordinates, Boolean> applyCycle(Map<Coordinates, Boolean> pocketSpace) {
        Map<Coordinates, Boolean> newPocketSpace = new HashMap<>();
        Set<Coordinates> additionalCoordinates = new HashSet<>();

        for (Map.Entry<Coordinates, Boolean> cube : pocketSpace.entrySet()) {
            updateCube(pocketSpace, newPocketSpace, additionalCoordinates, cube.getKey());
        }

        for (Coordinates coordinates : additionalCoordinates) {
            updateCube(pocketSpace, newPocketSpace, new HashSet<>(), coordinates);
        }
        return newPocketSpace;
    }

    private void updateCube(Map<Coordinates, Boolean> pocketSpace, Map<Coordinates, Boolean> newPocketSpace, Set<Coordinates> additionalCoordinates, Coordinates cube) {
        final List<Coordinates> neighbors = new ArrayList<>();
        findNeighbors(cube, new ArrayList<>(), neighbors);
        final long activeNeighbors = countNeighbors(pocketSpace, neighbors, true);

        final boolean shouldBeActive = activeNeighbors == 3 || (isActive(pocketSpace, cube) && activeNeighbors == 2);
        newPocketSpace.put(cube, shouldBeActive);

        if (isActive(pocketSpace, cube)) {
            neighbors.removeAll(pocketSpace.keySet());
            additionalCoordinates.addAll(neighbors);
        }
    }

    private long countNeighbors(Map<Coordinates, Boolean> pocketSpace, List<Coordinates> neighbors, boolean activeState) {
        return neighbors.stream().filter(c -> isActive(pocketSpace, c) == activeState).count();
    }

    private void findNeighbors(Coordinates coordinates, List<Integer> dim, List<Coordinates> neighbors) {
        if (dim.equals(coordinates.dim)) {
            return; // Cannot be a neighbor of itself
        }

        if (dim.size() == coordinates.dim.size()) {
            neighbors.add(new Coordinates(dim));
        } else {
            dim = new ArrayList<>(dim);
            dim.add(null);
            final int dimIndex = dim.size() - 1;
            for (int i = -1; i <= 1; i++) {
                dim.set(dimIndex, coordinates.dim.get(dimIndex) + i);
                findNeighbors(coordinates, dim, neighbors);
            }
        }
    }

    private boolean isActive(Map<Coordinates, Boolean> pocketSpace, Coordinates coordinates) {
        return pocketSpace.getOrDefault(coordinates, Boolean.FALSE);
    }

    private void activate(Map<Coordinates, Boolean> pocketSpace, Coordinates coordinates) {
        pocketSpace.put(coordinates, Boolean.TRUE);
    }

    private void deactivate(Map<Coordinates, Boolean> pocketSpace, Coordinates coordinates) {
        pocketSpace.put(coordinates, Boolean.FALSE);
    }

    private static class Coordinates {
        final List<Integer> dim;
        public Coordinates(List<Integer> dim) { this.dim = new ArrayList<>(dim); }
        @Override public int hashCode() { return Objects.hash(dim); }
        @Override public boolean equals(Object obj) { return obj instanceof Coordinates && hashCode() == obj.hashCode(); }
        @Override public String toString() { return dim.stream().map(Object::toString).collect(Collectors.joining(", ", "[", "]")); }
    }
}
