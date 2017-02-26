package com.computer.nandtetris.ch06.projects.asm;

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputFilePath = getInputFilePath(Arrays.asList(args));
        Assembler assembler = new AssemblerImpl();
        assembler.assemble(inputFilePath, getOutputFilePath(inputFilePath));
    }

    private static String getInputFilePath(Iterable<String> args) {
        return Iterables.getOnlyElement(args);
    }

    private static String getOutputFilePath(String inputFilePath) {
        return HackFilePath.from(inputFilePath).get();
    }
}
