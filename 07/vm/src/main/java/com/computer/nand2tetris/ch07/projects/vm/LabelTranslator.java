package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 6/29/17.
 */
public class LabelTranslator implements AssemblyTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return ImmutableList.of(Labels.gotoLabelOf(parsedLine).generateDefinitionText());
  }
}
