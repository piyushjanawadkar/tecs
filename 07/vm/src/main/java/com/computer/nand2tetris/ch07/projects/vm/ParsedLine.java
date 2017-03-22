package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;

/**
 * Created by jpiyush on 3/22/17.
 */
@AutoValue
abstract class ParsedLine {

    enum LineType {
        BLANK_LINE,
        COMMAND_PUSH,
        COMMAND_POP,
        COMMAND_ADD,
        COMMAND_SUB,
        COMMAND_NEG,
        COMMAND_AND,
        COMMAND_OR,
        COMMAND_NOT,
        COMMAND_LT,
        COMMAND_EQ,
        COMMAND_GT,
    }

    static ParsedLine create(String line, ParsedLine.LineType type, Optional<ParsedLocation> location) {
        return new AutoValue_ParsedLine(line, type, location);
    }

    abstract String line();
    abstract ParsedLine.LineType type();
    abstract Optional<ParsedLocation> location();

    public String toString() {
        return String.format("%s: { %s, %s}", line(), type(), location());
    }
}
