package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
public class SaveLocationTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> SAVE_LOCATION_ASM_SEQUENCE = ImmutableList.of(
      "@%d",
      "D=A",
      "@%s",
      "%s=M+D"
  );

  private final String segmentBase;
  private final String outputRegister;

  public SaveLocationTranslator(String segmentBase, String outputRegister) {
    this.segmentBase = segmentBase;
    this.outputRegister = outputRegister;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return AssemblySequenceFormatter
        .format(SAVE_LOCATION_ASM_SEQUENCE, parsedLine.location().get().offset(), segmentBase,
            outputRegister);
  }
}
