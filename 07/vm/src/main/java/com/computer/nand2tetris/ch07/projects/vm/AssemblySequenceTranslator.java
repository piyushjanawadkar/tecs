package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
public class AssemblySequenceTranslator implements AssemblyTranslator {

  private final ImmutableList<AssemblyTranslator> translators;

  public AssemblySequenceTranslator(AssemblyTranslator... translators) {
    this.translators = ImmutableList.copyOf(translators);
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return translators.stream().map(t -> t.translate(parsedLine)).flatMap(
        ImmutableList::stream).collect(ImmutableList.toImmutableList());
  }
}
