package com.computer.nandtetris.ch06.projects.asm;

public class SkippableLineParser implements InstructionParser {

    private final String line;

    public SkippableLineParser(String line) {
        this.line = line;
    }

    @Override
    public boolean matches() {
        return line.isEmpty();
    }

    @Override
    public ParsedComponents parse() {
        ParsedComponents components = new ParsedComponents();
        components.setLineType(ParsedComponents.LineType.SKIPPABLE_LINE);
        return components;
    }
}
