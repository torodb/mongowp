
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
