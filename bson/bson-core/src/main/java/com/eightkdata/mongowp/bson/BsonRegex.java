/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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

  @Nonnull
  Set<Options> getOptions();

  public String getOptionsAsText();

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
