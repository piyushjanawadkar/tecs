package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
public class CopyMToDTranslator implements AssemblyTranslator {

  private final AssemblyTranslator translator;

  public CopyMToDTranslator(AssemblyTranslator translator) {
    this.translator = translator;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return ImmutableListConcatenator.concat(translator.translate(parsedLine),
        ImmutableList.of("D=M"));
  }
}
