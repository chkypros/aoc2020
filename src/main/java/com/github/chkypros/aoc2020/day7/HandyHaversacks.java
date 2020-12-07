package com.github.chkypros.aoc2020.day7;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class HandyHaversacks extends SolutionTemplate {
    private static final Pattern CONTAINER_PATTERN = Pattern.compile("(?<container>.+) bags contain (?<contained>.+)\\.");
    private static final Pattern CONTAINED_PATTERN = Pattern.compile("(?<quantity>[0-9]+) (?<contained>.*) bag(s)?");

    public static void main(String[] args) throws Exception {
        new HandyHaversacks().solve(7);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        Map<String, Node> index = new HashMap<>();

        populateIndex(stream, index);

        return (long) findPossibleContainers(index.get("shiny gold")).size();
    }

    Set<String> findPossibleContainers(Node node) {
        Set<String> possibleContainers = new HashSet<>();
        node.containedIn.stream().map(v -> v.other.name).forEach(possibleContainers::add);
        node.containedIn.stream().map(v -> findPossibleContainers(v.other)).flatMap(Collection::stream).forEach(possibleContainers::add);
        return possibleContainers;
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        Map<String, Node> index = new HashMap<>();

        populateIndex(stream, index);

        return calculateContained(index.get("shiny gold"));
    }

    private Long calculateContained(Node node) {
        return node.contains.stream().mapToLong(v -> v.quantity * (1 + calculateContained(v.other))).sum();
    }

    private void populateIndex(Stream<String> stream, Map<String, Node> index) {
        final List<String> rules = stream.collect(Collectors.toList());
        for (String rule : rules) {
            final Matcher matcher = CONTAINER_PATTERN.matcher(rule);
            matcher.find();
            final String containerDesc = matcher.group("container");
            final String[] containedBags = matcher.group("contained").split(", ");

            final Node containerNode = index.computeIfAbsent(containerDesc, Node::new);
            for (String containedBag : containedBags) {
                final Matcher matcherContained = CONTAINED_PATTERN.matcher(containedBag);
                if (matcherContained.find()) {
                    final int quantity = Integer.parseInt(matcherContained.group("quantity"));
                    final String contained = matcherContained.group("contained");

                    final Node containedNode = index.computeIfAbsent(contained, Node::new);
                    containedNode.containedIn.add(new Vector(containerNode, quantity));
                    containerNode.contains.add(new Vector(containedNode, quantity));
                }
            }
        }
    }

    static class Node {
        String name;
        Set<Vector> containedIn = new HashSet<>();
        Set<Vector> contains = new HashSet<>();

        Node(String name) { this.name = name; }
    }

    static class Vector {
        Node other;
        int quantity;

        public Vector(Node other, int quantity) { this.other = other; this.quantity = quantity; }
    }
}
