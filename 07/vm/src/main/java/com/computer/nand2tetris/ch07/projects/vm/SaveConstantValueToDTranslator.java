package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
public class SaveConstantValueToDTranslator implements AssemblyTranslator {

  final int baseAddress;

  SaveConstantValueToDTranslator(int baseAddress) {
    this.baseAddress = baseAddress;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    AssemblyTranslator translator = new AssemblySequenceTranslator(
        new SaveConstantValueToATranslator(baseAddress),
        new EmitAssemblyTranslator(ImmutableList.of("D=A"))
    );
    return translator.translate(parsedLine);
  }
}
