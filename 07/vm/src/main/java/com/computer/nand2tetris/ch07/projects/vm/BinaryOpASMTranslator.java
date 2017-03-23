package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;

/**
 * Created by jpiyush on 3/23/17.
 */
public class BinaryOpASMTranslator implements ASMTranslator {

    private static final ImmutableList<String> BINARY_OP_ASM_SEQUENCE =
            ImmutableList.of(
                    "@SP     // seek to output",
                    "A=M-1",
                    "D=M",

                    "A=A-1   // D has popped element, A points to top",

                    "M=M%sD  // M has the output",

                    "D=A+1   // update SP",
                    "@SP",
                    "M=D"
            );

    private final String combiner;

    BinaryOpASMTranslator(String combiner) {
        this.combiner = combiner;
    }

    @Override
    public ImmutableList<String> translate(ParsedLine parsedLine) {
        return BINARY_OP_ASM_SEQUENCE
                .stream()
                .map(s -> s.replace("%s", combiner))
                .collect(ImmutableList.toImmutableList());
    }
}
