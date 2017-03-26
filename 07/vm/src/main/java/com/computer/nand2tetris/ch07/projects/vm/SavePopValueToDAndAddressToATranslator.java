package com.computer.nand2tetris.ch07.projects.vm;

import com.computer.nand2tetris.ch07.projects.vm.ParsedLocation.SegmentType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Created by jpiyush on 3/25/17.
 */

// pop contract: pop leaves popped value in D and address to push in A
// default pop:
//        (
//          (
//            (save location to D)
//            (save D to @13)
//          )
//          (execute pop to D)
//          (restore A from @13)
//        )
//
// pop to static
//        (
//          ()
//          (execute pop to D)
//          (@Xxx.y)
//        )
//
// pop to constant is an error
class SavePopValueToDAndAddressToATranslator implements AssemblyTranslator {

  private static final int TMP_MEMORY_ADDRESS = 13;

  private static final AssemblyTranslator SAVE_D_TO_TMP_MEMORY_ASSEMBLY_TRANSLATOR =
      new EmitAssemblyTranslator(
          ImmutableList.of(
              "@" + TMP_MEMORY_ADDRESS,
              "M=D"
          ));

  private static final AssemblyTranslator RESTORE_A_FROM_TMP_MEMORY_ASSEMBLY_TRANSLATOR =
      new EmitAssemblyTranslator(
          ImmutableList.of(
              "@" + TMP_MEMORY_ADDRESS,
              "A=M"
          ));

  private static final AssemblyTranslator EXECUTE_POP_TO_D_TRANSLATOR =
      new EmitAssemblyTranslator(
          ImmutableList.of(
              "@SP",
              "M=M-1",
              "A=M",
              "D=M"
          )
      );

  private static final AssemblyTranslator SAVE_ADDRESS_POP_RESTORE_ADDRESS_TRANSLATOR =
      new AssemblySequenceTranslator(
          SAVE_D_TO_TMP_MEMORY_ASSEMBLY_TRANSLATOR,
          EXECUTE_POP_TO_D_TRANSLATOR,
          RESTORE_A_FROM_TMP_MEMORY_ASSEMBLY_TRANSLATOR);

  private ImmutableMap<ParsedLocation.SegmentType, AssemblyTranslator> valueAddressTranslators =
      ImmutableMap.<ParsedLocation.SegmentType, AssemblyTranslator>builder()

          .put(SegmentType.SEGMENT_STATIC,
              new AssemblySequenceTranslator(
                  EXECUTE_POP_TO_D_TRANSLATOR,
                  new SaveStaticAddressToATranslator()))

          .put(SegmentType.SEGMENT_POINTER,
              new AssemblySequenceTranslator(
                  EXECUTE_POP_TO_D_TRANSLATOR,
                  new SaveConstantValueToATranslator(POINTER_BASE_ADDRESS)))

          .put(SegmentType.SEGMENT_TEMP,
              new AssemblySequenceTranslator(
                  EXECUTE_POP_TO_D_TRANSLATOR,
                  new SaveConstantValueToATranslator(TEMP_BASE_ADDRESS)))

          .put(SegmentType.SEGMENT_LOCAL,
              new AssemblySequenceTranslator(
                  new SaveLocationTranslator("LCL", "D"),
                  SAVE_ADDRESS_POP_RESTORE_ADDRESS_TRANSLATOR))

          .put(SegmentType.SEGMENT_ARGUMENT,
              new AssemblySequenceTranslator(
                  new SaveLocationTranslator("ARG", "D"),
                  SAVE_ADDRESS_POP_RESTORE_ADDRESS_TRANSLATOR))

          .put(SegmentType.SEGMENT_THIS,
              new AssemblySequenceTranslator(
                  new SaveLocationTranslator("THIS", "D"),
                  SAVE_ADDRESS_POP_RESTORE_ADDRESS_TRANSLATOR))

          .put(SegmentType.SEGMENT_THAT,
              new AssemblySequenceTranslator(
                  new SaveLocationTranslator("THAT", "D"),
                  SAVE_ADDRESS_POP_RESTORE_ADDRESS_TRANSLATOR))

          .build();

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    AssemblyTranslator translator = valueAddressTranslators
        .get(parsedLine.location().get().segmentType());
    Preconditions.checkNotNull(translator, "No translator found for %s in %s",
        parsedLine.location().get().segmentType(),
        parsedLine.line());
    return translator.translate(parsedLine);
  }
}
