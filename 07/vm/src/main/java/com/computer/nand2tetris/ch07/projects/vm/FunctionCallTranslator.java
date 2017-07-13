package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * Created by jpiyush on 6/27/17.
 */
public class FunctionCallTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> SAVE_ADDRESS_TO_D_FORMAT = ImmutableList.of(
      "@%s",
      "D=%s"
  );

  private static final ImmutableList<String> COPY_D_TO_LOCAL_SEQUENCE = ImmutableList.of(
      "// update LCL (current stack top is in D)",
      "@LCL",
      "M=D"
  );

  private static final ImmutableList<String> UPDATE_ARG_FORMAT = ImmutableList.of(
      "// update ARG (current stack top is in D)",
      "@%d",
      "D=D-A",
      "@ARG",
      "M=D"
  );
  private static final int SAVED_FRAME_SIZE = 5;  // return_address, LCL, ARG, THIS, THAT

  private static final ImmutableList<String> JUMP_TO_FUNCTION_FORMAT = ImmutableList.of(
      "// jump to called function",
      "@%s",
      "0;JMP"
  );
  private static final boolean SHOULD_DEREFERENCE = true;

  private AssemblyTranslator pushSequenceTranslator;

  public FunctionCallTranslator(AssemblyTranslator pushSequenceTranslator) {
    this.pushSequenceTranslator = pushSequenceTranslator;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();

    Label returnAddressLabel = Labels.postFunctionCallLabelOf(parsedLine);
    pushReturnAddress(returnAddressLabel, builder);
    pushFrame(builder);

    ParsedFunctionParams parsedFunction = parsedLine.function().get();
    updateState(parsedFunction, builder);
    jumpToFunction(parsedLine, builder);
    writeLabel(returnAddressLabel, builder);
    return builder.build();
  }

  private void pushReturnAddress(Label returnAddressLabel, ImmutableList.Builder<String> builder) {
    pushAddressLabelText(returnAddressLabel.text(), !SHOULD_DEREFERENCE, builder);
  }

  private void pushAddressLabelText(String addressLabelText,
      boolean shouldDereference, Builder<String> builder) {
    builder.add("// push " + addressLabelText);
    saveAddressToD(addressLabelText, shouldDereference, builder);
    builder.addAll(pushSequenceTranslator.translate(null));
  }

  private void saveAddressToD(String text, boolean shouldDerefernce,
      ImmutableList.Builder<String> builder) {
    String value = shouldDerefernce ? "M" : "A";
    builder.addAll(AssemblySequenceFormatter.format(SAVE_ADDRESS_TO_D_FORMAT, text, value));
  }

  private void pushFrame(ImmutableList.Builder<String> builder) {
    builder.add("// push frame");
    pushAddressLabelText(ADDRESS_IDENTIFIER_LOCAL, SHOULD_DEREFERENCE, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_ARG, SHOULD_DEREFERENCE, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_THIS, SHOULD_DEREFERENCE, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_THAT, SHOULD_DEREFERENCE, builder);
  }

  private void updateState(ParsedFunctionParams parsedFunction, Builder<String> builder) {
    builder.add("// update state");
    updateLocal(builder);
    updateArg(parsedFunction, builder);
  }

  private void updateArg(ParsedFunctionParams parsedFunction, Builder<String> builder) {
    int argOffset = parsedFunction.numArgs().get() + SAVED_FRAME_SIZE;
    builder
        .addAll(AssemblySequenceFormatter.format(UPDATE_ARG_FORMAT, argOffset));
  }

  private void updateLocal(ImmutableList.Builder<String> builder) {
    builder.addAll(COPY_D_TO_LOCAL_SEQUENCE);
  }

  private void jumpToFunction(ParsedLine parsedLine, ImmutableList.Builder<String> builder) {
    builder.addAll(AssemblySequenceFormatter
        .format(JUMP_TO_FUNCTION_FORMAT, Labels.functionNameLabelOf(parsedLine).text()));
  }

  private void writeLabel(Label returnAddressLabel, Builder<String> builder) {
    builder.add("");
    builder.add(returnAddressLabel.generateDefinitionText());
  }
}
