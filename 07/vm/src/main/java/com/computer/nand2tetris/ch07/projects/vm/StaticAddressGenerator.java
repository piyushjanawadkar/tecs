package com.computer.nand2tetris.ch07.projects.vm;

/**
 * Created by jpiyush on 3/25/17.
 */
public class StaticAddressGenerator {

  private StaticAddressGenerator() {}

  public static String generate(ParsedLine parsedLine) {
    return String.format("@%s.%d", parsedLine.fileBaseName(), parsedLine.location().get().offset());
  }
}
