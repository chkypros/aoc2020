package com.github.chkypros.aoc2020.day6;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class CustomCustoms extends SolutionTemplate {
    public static void main(String[] args) throws Exception {
        new CustomCustoms().solve(6);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {

        final List<String> lines = stream.collect(Collectors.toList());
        List<GroupPartOne> groupPartOnes = getGroupsPartOne(lines);

        return groupPartOnes.stream().mapToLong(g -> g.yesCategories.size()).sum();
    }

    private List<GroupPartOne> getGroupsPartOne(List<String> lines) {
        List<GroupPartOne> groupPartOnes = new ArrayList<>();

        GroupPartOne groupPartOne = new GroupPartOne();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                groupPartOnes.add(groupPartOne);
                groupPartOne = new GroupPartOne();
                continue;
            }

            for (char category : line.toCharArray()) {
                groupPartOne.yesCategories.add(category);
            }
        }
        groupPartOnes.add(groupPartOne);

        return groupPartOnes;
    }

    static class GroupPartOne {
        Set<Character> yesCategories = new HashSet<>();
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> lines = stream.collect(Collectors.toList());

        List<GroupPartTwo> groupsPartTwo = getGroupsPartTwo(lines);

        return groupsPartTwo.stream().map(g -> g.individualAnswersList.stream().reduce((a,b) -> {
            Set<Character> result = new HashSet<>();
            result.addAll(a.yesCategories);
            result.retainAll(b.yesCategories);
            return new IndividualAnswers(result);
        })).mapToLong(i -> i.get().yesCategories.size())
            .sum();
    }

    private List<GroupPartTwo> getGroupsPartTwo(List<String> lines) {
        List<GroupPartTwo> groupsPartTwo = new ArrayList<>();

        GroupPartTwo groupPartTwo = new GroupPartTwo();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                groupsPartTwo.add(groupPartTwo);
                groupPartTwo = new GroupPartTwo();
                continue;
            }

            IndividualAnswers individualAnswers = new IndividualAnswers();
            for (char category : line.toCharArray()) {
                individualAnswers.yesCategories.add(category);
            }
            groupPartTwo.individualAnswersList.add(individualAnswers);
        }
        groupsPartTwo.add(groupPartTwo);

        return groupsPartTwo;
    }

    static class GroupPartTwo {
        List<IndividualAnswers> individualAnswersList = new ArrayList<>();
    }

    static class IndividualAnswers {
        Set<Character> yesCategories = new HashSet<>();

        public IndividualAnswers() { }

        public IndividualAnswers(Set<Character> yesCategories) {
            this.yesCategories = yesCategories;
        }
    }
}
