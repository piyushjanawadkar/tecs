package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/25/17.
 */
class AssemblySequenceFormatter {

  private static final String FORMAT_DELIMITER = "!!";

  private AssemblySequenceFormatter() {
  }

  private static String generateFormat(ImmutableList<String> formatSequence) {
    return String.join(FORMAT_DELIMITER, formatSequence);
  }

  static ImmutableList<String> format(ImmutableList<String> format, Object... args) {
    return ImmutableList
        .copyOf(String.format(generateFormat(format), args).split(FORMAT_DELIMITER));
  }
}
