package com.computer.nand2tetris.ch07.projects.vm;

/**
 * Created by jpiyush on 6/29/17.
 */
final class Labels {

  private static final String SYS_INIT = "Sys.init";

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
    String contextFunctionName = parsedLine.contextFunction().get().name();
    String unqualifiedLabelText = parsedLine.label().get().text();
    return prefixedLabelOf(contextFunctionName, unqualifiedLabelText);
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

  public static Label sysInitLabel() {
    return Label.create(SYS_INIT);
  }
}
