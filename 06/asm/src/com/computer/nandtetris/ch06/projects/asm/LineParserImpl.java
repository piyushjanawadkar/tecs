package com.computer.nandtetris.ch06.projects.asm;

import java.io.BufferedReader;
import java.io.IOException;

public class LineParserImpl implements LineParser {

    private final BufferedReader reader;
    private final LineCounter counter;

    LineParserImpl(BufferedReader reader) {
        this.reader = reader;
        this.counter = new LineCounter();
    }

    @Override
    public ParsedLine getNextParsedLine() throws IOException {
        String line;
        ParsedLine parsedLine = null;
        while ((line = reader.readLine()) != null) {
            parsedLine = new ParsedLine(line, counter);
            if (!parsedLine.getType().equals(ParsedComponents.LineType.SKIPPABLE_LINE)) {
                break;
            }
        }

        return line != null ? parsedLine : null;
    }
}
