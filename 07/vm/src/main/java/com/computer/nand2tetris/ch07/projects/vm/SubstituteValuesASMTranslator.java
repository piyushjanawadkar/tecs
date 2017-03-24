package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by jpiyush on 3/24/17.
 */
public class SubstituteValuesASMTranslator implements ASMTranslator {

  private static final ImmutableMap<String, Function<ParsedLine, String>> dictionary =
      ImmutableMap.<String, Function<ParsedLine, String>>builder()
          .put("{LINENUM}", l -> String.valueOf(l.index()))
          .build();

  private static final String FORMAT_DELIMITER = "!!";

  private final ImmutableList<String> asmSequence;
  private final String[] args;

  public SubstituteValuesASMTranslator(ImmutableList<String> asmSequence, String... args) {
    this.asmSequence = asmSequence;
    this.args = args;
  }

  private static String generateFormat(ImmutableList<String> asmSequence, ParsedLine parsedLine) {
    return asmSequence.stream().map(l -> expand(l, parsedLine))
        .collect(Collectors.joining(FORMAT_DELIMITER));
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
    String formattedString = String
        .format(generateFormat(asmSequence, parsedLine), (Object[]) args);
    return ImmutableList.copyOf(formattedString.split(FORMAT_DELIMITER));
  }
}
