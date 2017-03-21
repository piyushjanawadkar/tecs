package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by jpiyush on 3/21/17.
 */
@AutoValue
abstract class IOPaths {
    public String outputAsmPath() {
        return null;
    }

    public List<String> inputVmPaths() {
        return null;
    }

    public static IOPaths create(ImmutableList<String> inputPaths, String outputPath) {
        return new AutoValue_IOPaths(inputPaths, outputPath);
    }

    abstract ImmutableList<String> inputPaths();
    abstract String outputPath();
}
