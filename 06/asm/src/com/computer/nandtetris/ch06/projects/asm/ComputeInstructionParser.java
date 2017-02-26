package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComputeInstructionParser extends AbstractInstructionParser {

    private static final Pattern COMPUTE_INSTRUCTION_PATTERN =
            Pattern.compile("^([ADM]{1,3}=)?([^;]+)(;[a-zA-Z]{3,3})?$");

    private static final String DESTINATION_DELIMITER = "=";
    private static final String JUMP_SPEC_DELIMITER = ";";
    private final String line;


    ComputeInstructionParser(String line) {
        super(line, COMPUTE_INSTRUCTION_PATTERN, ParsedComponents.LineType.C_INSTRUCTION);
        this.line = line;
    }

    @Override
    protected ParsedComponents parseInternal(Matcher matcher) {
        ParsedComponents components = new ParsedComponents();

        Preconditions.checkArgument(matcher.groupCount() == 3, matcher.groupCount());

        if (matcher.group(1) != null) {
            components.setDestination(parseDestination(matcher.group(1)));
        }

        Preconditions.checkNotNull(matcher.group(2));
        components.setComputeSpec(Optional.of(matcher.group(2)));

        if (matcher.group(3) != null) {
            components.setJumpSpec(parseJumpSpec(matcher.group(3)));
        }

        return components;
    }

    private boolean hasDestinationDelimiter(String delimitedSpec) {
        return delimitedSpec.endsWith(DESTINATION_DELIMITER);
    }

    private Optional<String> parseDestination(String delimitedSpec) {
        Preconditions.checkArgument(hasDestinationDelimiter(delimitedSpec));
        return Optional.of(stripTrailingDelimiter(delimitedSpec, DESTINATION_DELIMITER));
    }

    private boolean hasJumpSpecDelimiter(String delimitedSpec) {
        return delimitedSpec.startsWith(JUMP_SPEC_DELIMITER);
    }

    private Optional<String> parseJumpSpec(String delimitedSpec) {
        Preconditions.checkArgument(hasJumpSpecDelimiter(delimitedSpec));
        return Optional.of(stripLeadingDelimiter(delimitedSpec, JUMP_SPEC_DELIMITER));
    }

    private String stripTrailingDelimiter(String delimitedSpec, String delimiter) {
        return delimitedSpec.substring(0, delimitedSpec.lastIndexOf(delimiter));
    }

    private String stripLeadingDelimiter(String delimitedSpec, String delimiter) {
        return delimitedSpec.substring(delimiter.length());
    }
}