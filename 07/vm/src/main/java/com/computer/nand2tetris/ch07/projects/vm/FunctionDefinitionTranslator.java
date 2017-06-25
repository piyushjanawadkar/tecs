package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * Created by jpiyush on 6/25/17.
 */
public class FunctionDefinitionTranslator implements AssemblyTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();

    addFunctionName(parsedLine, builder);
    addLocalVariables(parsedLine, builder);

    return builder.build();
  }

  private void addFunctionName(ParsedLine parsedLine, Builder<String> builder) {
    System.err.println(parsedLine);
    builder.add(String.format("(%s)", parsedLine.function().get().name()));
  }

  private void addLocalVariables(ParsedLine parsedLine, Builder<String> builder) {
    // TODO(jpiyush): implement
  }
}
