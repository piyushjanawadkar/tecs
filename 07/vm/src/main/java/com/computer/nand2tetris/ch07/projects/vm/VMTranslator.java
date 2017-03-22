package com.computer.nand2tetris.ch07.projects.vm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by jpiyush on 3/21/17.
 */
class VMTranslator {
    private final BufferedWriter writer;
    private final LineParser lineParser;
    private final ASMGenerator asmGenerator;

    VMTranslator(BufferedWriter writer, LineParser lineParser, ASMGenerator asmGenerator) {
        this.writer = writer;
        this.lineParser = lineParser;
        this.asmGenerator = asmGenerator;
    }

    void translate(BufferedReader reader) {
        reader.lines()
                .map(lineParser::parse)
                .map(asmGenerator::generate)
                .flatMap(l -> l.stream())
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
