package com.computer.nandtetris.ch06.projects.asm;

import java.io.IOException;

public interface MnemonicTranslator {
    void translate(ParsedLine parsedLine) throws IOException;
}
