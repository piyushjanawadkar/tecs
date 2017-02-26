package com.computer.nandtetris.ch06.projects.asm;

import java.io.IOException;

class ParserImpl implements Parser {

    private final MnemonicTranslator translator;
    private final LineParser lineParser;

    public ParserImpl(MnemonicTranslator translator, LineParser lineParser) {
        this.translator = translator;
        this.lineParser = lineParser;
    }

    @Override
    public void parse() throws IOException {
        ParsedLine parsedLine;
        while ((parsedLine = lineParser.getNextParsedLine()) != null) {
            translator.translate(parsedLine);
        }
    }
}
