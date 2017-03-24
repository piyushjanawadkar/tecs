package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by jpiyush on 3/21/17.
 */
class VMTranslator {

  private final BufferedWriter writer;
  private final LineParser lineParser;
  private final ASMTranslator asmTranslator;

  VMTranslator(BufferedWriter writer, LineParser lineParser, ASMTranslator asmTranslator) {
    this.writer = writer;
    this.lineParser = lineParser;
    this.asmTranslator = asmTranslator;
  }

  void translate(BufferedReader reader) {
    reader.lines()
        .map(lineParser::parse)
        .map(asmTranslator::translate)
        .flatMap(ImmutableList::stream)
        .forEach(this::writeAndFlush);
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
