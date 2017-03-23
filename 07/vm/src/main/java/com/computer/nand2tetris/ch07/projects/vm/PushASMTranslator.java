package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/23/17.
 */
public class PushASMTranslator implements ASMTranslator {

    private static final String BLANK_ASM_LINE = "";

    private static final ImmutableList<String> PUSH_ASM_SEQUENCE =
            ImmutableList.of(
                    "// push contents of D",
                    "@SP",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M+1"
            );


    private final LocationASMTranslator locationAsmGenerator;

    public PushASMTranslator(LocationASMTranslator locationAsmGenerator) {
        this.locationAsmGenerator = locationAsmGenerator;
    }

    @Override
    public ImmutableList<String> translate(ParsedLine parsedLine) {
        return format(parsedLine, concat(locationAsmGenerator.translate(parsedLine), PUSH_ASM_SEQUENCE));
    }

    private ImmutableList<String> format(ParsedLine parsedLine, Stream<String> sequence) {
        return Streams.concat(
                sequence,
                Stream.of(BLANK_ASM_LINE))
                .collect(ImmutableList.toImmutableList());
    }

    private Stream<String> concat(ImmutableList<String> first, ImmutableList<String> second) {
        return Streams.concat(first.stream(), second.stream());
    }
}
