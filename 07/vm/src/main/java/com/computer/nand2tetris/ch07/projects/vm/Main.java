package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.Iterables;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    IOPathsGenerator ioPathsGenerator = new IOPathsGenerator(args);
    IOPaths ioPaths = ioPathsGenerator.generate();

    System.out.println(ioPaths);

    VirtualMachineTranslator translator = new VirtualMachineTranslator(
        createWriter(ioPaths.outputPath()),
        new LineParser(),
        new AssemblyTranslatorImpl());
    translator.translate(createReader(Iterables.getOnlyElement(ioPaths.inputPaths())));
    translator.done();
  }

  private static BufferedReader createReader(String filePath) throws FileNotFoundException {
    return new BufferedReader(new FileReader(filePath));
  }

  private static BufferedWriter createWriter(String filePath) throws IOException {
    return new BufferedWriter(new FileWriter(filePath));
  }
}
