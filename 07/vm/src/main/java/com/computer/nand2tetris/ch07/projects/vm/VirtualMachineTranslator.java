package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
    inputFiles.stream().forEachOrdered(f -> {
      lineParser.resetIndex();
      translateFile(f);
    });
  }

  private void translateFile(InputFile inputFile) {
    try {
      createReader(inputFile).lines()
          .map(l -> lineParser.parse(l, inputFile.baseName()))
          .map(asmTranslator::translate)
          .flatMap(ImmutableList::stream)
          .forEach(this::writeAndFlush);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private BufferedReader createReader(InputFile inputFile) throws FileNotFoundException {
    return new BufferedReader(new FileReader(inputFile.path()));
  }

  private void writeAndFlush(String line) {
    try {
      writer.write(line);
      writer.newLine();
      writer.flush();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public void done() throws IOException {
    TRAILING_ASM_INFINITE_LOOP.stream().forEach(this::writeAndFlush);
    writer.close();
  }
}
