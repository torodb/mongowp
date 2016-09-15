package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

/**
 *
 */
public enum MemberState {

    /**
     * serving still starting up, or still trying to initiate the set.
     */
    RS_STARTUP(0),
    /**
     * this server thinks it is primary.
     */
    RS_PRIMARY(1),
    /**
     * this server thinks it is a secondary (slave mode).
     */
    RS_SECONDARY(2),
    /**
     * recovering/resyncing; after recovery usually auto-transitions to secondary.
     */
    RS_RECOVERING(3),
    /**
     * loaded config, still determining who is primary.
     */
    RS_STARTUP2(5),
    /**
     * remote node not yet reached
     */
    RS_UNKNOWN(6),
    RS_ARBITER(7),
    /**
     * node not reachable for a report
     */
    RS_DOWN(8),
    RS_ROLLBACK(9),
    /**
     * node removed from replica set
     */
    RS_REMOVED(10);

    private final int id;

    private MemberState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static MemberState fromId(int id) throws IllegalArgumentException {
        for (MemberState value : MemberState.values()) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("There is no member state whose id is equal to '"
                + id + "'");
    }

    public static int getMaxId() {
        return values()[values().length - 1].id;
    }
}
