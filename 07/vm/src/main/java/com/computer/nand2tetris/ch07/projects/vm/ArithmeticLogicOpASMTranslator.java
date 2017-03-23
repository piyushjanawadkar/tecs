package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/23/17.
 */
public class ArithmeticLogicOpASMTranslator implements ASMTranslator {

    private final String operator;
    private final ImmutableList<String> asmSequence;

    public ArithmeticLogicOpASMTranslator(ImmutableList<String> asmSequence, String operator) {
        this.operator = operator;
        this.asmSequence = asmSequence;
    }

    @Override
    public ImmutableList<String> translate(ParsedLine parsedLine) {
        return asmSequence
                .stream()
                .map(s -> s.replace("%s", operator))
                .collect(ImmutableList.toImmutableList());
    }
}
