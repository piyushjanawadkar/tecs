package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.Iterables;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        IOPathsGenerator ioPathsGenerator = new IOPathsGenerator(args);
        IOPaths ioPaths = ioPathsGenerator.generate();
        VMTranslator translator = new VMTranslator(createWriter(ioPaths.outputPath()));
        translator.translate(createReader(Iterables.getOnlyElement(ioPaths.inputPaths())));
    }

    private static BufferedReader createReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    private static BufferedWriter createWriter(String filePath) throws IOException {
        return new BufferedWriter(new FileWriter(filePath));
    }
}
