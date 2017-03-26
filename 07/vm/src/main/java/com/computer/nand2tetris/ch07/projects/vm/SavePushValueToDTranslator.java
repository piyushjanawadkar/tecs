package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by jpiyush on 3/25/17.
 */

// push contract: value to push is in D

// default push
//        (
//            (save location to A)
//            D=M
//        )
// push from static
//        (
//          @Xxx.y
//          D=M
//        )
// push from constant
//        (
//          @c
//          D=A
//        )
public class SavePushValueToDTranslator implements AssemblyTranslator {

  private static final int POINTER_BASE_ADDRESS = 3;
  private static final int TEMP_BASE_ADDRESS = 5;

  private static final ImmutableMap<ParsedLocation.SegmentType, AssemblyTranslator> pushValueSaverBySegmentType =
      ImmutableMap.<ParsedLocation.SegmentType, AssemblyTranslator>builder()
          .put(ParsedLocation.SegmentType.SEGMENT_CONSTANT, new SaveConstantValueToDTranslator(0))
          .put(ParsedLocation.SegmentType.SEGMENT_STATIC,
              new CopyMToDTranslator(new SaveStaticValueToMTranslator()))
          .put(ParsedLocation.SegmentType.SEGMENT_LOCAL,
              new CopyMToDTranslator(new SaveLocationTranslator("LCL", "A")))
          .put(ParsedLocation.SegmentType.SEGMENT_ARGUMENT,
              new CopyMToDTranslator(new SaveLocationTranslator("ARG", "A")))
          .put(ParsedLocation.SegmentType.SEGMENT_THIS,
              new CopyMToDTranslator(new SaveLocationTranslator("THIS", "A")))
          .put(ParsedLocation.SegmentType.SEGMENT_THAT,
              new CopyMToDTranslator(new SaveLocationTranslator("THAT", "A")))
          .put(ParsedLocation.SegmentType.SEGMENT_POINTER,
              new SaveConstantValueToDTranslator(POINTER_BASE_ADDRESS))
          .put(ParsedLocation.SegmentType.SEGMENT_TEMP,
              new SaveConstantValueToDTranslator(TEMP_BASE_ADDRESS))
          .build();

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    Preconditions.checkArgument(parsedLine.location().isPresent(), "location expected in %s",
        parsedLine.line());
    ParsedLocation location = parsedLine.location().get();

    AssemblyTranslator translator = pushValueSaverBySegmentType.get(location.segmentType());
    Preconditions
        .checkNotNull(translator, "No translator found for %s in %s", location.segmentType(),
            parsedLine.line());

    return translator.translate(parsedLine);
  }
}
