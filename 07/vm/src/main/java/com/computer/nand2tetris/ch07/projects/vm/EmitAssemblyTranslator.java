package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/26/17.
 */
public class EmitAssemblyTranslator implements AssemblyTranslator {

  private final ImmutableList<String> assemblySequence;

  public EmitAssemblyTranslator(ImmutableList<String> assemblySequence) {
    this.assemblySequence = assemblySequence;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return assemblySequence;
  }
}
