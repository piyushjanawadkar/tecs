package com.computer.nand2tetris.ch07.projects.vm;

import com.google.auto.value.AutoValue;

/**
 * Created by jpiyush on 6/27/17.
 */
@AutoValue
abstract class Label {
  static Label create(String text) {
    return new AutoValue_Label(text);
  }

  abstract String text();

  public String generateDefinitionText() {
    return String.format("(%s)", text());
  }
}
