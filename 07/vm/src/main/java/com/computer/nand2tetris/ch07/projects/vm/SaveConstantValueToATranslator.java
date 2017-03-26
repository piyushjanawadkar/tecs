package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/26/17.
 */
public class SaveConstantValueToATranslator implements AssemblyTranslator {

  private final int baseAddress;

  SaveConstantValueToATranslator(int baseAddress) {
    this.baseAddress = baseAddress;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    int address = baseAddress + parsedLine.location().get().offset();
    return ImmutableList.of("@" + address);
  }
}
