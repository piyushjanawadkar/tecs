package com.computer.nand2tetris.ch07.projects.vm;

import com.computer.nand2tetris.ch07.projects.vm.ParsedLine.LineType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by jpiyush on 3/23/17.
 */
public class AssemblyTranslatorImpl implements AssemblyTranslator {

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

  private static final ImmutableList<String> PUSH_ASM_SEQUENCE =
      ImmutableList.of(
          "// push contents of D",
          "@SP",
          "A=M",
          "M=D",
          "@SP",
          "M=M+1"
      );

  private static final int RELATIONAL_OP_VALUE_TRUE = -1;
  private static final int RELATIONAL_OP_VALUE_FALSE = 0;

  private static final ImmutableMap<ParsedLine.LineType, AssemblyTranslator> generatorsByType =
      ImmutableMap.<ParsedLine.LineType, AssemblyTranslator>builder()
          .put(ParsedLine.LineType.BLANK_LINE, new EmitAssemblyTranslator(ImmutableList.of()))

          .put(ParsedLine.LineType.COMMAND_PUSH,
              new AssemblySequenceTranslator(new SavePushValueToDTranslator(),
                  new EmitAssemblyTranslator(PUSH_ASM_SEQUENCE)))
          .put(ParsedLine.LineType.COMMAND_POP,
              new AssemblySequenceTranslator(new SavePopValueToDAndAddressToATranslator(),
                  new EmitAssemblyTranslator(ImmutableList.of("M=D"))))

          .put(ParsedLine.LineType.COMMAND_ADD,
              new SubstituteValuesAssemblyTranslator(BINARY_OP_ASM_SEQUENCE, "+"))
          .put(ParsedLine.LineType.COMMAND_SUB,
              new SubstituteValuesAssemblyTranslator(BINARY_OP_ASM_SEQUENCE, "-"))
          .put(ParsedLine.LineType.COMMAND_NEG,
              new SubstituteValuesAssemblyTranslator(UNARY_OP_ASM_SEQUENCE, "-"))

          .put(ParsedLine.LineType.COMMAND_AND,
              new SubstituteValuesAssemblyTranslator(BINARY_OP_ASM_SEQUENCE, "&"))
          .put(ParsedLine.LineType.COMMAND_OR,
              new SubstituteValuesAssemblyTranslator(BINARY_OP_ASM_SEQUENCE, "|"))
          .put(ParsedLine.LineType.COMMAND_NOT,
              new SubstituteValuesAssemblyTranslator(UNARY_OP_ASM_SEQUENCE, "!"))

          .put(ParsedLine.LineType.COMMAND_LT,
              new RelationalOpAssemblyTranslator("JLT", RELATIONAL_OP_VALUE_TRUE,
                  RELATIONAL_OP_VALUE_FALSE))
          .put(ParsedLine.LineType.COMMAND_EQ,
              new RelationalOpAssemblyTranslator("JNE", RELATIONAL_OP_VALUE_FALSE,
                  RELATIONAL_OP_VALUE_TRUE))
          .put(ParsedLine.LineType.COMMAND_GT,
              new RelationalOpAssemblyTranslator("JGT", RELATIONAL_OP_VALUE_TRUE,
                  RELATIONAL_OP_VALUE_FALSE))

          .put(ParsedLine.LineType.FUNCTION_DEFINITION, new FunctionDeclarationTranslator())
          .put(LineType.FUNCTION_RETURN, new FunctionReturnTranslator())
          .build();

  private static ImmutableList<String> format(ParsedLine parsedLine,
      ImmutableList<String> sequence) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();

    if (!parsedLine.line().isEmpty()) {
      builder.add(String.format("// %s", parsedLine.line()));
    }
    builder.addAll(sequence);

    ImmutableList<String> commentQualifiedSequence = builder.build();

    if (commentQualifiedSequence.isEmpty()) {
      return commentQualifiedSequence;
    }

    builder.add(BLANK_ASM_LINE);
    return builder.build();
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    AssemblyTranslator generator = generatorsByType.get(parsedLine.type());
    Preconditions.checkNotNull(generator, "No generator found for %s", parsedLine.type());
    return format(parsedLine, generator.translate(parsedLine));
  }
}
