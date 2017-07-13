package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * Created by jpiyush on 3/21/17.
 */
class IOPathsGenerator {

  private static final String VM_FILE_EXTENSION = ".vm";
  private static final String ASM_FILE_EXTENSION = ".asm";

  private static final FileFilter IS_VM_PATH = new FileFilter() {
    public boolean accept(File path) {
      return hasVMSuffix(path.getName());
    }
  };

  private static ImmutableList<String> generateInputPaths(String inputLocation) {
    File path = new File(inputLocation);
    if (!path.isDirectory()) {
      Preconditions.checkArgument(IS_VM_PATH.accept(path));
      return ImmutableList.of(inputLocation);
    }

    File[] vmFiles = path.listFiles(IS_VM_PATH);
    return Arrays.stream(vmFiles).map(File::getAbsolutePath)
        .collect(ImmutableList.toImmutableList());
  }

  private static String generateOutputPath(String inputLocation) {
    if (!hasVMSuffix(inputLocation)) {
      String fileName = extractBaseName(inputLocation) + ASM_FILE_EXTENSION;
      return extendPath(inputLocation, fileName);
    }

    return replaceExtension(inputLocation, VM_FILE_EXTENSION, ASM_FILE_EXTENSION);
  }

  private static String extendPath(String path, String pathComponent) {
    return new File(path, pathComponent).getPath();
  }

  private static String getExtensionRegex(String extension) {
    return extension + "$";
  }

  private static boolean hasVMSuffix(String path) {
    return path.endsWith(VM_FILE_EXTENSION);
  }

  private static ImmutableList<InputFile> generateInputFiles(ImmutableList<String> inputPaths) {
    return inputPaths.stream().map(p -> InputFile.create(p, extractFileBaseName(p))).collect(
        ImmutableList.toImmutableList());
  }

  private static String extractFileBaseName(String path) {
    String fileName = extractBaseName(path);
    return replaceExtension(fileName, VM_FILE_EXTENSION, "");
  }

  private static String extractBaseName(String path) {
    return new File(path).getName();
  }

  private static String replaceExtension(String path, String oldExtension, String newExtension) {
    return path.replaceFirst(getExtensionRegex(oldExtension), newExtension);
  }

  IOPaths generate(String[] args) {
    String inputLocation = Iterables.getOnlyElement(Arrays.asList(args));
    ImmutableList<InputFile> inputFiles = generateInputFiles(generateInputPaths(inputLocation));
    return IOPaths.create(inputFiles, generateOutputPath(inputLocation));
  }
}
