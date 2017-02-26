package com.computer.nandtetris.ch06.projects.asm;

import java.io.*;

class AssemblerImpl implements Assembler {

    public void assemble(String inputFilePath, String outputFilePath) throws IOException {
        SymbolTable symbolTable = executeFirstPass(inputFilePath);
        executeSecondPass(inputFilePath, symbolTable, outputFilePath);
    }

    SymbolTable executeFirstPass(String inputFilePath) throws IOException {
        BufferedReader reader = createFileReader(inputFilePath);
        SymbolTable symbolTable = buildSymbolTable(reader);
        reader.close();
        return symbolTable;
    }

    void executeSecondPass(
            String inputFilePath,
            SymbolTable symbolTable,
            String outputFilePath) throws IOException {
        BufferedReader reader = createFileReader(inputFilePath);
        BufferedWriter writer = createFileWriter(outputFilePath);
        parse(reader, symbolTable, writer);
        reader.close();
        writer.close();
    }

    private static SymbolTable buildSymbolTable(BufferedReader reader) throws IOException {
        SymbolTable symbolTable = new SymbolTableImpl(new LineParserImpl(reader));
        symbolTable.addSymbols();
        return symbolTable;
    }

    private static void parse(
            BufferedReader reader,
            SymbolTable symbolTable,
            BufferedWriter writer) throws IOException {
        Parser parser =
                new ParserImpl(
                        new MnemonicTranslatorImpl(symbolTable, writer),
                        new LineParserImpl(reader));
        parser.parse();
    }

    private static BufferedReader createFileReader(String path) throws FileNotFoundException {
        return new BufferedReader(new FileReader(path));
    }

    private static BufferedWriter createFileWriter(String outputFilePath) throws IOException {
        return new BufferedWriter(new FileWriter(outputFilePath));
    }
}
