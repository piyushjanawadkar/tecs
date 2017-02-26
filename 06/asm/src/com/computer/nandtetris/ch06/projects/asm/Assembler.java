package com.computer.nandtetris.ch06.projects.asm;

import java.io.IOException;

interface Assembler {
    void assemble(String inputFilePath, String outputFilePath) throws IOException;
}
