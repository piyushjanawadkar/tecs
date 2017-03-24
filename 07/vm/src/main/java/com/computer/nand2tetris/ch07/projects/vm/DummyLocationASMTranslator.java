package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/24/17.
 */
public class DummyLocationASMTranslator implements LocationASMTranslator {

  @Override
  public ImmutableList<String> translate(ParsedLocation parsedLocation) {
    return ImmutableList.of(parsedLocation.toString());
  }
}
