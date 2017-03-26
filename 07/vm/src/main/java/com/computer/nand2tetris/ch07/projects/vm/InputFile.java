package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;

/**
 * Created by jpiyush on 3/25/17.
 */

@AutoValue
abstract class InputFile {
  static InputFile create(String path, String baseName) {
    return new AutoValue_InputFile(path, baseName);
  }
  abstract String path();
  abstract String baseName();

  public String toString() {
    return String.format("%s [%s]", path(), baseName());
  }
}
