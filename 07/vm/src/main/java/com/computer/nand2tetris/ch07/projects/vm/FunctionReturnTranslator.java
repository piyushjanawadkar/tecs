package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by jpiyush on 6/25/17.
 */
public class FunctionReturnTranslator implements AssemblyTranslator {

  private static final String RETURN_ADDRESS_TEMP_LOCATION = "R13";

  private static final ImmutableList<String> RETURN_ADDRESS_SAVE_TEMP_SEQUENCE_FORMAT =
      ImmutableList.of(
          "// save return address for later jump",
          "@5",
          "D=A",
          "@LCL",
          "A=M-D",
          "D=M",
          "@%s",
          "M=D"
      );

  private static final ImmutableList<String> RETURN_VALUE_SAVE_AND_STACK_POP_SEQUENCE =
      ImmutableList.of(
          "// save return value and pop stack",
          "@SP",
          "A=M-1",
          "D=M",

          "@ARG",
          "A=M",
          "M=D",
          "D=A+1",
          "@SP",
          "M=D"
      );

  private static final ImmutableList<String> SAVE_OUTPUT_LOCATION_AT_OFFSET_ONE_SEQUENCE =
      ImmutableList.of(
          "@LCL",
          "A=M-1"
      );

  private static final ImmutableList<String> SAVE_OUTPUT_LOCATION_AT_OTHER_OFFSET_SEQUENCE_FORMAT =
      ImmutableList.of(
          "@%d",
          "D=A",
          "@LCL",
          "A=M-D"
      );

  private static final ImmutableList<String> COPY_VALUE_TO_OUTPUT_LOCATION_SEQUENCE_FORMAT =
      ImmutableList.of(
          "D=M",
          "@%s",
          "M=D"
      );

  private static final ImmutableList<String> JUMP_TO_RETURN_ADDRESS_SEQUENCE_FORMAT =
      ImmutableList.of(
          "// jump to return address",
          "@%s",
          "A=M",
          "0;JMP"
      );

  private static final ImmutableMap<String, Integer> OFFSETS_RELATIVE_TO_LOCAL_BY_OUTPUT_LOCATION =
      ImmutableMap.of(
          ADDRESS_IDENTIFIER_LOCAL, 4,
          ADDRESS_IDENTIFIER_ARG, 3,
          ADDRESS_IDENTIFIER_THIS, 2,
          ADDRESS_IDENTIFIER_THAT, 1
      );

  private static void saveReturnAddressInTempSegment(String tempLocation,
      ImmutableList.Builder<String> builder) {
    builder.addAll(
        AssemblySequenceFormatter.format(
            RETURN_ADDRESS_SAVE_TEMP_SEQUENCE_FORMAT, tempLocation));
  }

  private static void saveReturnValueAndPopStack(ImmutableList.Builder<String> builder) {
    builder.addAll(RETURN_VALUE_SAVE_AND_STACK_POP_SEQUENCE);
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    saveReturnAddressInTempSegment(RETURN_ADDRESS_TEMP_LOCATION, builder);
    saveReturnValueAndPopStack(builder);
    restoreState(builder);
    jumpToReturnAddress(RETURN_ADDRESS_TEMP_LOCATION, builder);
    return builder.build();
  }

  private void restoreState(ImmutableList.Builder<String> builder) {
    restoreCell(ADDRESS_IDENTIFIER_THAT, builder);
    restoreCell(ADDRESS_IDENTIFIER_THIS, builder);
    restoreCell(ADDRESS_IDENTIFIER_ARG, builder);
    restoreCell(ADDRESS_IDENTIFIER_LOCAL, builder);
  }

  private void jumpToReturnAddress(String outputLocation, ImmutableList.Builder<String> builder) {
    builder.addAll(
        AssemblySequenceFormatter.format(JUMP_TO_RETURN_ADDRESS_SEQUENCE_FORMAT, outputLocation));
  }

  private void restoreCell(String outputLocation,
      ImmutableList.Builder<String> builder) {
    int offset = OFFSETS_RELATIVE_TO_LOCAL_BY_OUTPUT_LOCATION.get(outputLocation);
    builder.add("// restore " + outputLocation);
    builder.addAll(getSaveOutputLocationInAddressRegisterSequence(offset));
    builder.addAll(
        AssemblySequenceFormatter
            .format(COPY_VALUE_TO_OUTPUT_LOCATION_SEQUENCE_FORMAT, outputLocation));
  }

  private ImmutableList<String> getSaveOutputLocationInAddressRegisterSequence(int offset) {
    return offset == 1 ? SAVE_OUTPUT_LOCATION_AT_OFFSET_ONE_SEQUENCE
        : AssemblySequenceFormatter
            .format(SAVE_OUTPUT_LOCATION_AT_OTHER_OFFSET_SEQUENCE_FORMAT, offset);
  }
}
