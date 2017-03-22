package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;

/**
 * Created by jpiyush on 3/22/17.
 */
@AutoValue
abstract class ParsedLine {
    static ParsedLine create(String line) {
        return new AutoValue_ParsedLine(line);
    }

    abstract String line();

    public String toString() {
        return line();
    }
}
