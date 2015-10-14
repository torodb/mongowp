
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.WriteConcernMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.FailedToParseException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.collect.*;
import com.mongodb.WriteConcern;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.bson.*;

/**
 *
 */
public class ReplicaSetConfig {

    private static final String VERSION_FIELD_NAME = "version";
    private static final String ID_FIELD_NAME = "_id";
    private static final String MEMBERS_FIELD_NAME = "members";
    private static final String SETTINGS_FIELD_NAME = "settings";
    private static final String STEP_DOWN_CHECK_WRITE_CONCERN_MODE_FIELD_NAME = "$stepDownCheck";
    private static final String PROTOCOL_VERSION_FIELD_NAME = "protocolVersion";
    
    private static final String HEARTHBEAT_TIMEOUT_FIELD_NAME = "heartbeatTimeoutSecs";
    private static final String CHAINING_ALLOWED_FIELD_NAME = "chainingAllowed";
    private static final String GET_LAST_ERROR_DEFAULTS_FIELD_NAME = "getLastErrorDefaults";
    private static final String GET_LAST_ERROR_MODES_FIELD_NAME = "getLastErrorModes";
    
    private static final BsonInt32 DEFAULT_HEARTBEAT_TIMEOUT = new BsonInt32(10);
    private static final BsonBoolean DEFAULT_CHAINING_ALLOWED = BsonBoolean.TRUE;
    
