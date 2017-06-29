package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 6/25/17.
 */
public class FunctionDeclarationTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> LOCAL_VARIABLE_PUSH_BEGIN_SEQUENCE = ImmutableList.of(
      "// copy initial value to D",
      "@0",
      "D=A",
      "// point A to stack top",
      "@SP",
      "A=M"
  );

  private static final ImmutableList<String> LOCAL_VARIABLE_PUSH_SEQUENCE = ImmutableList.of(
      "M=D",
      "%s=A+1"
  );

  private static final ImmutableList<String> LOCAL_VARIABLE_PUSH_END_SEQUENCE = ImmutableList.of(
      "//save new stack top",
      "@SP",
      "M=D"
  );

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();

    ParsedFunctionParams parsedFunctionParams = parsedLine.function().get();
    translateFunctionName(parsedLine, builder);
    translateLocalVariables(parsedFunctionParams.numLocalArgs().get(), builder);

    return builder.build();
  }

  private void translateFunctionName(ParsedLine parsedLine, ImmutableList.Builder<String> builder) {
    Label label = Labels.functionNameLabelOf(parsedLine);
    builder.add(label.generateDefinitionText());
  }

  private void translateLocalVariables(int numLocalVars, ImmutableList.Builder<String> builder) {
    if (numLocalVars == 0) {
      return;
    }

    builder.addAll(LOCAL_VARIABLE_PUSH_BEGIN_SEQUENCE);
    builder.add("// push initialized local variables");
    for (int i = 0; i < numLocalVars - 1; i++) {
      pushLocalVariable(builder);
    }
    pushLastLocalVariable(builder);
    builder.addAll(LOCAL_VARIABLE_PUSH_END_SEQUENCE);
  }

  private void pushLocalVariable(ImmutableList.Builder<String> builder) {
    builder.addAll(AssemblySequenceFormatter.format(LOCAL_VARIABLE_PUSH_SEQUENCE, "A"));
  }

  private void pushLastLocalVariable(ImmutableList.Builder<String> builder) {
    builder.addAll(AssemblySequenceFormatter.format(LOCAL_VARIABLE_PUSH_SEQUENCE, "D"));
  }
}
