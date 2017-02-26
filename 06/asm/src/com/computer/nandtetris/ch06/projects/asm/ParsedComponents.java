package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;

class ParsedComponents {

    enum LineType {
        UNKNOWN_LINE_TYPE,
        SKIPPABLE_LINE,
        A_INSTRUCTION,
        C_INSTRUCTION,
        PSEUDO_COMMAND,
    }

    private LineType lineType = LineType.UNKNOWN_LINE_TYPE;

    private Optional<String> symbol = Optional.absent();

    private Optional<Integer> address = Optional.absent();

    private Optional<String> destination = Optional.absent();

    private Optional<String> computeSpec = Optional.absent();

    private Optional<String> jumpSpec = Optional.absent();

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public Optional<String> getSymbol() {
        return symbol;
    }

    public void setSymbol(Optional<String> symbol) {
        this.symbol = symbol;
    }

    public Optional<Integer> getAddress() {
        return address;
    }

    public void setAddress(Optional<Integer> address) {
        this.address = address;
    }

    public Optional<String> getDestination() {
        return destination;
    }

    public void setDestination(Optional<String> destination) {
        this.destination = destination;
    }

    public Optional<String> getComputeSpec() {
        return computeSpec;
    }

    public void setComputeSpec(Optional<String> computeSpec) {
        this.computeSpec = computeSpec;
    }

    public Optional<String> getJumpSpec() {
        return jumpSpec;
    }

    public void setJumpSpec(Optional<String> jumpSpec) {
        this.jumpSpec = jumpSpec;
    }

    public String toString() {
        return String.format("type=%s, symbol=%s, address=%s, destination=%s, compute=%s, jump=%s",
                getLineType(), getSymbol(), getAddress(), getDestination(), getComputeSpec(), getJumpSpec());
    }
}
