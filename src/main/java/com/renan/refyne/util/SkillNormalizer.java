package com.renan.refyne.util;

import java.text.Normalizer;

public class SkillNormalizer {

  public static String normalize(String input) {
    if (input == null) return null;

    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
      .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
      .toLowerCase()
      .replaceAll("[^a-z0-9]", "");

    return normalized;
  }
}
