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

package com.eightkdata.mongowp;

import javax.annotation.Nullable;

/**
 *
 */
public enum MongoVersion {

  UNKNOWN(0, 0),
  V2_4(2, 4),
  V2_6(2, 6),
  V3_0(3, 0),
  V3_2(3, 2);

  private final int major;
  private final int minor;

  private MongoVersion(int major, int minor) {
    this.major = major;
    this.minor = minor;
  }

  @Nullable
  public static MongoVersion fromMongoString(String versionString) {
    if (versionString == null) {
      return null;
    }
    int firstDot = versionString.indexOf('.');
    if (firstDot < 0) {
      throw new IllegalArgumentException(versionString + " is not "
          + "recognized as a version string");
    }
    if (firstDot == versionString.length() - 1) {
      throw new IllegalArgumentException(versionString + " is not "
          + "recognized as a version string");
    }
    String majorString = versionString.substring(0, firstDot);
    int major;
    try {
      major = Integer.parseInt(majorString);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(versionString + " is not "
          + "recognized as a version string", ex);
    }

    int minor;
    String minorString;
    int secondDot = versionString.indexOf('.', firstDot + 1);
    if (secondDot < 0) {
      minorString = versionString.substring(firstDot + 1);
    } else {
      minorString = versionString.substring(firstDot + 1, secondDot);
    }

    try {
      minor = Integer.parseInt(minorString);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(versionString + " is not "
          + "recognized as a version string", ex);
    }

    return fromIntVersion(major, minor);
  }

  public static MongoVersion fromIntVersion(int major, int minor) {
    for (MongoVersion value : MongoVersion.values()) {
      if (!value.equals(UNKNOWN) && value.major == major && value.minor == minor) {
        return value;
      }
    }
    return UNKNOWN;
  }

  public int getMajor() {
    return major;
  }

  public int getMinor() {
    return minor;
  }

}
