package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * Created by jpiyush on 6/27/17.
 */
public class FunctionCallTranslator implements AssemblyTranslator {

  private static final ImmutableList<String> SAVE_ADDRESS_TO_D_FORMAT = ImmutableList.of(
      "@%s",
      "D=A"
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
  private static final int SAVED_FRAME_SIZE = 5;  // return address, LCL, ARG, THIS, THAT

  private LabelGenerator labelGenerator;
  private AssemblyTranslator pushSequenceTranslator;

  public FunctionCallTranslator(
      LabelGenerator labelGenerator, AssemblyTranslator pushSequenceTranslator) {
    this.labelGenerator = labelGenerator;
    this.pushSequenceTranslator = pushSequenceTranslator;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    Label returnAddressLabel = createReturnAddressLabel(parsedLine);
    pushReturnAddress(returnAddressLabel, builder);
    pushFrame(builder);
    updateState(parsedLine, builder);
    jumpToFunction(builder);
    writeLabel(builder);
    return builder.build();
  }

  private Label createReturnAddressLabel(ParsedLine parsedLine) {
    return labelGenerator.generate(parsedLine);
  }

  private void pushReturnAddress(Label returnAddressLabel, ImmutableList.Builder<String> builder) {
    pushAddressLabelText(returnAddressLabel.text(), builder);
  }

  private void pushAddressLabelText(String addressLabelText,
      ImmutableList.Builder<String> builder) {
    builder.add("// push " + addressLabelText);
    saveAddressToD(addressLabelText, builder);
    builder.addAll(pushSequenceTranslator.translate(null));
  }

  private void saveAddressToD(String text, ImmutableList.Builder<String> builder) {
    builder.addAll(AssemblySequenceFormatter.format(SAVE_ADDRESS_TO_D_FORMAT, text));
  }

  private void pushFrame(ImmutableList.Builder<String> builder) {
    builder.add("// push frame");
    pushAddressLabelText(ADDRESS_IDENTIFIER_LOCAL, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_ARG, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_THIS, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_THAT, builder);
  }

  private void updateState(ParsedLine parsedLine, Builder<String> builder) {
    builder.add("// update state");
    updateLocal(builder);
    updateArg(parsedLine, builder);
  }

  private void updateArg(ParsedLine parsedLine, Builder<String> builder) {
    int argOffset = parsedLine.function().get().numArgs().get();
    builder
        .addAll(AssemblySequenceFormatter.format(UPDATE_ARG_FORMAT, argOffset + SAVED_FRAME_SIZE));
  }

  private void updateLocal(ImmutableList.Builder<String> builder) {
    builder.addAll(COPY_D_TO_LOCAL_SEQUENCE);
  }

  private void jumpToFunction(ImmutableList.Builder<String> builder) {

  }

  private void writeLabel(ImmutableList.Builder<String> builder) {
  }
}
