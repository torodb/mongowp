
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.WriteConcern;
import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.utils.BsonArrayBuilder;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.collect.*;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 *
 */
public class ReplicaSetConfig {

    private static final IntField VERSION_FIELD = new IntField("version");
    private static final StringField ID_FIELD = new StringField("_id");
    private static final ArrayField MEMBERS_FIELD = new ArrayField("members");
    private static final DocField SETTINGS_FIELD = new DocField("settings");
    private static final String STEP_DOWN_CHECK_WRITE_CONCERN_MODE_FIELD_NAME = "$stepDownCheck";
    private static final LongField PROTOCOL_VERSION_FIELD = new LongField("protocolVersion");
    
    private static final IntField HEARTHBEAT_TIMEOUT_FIELD = new IntField("heartbeatTimeoutSecs");
    private static final BooleanField CHAINING_ALLOWED_FIELD = new BooleanField("chainingAllowed");
    private static final DocField GET_LAST_ERROR_DEFAULTS_FIELD = new DocField("getLastErrorDefaults");
    private static final DocField GET_LAST_ERROR_MODES_FIELD = new DocField("getLastErrorModes");
    
    private static final int DEFAULT_HEARTBEAT_TIMEOUT = 10;
    private static final boolean DEFAULT_CHAINING_ALLOWED = true;
    
    private static final ImmutableSet<String> VALID_FIELD_NAMES = ImmutableSet.of(
            VERSION_FIELD.getFieldName(), ID_FIELD.getFieldName(),
            MEMBERS_FIELD.getFieldName(), SETTINGS_FIELD.getFieldName(),
            STEP_DOWN_CHECK_WRITE_CONCERN_MODE_FIELD_NAME,
            PROTOCOL_VERSION_FIELD.getFieldName()
    );
    
    private final String setName;
    private final int version;
    private final ImmutableMap<Integer, MemberConfig> members;
    private final WriteConcern defaultWriteConcern;
    private final int heartbeatTimeoutPeriod;
    private final boolean chainingAllowed;
    private final int majorityVoteCount;
    private final int writeMajority;
    private final int totalVotingMembers;
    private final Map<String, String> tagConfig;
    private final ImmutableTable<String, String, Integer> customWriteConcern;
    private final long protocolVersion;

    public ReplicaSetConfig(
            String id, 
            int version, 
            ImmutableList<MemberConfig> members,
            WriteConcern defaultWriteConcern, 
            int heartbeatTimeoutPeriod, 
            boolean chainingAllowed, 
            ImmutableTable<String, String, Integer> customWriteConcern,
            long protocolVersion) {
        this.setName = id;
        this.version = version;
        this.defaultWriteConcern = defaultWriteConcern;
        this.heartbeatTimeoutPeriod = heartbeatTimeoutPeriod;
        this.chainingAllowed = chainingAllowed;
        this.protocolVersion = protocolVersion;
        this.tagConfig = Maps.newHashMap();
        this.customWriteConcern = customWriteConcern;
        
        int voters = 0;
        int arbiters = 0;
        ImmutableMap.Builder<Integer, MemberConfig> membersBuilder = ImmutableBiMap.builder();
        for (MemberConfig member : members) {
            if (member.isArbiter()) {
                arbiters++;
            }
            if (member.isVoter()) {
                voters++;
            }
            membersBuilder.put(member.getId(), member);
        }

        this.members = membersBuilder.build();
        this.totalVotingMembers = voters;
        this.majorityVoteCount = voters / 2 + 1;
        this.writeMajority = Math.min(majorityVoteCount, voters - arbiters);
    }

    

    public String getSetName() {
        return setName;
    }

    public int getVersion() {
        return version;
    }

    public ImmutableMap<Integer, MemberConfig> getMembers() {
        return members;
    }

    public WriteConcern getDefaultWriteConcern() {
        return defaultWriteConcern;
    }

    public int getHeartbeatTimeoutPeriod() {
        return heartbeatTimeoutPeriod;
    }

    public boolean isChainingAllowed() {
        return chainingAllowed;
    }

    public int getMajorityVoteCount() {
        return majorityVoteCount;
    }

    public int getWriteMajority() {
        return writeMajority;
    }

    public int getTotalVotingMembers() {
        return totalVotingMembers;
    }

    public Map<String, String> getTagConfig() {
        return Collections.unmodifiableMap(tagConfig);
    }

    public long getProtocolVersion() {
        return protocolVersion;
    }
    
