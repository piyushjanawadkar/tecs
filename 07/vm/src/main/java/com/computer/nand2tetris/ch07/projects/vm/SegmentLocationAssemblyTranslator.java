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
public class SegmentLocationAssemblyTranslator implements AssemblyTranslator {

  private static final String POINTER_SEGMENT_BASE = "3";
  private static final String TEMP_SEGMENT_BASE = "5";

  private static final ImmutableMap<ParsedLocation.SegmentType, LocationAssemblyTranslator> locationTranslatorBySegmentType =
      ImmutableMap.<ParsedLocation.SegmentType, LocationAssemblyTranslator>builder()
          .put(ParsedLocation.SegmentType.SEGMENT_CONSTANT,
              new ConstantSegmentLocationAssemblyTranslator())
          .put(ParsedLocation.SegmentType.SEGMENT_LOCAL,
              new BaseOffsetLocationAssemblyTranslator("LCL"))
          .put(ParsedLocation.SegmentType.SEGMENT_ARGUMENT,
              new BaseOffsetLocationAssemblyTranslator("ARG"))
          .put(ParsedLocation.SegmentType.SEGMENT_THIS,
              new BaseOffsetLocationAssemblyTranslator("THIS"))
          .put(ParsedLocation.SegmentType.SEGMENT_THAT,
              new BaseOffsetLocationAssemblyTranslator("THAT"))
          .put(ParsedLocation.SegmentType.SEGMENT_POINTER, new BaseOffsetLocationAssemblyTranslator(
              POINTER_SEGMENT_BASE))
          .put(ParsedLocation.SegmentType.SEGMENT_TEMP, new BaseOffsetLocationAssemblyTranslator(
              TEMP_SEGMENT_BASE))
          .put(ParsedLocation.SegmentType.SEGMENT_STATIC, new DummyLocationAssemblyTranslator())
          .build();

  private static ImmutableList<String> format(ParsedLocation parsedLocation,
      ImmutableList<String> sequence) {
    String comment = String
        .format("// %s %d", parsedLocation.segmentType(), parsedLocation.offset());
    return Streams.concat(
        Stream.of(comment),
        sequence.stream())
        .collect(ImmutableList.toImmutableList());
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    Optional<ParsedLocation> parsedLocation = parsedLine.location();
    Preconditions
        .checkArgument(parsedLocation.isPresent(), "location expected in %s", parsedLine.line());

    LocationAssemblyTranslator translator = locationTranslatorBySegmentType
        .get(parsedLocation.get().segmentType());
    Preconditions.checkNotNull(translator, "No location translator bound to %s",
        parsedLocation.get().segmentType());

    return format(parsedLocation.get(), translator.translate(parsedLocation.get()));
  }
}
