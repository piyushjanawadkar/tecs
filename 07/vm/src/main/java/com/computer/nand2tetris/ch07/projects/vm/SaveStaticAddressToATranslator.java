package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
public class SaveStaticAddressToATranslator implements AssemblyTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return ImmutableList.of(
        String.format("@%s.%d", parsedLine.fileBaseName(), parsedLine.location().get().offset()));
  }
}
