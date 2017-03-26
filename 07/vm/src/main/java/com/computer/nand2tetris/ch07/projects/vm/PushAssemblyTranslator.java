package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

/**
 * Created by jpiyush on 3/23/17.
 */

// push contract: value to push is in D

// default push
//      (
//        (
//            (save location to A)
//            D=M
//        )
//        (execute push of D)
//      )
// push from static
//      (
//        (
//          @Xxx.y
//          D=M
//        )
//        (execute push of D)
//      )
// push from constant
//      (
//        (
//          @c
//          D=A
//        )
//        (execute push of D)
//      )
public class PushAssemblyTranslator implements AssemblyTranslator {


  private static final ImmutableList<String> PUSH_ASM_SEQUENCE =
      ImmutableList.of(
          "// push contents of D",
          "@SP",
          "A=M",
          "M=D",
          "@SP",
          "M=M+1"
      );


  private final SavePushValueToDTranslator savePushValueToDTranslator;

  public PushAssemblyTranslator(SegmentLocationAssemblyTranslator locationAsmGenerator) {
    this.savePushValueToDTranslator = new SavePushValueToDTranslator();
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return concat(savePushValueToDTranslator.translate(parsedLine), PUSH_ASM_SEQUENCE);
  }

  private ImmutableList<String> concat(ImmutableList<String> first, ImmutableList<String> second) {
    return Streams.concat(first.stream(), second.stream()).collect(ImmutableList.toImmutableList());
  }
}
