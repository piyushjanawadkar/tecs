package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;
import com.google.common.base.Verify;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

class ParsedLine {

    private static final int INVALID_INDEX = -1;

    private String line = null;
    private int index = INVALID_INDEX;
    private ParsedComponents components = new ParsedComponents();

    private static final ImmutableSet<ParsedComponents.LineType> NON_ASSEMBLED_LINE_TYPES =
            ImmutableSet.of(
                    ParsedComponents.LineType.SKIPPABLE_LINE,
                    ParsedComponents.LineType.PSEUDO_COMMAND);  // bug #1: omitted this.

    ParsedLine(String inputLine, LineCounter counter) {
        line = preProcess(inputLine);

        InstructionParser parser = createInstructionParser(line);
        components = parser.parse();

        if (!NON_ASSEMBLED_LINE_TYPES.contains(getType())) {
            index = counter.get();
        }
    }

    private static InstructionParser createInstructionParser(String line) {
        ImmutableList<InstructionParser> parsers = createInstructionParsers(line);
        Optional<InstructionParser> parser =
                Optional.fromJavaUtil(parsers.stream().filter(p -> p.matches()).findFirst());
        Verify.verify(parser.isPresent());
        return parser.get();
    }

    private static ImmutableList<InstructionParser> createInstructionParsers(String line) {
        return ImmutableList.of(
                new SkippableLineParser(line),
                new AddressInstructionParser(line),
                new PseudoCommandParser(line),
                new ComputeInstructionParser(line));
    }

    private static String preProcess(String line) {
        line = line.replaceAll("\\s+", "");
        int commentIndex = line.indexOf("//");
        if (commentIndex >= 0) {
            return line.substring(0, commentIndex);
        }

        return line;
    }

    Integer getIndex() {
        return new Integer(index);
    }

    Optional<String> getSymbol() {
        return components.getSymbol();
    }

    ParsedComponents.LineType getType() {
        return components.getLineType();
    }

    public Optional<Integer> getAddress() {
        return components.getAddress();
    }

    public Optional<String> getDestination() {
        return components.getDestination();
    }

    public Optional<String> getComputeSpec() {
        return components.getComputeSpec();
    }

    public Optional<String> getJumpSpec() {
        return components.getJumpSpec();
    }

    public String toString() {
        return String.format("line: %s, components: %s", line, components.toString());
    }
}
