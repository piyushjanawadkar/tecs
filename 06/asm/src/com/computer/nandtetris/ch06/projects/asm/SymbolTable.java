package com.computer.nandtetris.ch06.projects.asm;

import java.io.BufferedReader;
import java.io.IOException;

public interface SymbolTable {
    void addSymbols() throws IOException;

    Integer get(String s);
}
