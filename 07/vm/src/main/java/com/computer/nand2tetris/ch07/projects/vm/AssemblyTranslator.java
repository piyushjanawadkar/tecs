package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/22/17.
 */
interface AssemblyTranslator {

  public static final int POINTER_BASE_ADDRESS = 3;
  public static final int TEMP_BASE_ADDRESS = 5;

  ImmutableList<String> translate(ParsedLine parsedLine);
}
