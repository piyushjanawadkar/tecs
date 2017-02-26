package com.computer.nandtetris.ch06.projects.asm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class AbstractInstructionParser implements InstructionParser {

    public static final Pattern SYMBOL_PATTERN = Pattern.compile("[a-zA-Z_.$:][a-zA-Z_.$:0-9]*");

    private Matcher matcher;
    ParsedComponents.LineType lineType;

    AbstractInstructionParser(String line, Pattern pattern, ParsedComponents.LineType lineType) {
        matcher = pattern.matcher(line);
        this.lineType = lineType;
    }

    @Override
    public boolean matches() {
        return matcher.matches();
    }

    @Override
    public final ParsedComponents parse() {
        ParsedComponents components = parseInternal(matcher);
        components.setLineType(lineType);
        return components;
    }

    abstract protected ParsedComponents parseInternal(Matcher matcher);
}
