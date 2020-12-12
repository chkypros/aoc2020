package com.github.chkypros.aoc2020.day11;

import com.github.chkypros.aoc2020.SolutionTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
public class SeatingSystem extends SolutionTemplate {
    private static final Map<String, Set<Coordinates>> NEIGHBOR_COORDINATE_DELTAS = new HashMap<>();

    static {
        /*
         *  Break layout into 9 areas:
         *  U_L | U_C | U_R
         *  ---------------
         *  M_L | M_C | M_R
         *  ---------------
         *  B_L | B_C | B_R
         */
        NEIGHBOR_COORDINATE_DELTAS.put("U_L", Set.of(
            Coordinates.of(0, 1),
            Coordinates.of(1, 0),
            Coordinates.of(1, 1)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("U_C", Set.of(
            Coordinates.of(0, -1),
            Coordinates.of(0, 1),
            Coordinates.of(1, -1),
            Coordinates.of(1, 0),
            Coordinates.of(1, 1)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("U_R", Set.of(
            Coordinates.of(0, -1),
            Coordinates.of(1, -1),
            Coordinates.of(1, 0)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("M_L", Set.of(
            Coordinates.of(-1, 0),
            Coordinates.of(-1, 1),
            Coordinates.of(0, 1),
            Coordinates.of(1, 0),
            Coordinates.of(1, 1)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("M_C", Set.of(
            Coordinates.of(-1, -1),
            Coordinates.of(-1, 0),
            Coordinates.of(-1, 1),
            Coordinates.of(0, -1),
            Coordinates.of(0, 1),
            Coordinates.of(1, -1),
            Coordinates.of(1, 0),
            Coordinates.of(1, 1)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("M_R", Set.of(
            Coordinates.of(-1, -1),
            Coordinates.of(-1, 0),
            Coordinates.of(0, -1),
            Coordinates.of(1, -1),
            Coordinates.of(1, 0)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("B_L", Set.of(
            Coordinates.of(-1, 0),
            Coordinates.of(-1, 1),
            Coordinates.of(0, 1)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("B_C", Set.of(
            Coordinates.of(-1, -1),
            Coordinates.of(-1, 0),
            Coordinates.of(-1, 1),
            Coordinates.of(0, -1),
            Coordinates.of(0, 1)
        ));
        NEIGHBOR_COORDINATE_DELTAS.put("B_R", Set.of(
            Coordinates.of(-1, -1),
            Coordinates.of(-1, 0),
            Coordinates.of(0, -1)
        ));
    }

    public static void main(String[] args) throws Exception {
        new SeatingSystem().solve(11);
    }

    @Override
    protected Long solvePartOne(Stream<String> stream) throws Exception {
        final List<String> layout = stream.collect(Collectors.toList());
        return countOccupiedAtEquilibrium(layout, 4, Optional.of(1));
    }

    @Override
    protected Long solvePartTwo(Stream<String> stream) throws Exception {
        final List<String> layout = stream.collect(Collectors.toList());
        return countOccupiedAtEquilibrium(layout, 5, Optional.empty());
    }

    private Long countOccupiedAtEquilibrium(List<String> layout, int maxAdjacentOccupied, Optional<Integer> limit) {
        boolean cellsChanged;
        do {
            Set<Coordinates> changingSeats = new HashSet<>();
            for (int i = 0; i < layout.size(); i++) {
                for (int j = 0; j < layout.get(i).length(); j++) {
                    Coordinates coordinates = Coordinates.of(i, j);
                    if (checkSeatChange(layout, maxAdjacentOccupied, limit, i, j, coordinates)) {
                        changingSeats.add(coordinates);
                    }
                }
            }

            cellsChanged = !changingSeats.isEmpty();

            for (Coordinates coordinates : changingSeats) {
                char newCharacter = (layout.get(coordinates.row).charAt(coordinates.column) == 'L') ? '#' : 'L';
                StringBuilder newRow = new StringBuilder(layout.get(coordinates.row));
                newRow.setCharAt(coordinates.column, newCharacter);
                layout.set(coordinates.row, newRow.toString());
            }
        } while (cellsChanged);

        return layout.stream()
            .flatMap(r -> Arrays.stream(r.split("")))
            .filter("#"::equals)
            .count();
    }

    private boolean checkSeatChange(List<String> layout, int maxAdjacentOccupied, Optional<Integer> limit, int i, int j, Coordinates coordinates) {
        return layout.get(i).charAt(j) == 'L' && checkAllAdjacent(layout, coordinates, c -> c == '.' || c == 'L', limit)
            || layout.get(i).charAt(j) == '#' && countAdjacent(layout, coordinates, c -> c == '#', limit) >= maxAdjacentOccupied;
    }

    /*
     *  Break layout into 9 areas:
     *  U_L | U_C | U_R
     *  ---------------
     *  M_L | M_C | M_R
     *  ---------------
     *  B_L | B_C | B_R
     */
    private boolean checkAllAdjacent(List<String> layout, Coordinates coordinates, Predicate<Character> predicate, Optional<Integer> limit) {
        char rowInd = getRowInd(layout, coordinates);
        char columnInd = getColumnInd(layout, coordinates);

        final Set<Coordinates> coordinatesDeltas = NEIGHBOR_COORDINATE_DELTAS.get(rowInd + "_" + columnInd);

        return coordinatesDeltas.stream()
            .map(cd -> getAdjacentChar(layout, coordinates, cd, limit))
            .allMatch(predicate);
    }

    private long countAdjacent(List<String> layout, Coordinates coordinates, Predicate<Character> predicate, Optional<Integer> limit) {
        char rowInd = getRowInd(layout, coordinates);
        char columnInd = getColumnInd(layout, coordinates);

        final Set<Coordinates> coordinatesDeltas = NEIGHBOR_COORDINATE_DELTAS.get(rowInd + "_" + columnInd);

        return coordinatesDeltas.stream()
            .map(cd -> getAdjacentChar(layout, coordinates, cd, limit))
            .filter(predicate)
            .count();
    }

    private char getAdjacentChar(List<String> layout, Coordinates coordinates, Coordinates cd, Optional<Integer> limit) {
        int i = 1;
        while ((limit.isEmpty() || i <= limit.get())
            && validCoordinates(layout, coordinates.row + i * cd.row, coordinates.column + i * cd.column)) {
            final char adjecentChar = layout.get(coordinates.row + i * cd.row).charAt(coordinates.column + i * cd.column);
            if (adjecentChar != '.') {
                return adjecentChar;
            }
            i++;
        }

        return '.';
    }

    private boolean validCoordinates(List<String> layout, int row, int column) {
        return row >= 0 && row < layout.size()
            && column >= 0 && column <  layout.get(0).length();
    }

    private char getColumnInd(List<String> layout, Coordinates coordinates) {
        char columnInd = 'C';
        if (coordinates.column == 0) { columnInd = 'L'; }
        else if (coordinates.column == layout.get(0).length() - 1) { columnInd = 'R'; }
        return columnInd;
    }

    private char getRowInd(List<String> layout, Coordinates coordinates) {
        char rowInd = 'M';
        if (coordinates.row == 0) { rowInd = 'U'; }
        else if (coordinates.row == layout.size() - 1) { rowInd = 'B'; }
        return rowInd;
    }

    private static class Coordinates {
        final int row;
        final int column;
        Coordinates(int row, int column) { this.row = row; this.column = column; }
        static Coordinates of(int row, int column) { return new Coordinates(row, column); }
        @Override public String toString() { return "[" + row + "," + column + "]"; }
    }
}
