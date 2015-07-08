
package com.eightkdata.mongowp.mongoserver.api.safe.pojos;

import com.google.common.primitives.UnsignedInteger;
import org.bson.BsonTimestamp;
import org.threeten.bp.Instant;

/**
 * OpTime encompasses an Instant and a 64-bit Term
 * number. OpTime can be used to label every op in an oplog with a unique
 * identifier.
 */
public class OpTime implements Comparable<OpTime> {
 
    private final UnsignedInteger secs;
    private final UnsignedInteger term;

    public OpTime(Instant instant) {
        this.secs = UnsignedInteger.valueOf(instant.getEpochSecond());
        this.term = UnsignedInteger.valueOf(instant.getNano());
    }

    public OpTime(Instant instant, UnsignedInteger term) {
        this.secs = UnsignedInteger.valueOf(instant.getEpochSecond());
        this.term = term;
    }

    public OpTime(UnsignedInteger secs, UnsignedInteger term) {
        this.secs = secs;
        this.term = term;
    }

    public UnsignedInteger getSecs() {
        return secs;
    }
    
    public UnsignedInteger getTerm() {
        return term;
    }

    public BsonTimestamp asBsonTimestamp() {
        return new BsonTimestamp(secs.intValue(), term.intValue());
    }

    public long toEpochMilli() {
        return secs.longValue() * 1000;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.secs != null ? this.secs.hashCode() : 0);
        hash = 29 * hash + (this.term != null ? this.term.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpTime other = (OpTime) obj;
        if (this.secs != other.secs && (this.secs == null ||
                !this.secs.equals(other.secs))) {
            return false;
        }
        if (this.term != other.term && (this.term == null ||
                !this.term.equals(other.term))) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(OpTime o) {
        int diff = secs.compareTo(o.getSecs());
        if (diff != 0) {
            return diff;
        }
        return term.compareTo(o.getTerm());
    }
}
