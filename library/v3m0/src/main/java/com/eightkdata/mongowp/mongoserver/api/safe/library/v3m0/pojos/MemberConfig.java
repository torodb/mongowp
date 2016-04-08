
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonInt64;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import javax.annotation.concurrent.Immutable;

/**
 * Replication configuration about a particular member of a replica set.
 */
@Immutable
public class MemberConfig {

    public static final HostAndPortField HOST_FIELD = new HostAndPortField("host");
    public static final BooleanField ARBITER_ONLY_FIELD = new BooleanField("arbiterOnly");
    public static final BooleanField BUILD_INDEXES_FIELD = new BooleanField("buildIndexes");
    public static final BooleanField HIDDEN_FIELD = new BooleanField("hidden");
    public static final DoubleField PRIORITY_FIELD = new DoubleField("priority");
    public static final DocField TAGS_FIELD = new DocField("tags");
    public static final LongField SLAVE_DELAY_FIELD = new LongField("slaveDelay");
    public static final IntField VOTES_FIELD = new IntField("votes");
    public static final IntField ID_FIELD = new IntField("_id");
    
    private static final long MAX_SLAVE_DELAY = 3600 * 24 * 366;
    private static final int DEFAULT_VOTES = 1;
    private static final double DEFAULT_PRIORITY = 1;
    private static final boolean DEFAULT_ARBITER_ONLY = false;
    private static final BsonInt64 DEFAULT_SLAVE_DELAY = DefaultBsonValues.newLong(0);
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

    /**
     * @return the id of this member on the configuration. On a valid MemberConfig, this value is always on [0, 255]
     */
    public int getId() {
        return id;
    }

    public HostAndPort getHostAndPort() {
        return host;
    }

    public double getPriority() {
        return priority;
    }

    public int getNumVotes() {
        return votes;
    }

    public boolean isArbiter() {
        return arbiterOnly;
    }

    public boolean isElectable() {
        return !isArbiter() && getPriority() > 0;
    }

    public long getSlaveDelay() {
        return slaveDelay;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean buildIndexes() {
        return buildIndexes;
    }

    public ImmutableMap<String, String> getTags() {
        return tags;
    }

    public static MemberConfig fromDocument(BsonDocument bson) throws
            TypesMismatchException, NoSuchKeyException, BadValueException {
        int id = BsonReaderTool.getNumeric(bson, "_id").intValue();
        HostAndPort host = BsonReaderTool.getHostAndPort(bson, "host");
        int votes = BsonReaderTool.getInteger(bson, "votes", DEFAULT_VOTES);
        double priority = BsonReaderTool.getDouble(bson, "priority", DEFAULT_PRIORITY);
        boolean arbiterOnly
                = BsonReaderTool.getBooleanOrNumeric(bson, "arbiterOnly", DEFAULT_ARBITER_ONLY);
        long slaveDelay = BsonReaderTool.getNumeric(bson, "slaveDelay", DEFAULT_SLAVE_DELAY).longValue();
        boolean hidden
                = BsonReaderTool.getBooleanOrNumeric(bson, "hidden", DEFAULT_HIDDEN);
        boolean buildIndexes
                = BsonReaderTool.getBooleanOrNumeric(bson, "buildIndexes", DEFAULT_BUILD_INDEXES);
        BsonDocument castedTags = BsonReaderTool.getDocument(bson, "tags");
        ImmutableMap.Builder<String, String> tagsBuilder
                = ImmutableMap.builder();
        for (Entry entry : castedTags) {
            BsonValue value = entry.getValue();
            if (value.isString()) {
                throw new TypesMismatchException(entry.getKey(), "string", value.getType());
            }
            String castedValue = value.asString().getValue();
            tagsBuilder.put(entry.getKey(), castedValue);
        }
        return new MemberConfig(id, host, priority, votes, arbiterOnly, slaveDelay, hidden, buildIndexes, tagsBuilder.build());
    }

    public void validate() throws BadValueException {
        if (id < 0 || id > 255) {
            throw new BadValueException(ID_FIELD + " field value of " + id + " is out of range.");
        }

        if (priority < 0 || priority > 1000) {
            throw new BadValueException(PRIORITY_FIELD + " field value of " + priority + " is out of range");
        }
        if (votes != 0 && votes != 1) {
            throw new BadValueException(VOTES_FIELD + " field value is " + votes + " but must be 0 or 1");
        }
        if (arbiterOnly) {
            if (!tags.isEmpty()) {
                throw new BadValueException("Cannot set tags on arbiters.");
            }
            if (!isVoter()) {
                throw new BadValueException("Arbiter must vote (cannot have 0 votes)");
            }
        }
        if (slaveDelay < 0 || slaveDelay > MAX_SLAVE_DELAY) {
            throw new BadValueException(SLAVE_DELAY_FIELD + " field value of " + slaveDelay
                    + " seconds is out of range");
        }
        if (slaveDelay > 0 && priority != 0) {
            throw new BadValueException("slaveDelay requires priority be zero");
        }
        if (hidden && priority != 0) {
            throw new BadValueException("priority must be 0 when hidden=true");
        }
        if (!buildIndexes && priority != 0) {
            throw new BadValueException("priority must be 0 when buildIndexes=false");
        }
    }

    public BsonDocument toBSON() {
        BsonDocumentBuilder object = new BsonDocumentBuilder();
        object.append(ID_FIELD, id);
        object.append(HOST_FIELD, host);
        object.append(ARBITER_ONLY_FIELD, arbiterOnly);
        object.append(BUILD_INDEXES_FIELD, buildIndexes);
        object.append(HIDDEN_FIELD, hidden);
        object.append(PRIORITY_FIELD, priority);
        BsonDocumentBuilder tagsDoc = new BsonDocumentBuilder();
        for (java.util.Map.Entry<String, String> entry : tags.entrySet()) {
            tagsDoc.appendUnsafe(entry.getKey(), DefaultBsonValues.newString(entry.getValue()));
        }
        object.append(TAGS_FIELD, tagsDoc.build());
        object.append(SLAVE_DELAY_FIELD, slaveDelay);
        object.append(VOTES_FIELD, votes);
        return object.build();
    }

    boolean isVoter() {
        return votes != 0;
    }

    /**
     *
     * @param customWriteConcerns
     * @return true iff this member contais at least one non internal tag
     */
    public boolean hasTags() {
        for (java.util.Map.Entry<String, String> entry : getTags().entrySet()) {
            String tagKey = entry.getKey();
            if (tagKey.charAt(0) == '$') {
                // Filter out internal tags
                continue;
            }
            return true;
        }
        return false;
    }

}
