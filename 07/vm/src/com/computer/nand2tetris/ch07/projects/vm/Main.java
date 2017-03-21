package com.computer.nand2tetris.ch07.projects.vm;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        IOPathsGenerator ioPathsGenerator = new IOPathsGenerator(args);
        IOPaths ioPaths = ioPathsGenerator.generate();
        VMTranslator translator = new VMTranslator(createWriter(ioPaths.outputAsmPath()));
        translator.translate(createReader(Iterables.getOnlyElement(ioPaths.inputVmPaths())));
    }

    private static BufferedReader createReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(filePath));
    }

    private static BufferedWriter createWriter(String filePath) throws IOException {
        return new BufferedWriter(new FileWriter(filePath));
    }
}
