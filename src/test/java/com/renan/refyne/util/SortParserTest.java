package com.renan.refyne.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SortParser")
class SortParserTest {

  @Nested
  @DisplayName("parse(String)")
  class Parse {

    @Test
    @DisplayName("null returns default DESC appliedAt")
    void nullReturnsDefault() {
      Sort sort = SortParser.parse(null);
      assertThat(sort.getOrderFor("appliedAt")).isNotNull();
      assertThat(sort.getOrderFor("appliedAt").isDescending()).isTrue();
    }

    @Test
    @DisplayName("blank string returns default sort")
    void blankReturnsDefault() {
      Sort sort = SortParser.parse("   ");
      assertThat(sort.getOrderFor("appliedAt")).isNotNull();
      assertThat(sort.getOrderFor("appliedAt").isDescending()).isTrue();
    }

    @Test
    @DisplayName("field,asc produces ascending sort on given field")
    void ascDirectionIsParsed() {
      Sort sort = SortParser.parse("fullName,asc");
      assertThat(sort.getOrderFor("fullName")).isNotNull();
      assertThat(sort.getOrderFor("fullName").isAscending()).isTrue();
    }

    @Test
    @DisplayName("field,desc produces descending sort on given field")
    void descDirectionIsParsed() {
      Sort sort = SortParser.parse("email,desc");
      assertThat(sort.getOrderFor("email")).isNotNull();
      assertThat(sort.getOrderFor("email").isDescending()).isTrue();
    }

    @Test
    @DisplayName("field with no direction defaults to DESC")
    void missingDirectionDefaultsToDesc() {
      Sort sort = SortParser.parse("city");
      assertThat(sort.getOrderFor("city")).isNotNull();
      assertThat(sort.getOrderFor("city").isDescending()).isTrue();
    }

    @Test
    @DisplayName("direction matching is case-insensitive")
    void directionIsCaseInsensitive() {
      assertThat(SortParser.parse("fullName,ASC").getOrderFor("fullName").isAscending()).isTrue();
      assertThat(SortParser.parse("fullName,Asc").getOrderFor("fullName").isAscending()).isTrue();
      assertThat(SortParser.parse("fullName,DESC").getOrderFor("fullName").isDescending()).isTrue();
    }

    @Test
    @DisplayName("strips whitespace from field and direction")
    void stripsWhitespace() {
      Sort sort = SortParser.parse("fullName , asc");
      assertThat(sort.getOrderFor("fullName")).isNotNull();
      assertThat(sort.getOrderFor("fullName").isAscending()).isTrue();
    }
  }

  @Nested
  @DisplayName("toPageable(int, int, String)")
  class ToPageable {

    @Test
    @DisplayName("builds pageable with correct page number and size")
    void pageAndSizeAreApplied() {
      Pageable pageable = SortParser.toPageable(2, 5, null);
      assertThat(pageable.getPageNumber()).isEqualTo(2);
      assertThat(pageable.getPageSize()).isEqualTo(5);
    }

    @Test
    @DisplayName("sort string is applied to pageable")
    void sortIsApplied() {
      Pageable pageable = SortParser.toPageable(0, 10, "fullName,asc");
      assertThat(pageable.getSort().getOrderFor("fullName")).isNotNull();
      assertThat(pageable.getSort().getOrderFor("fullName").isAscending()).isTrue();
    }

    @Test
    @DisplayName("null sort produces default pageable sort")
    void nullSortProducesDefault() {
      Pageable pageable = SortParser.toPageable(0, 10, null);
      assertThat(pageable.getSort().getOrderFor("appliedAt")).isNotNull();
      assertThat(pageable.getSort().getOrderFor("appliedAt").isDescending()).isTrue();
    }
  }
}
