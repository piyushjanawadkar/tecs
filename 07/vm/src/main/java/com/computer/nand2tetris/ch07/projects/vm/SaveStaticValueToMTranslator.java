package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
public class SaveStaticValueToMTranslator implements AssemblyTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return ImmutableList.of(StaticAddressGenerator.generate(parsedLine));
  }
}
