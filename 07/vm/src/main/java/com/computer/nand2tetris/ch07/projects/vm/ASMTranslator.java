package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/22/17.
 */
interface ASMTranslator {
    ImmutableList<String> translate(ParsedLine parsedLine);
}
