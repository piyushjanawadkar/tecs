package com.computer.nand2tetris.ch07.projects.vm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    IOPathsGenerator ioPathsGenerator = new IOPathsGenerator();
    IOPaths ioPaths = ioPathsGenerator.generate(args);

    System.out.println(ioPaths);

    VirtualMachineTranslator translator = new VirtualMachineTranslator(
        createWriter(ioPaths.outputPath()),
        new LineParser(),
        new AssemblyTranslatorImpl());
    translator.translate(ioPaths.inputFiles());
    translator.done();
  }

  private static BufferedWriter createWriter(String filePath) throws IOException {
    return new BufferedWriter(new FileWriter(filePath));
  }
}
