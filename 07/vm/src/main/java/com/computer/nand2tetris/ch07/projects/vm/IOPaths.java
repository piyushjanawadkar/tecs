package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/21/17.
 */
@AutoValue
abstract class IOPaths {

  static IOPaths create(ImmutableList<InputFile> inputFiles, String outputPath) {
    return new AutoValue_IOPaths(inputFiles, outputPath);
  }

  abstract ImmutableList<InputFile> inputFiles();

  abstract String outputPath();

  public String toString() {
    return Streams.concat(
        Stream.of(outputPath()),
        inputFiles().stream().map(InputFile::toString)).collect(Collectors.joining(", "));
  }
}
