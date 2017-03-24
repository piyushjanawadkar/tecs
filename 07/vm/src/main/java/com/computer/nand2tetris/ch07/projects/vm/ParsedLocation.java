package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;

/**
 * Created by jpiyush on 3/22/17.
 */
@AutoValue
abstract class ParsedLocation {

  static ParsedLocation create(SegmentType segmentType, int offset) {
    return new AutoValue_ParsedLocation(segmentType, offset);
  }

  abstract SegmentType segmentType();

  abstract int offset();

  public String toString() {
    return String.format("%s[%d]", segmentType(), offset());
  }

  enum SegmentType {
    SEGMENT_ARGUMENT,
    SEGMENT_THIS,
    SEGMENT_THAT,
    SEGMENT_POINTER,
    SEGMENT_TEMP,
    SEGMENT_STATIC,
    SEGMENT_CONSTANT,
    SEGMENT_LOCAL
  }
}
