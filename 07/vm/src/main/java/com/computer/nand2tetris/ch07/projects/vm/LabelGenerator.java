package com.computer.nand2tetris.ch07.projects.vm;

/**
 * Created by jpiyush on 6/27/17.
 */
interface LabelGenerator {
  boolean supports(ParsedLine.LineType lineType);
  Label generate(ParsedLine parsedLine);
}
