package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/23/17.
 */
public class ASMTranslatorImpl implements ASMTranslator {

  private static final LocationASMTranslator LOCATION_ASM_GENERATOR = new LocationASMTranslator();

  private static final String BLANK_ASM_LINE = "";

  private static final ImmutableList<String> BINARY_OP_ASM_SEQUENCE =
      ImmutableList.of(
          "@SP     // seek to and copy output",
          "A=M-1",
          "D=M",

          "A=A-1   // D has popped element, A points to top",

          "M=M%sD   // M has the output",

          "D=A+1   // update SP",
          "@SP",
          "M=D"
      );

  private static final ImmutableList<String> UNARY_OP_ASM_SEQUENCE =
      ImmutableList.of(
          "@SP",
          "A=M-1",
          "M=%sM"
      );

  private static final String RELATIONAL_OP_VALUE_TRUE = "-1";
  private static final String RELATIONAL_OP_VALUE_FALSE = "0";

  private static final ImmutableMap<ParsedLine.LineType, ASMTranslator> generatorsByType =
      ImmutableMap.<ParsedLine.LineType, ASMTranslator>builder()
          .put(ParsedLine.LineType.COMMAND_PUSH, new PushASMTranslator(LOCATION_ASM_GENERATOR))
          .put(ParsedLine.LineType.COMMAND_POP, new DummyASMTranslator())

          .put(ParsedLine.LineType.COMMAND_ADD,
              new SubstituteValuesASMTranslator(BINARY_OP_ASM_SEQUENCE, "+"))
          .put(ParsedLine.LineType.COMMAND_SUB,
              new SubstituteValuesASMTranslator(BINARY_OP_ASM_SEQUENCE, "-"))
          .put(ParsedLine.LineType.COMMAND_NEG,
              new SubstituteValuesASMTranslator(UNARY_OP_ASM_SEQUENCE, "-"))

          .put(ParsedLine.LineType.COMMAND_AND,
              new SubstituteValuesASMTranslator(BINARY_OP_ASM_SEQUENCE, "&"))
          .put(ParsedLine.LineType.COMMAND_OR,
              new SubstituteValuesASMTranslator(BINARY_OP_ASM_SEQUENCE, "|"))
          .put(ParsedLine.LineType.COMMAND_NOT,
              new SubstituteValuesASMTranslator(UNARY_OP_ASM_SEQUENCE, "!"))

          .put(ParsedLine.LineType.COMMAND_LT,
              new RelationalOpASMTranslator("JLT", RELATIONAL_OP_VALUE_TRUE,
                  RELATIONAL_OP_VALUE_FALSE))
          .put(ParsedLine.LineType.COMMAND_EQ,
              new RelationalOpASMTranslator("JNE", RELATIONAL_OP_VALUE_FALSE,
                  RELATIONAL_OP_VALUE_TRUE))
          .put(ParsedLine.LineType.COMMAND_GT,
              new RelationalOpASMTranslator("JGT", RELATIONAL_OP_VALUE_TRUE,
                  RELATIONAL_OP_VALUE_FALSE))
          .build();

  private static ImmutableList<String> format(ParsedLine parsedLine,
      ImmutableList<String> sequence) {
    String comment = String.format("// %s", parsedLine.line());
    return Streams.concat(
        Stream.of(comment),
        Streams.concat(
            sequence.stream(), Stream.of(BLANK_ASM_LINE)))
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    if (parsedLine.type().equals(ParsedLine.LineType.BLANK_LINE)) {
      return ImmutableList.of();
    }

    ASMTranslator generator = generatorsByType.get(parsedLine.type());
    Preconditions.checkNotNull(generator, "No generator found for %s", parsedLine.type());
    return format(parsedLine, generator.translate(parsedLine));
  }
}
