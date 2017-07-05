package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map.Entry;
import java.util.function.Function;

/**
 * Created by jpiyush on 3/24/17.
 */
public class SubstituteValuesAssemblyTranslator implements AssemblyTranslator {

  private static final ImmutableMap<String, Function<ParsedLine, String>> dictionary =
      ImmutableMap.<String, Function<ParsedLine, String>>builder()
          .put("{RELATIONAL_OP_LABEL_REFERENCE}",
              l -> Labels.relationalOpLabelOf(l).generateReferenceText())
          .put("{RELATIONAL_OP_DONE_LABEL_REFERENCE}",
              l -> Labels.relationalOpDoneLabelOf(l).generateReferenceText())
          .put("{RELATIONAL_OP_LABEL_DEFINITION}",
              l -> Labels.relationalOpLabelOf(l).generateDefinitionText())
          .put("{RELATIONAL_OP_DONE_LABEL_DEFINITION}",
              l -> Labels.relationalOpDoneLabelOf(l).generateDefinitionText())
          .build();

  private final ImmutableList<String> asmSequence;
  private final Object[] args;

  public SubstituteValuesAssemblyTranslator(ImmutableList<String> asmSequence, Object... args) {
    this.asmSequence = asmSequence;
    this.args = args;
  }

  private static ImmutableList<String> generateFormat(ImmutableList<String> asmSequence,
      ParsedLine parsedLine) {
    return asmSequence.stream().map(l -> expand(l, parsedLine))
        .collect(ImmutableList.toImmutableList());
  }

  private static String expand(String pattern, ParsedLine parsedLine) {
    String value = pattern;
    for (Entry<String, Function<ParsedLine, String>> entry : dictionary.entrySet()) {
      value = value.replace(entry.getKey(), entry.getValue().apply(parsedLine));
    }

    return value;
  }

  @Override
  public ImmutableList<String> translate(ParsedLine parsedLine) {
    return AssemblySequenceFormatter.format(generateFormat(asmSequence, parsedLine), args);
  }
}
