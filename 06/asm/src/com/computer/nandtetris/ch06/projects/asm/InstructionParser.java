package com.computer.nandtetris.ch06.projects.asm;

interface InstructionParser {
    boolean matches();

    ParsedComponents parse();
}
