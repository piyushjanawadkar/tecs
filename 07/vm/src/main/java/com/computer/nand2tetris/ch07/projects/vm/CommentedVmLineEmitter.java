package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

public class CommentedVmLineEmitter implements AssemblyTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return ImmutableList.of("// " + parsedLine.toString());
  }
}
