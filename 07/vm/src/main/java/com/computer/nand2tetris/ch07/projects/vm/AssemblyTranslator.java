package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/22/17.
 */
interface AssemblyTranslator {

  public static final int POINTER_BASE_ADDRESS = 3;
  public static final int TEMP_BASE_ADDRESS = 5;

  public static final String ADDRESS_IDENTIFIER_LOCAL = "LCL";
  public static final String ADDRESS_IDENTIFIER_ARG = "ARG";
  public static final String ADDRESS_IDENTIFIER_THIS = "THIS";
  public static final String ADDRESS_IDENTIFIER_THAT = "THAT";

  ImmutableList<String> translate(ParsedLine parsedLine);
}
