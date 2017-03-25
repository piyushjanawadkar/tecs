package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/24/17.
 */
public class BaseOffsetLocationAssemblyTranslator implements LocationAssemblyTranslator {

  private static final ImmutableList<String> ASM_SEQUENCE = ImmutableList.of(
      "@%d",
      "D=A",
      "@%s",
      "A=M+D",
      "D=M"
  );

  private final String base;

  public BaseOffsetLocationAssemblyTranslator(String base) {
    this.base = base;
  }

  @Override
  public ImmutableList<String> translate(ParsedLocation parsedLocation) {
    return AssemblySequenceFormatter
        .format(ASM_SEQUENCE, new Integer(parsedLocation.offset()), base);
  }
}
