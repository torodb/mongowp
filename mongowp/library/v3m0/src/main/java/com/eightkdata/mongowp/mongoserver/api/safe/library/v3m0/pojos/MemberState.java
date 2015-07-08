package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

/**
 *
 */
public enum MemberState {

    RS_STARTUP(0),
    RS_PRIMARY(1),
    RS_SECONDARY(2),
    RS_RECOVERING(3),
    RS_STARTUP2(5),
    RS_UNKNOWN(6),
    /*
     * remote node not yet reached
     */
    RS_ARBITER(7),
    RS_DOWN(8), /*
     * node not reachable for a report
     */

    RS_ROLLBACK(9),
    RS_REMOVED(10), /*
     * node removed from replica set
     */

    RS_MAX(10);

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
        throw new IllegalArgumentException("There is no member state whose id is equal to '"+id+"'");
    }
}