    public static ReplicaSetConfig fromDocument(@Nonnull BsonDocument bson)
            throws BadValueException, TypesMismatchException, NoSuchKeyException, FailedToParseException {
        BsonReaderTool.checkOnlyHasFields("replica set configuration", bson, VALID_FIELD_NAMES);

        String id = BsonReaderTool.getString(bson, ID_FIELD);

        int version = BsonReaderTool.getInteger(bson, VERSION_FIELD);

        BsonArray uncastedMembers = BsonReaderTool.getArray(bson, MEMBERS_FIELD);
        ImmutableList.Builder<MemberConfig> membersBuilder = ImmutableList.builder();
        int i = 0;
        for (BsonValue uncastedMember : uncastedMembers) {
            if (uncastedMember == null || !uncastedMember.isDocument()) {
                    throw new TypesMismatchException(
                            Integer.toString(i),
                            "object", 
                            uncastedMember == null ? null : uncastedMember.getType()
                    );
            }
            membersBuilder.add(MemberConfig.fromDocument(uncastedMember.asDocument()));
            i++;
        }
        
        BsonDocument settings;
        try {
            settings = BsonReaderTool.getDocument(bson, SETTINGS_FIELD);
        } catch (NoSuchKeyException ex) {
            settings = DefaultBsonValues.EMPTY_DOC;
        }
        int hbTimeout = BsonReaderTool.getInteger(settings, HEARTHBEAT_TIMEOUT_FIELD, DEFAULT_HEARTBEAT_TIMEOUT);
        
        boolean chainingAllowed = BsonReaderTool.getBoolean(settings, CHAINING_ALLOWED_FIELD, DEFAULT_CHAINING_ALLOWED);


        BsonDocument uncastedGetLastErrorDefaults = BsonReaderTool.getDocument(
                settings,
                GET_LAST_ERROR_DEFAULTS_FIELD
        );

        WriteConcern wc = WriteConcern.fromDocument(uncastedGetLastErrorDefaults);
        
        BsonDocument uncastedCustomWriteConcerns;
        try {
            uncastedCustomWriteConcerns = BsonReaderTool.getDocument(settings, GET_LAST_ERROR_MODES_FIELD);
        } catch (NoSuchKeyException ex) {
            uncastedCustomWriteConcerns = DefaultBsonValues.EMPTY_DOC;
        }
        ImmutableTable<String, String, Integer> customWriteConcernsBuilder 
                = parseCustomWriteConcerns(uncastedCustomWriteConcerns);
        
        long protocolVersion = BsonReaderTool.getLong(settings, PROTOCOL_VERSION_FIELD);
        
        return new ReplicaSetConfig(
                id, 
                version, 
                membersBuilder.build(), 
                wc, 
                hbTimeout, 
                chainingAllowed, 
                customWriteConcernsBuilder,
                protocolVersion
        );
    }
    
    private static ImmutableTable<String, String, Integer> parseCustomWriteConcerns(BsonDocument bson)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        ImmutableTable.Builder<String, String, Integer> builder = ImmutableTable.builder();
        for (Entry<?> customWriteNameEntry : bson) {
            BsonDocument constraintDoc = BsonReaderTool.getDocument(bson, customWriteNameEntry.getKey());
            for (Entry<?> tagEntry : constraintDoc) {
                int intValue;
                try {
                    intValue = tagEntry.getValue().asNumber().intValue();
                } catch (UnsupportedOperationException ex) {
                    String fieldName = 
                            SETTINGS_FIELD.getFieldName()
                            + '.' + GET_LAST_ERROR_MODES_FIELD.getFieldName()
                            + '.' + customWriteNameEntry
                            + '.' + constraintDoc;
                    BsonValue tagValue = tagEntry.getValue();
                    BsonType tagType;
                    if (tagValue == null) {
                        tagType = BsonType.NULL;
                    } else {
                        tagType = tagValue.getType();
                    }
                    throw new TypesMismatchException(
                            fieldName, 
                            "number", 
                            tagType,
                            "Expected " + fieldName + " to be a number, not " + tagType.toString().toLowerCase(Locale.ROOT)
                    );
                }
                if (intValue <= 0) {
                    String fieldName = 
                            SETTINGS_FIELD.getFieldName()
                            + '.' + GET_LAST_ERROR_MODES_FIELD.getFieldName()
                            + '.' + customWriteNameEntry
                            + '.' + constraintDoc;
                    throw new BadValueException("Value of " + fieldName + " must be positive, but found " + intValue);
                }
                builder.put(customWriteNameEntry.getKey(), tagEntry.getKey(), intValue);
            }
        }
        return builder.build();
    }
    
    public BsonDocument toBSON() {
        BsonDocumentBuilder result = new BsonDocumentBuilder();
        result.append(ID_FIELD, setName);
        result.append(VERSION_FIELD, version);
        
        BsonArrayBuilder membersList = new BsonArrayBuilder();
        for (MemberConfig member : members.values()) {
            membersList.add(member.toBSON());
        }
        result.append(MEMBERS_FIELD, membersList.build());
        
        BsonDocumentBuilder settingsBuilder = new BsonDocumentBuilder();
        settingsBuilder.append(CHAINING_ALLOWED_FIELD, chainingAllowed);
        settingsBuilder.append(HEARTHBEAT_TIMEOUT_FIELD, heartbeatTimeoutPeriod);
        
        BsonDocumentBuilder customWrites = new BsonDocumentBuilder();
        for (String customWriteName : customWriteConcern.rowKeySet()) {
            if (customWriteName.startsWith("$")) { //MongoDB uses $ as an internal mode
                continue; 
            }
            BsonDocument tagMap = toBson(customWriteConcern.row(customWriteName));
            customWrites.appendUnsafe(customWriteName, tagMap);
        }
        settingsBuilder.append(GET_LAST_ERROR_MODES_FIELD, customWrites);
        settingsBuilder.append(GET_LAST_ERROR_DEFAULTS_FIELD, defaultWriteConcern.toDocument());
        settingsBuilder.append(PROTOCOL_VERSION_FIELD, protocolVersion);
     
        result.append(SETTINGS_FIELD, settingsBuilder);
        return result.build();
    }

    private BsonDocument toBson(ImmutableMap<String, Integer> map) {
        BsonDocumentBuilder result = new BsonDocumentBuilder();
        for (java.util.Map.Entry<String, Integer> entry : map.entrySet()) {
            result.appendUnsafe(entry.getKey(), DefaultBsonValues.newInt(entry.getValue()));
        }
        return result.build();
    }
    
}
