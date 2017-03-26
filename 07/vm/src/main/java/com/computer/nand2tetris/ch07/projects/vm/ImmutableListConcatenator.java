package com.computer.nand2tetris.ch07.projects.vm;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

/**
 * Created by jpiyush on 3/25/17.
 */
public class ImmutableListConcatenator {

  public static ImmutableList<String> concat(ImmutableList<String> first,
      ImmutableList<String> second) {
    return Streams.concat(first.stream(), second.stream()).collect(ImmutableList.toImmutableList());
  }
}
