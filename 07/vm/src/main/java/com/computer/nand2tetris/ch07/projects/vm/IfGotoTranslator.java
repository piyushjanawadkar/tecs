package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 6/30/17.
 */
public class IfGotoTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> IF_GOTO_FORMAT = ImmutableList.of(
      "@SP",
      "A=M-1",
      "D=M",
      "@SP",
      "M=M-1",
      "@%s",
      "D;JNE"
  );

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return AssemblySequenceFormatter
        .format(IF_GOTO_FORMAT, Labels.gotoLabelOf(parsedLine).text());
  }
}
