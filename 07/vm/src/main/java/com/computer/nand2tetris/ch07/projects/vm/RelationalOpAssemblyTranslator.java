package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/24/17.
 */
public class RelationalOpAssemblyTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> RELATIONAL_OP_ASM_SEQUENCE = ImmutableList.of(
      "@SP",
      "A=M-1",
      "D=M  // D has first popped element. A is current stack top",

      "A=A-1",
      "D=M-D  // D has the difference of popped elements",

      "@Label%s{LINENUM}",
      "D;%s",
      "D=%s",
      "@Labeldone{LINENUM}",
      "0;JMP",
      "(Label%s{LINENUM})",
      "D=%s",
      "(Labeldone{LINENUM})  // D has the output of op",

      "@SP",
      "A=M-1",
      "A=A-1",
      "M=D",
      "D=A+1",
      "@SP",
      "M=D  // SP is updated"
  );

  private final String jumpInstruction;
  private final Integer consequentValue;
  private final Integer alternateValue;
  private final SubstituteValuesAssemblyTranslator substitutor;

  public RelationalOpAssemblyTranslator(String jumpInstruction, Integer consequentValue,
      Integer alternateValue) {
    this.jumpInstruction = jumpInstruction;
    this.consequentValue = consequentValue;
    this.alternateValue = alternateValue;
    substitutor = new SubstituteValuesAssemblyTranslator(RELATIONAL_OP_ASM_SEQUENCE,
        jumpInstruction.toLowerCase(), jumpInstruction, alternateValue,
        jumpInstruction.toLowerCase(), consequentValue);
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return substitutor.translate(parsedLine);
  }
}
