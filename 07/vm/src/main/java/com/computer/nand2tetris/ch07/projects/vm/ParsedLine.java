package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;

/**
 * Created by jpiyush on 3/22/17.
 */
@AutoValue
abstract class ParsedLine {

  static ParsedLine create(String line, ParsedLine.LineType type,
      Optional<ParsedLocation> location, int index, String fileBaseName) {
    return new AutoValue_ParsedLine(line, type, location, index, fileBaseName);
  }

  abstract String line();

  abstract ParsedLine.LineType type();

  abstract Optional<ParsedLocation> location();

  abstract int index();

  public abstract String fileBaseName();

  public String toString() {
    return String
        .format("%s:%d %s: { %s, %s}", fileBaseName(), index(), line(), type(), location());
  }

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
    COMMAND_GT, FUNCTION_DEFINITION, FUNCTION_RETURN,
  }
}
