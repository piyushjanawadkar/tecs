package com.computer.nand2tetris.ch07.projects.vm;

/**
 * Created by jpiyush on 6/29/17.
 */
final class Labels {

  private Labels() {
  }

  static Label labelOf(String fileBaseName, String text) {
    return Label.create(String.format("%s.%s", fileBaseName, text));
  }

  static Label functionNameLabelOf(ParsedLine parsedLine) {
    return labelOf(parsedLine.fileBaseName(), parsedLine.function().get().name());
  }

  static Label postFunctionCallLabelOf(ParsedLine parsedLine) {
    return labelOf(parsedLine.fileBaseName(),
        String.format("Aft.%s", parsedLine.function().get().name()));
  }
}
