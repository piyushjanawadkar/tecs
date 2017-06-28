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
    updateState(builder);
    jumpToFunction(builder);
    writeLabel(builder);
    return builder.build();
  }

  private Label createReturnAddressLabel(ParsedLine parsedLine) {
    return labelGenerator.generate(parsedLine);
  }

  private void pushReturnAddress(Label returnAddressLabel, Builder<String> builder) {
    pushAddressLabelText(returnAddressLabel.text(), builder);
  }

  private void pushAddressLabelText(String addressLabelText, Builder<String> builder) {
    builder.add("// push " + addressLabelText);
    saveAddressToD(addressLabelText, builder);
    builder.addAll(pushSequenceTranslator.translate(null));
  }

  private void saveAddressToD(String text, Builder<String> builder) {
    builder.addAll(AssemblySequenceFormatter.format(SAVE_ADDRESS_TO_D_FORMAT, text));
  }

  private void pushFrame(Builder<String> builder) {
    builder.add("// push frame");
    pushAddressLabelText(ADDRESS_IDENTIFIER_LOCAL, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_ARG, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_THIS, builder);
    pushAddressLabelText(ADDRESS_IDENTIFIER_THAT, builder);
  }

  private void updateState(Builder<String> builder) {

  }

  private void jumpToFunction(Builder<String> builder) {

  }

  private void writeLabel(Builder<String> builder) {
  }
}
