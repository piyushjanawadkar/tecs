package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/24/17.
 */
public interface LocationAssemblyTranslator {

  ImmutableList<String> translate(ParsedLocation parsedLocation);
}
