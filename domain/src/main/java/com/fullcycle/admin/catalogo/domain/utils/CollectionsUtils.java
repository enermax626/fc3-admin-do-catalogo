package com.fullcycle.admin.catalogo.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionsUtils {

  public static <IN, OUT> Set<OUT> mapTo(Set<IN> input, Function<IN, OUT> mapper) {
    return input.stream().map(mapper).collect(Collectors.toSet());
  }

}
