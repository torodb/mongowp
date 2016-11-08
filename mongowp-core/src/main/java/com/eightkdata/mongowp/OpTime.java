/*
 * MongoWP - MongoWP: Core
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © 2014 8Kdata (www.8kdata.com)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © 2014 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © ${project.inceptionYear} 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © ${project.inceptionYear} ${owner} (${email})
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp;

import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.bson.utils.TimestampToDateTime;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.DateTimeField;
import com.eightkdata.mongowp.fields.LongField;
import com.eightkdata.mongowp.fields.TimestampField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.primitives.UnsignedLongs;
import java.io.Serializable;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * OpTime encompasses an Instant and a 64-bit Term
 * number. OpTime can be used to label every op in an oplog with a unique
 * identifier.
 */
public class OpTime implements Comparable<OpTime>, Serializable {

    private static final TimestampField TIMESTAMP_FIELD = new TimestampField("ts");
    private static final LongField TERM_FIELD = new LongField("t");
    private static final long serialVersionUID = 2849102384020201828L;
    /**
     * The term of an OpTime generated by old protocol version.
     */
    private static final long UNINITIALIZED_TERM = -1;
    private static final BsonInt64 UNINITIALIZED_TERM_BSON = DefaultBsonValues.newLong(-1);
    /**
     * The initial term after the first time upgrading from protocol version 0.
     * <p>
     * This is also the initial term for nodes that were recently started up but
     * have not yet joined the cluster, all in protocol version 1.
     */
    private static final long INITIAL_TERM = 0;
    /**
     * Default OpTime, also the smallest one.
     */
    public static final OpTime EPOCH = new OpTime(
            DefaultBsonValues.newTimestamp(0, 0),
            UNINITIALIZED_TERM);

    private final BsonTimestamp timestamp;
    private final long term;

    public OpTime(BsonTimestamp timestamp, long term) {
        this.timestamp = timestamp;
        this.term = term;
    }

    /**
     * Constructor used to create optimes from older replication versions, where
     * {@link #getTerm()} is initialized to a default value.
     * @param timestamp
     */
    public OpTime(BsonTimestamp timestamp) {
        this(timestamp, UNINITIALIZED_TERM);
    }

    public static OpTime ofSeconds(int seconds) {
        return new OpTime(DefaultBsonValues.newTimestamp(seconds, 0));
    }

    @Nonnull
    public static OpTime fromBson(BsonDocument doc) throws TypesMismatchException, NoSuchKeyException {
        BsonTimestamp ts = BsonReaderTool.getTimestamp(doc, TIMESTAMP_FIELD);
        //TODO(gortiz): check precision lost
        long term = BsonReaderTool.getNumeric(doc, TERM_FIELD)
                .getValue()
                .longValue();
        return new OpTime(ts, term);
        
    }

    public static OpTime fromOldBson(BsonValue<?> val) {
        BsonTimestamp ts = TimestampToDateTime.toTimestamp(
                val.asDateTime(), DefaultBsonValues::newTimestamp);
        return new OpTime(ts);
    }

    /**
     *
     * @return the serialization of this optime on a bson, using the old version
     */
    public BsonDateTime toOldBson() {
        return DefaultBsonValues.newDateTime(getTimestamp());
    }

    public void appendAsOldBson(BsonDocumentBuilder builder, String fieldId) {
        builder.appendUnsafe(fieldId, toOldBson());
    }

    public void appendAsOldBson(BsonDocumentBuilder builder, DateTimeField field) {
        appendAsOldBson(builder, field.getFieldName());
    }

    @Nonnull
    public static OpTime fromOplogEntry(BsonDocument doc) throws TypesMismatchException, NoSuchKeyException {
        BsonTimestamp ts = BsonReaderTool.getTimestamp(doc, TIMESTAMP_FIELD);
        //TODO(gortiz): check precision lost
        long term = BsonReaderTool.getNumeric(doc, TERM_FIELD, UNINITIALIZED_TERM_BSON)
                .getValue()
                .longValue();
        return new OpTime(ts, term);
    }

    public int getSecs() {
        return timestamp.getSecondsSinceEpoch();
    }

    public long getTerm() {
        return term;
    }

    public BsonTimestamp getTimestamp() {
        return timestamp;
    }

    public BsonDocument toBson() {
        return new BsonDocumentBuilder(2)
                .append(TIMESTAMP_FIELD, timestamp)
                .append(TERM_FIELD, term)
                .build();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.timestamp);
        hash = 13 * hash + (int) (this.term ^ (this.term >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpTime other = (OpTime) obj;
        if (this.term != other.term) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(OpTime o) {
        int diff = timestamp.compareTo(o.getTimestamp());
        if (diff != 0) {
            return diff;
        }
        return UnsignedLongs.compare(term, o.getTerm());
    }

    public final boolean isAfter(@Nonnull OpTime other) {
        return compareTo(other) > 0;
    }

    public final boolean isEqualOrAfter(@Nonnull OpTime other) {
        return compareTo(other) >= 0;
    }

    public final boolean isBefore(@Nonnull OpTime other) {
        return compareTo(other) < 0;
    }

    public final boolean isEqualOrBefore(@Nonnull OpTime other) {
        return compareTo(other) <= 0;
    }

    @Override
    public String toString() {
        return "{t: " + timestamp + ", i: "+ UnsignedLongs.toString(term) + "}";
    }
}