    private static final ImmutableSet<String> VALID_FIELD_NAMES = ImmutableSet.of(
            VERSION_FIELD_NAME, ID_FIELD_NAME, MEMBERS_FIELD_NAME, SETTINGS_FIELD_NAME, STEP_DOWN_CHECK_WRITE_CONCERN_MODE_FIELD_NAME, PROTOCOL_VERSION_FIELD_NAME
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

        String id = bson.getString(ID_FIELD_NAME).getValue();

        int version = bson.getInt32(VERSION_FIELD_NAME).getValue();

        BsonDocument uncastedMembers = bson.getDocument(MEMBERS_FIELD_NAME);
        ImmutableList.Builder<MemberConfig> membersBuilder = ImmutableList.builder();
        for (String field : uncastedMembers.keySet()) {
            BsonValue uncastedMember = uncastedMembers.get(field);
            if (uncastedMember == null || !uncastedMember.isDocument()) {
                    throw new TypesMismatchException(
                            field, 
                            "object", 
                            uncastedMember == null ? null : uncastedMember.getBsonType()
                    );
            }
            membersBuilder.add(MemberConfig.fromDocument(uncastedMember.asDocument()));
        }
        
        BsonDocument settings;
        try {
            settings = BsonReaderTool.getDocument(bson, SETTINGS_FIELD_NAME);
        } catch (NoSuchKeyException ex) {
            settings = new BsonDocument();
        }
        int hbTimeout = settings.getInt32(HEARTHBEAT_TIMEOUT_FIELD_NAME, DEFAULT_HEARTBEAT_TIMEOUT).intValue();
        
        boolean chainingAllowed = settings.getBoolean(CHAINING_ALLOWED_FIELD_NAME, DEFAULT_CHAINING_ALLOWED).getValue();


        BsonDocument uncastedGetLastErrorDefaults = BsonReaderTool.getDocument(
                settings,
                GET_LAST_ERROR_DEFAULTS_FIELD_NAME
        );

        WriteConcern wc = WriteConcernMarshaller.unmarshall(uncastedGetLastErrorDefaults);
        
        BsonDocument uncastedCustomWriteConcerns;
        try {
            uncastedCustomWriteConcerns = BsonReaderTool.getDocument(settings, GET_LAST_ERROR_MODES_FIELD_NAME);
        } catch (NoSuchKeyException ex) {
            uncastedCustomWriteConcerns = new BsonDocument();
        }
        ImmutableTable<String, String, Integer> customWriteConcernsBuilder 
                = parseCustomWriteConcerns(uncastedCustomWriteConcerns);
        
        long protocolVersion = settings.getInt64(PROTOCOL_VERSION_FIELD_NAME).getValue();
        
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
        for (String customWriteName : bson.keySet()) {
            BsonDocument constraintDoc = BsonReaderTool.getDocument(bson, customWriteName);
            for (String tag : constraintDoc.keySet()) {
                int intValue;
                try {
                    intValue = constraintDoc.getNumber(tag).intValue();
                } catch (BsonInvalidOperationException ex) {
                    String fieldName = 
                            SETTINGS_FIELD_NAME 
                            + '.' + GET_LAST_ERROR_MODES_FIELD_NAME 
                            + '.' + customWriteName
                            + '.' + constraintDoc;
                    BsonValue tagValue = constraintDoc.get(tag);
                    BsonType tagType;
                    if (tagValue == null) {
                        tagType = BsonType.NULL;
                    } else {
                        tagType = tagValue.getBsonType();
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
                            SETTINGS_FIELD_NAME 
                            + '.' + GET_LAST_ERROR_MODES_FIELD_NAME 
                            + '.' + customWriteName
                            + '.' + constraintDoc;
                    throw new BadValueException("Value of " + fieldName + " must be positive, but found " + intValue);
                }
                builder.put(customWriteName, tag, intValue);
            }
        }
        return builder.build();
    }
    
    public BsonDocument toBSON() {
        BsonDocument result = new BsonDocument();
        result.put(ID_FIELD_NAME, new BsonString(setName));
        result.put(VERSION_FIELD_NAME, new BsonInt32(version));
        
        BsonArray membersList = new BsonArray();
        for (MemberConfig member : members.values()) {
            membersList.add(member.toBSON());
        }
        result.put(MEMBERS_FIELD_NAME, membersList);
        
        BsonDocument settingsBuilder = new BsonDocument();
        settingsBuilder.put(CHAINING_ALLOWED_FIELD_NAME, BsonBoolean.valueOf(chainingAllowed));
        settingsBuilder.put(HEARTHBEAT_TIMEOUT_FIELD_NAME, new BsonInt32(heartbeatTimeoutPeriod));
        
        BsonDocument customWrites = new BsonDocument();
        for (String customWriteName : customWriteConcern.rowKeySet()) {
            if (customWriteName.startsWith("$")) { //MongoDB uses $ as an internal mode
                continue; 
            }
            BsonDocument tagMap = toBson(customWriteConcern.row(customWriteName));
            customWrites.put(customWriteName, tagMap);
        }
        settingsBuilder.put(GET_LAST_ERROR_MODES_FIELD_NAME, customWrites);
        settingsBuilder.put(GET_LAST_ERROR_DEFAULTS_FIELD_NAME, toBson(defaultWriteConcern));
        settingsBuilder.put(PROTOCOL_VERSION_FIELD_NAME, new BsonInt64(protocolVersion));
     
        result.put(SETTINGS_FIELD_NAME, settingsBuilder);
        return result;
    }

    private BsonDocument toBson(ImmutableMap<String, Integer> map) {
        BsonDocument result = new BsonDocument();
        for (Entry<String, Integer> entry : map.entrySet()) {
            result.put(entry.getKey(), new BsonInt32(entry.getValue()));
        }
        return result;
    }
    
    private BsonDocument toBson(WriteConcern wc) {
        BsonDocument object = new BsonDocument();

        Object wObject = wc.getWObject();
        if (wObject instanceof String) {
            object.put("w", new BsonString((String) wObject));
        }
        else {
            assert wObject instanceof Integer;
            object.put("w", new BsonInt32((Integer) wObject));
        }
        if (wc.getFsync()) {
            object.put("fsync", BsonBoolean.TRUE);
        }
        if (wc.getJ()) {
            object.put("j", BsonBoolean.TRUE);
        }
        object.put("wtimeout", new BsonInt32(wc.getWtimeout()));
        
        return object;
    }

    
}
