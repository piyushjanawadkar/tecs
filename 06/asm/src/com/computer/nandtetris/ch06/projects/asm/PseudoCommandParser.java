package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PseudoCommandParser extends AbstractInstructionParser {

    private static final Pattern PSEUDO_COMMAND_PATTERN =
            Pattern.compile(String.format("[(](%s)[)]", SYMBOL_PATTERN));

    PseudoCommandParser(String line) {
        super(line, PSEUDO_COMMAND_PATTERN, ParsedComponents.LineType.PSEUDO_COMMAND);
    }

    @Override
    protected ParsedComponents parseInternal(Matcher matcher) {
        ParsedComponents components = new ParsedComponents();
        components.setSymbol(Optional.of(matcher.group(1)));
        return components;
    }
}
