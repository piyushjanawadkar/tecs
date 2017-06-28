package com.computer.nand2tetris.ch07.projects.vm;

import com.computer.nand2tetris.ch07.projects.vm.ParsedLine.LineType;

/**
 * Created by jpiyush on 6/27/17.
 */
public class PostFunctionCallLabelGenerator implements LabelGenerator {

  @Override
  public boolean supports(LineType lineType) {
    return lineType.equals(ParsedLine.LineType.FUNCTION_CALL);
  }

  @Override
  public Label generate(ParsedLine parsedLine) {
    return Label.create(
        String.format("%s.Aft.%s", parsedLine.fileBaseName(), parsedLine.function().get().name()));
  }
}
