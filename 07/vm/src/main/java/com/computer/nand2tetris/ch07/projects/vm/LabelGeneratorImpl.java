package com.computer.nand2tetris.ch07.projects.vm;

import com.computer.nand2tetris.ch07.projects.vm.ParsedLine.LineType;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MoreCollectors;
import java.util.Optional;

/**
 * Created by jpiyush on 6/27/17.
 */
public class LabelGeneratorImpl implements LabelGenerator {

  private static final ImmutableList<LabelGenerator> generators = ImmutableList.of(
      new PostFunctionCallLabelGenerator()
  );

  @Override
  public boolean supports(LineType lineType) {
    return generators.stream().anyMatch(g -> g.supports(lineType));
  }

  @Override
  public Label generate(ParsedLine parsedLine) {
    Optional<LabelGenerator> generator = generators.stream()
        .filter(g -> g.supports(parsedLine.type())).collect(MoreCollectors.toOptional());
    Preconditions
        .checkArgument(generator.isPresent(), "No generator bound to: " + parsedLine.type());
    return generator.get().generate(parsedLine);
  }
}
