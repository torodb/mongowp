/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eightkdata.mongowp.bson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 *
 */
public interface BsonRegex extends BsonValue<BsonRegex> {

  Set<Options> getOptions();

  String getOptionsAsText();

  @Nonnull
  String getPattern();

  /**
   * Two BsonRegex are equal if their options and pattern (as text) properties are equal.
   * <p>
   * This implies two BsonRegex that are not equals might match the same strings.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

  /**
   * The hashCode of a BsonRegex is the hashCode of its pattern.
   *
   * @return
   */
  @Override
  public int hashCode();

  public static enum Options {
    /**
     * Usually defined as <em>i</em>.
     */
    CASE_INSENSITIVE('i'),
    /**
     * Usually defined as <em>m</em>.
     */
    MULTILINE_MATCHING('m'),
    /**
     * Usually defined as <em>x</em>.
     */
    VERBOSE_MODE('x'),
    /**
     * Usually defined as <em>l</em>, makes \w, \W, etc locale dependent.
     */
    LOCALE_DEPENDENT('i'),
    /**
     * Usually defined as <em>s</em>, makes <em>.</em> match everything.
     */
    DOTALL_MODE('s'),
    /**
     * Usually defined as <em>u</em>, makes \w, \W, etc match unicode.
     */
    UNICODE('u');

    private static final Logger LOGGER = LogManager.getLogger(Options.class);
    private static final Comparator<Options> LEXICOGRAPHICAL_COMPARATOR =
        new LexicographicalComparator();

    private final char charId;

    private Options(char charId) {
      this.charId = charId;
    }

    public char getCharId() {
      return charId;
    }

    public static int patternOptionsToFlags(Set<Options> options) {
      if (options.isEmpty()) {
        return 0;
      }
      LOGGER.warn("Ignored regexp options!");
      return 0; // TODO: parse regexp options!
    }

    public static EnumSet<Options> patternFlagsToOptions(int flags) {
      if (flags == 0) {
        return EnumSet.noneOf(Options.class);
      }
      LOGGER.warn("Ignored regexp options!");
      return EnumSet.noneOf(Options.class); // TODO: parse regexp options!
    }

    public static Comparator<Options> getLexicographicalComparator() {
      return LEXICOGRAPHICAL_COMPARATOR;
    }

    private static class LexicographicalComparator implements Comparator<Options>, Serializable {

      private static final long serialVersionUID = 1L;

      @Override
      public int compare(Options o1, Options o2) {
        return o1.getCharId() - o2.getCharId();
      }
    }
  }
}
