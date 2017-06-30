package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 6/28/17.
 */
public class PushValueInDTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> PUSH_SEQUENCE_FORMAT = ImmutableList.of(
      "// push contents of D",
      "@SP",
      "A=M",
      "M=D",
      "@SP",
      "M%s=M+1"
  );

  private final boolean saveUpdatedSPInD;

  PushValueInDTranslator(boolean saveUpdatedSPInD) {
    this.saveUpdatedSPInD = saveUpdatedSPInD;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return AssemblySequenceFormatter.format(PUSH_SEQUENCE_FORMAT, saveUpdatedSPInD ? "D" : "");
  }
}
