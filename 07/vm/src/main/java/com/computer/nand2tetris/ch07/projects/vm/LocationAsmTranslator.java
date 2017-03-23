package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/23/17.
 */
public class LocationASMTranslator implements ASMTranslator {

    ImmutableMap<ParsedLocation.SegmentType, ASMTranslator> locationTranslatorBySegmentType =
            ImmutableMap.<ParsedLocation.SegmentType, ASMTranslator>builder()
                    .put(ParsedLocation.SegmentType.SEGMENT_CONSTANT, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_LOCAL, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_ARGUMENT, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_THIS, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_THAT, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_POINTER, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_TEMP, new DummyASMTranslator())
                    .put(ParsedLocation.SegmentType.SEGMENT_STATIC, new DummyASMTranslator())
                    .build();

    private static ImmutableList<String> format(ParsedLocation parsedLocation, ImmutableList<String> sequence) {
        String comment = String.format("// %s %d", parsedLocation.segmentType(), parsedLocation.offset());
        return Streams.concat(
                Stream.of(comment),
                sequence.stream())
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public ImmutableList<String> translate(ParsedLine parsedLine) {
        Optional<ParsedLocation> parsedLocation = parsedLine.location();
        Preconditions.checkArgument(parsedLocation.isPresent(), "location expected in %s", parsedLine.line());

        ASMTranslator translator = locationTranslatorBySegmentType.get(parsedLocation.get().segmentType());
        Preconditions.checkNotNull(translator, "No location translator bound to %s", parsedLocation.get().segmentType());

        return format(parsedLocation.get(), translator.translate(parsedLine));
    }
}
