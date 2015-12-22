
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

/**
 *
 */
public enum ReplSetProtocolVersion {
    V0(0),
    V1(1);
    
    private final long versionId;

    private ReplSetProtocolVersion(long versionId) {
        this.versionId = versionId;
    }

    public long getVersionId() {
        return versionId;
    }
    
    public static ReplSetProtocolVersion fromVersionId(long versionId) {
        for (ReplSetProtocolVersion value : ReplSetProtocolVersion.values()) {
            if (value.versionId == versionId) {
                return value;
            }
        }
        throw new IllegalArgumentException(
                "There is no replica set protocol version whose version id "
                + "is " + versionId
        );
    }
}
