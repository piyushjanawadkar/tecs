package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;
import com.google.common.base.Verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressInstructionParser extends AbstractInstructionParser {

    private static final Pattern ADDRESS_INSTRUCTION_PATTERN = Pattern.compile("^@(.+)");

    private final Pattern ADDRESS_CONSTANT_PATTERN = Pattern.compile("[0-9]+");

    AddressInstructionParser(String line) {
        super(line, ADDRESS_INSTRUCTION_PATTERN, ParsedComponents.LineType.A_INSTRUCTION);
    }

    @Override
    protected ParsedComponents parseInternal(Matcher matcher) {
        ParsedComponents components = new ParsedComponents();

        String value = matcher.group(1);

        if (ADDRESS_CONSTANT_PATTERN.matcher(value).matches()) {
            components.setAddress(Optional.of(Integer.parseInt(value)));
            return components;
        }

        Verify.verify(SYMBOL_PATTERN.matcher(value).matches());
        components.setSymbol(Optional.of(value));
        return components;
    }
}
