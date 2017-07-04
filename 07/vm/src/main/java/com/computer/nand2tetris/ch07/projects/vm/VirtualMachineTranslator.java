package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/21/17.
 */
class VirtualMachineTranslator {

  private static final ImmutableList<String> TRAILING_ASM_INFINITE_LOOP = ImmutableList.of(
      "// Trailing infinite loop",
      "(End)",
      "@End",
      "0;JMP"
  );
  private static final int STACK_BEGIN_ADDRESS = 256;
  private static final int HEAP_BEGIN_ADDRESS = 2048;
  private static final ImmutableList<String> POINTER_INITIALIZATION_SEQUENCE = ImmutableList.of(
      "// initialize %s to %d",
      "@%d",
      "D=A",
      "@%s",
      "M=D"
  );

  private final BufferedWriter writer;
  private final LineParser lineParser;
  private final AssemblyTranslator asmTranslator;

  VirtualMachineTranslator(BufferedWriter writer, LineParser lineParser,
      AssemblyTranslator asmTranslator) {
    this.writer = writer;
    this.lineParser = lineParser;
    this.asmTranslator = asmTranslator;
  }

  public void translate(ImmutableList<InputFile> inputFiles) {
    ImmutableList<String> asmStatements = inputFiles.stream().map(f -> {
      lineParser.resetState();
      return translateFile(f);
    }).flatMap(Function.identity()).collect(ImmutableList.toImmutableList());

    Label sysInitLabel = Labels.sysInitLabel();
    if (asmStatements.contains(sysInitLabel.generateDefinitionText())) {
      asmStatements = initializeState(sysInitLabel, asmStatements);
    }

    writeLines(asmStatements);
  }

  private ImmutableList<String> initializeState(Label sysInitLabel,
      ImmutableList<String> asmStatements) {
    initializePointers();
    return addInitialJump(sysInitLabel, asmStatements);
  }

  private ImmutableList<String> addInitialJump(Label sysInitLabel,
      ImmutableList<String> asmStatements) {
    String initialCallInstruction = String.format("call %s 0", sysInitLabel.text());
    lineParser.resetState();
    ParsedLine parsedLine = lineParser.parse(initialCallInstruction, "");
    return ImmutableList.<String>builder().addAll(asmTranslator.translate(parsedLine))
        .addAll(asmStatements).build();
  }

  private void initializePointers() {
    initializePointer("SP", STACK_BEGIN_ADDRESS);
    initializePointer("THIS", HEAP_BEGIN_ADDRESS);
    initializePointer("THAT", HEAP_BEGIN_ADDRESS);
  }

  private void initializePointer(String pointer, int address) {
    writeLines(AssemblySequenceFormatter
        .format(POINTER_INITIALIZATION_SEQUENCE, pointer, address, address, pointer));
    writeNewLine();
  }

  private void writeNewLine() {
    try {
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Stream<String> translateFile(InputFile inputFile) {
    try {
      return createReader(inputFile).lines()
          .map(l -> lineParser.parse(l, inputFile.baseName()))
          .map(asmTranslator::translate)
          .flatMap(ImmutableList::stream);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private BufferedReader createReader(InputFile inputFile) throws FileNotFoundException {
    return new BufferedReader(new FileReader(inputFile.path()));
  }

  private void writeLines(ImmutableList<String> lines) {
    lines.stream().forEachOrdered(this::writeLineAndFlush);
  }

  private void writeLineAndFlush(String line) {
    try {
      writer.write(line);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void done() throws IOException {
    writeLines(TRAILING_ASM_INFINITE_LOOP);
    writer.close();
  }
}
