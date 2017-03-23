package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/23/17.
 */
public class ASMTranslatorImpl implements ASMTranslator {

    private static final LocationASMTranslator LOCATION_ASM_GENERATOR = new LocationASMTranslator();

    private static final String BLANK_ASM_LINE = "";

    private static final ImmutableMap<ParsedLine.LineType, ASMTranslator> generatorsByType =
            ImmutableMap.<ParsedLine.LineType, ASMTranslator>builder()
                    .put(ParsedLine.LineType.COMMAND_PUSH, new PushASMTranslator(LOCATION_ASM_GENERATOR))
                    .put(ParsedLine.LineType.COMMAND_POP, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_ADD, new BinaryOpASMTranslator("+"))
                    .put(ParsedLine.LineType.COMMAND_SUB, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_NEG, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_AND, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_OR, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_NOT, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_LT, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_EQ, new DummyASMTranslator())
                    .put(ParsedLine.LineType.COMMAND_GT, new DummyASMTranslator())
                    .build();

    @Override
    public ImmutableList<String> translate(ParsedLine parsedLine) {
        if (parsedLine.type().equals(ParsedLine.LineType.BLANK_LINE)) {
            return ImmutableList.of();
        }

        ASMTranslator generator = generatorsByType.get(parsedLine.type());
        Preconditions.checkNotNull(generator, "No generator found for %s", parsedLine.type());
        return format(parsedLine, generator.translate(parsedLine));
    }

    private static ImmutableList<String> format(ParsedLine parsedLine, ImmutableList<String> sequence) {
        String comment = String.format("// %s", parsedLine.line());
        return Streams.concat(
                Stream.of(comment),
                Streams.concat(
                        sequence.stream(), Stream.of(BLANK_ASM_LINE)))
                .collect(ImmutableList.toImmutableList());
    }
}
