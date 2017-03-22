package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jpiyush on 3/21/17.
 */
@AutoValue
abstract class IOPaths {
    static IOPaths create(ImmutableList<String> inputPaths, String outputPath) {
        return new AutoValue_IOPaths(inputPaths, outputPath);
    }

    abstract ImmutableList<String> inputPaths();

    abstract String outputPath();

    public String toString() {
        return Streams.concat(
                Stream.of(outputPath()),
                inputPaths().stream()).collect(Collectors.joining(", "));
    }
}
