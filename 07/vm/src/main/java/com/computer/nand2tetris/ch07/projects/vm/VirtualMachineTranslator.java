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

  private final BufferedWriter writer;
  private final LineParser lineParser;
  private final AssemblyTranslator asmTranslator;

  VirtualMachineTranslator(BufferedWriter writer, LineParser lineParser,
      AssemblyTranslator asmTranslator) {
    this.writer = writer;
    this.lineParser = lineParser;
    this.asmTranslator = asmTranslator;
  }

  void translate(InputFile inputFile) throws FileNotFoundException {
    createReader(inputFile).lines()
        .map(l -> lineParser.parse(l, inputFile.baseName()))
        .map(asmTranslator::translate)
        .flatMap(ImmutableList::stream)
        .forEach(this::writeAndFlush);
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
    writer.close();
  }
}
