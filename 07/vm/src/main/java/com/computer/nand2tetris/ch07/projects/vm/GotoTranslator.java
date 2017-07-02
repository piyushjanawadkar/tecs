package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 6/29/17.
 */
public class GotoTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> GOTO_FORMAT = ImmutableList.of(
      "@%s",
      "0;JMP"
  );

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return AssemblySequenceFormatter.format(GOTO_FORMAT, Labels.gotoLabelOf(parsedLine).text());
  }
}
