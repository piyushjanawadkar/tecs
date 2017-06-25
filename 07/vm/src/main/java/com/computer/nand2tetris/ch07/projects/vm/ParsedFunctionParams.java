package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;

/**
 * Created by jpiyush on 6/25/17.
 */
@AutoValue
abstract class ParsedFunctionParams {
  static ParsedFunctionParams create(String name, int numLocalArgs) {
    return new AutoValue_ParsedFunctionParams(name, numLocalArgs);
  }

  abstract String name();

  abstract int numLocalArgs();
}
