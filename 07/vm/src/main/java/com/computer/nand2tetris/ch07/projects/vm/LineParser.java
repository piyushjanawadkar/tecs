package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * Created by jpiyush on 3/22/17.
 */
class LineParser {

  private static final String TERM_PUSH = "push";
  private static final String TERM_POP = "pop";
  private static final String TERM_FUNCTION = "function";
  private static final String TERM_FUNCTION_CALL = "call";
  private static final String TERM_LABEL = "label";
  private static final String TERM_GOTO = "goto";

  private static final ImmutableMap<String, ParsedLine.LineType> lineTypeByTerm =
      ImmutableMap.<String, ParsedLine.LineType>builder()
          .put(TERM_PUSH, ParsedLine.LineType.COMMAND_PUSH)
          .put(TERM_POP, ParsedLine.LineType.COMMAND_POP)
          .put("add", ParsedLine.LineType.COMMAND_ADD)
          .put("sub", ParsedLine.LineType.COMMAND_SUB)
          .put("neg", ParsedLine.LineType.COMMAND_NEG)
          .put("and", ParsedLine.LineType.COMMAND_AND)
          .put("or", ParsedLine.LineType.COMMAND_OR)
          .put("not", ParsedLine.LineType.COMMAND_NOT)
          .put("lt", ParsedLine.LineType.COMMAND_LT)
          .put("eq", ParsedLine.LineType.COMMAND_EQ)
          .put("gt", ParsedLine.LineType.COMMAND_GT)
          .put(TERM_FUNCTION, ParsedLine.LineType.FUNCTION_DEFINITION)
          .put("return", ParsedLine.LineType.FUNCTION_RETURN)
          .put("call", ParsedLine.LineType.FUNCTION_CALL)
          .put(TERM_LABEL, ParsedLine.LineType.LABEL)
          .put(TERM_GOTO, ParsedLine.LineType.GOTO)
          .build();

  private static final ImmutableMap<String, ParsedLocation.SegmentType> segmentTypeByTerm =
      ImmutableMap.<String, ParsedLocation.SegmentType>builder()
          .put("local", ParsedLocation.SegmentType.SEGMENT_LOCAL)
          .put("argument", ParsedLocation.SegmentType.SEGMENT_ARGUMENT)
          .put("this", ParsedLocation.SegmentType.SEGMENT_THIS)
          .put("that", ParsedLocation.SegmentType.SEGMENT_THAT)
          .put("pointer", ParsedLocation.SegmentType.SEGMENT_POINTER)
          .put("temp", ParsedLocation.SegmentType.SEGMENT_TEMP)
          .put("static", ParsedLocation.SegmentType.SEGMENT_STATIC)
          .put("constant", ParsedLocation.SegmentType.SEGMENT_CONSTANT)
          .build();

  private int nextIndex = 0;

  private static ParsedLocation createParsedLocation(String[] terms) {
    Preconditions.checkArgument(terms.length == 3, "Expected 3 terms. Found: %s", terms.toString());
    ParsedLocation.SegmentType segmentType = segmentTypeByTerm.get(terms[1]);
    int offset = Integer.parseInt(terms[2]);
    return ParsedLocation.create(segmentType, offset);
  }

  private static String[] split(String line) {
    return line.trim().split(" ");
  }

  private static String preprocess(String line) {
    return stripComment(line).replaceAll("\\s+", " ");
  }

  private static String stripComment(String line) {
    int commentIndex = line.indexOf("//");
    if (commentIndex < 0) {
      return line;
    }

    return line.substring(0, commentIndex);
  }

  private ParsedLine parseStatement(String line, String fileBaseName) {
    String[] terms = split(line);

    String firstTerm = terms[0];
    ParsedLine.LineType command = lineTypeByTerm.get(firstTerm);
    Preconditions.checkNotNull(command, "Unknown command \"%s\" in \"%s\"", firstTerm, line);

    Optional<ParsedLocation> parsedLocation = Optional.absent();
    if (firstTerm.equals(TERM_PUSH) || firstTerm.equals(TERM_POP)) {
      parsedLocation = Optional.of(createParsedLocation(terms));
    }

    Optional<ParsedFunctionParams> parsedFunctionParams = Optional.absent();
    if (firstTerm.equals(TERM_FUNCTION)) {
      parsedFunctionParams = Optional.of(createParsedFunctionParams(terms));
    } else if (firstTerm.equals(TERM_FUNCTION_CALL)) {
      parsedFunctionParams = Optional.of(createParsedFunctionCallParams(terms));
    }

    Optional<Label> label = Optional.absent();
    if (firstTerm.equals(TERM_LABEL) || firstTerm.equals(TERM_GOTO)) {
      label = Optional.of(Labels.labelOf(fileBaseName, terms[1]));
    }

    return ParsedLine
        .create(line, command, parsedLocation, nextIndex++, fileBaseName, parsedFunctionParams,
            label);
  }

  private ParsedFunctionParams createParsedFunctionCallParams(String[] terms) {
    Preconditions.checkArgument(terms.length == 3, "Expected 3 terms. Found: %s", terms.toString());
    Integer numArgs = Integer.valueOf(terms[2]);
    return ParsedFunctionParams.create(terms[1], Optional.of(numArgs), Optional.absent());
  }

  private ParsedFunctionParams createParsedFunctionParams(String[] terms) {
    Preconditions.checkArgument(terms.length == 3, "Expected 3 terms. Found: %s", terms.toString());
    Integer numLocalArgs = Integer.valueOf(terms[2]);
    return ParsedFunctionParams.create(terms[1], Optional.absent(), Optional.of(numLocalArgs));
  }

  ParsedLine parse(String line, String fileBaseName) {
    line = preprocess(line);

    if (line.isEmpty()) {
      return ParsedLine
          .create(line, ParsedLine.LineType.BLANK_LINE, Optional.absent(), nextIndex++,
              fileBaseName, Optional.absent(), Optional.absent());
    }

    return parseStatement(line, fileBaseName);
  }
}
