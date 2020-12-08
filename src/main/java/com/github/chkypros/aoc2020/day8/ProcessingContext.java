package com.github.chkypros.aoc2020.day8;

/**
 * @author <a href="mailto:kypros.chrysanthou@britebill.com">Kypros Chrysanthou</a>
 */
class ProcessingContext {
    private long accumulator = 0;
    private int currentLine = 0;

    public long getAccumulator() {
        return accumulator;
    }

    public void setAccumulator(long accumulator) {
        this.accumulator = accumulator;
    }

    public int getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }
}
