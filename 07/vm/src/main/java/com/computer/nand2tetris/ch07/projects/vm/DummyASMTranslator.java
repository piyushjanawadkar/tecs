package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/23/17.
 */
public class DummyASMTranslator implements ASMTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return ImmutableList.of(parsedLine.toString());
  }
}
