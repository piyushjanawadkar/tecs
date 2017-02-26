package com.computer.nandtetris.ch06.projects.asm;

import java.io.IOException;

public interface LineParser {
    ParsedLine getNextParsedLine() throws IOException;
}
