package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;
import com.google.common.base.Optional;


/**
 * Created by jpiyush on 6/25/17.
 */
@AutoValue
abstract class ParsedFunctionParams {
  static ParsedFunctionParams create(String name, Optional<Integer> numArgs, Optional<Integer> numLocalArgs) {
    return new AutoValue_ParsedFunctionParams(name, numArgs, numLocalArgs);
  }

  abstract String name();

  abstract Optional<Integer> numArgs();

  abstract Optional<Integer> numLocalArgs();
}
