package com.computer.nandtetris.ch06.projects.asm;

class Instruction {

    StringBuilder bitSequenceBuilder;

    Instruction() {
        bitSequenceBuilder = new StringBuilder();
    }

    void append(String bitSequence) {
        bitSequenceBuilder.append(bitSequence);
    }

    public String toString() {
        return bitSequenceBuilder.toString();
    }
}
