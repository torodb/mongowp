
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import java.util.Map.Entry;
import javax.annotation.concurrent.Immutable;
import org.bson.*;

/**
 * Replication configuration about a particular member of a replica set.
 */
@Immutable
public class MemberConfig {
    private static final String ID_FIELD_NAME = "_id";
    private static final BsonInt32 DEFAULT_VOTES = new BsonInt32(1);
    private static final BsonDouble DEFAULT_PRIORITY = new BsonDouble(1);
    private static final boolean DEFAULT_ARBITER_ONLY = false;
    private static final BsonInt64 DEFAULT_SLAVE_DELAY = new BsonInt64(0);
    private static final boolean DEFAULT_HIDDEN = false;
    private static final boolean DEFAULT_BUILD_INDEXES = true;
    private final int id;
    private final HostAndPort host;
    private final double priority;
    private final int votes;
    private final boolean arbiterOnly;
    /**
     * In seconds
     */
    private final long slaveDelay;
    private final boolean hidden;
    private final boolean buildIndexes;
    private final ImmutableMap<String, String> tags;

    public MemberConfig(int id, HostAndPort host, double priority, int votes, boolean arbiterOnly, long slaveDelay, boolean hidden, boolean buildIndexes, ImmutableMap<String, String> tags) {
        this.id = id;
        this.host = host;
        this.priority = priority;
        this.votes = votes;
        this.arbiterOnly = arbiterOnly;
        this.slaveDelay = slaveDelay;
        this.hidden = hidden;
        this.buildIndexes = buildIndexes;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public HostAndPort getHost() {
        return host;
    }

    public double getPriority() {
        return priority;
    }

    public int getVotes() {
        return votes;
    }

    public boolean isArbiter() {
        return arbiterOnly;
    }

    public long getSlaveDelay() {
        return slaveDelay;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isBuildIndexes() {
        return buildIndexes;
    }

    public ImmutableMap<String, String> getTags() {
        return tags;
    }

    public static MemberConfig fromDocument(BsonDocument bson) throws
            TypesMismatchException, NoSuchKeyException {
        int id = BsonReaderTool.getNumeric(bson, "_id").intValue();
        HostAndPort host = BsonReaderTool.getHostAndPort(bson, "host");
        int votes
                = BsonReaderTool.getNumeric(bson, "votes", DEFAULT_VOTES).intValue();
        double priority
                = BsonReaderTool.getNumeric(bson, "priority", DEFAULT_PRIORITY).doubleValue();
        boolean arbiterOnly
                = BsonReaderTool.getBooleanOrNumeric(bson, "arbiterOnly", DEFAULT_ARBITER_ONLY);
        long slaveDelay
                = BsonReaderTool.getNumeric(bson, "slaveDelay", DEFAULT_SLAVE_DELAY).longValue();
        boolean hidden
                = BsonReaderTool.getBooleanOrNumeric(bson, "hidden", DEFAULT_HIDDEN);
        boolean buildIndexes
                = BsonReaderTool.getBooleanOrNumeric(bson, "buildIndexes", DEFAULT_BUILD_INDEXES);
        BsonDocument castedTags = BsonReaderTool.getDocument(bson, "tags");
        ImmutableMap.Builder<String, String> tagsBuilder
                = ImmutableMap.builder();
        for (String field : castedTags.keySet()) {
            BsonValue value = castedTags.get(field);
            if (value == null || value.isString()) {
                throw new TypesMismatchException(field, "string", value == null
                        ? null : value.getBsonType());
            }
            String castedValue = value.asString().getValue();
            tagsBuilder.put(field, castedValue);
        }
        return new MemberConfig(id, host, priority, votes, arbiterOnly, slaveDelay, hidden, buildIndexes, tagsBuilder.build());
    }

    public BsonDocument toBSON() {
        BsonDocument object = new BsonDocument();
        object.put(ID_FIELD_NAME, new BsonInt32(id));
        object.put("host", new BsonString(host.toString()));
        object.put("arbiterOnly", BsonBoolean.valueOf(arbiterOnly));
        object.put("buildIndexes", BsonBoolean.valueOf(buildIndexes));
        object.put("hidden", BsonBoolean.valueOf(hidden));
        object.put("priority", new BsonDouble(priority));
        BsonDocument tagsDoc = new BsonDocument();
        for (Entry<String, String> entry : tags.entrySet()) {
            tagsDoc.put(entry.getKey(), new BsonString(entry.getValue()));
        }
        object.put("tags", tagsDoc);
        object.put("slaveDelay", new BsonInt64(slaveDelay));
        object.put("votes", new BsonInt32(votes));
        return object;
    }

    boolean isVoter() {
        return votes != 0;
    }

}
