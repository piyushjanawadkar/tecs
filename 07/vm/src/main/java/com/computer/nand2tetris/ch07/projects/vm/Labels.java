package com.computer.nand2tetris.ch07.projects.vm;

/**
 * Created by jpiyush on 6/29/17.
 */
final class Labels {

  private static final String SYS_INIT = "Sys.init";
  private static final String RELATIONAL_OP_DONE_QUALIFIER_TEXT = "DONE";

  private Labels() {
  }

  static Label functionNameLabelOf(ParsedLine parsedLine) {
    String functionName = getFunctionName(parsedLine);
    return Label.create(functionName);
  }

  static Label postFunctionCallLabelOf(ParsedLine parsedLine) {
    Label functionCallLabel = prefixedLabelOf(getFunctionName(parsedLine),
        Integer.toString(parsedLine.index()));
    Label postFunctionCallLabel = prefixedLabelOf("Aft", functionCallLabel.text());
    return fileNamePrefixedLabelOf(parsedLine, postFunctionCallLabel.text());
  }

  static Label gotoLabelOf(ParsedLine parsedLine) {
    String unqualifiedLabelText = parsedLine.label().get().text();

    if (parsedLine.contextFunction().isPresent()) {
      return prefixedLabelOf(parsedLine.contextFunction().get().name(), unqualifiedLabelText);
    }

    // BasicLoop.vm has a label outside a function and hence lacks a context function.
    return fileNamePrefixedLabelOf(parsedLine, unqualifiedLabelText);
  }

  static Label sysInitLabel() {
    return Label.create(SYS_INIT);
  }

  static Label relationalOpLabelOf(ParsedLine parsedLine) {
    return fileNamePrefixedLabelOf(parsedLine,
        generateRelationalLabelText(parsedLine.type().toString(), parsedLine.index()));
  }

  static Label relationalOpDoneLabelOf(ParsedLine parsedLine) {
    return fileNamePrefixedLabelOf(parsedLine,
        generateRelationalLabelText(RELATIONAL_OP_DONE_QUALIFIER_TEXT, parsedLine.index()));
  }

  private static Label prefixedLabelOf(String prefix, String text) {
    return Label.create(String.format("%s.%s", prefix, text));
  }

  private static Label fileNamePrefixedLabelOf(ParsedLine parsedLine, String text) {
    return prefixedLabelOf(parsedLine.fileBaseName(), text);
  }

  private static String getFunctionName(ParsedLine parsedLine) {
    return parsedLine.function().get().name();
  }

  private static String generateRelationalLabelText(String text, int index) {
    return String.format("%s.%d", text, index);
  }
}
