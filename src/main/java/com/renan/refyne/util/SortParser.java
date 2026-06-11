package com.renan.refyne.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Utility for converting raw HTTP sort query parameters into Spring Data
 * {@link Sort} and {@link Pageable} objects.
 *
 * <p>Expected sort format: {@code field,direction} (e.g. {@code fullName,asc}).
 * Direction defaults to {@code desc} when omitted or unrecognised.
 */
public class SortParser {

  private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "appliedAt");

  private SortParser() {}

  /**
   * Parses a sort string into a Spring Data {@link Sort}.
   * Returns {@link #DEFAULT_SORT} when {@code sort} is null or blank.
   */
  public static Sort parse(String sort) {
    if (sort == null || sort.isBlank()) {
      return DEFAULT_SORT;
    }
    String[] parts = sort.split(",");
    String field = parts[0].strip();
    Sort.Direction dir = parts.length > 1 && parts[1].strip().equalsIgnoreCase("asc")
      ? Sort.Direction.ASC
      : Sort.Direction.DESC;
    return Sort.by(dir, field);
  }

  /**
   * Builds a {@link Pageable} from raw page/size/sort query parameters.
   * Parsing the sort string is delegated to {@link #parse(String)}.
   */
  public static Pageable toPageable(int page, int size, String sort) {
    return PageRequest.of(page, size, parse(sort));
  }
}
