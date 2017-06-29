package com.computer.nand2tetris.ch07.projects.vm;

/**
 * Created by jpiyush on 6/29/17.
 */
final class Labels {

  private Labels() {
  }

  static Label labelOf(String text) {
    return Label.create(text);
  }

  static Label functionNameLabelOf(ParsedLine parsedLine) {
    String text = String
        .format("%s.%s", parsedLine.fileBaseName(), parsedLine.function().get().name());
    return labelOf(text);
  }

  static Label postFunctionCallLabelOf(ParsedLine parsedLine) {
    String text = String
        .format("%s.Aft.%s", parsedLine.fileBaseName(), parsedLine.function().get().name());
    return labelOf(text);
  }
}
