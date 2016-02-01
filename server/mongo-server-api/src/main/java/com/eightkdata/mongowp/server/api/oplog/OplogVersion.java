
package com.eightkdata.mongowp.server.api.oplog;

/**
 *
 */
public enum OplogVersion {
    V1(1),
    V2(2);

    private final int numericValue;

    private OplogVersion(int numericValue) {
        this.numericValue = numericValue;
    }

    public static OplogVersion valueOf(int i) {
        for (OplogVersion oplogVersion : OplogVersion.values()) {
            if (oplogVersion.numericValue == i) {
                return oplogVersion;
            }
        }
        throw new IllegalArgumentException("Unknown version "+ i);
    }

    public int getNumericValue() {
        return numericValue;
    }
}
