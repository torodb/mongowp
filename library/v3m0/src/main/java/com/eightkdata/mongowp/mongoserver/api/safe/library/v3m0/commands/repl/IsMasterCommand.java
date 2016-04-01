
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.FailedToParseException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.IsMasterCommand.IsMasterReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberState;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonArrayBuilder;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import com.google.common.primitives.UnsignedInteger;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
public class IsMasterCommand extends AbstractCommand<Empty, IsMasterReply> {

    public static final IsMasterCommand INSTANCE = new IsMasterCommand();

    private IsMasterCommand() {
        super("isMaster");
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc) {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) {
        return EmptyCommandArgumentMarshaller.marshallEmptyArgument(this);
    }

    @Override
    public Class<? extends IsMasterReply> getResultClass() {
        return IsMasterReply.class;
    }

    @Override
    public BsonDocument marshallResult(IsMasterReply reply) {
        return reply.toBson();
    }

    @Override
    public IsMasterReply unmarshallResult(BsonDocument replyDoc) throws TypesMismatchException, NoSuchKeyException, FailedToParseException {
        return IsMasterReply.fromDocument(replyDoc);
    }

    @Immutable
    public static class IsMasterReply {

        public static final IsMasterReply NOT_CONFIGURED = new IsMasterReply();
        private static final BooleanField IS_MASTER_FIELD = new BooleanField("ismaster");
        private static final BooleanField SECONDARY_FIELD = new BooleanField("secondary");
        private static final StringField SET_NAME_FIELD = new StringField("setName");
        private static final IntField SET_VERSION_FIELD = new IntField("setVersion");
        private static final ArrayField HOSTS_FIELD = new ArrayField("hosts");
        private static final ArrayField PASSIVES_FIELD = new ArrayField("passives");
        private static final ArrayField ARBITERS_FIELD = new ArrayField("arbiters");
        private static final HostAndPortField PRIMARY_FIELD = new HostAndPortField("primary");
        private static final BooleanField ARBITER_ONLY_FIELD = new BooleanField("arbiterOnly");
        private static final BooleanField PASSIVE_FIELD = new BooleanField("passive");
        private static final BooleanField HIDDEN_FIELD = new BooleanField("hidden");
        private static final BooleanField BUILD_INDEXES_FIELD = new BooleanField("buildIndexes");
        private static final IntField SLAVE_DELAY_FIELD = new IntField("slaveDelay");
        private static final DocField TAGS_FIELD = new DocField("tags");
        private static final HostAndPortField ME_FIELD = new HostAndPortField("me");
        private static final ObjectIdField ELECTION_ID_FIELD = new ObjectIdField("electionId");
        private static final StringField INFO_FIELD = new StringField("info");
        private static final BooleanField IS_REPLICA_SET_FIELD = new BooleanField("isreplicaset");

        private final boolean master;
        private final boolean secondary;
        private final String setName;
        private final Integer setVersion;
        private final List<HostAndPort> hosts;
        private final List<HostAndPort> passives;
        private final List<HostAndPort> arbiters;
        private final HostAndPort primary;
        private final Boolean arbiterOnly;
        private final Boolean passive;
        private final Boolean hidden;
        private final Boolean buildIndexes;
        private final UnsignedInteger slaveDelay;
        private final Map<String, String> tags;
        private final HostAndPort me;
        private final BsonObjectId electionId;
        private final boolean configSet;

        public IsMasterReply(
                MemberState myState,
                @Nonnull String setName,
                Integer setVersion,
                @Nullable ImmutableList<HostAndPort> hosts,
                @Nullable ImmutableList<HostAndPort> passives,
                @Nullable ImmutableList<HostAndPort> arbiters,
                HostAndPort primary,
                Boolean arbiterOnly,
                Boolean passive,
                Boolean hidden,
                Boolean buildIndexes,
                UnsignedInteger slaveDelay,
                @Nullable ImmutableMap<String, String> tags,
                @Nonnull HostAndPort me,
                @Nullable BsonObjectId electionId) {
            this(myState == MemberState.RS_PRIMARY, myState == MemberState.RS_SECONDARY, setName,
                    setVersion, (List<HostAndPort>) hosts, (List<HostAndPort>) passives,
                    (List<HostAndPort>) arbiters, primary, arbiterOnly, passive, hidden,
                    buildIndexes, slaveDelay, tags, me, electionId);
        }

        private IsMasterReply(
                boolean master,
                boolean secondary,
                @Nonnull String setName,
                Integer setVersion,
                @Nullable List<HostAndPort> hosts,
                @Nullable List<HostAndPort> passives,
                @Nullable List<HostAndPort> arbiters,
                HostAndPort primary,
                Boolean arbiterOnly,
                Boolean passive,
                Boolean hidden,
                Boolean buildIndexes,
                UnsignedInteger slaveDelay,
                @Nullable Map<String, String> tags,
                @Nonnull HostAndPort me,
                @Nullable BsonObjectId electionId) {
            this.master = master;
            this.secondary = secondary;

            assert !(master & secondary);
            
            this.setName = setName;
            this.setVersion = setVersion;
            this.hosts = hosts;
            this.passives = passives;
            this.arbiters = arbiters;
            this.primary = primary;
            this.arbiterOnly = arbiterOnly;
            this.passive = passive;
            this.hidden = hidden;
            this.buildIndexes = buildIndexes;
            this.slaveDelay = slaveDelay;
            this.tags = tags;
            this.me = me;
            this.electionId = electionId;

            configSet = true;
        }

        private IsMasterReply() {
            this.configSet = false;

            this.master = false;
            this.secondary = false;
            this.setName = null;
            this.setVersion = null;
            this.hosts = null;
            this.passives = null;
            this.arbiters = null;
            this.primary = null;
            this.arbiterOnly = null;
            this.passive = null;
            this.hidden = null;
            this.buildIndexes = null;
            this.slaveDelay = null;
            this.tags = null;
            this.me = null;
            this.electionId = null;
        }

        private BsonDocument toBson() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            if (!configSet) {
                builder.append(IS_MASTER_FIELD, false);
                builder.append(SECONDARY_FIELD, false);
                builder.append(INFO_FIELD, "Does not have a valid replica set config");
                builder.append(IS_REPLICA_SET_FIELD, true);

                return builder.build();
            }

            assert setName != null;
            builder.append(SET_NAME_FIELD, setName);
            assert setVersion != null;
            builder.append(SET_VERSION_FIELD, setVersion);
            builder.append(IS_MASTER_FIELD, master);
            builder.append(SECONDARY_FIELD, secondary);

            if (hosts != null) {
                builder.append(HOSTS_FIELD, toBsonArray(hosts));
            }
            if (passives != null) {
                builder.append(PASSIVES_FIELD, toBsonArray(passives));
            }
            if (arbiters != null) {
                builder.append(ARBITERS_FIELD, toBsonArray(passives));
            }
            if (primary != null) {
                builder.append(PRIMARY_FIELD, primary);
            }
            if (arbiterOnly != null) {
                builder.append(ARBITER_ONLY_FIELD, arbiterOnly);
            }
            if (passive != null) {
                builder.append(PASSIVE_FIELD, passive);
            }
            if (hidden != null) {
                builder.append(HIDDEN_FIELD, hidden);
            }
            if (buildIndexes != null) {
                builder.append(BUILD_INDEXES_FIELD, buildIndexes);
            }
            if (slaveDelay != null) {
                builder.append(SLAVE_DELAY_FIELD, slaveDelay.intValue());
            }
            if (tags != null) {
                builder.append(TAGS_FIELD, toBsonDocument(tags));
            }

            assert me != null;
            builder.append(ME_FIELD, me);
            if (electionId != null) {
                builder.append(ELECTION_ID_FIELD, electionId);
            }
            return builder.build();
        }

        private BsonArray toBsonArray(@Nonnull List<HostAndPort> hostsAndPortList) {
            BsonArrayBuilder bsonArray = new BsonArrayBuilder();
            for (HostAndPort hostAndPort : hostsAndPortList) {
                bsonArray.add(hostAndPort.toString());
            }
            return bsonArray.build();
        }

        private BsonDocument toBsonDocument(Map<String, String> map) {
            BsonDocumentBuilder doc = new BsonDocumentBuilder();
            for (java.util.Map.Entry<String, String> entrySet : map.entrySet()) {
                doc.appendUnsafe(entrySet.getKey(), DefaultBsonValues.newString(entrySet.getValue()));
            }
            return doc.build();
        }

        private static ImmutableList<HostAndPort> fromBSONArray(BsonDocument bson, ArrayField field)
                throws TypesMismatchException, NoSuchKeyException {
            if (!bson.containsKey(field.getFieldName())) {
                return ImmutableList.of();
            }
            else {
                ImmutableList.Builder<HostAndPort> resultBuilder = ImmutableList.builder();
                BsonArray uncastedList = BsonReaderTool.getArray(bson, field);
                for (int i = 0; i < uncastedList.size(); i++) {
                    BsonValue uncastedValue = uncastedList.get(i);
                    if (!uncastedValue.isString()) {
                        throw new TypesMismatchException(
                                Integer.toString(i),
                                "string",
                                uncastedValue.getType(),
                                "Elements in \"" + field + "\" array of isMaster "
                                        + "response must be of type string but "
                                        + "found type " + uncastedValue.getType()
                        );
                    }
                    resultBuilder.add(HostAndPort.fromString(uncastedList.asString().getValue()));
                }
                return resultBuilder.build();
            }
        }

        private static IsMasterReply fromDocument(BsonDocument bson) throws TypesMismatchException, NoSuchKeyException, FailedToParseException {
            boolean master = BsonReaderTool.getBoolean(bson, IS_MASTER_FIELD);
            boolean secondary = BsonReaderTool.getBoolean(bson, SECONDARY_FIELD);
            if (bson.containsKey(INFO_FIELD.getFieldName())) {
                if (master || secondary || !bson.containsKey(IS_REPLICA_SET_FIELD.getFieldName())
                        || !bson.get(IS_REPLICA_SET_FIELD.getFieldName()).isBoolean() ||
                        !bson.get(IS_REPLICA_SET_FIELD.getFieldName()).asBoolean().getValue()) {
                    throw new FailedToParseException("Expected presence of \""
                            + INFO_FIELD + "\" field to indicate no valid "
                            + "config loaded, but other fields weren't as we "
                            + "expected");
                }
                return NOT_CONFIGURED;
            }
            else if (bson.containsKey(IS_REPLICA_SET_FIELD.getFieldName())) {
                throw new FailedToParseException("Found \"" + IS_REPLICA_SET_FIELD
                        + "\" field which should indicate that no valid config "
                        + "is loaded, but we didn't also have an \"" + INFO_FIELD
                        + "\" field as we expected"
                );
            }
            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD);
            int setVersion = BsonReaderTool.getNumeric(bson, SET_VERSION_FIELD).intValue();

            ImmutableList<HostAndPort> hosts = fromBSONArray(bson, HOSTS_FIELD);
            ImmutableList<HostAndPort> passives = fromBSONArray(bson, PASSIVES_FIELD);
            ImmutableList<HostAndPort> arbiters = fromBSONArray(bson, ARBITERS_FIELD);

            HostAndPort primary = BsonReaderTool.getHostAndPort(bson, PRIMARY_FIELD, null);
            boolean arbiterOnly = BsonReaderTool.getBoolean(bson, ARBITER_ONLY_FIELD, false);
            boolean passive = BsonReaderTool.getBoolean(bson, PASSIVE_FIELD, false);
            boolean hidden = BsonReaderTool.getBoolean(bson, HIDDEN_FIELD, false);
            boolean buildIndexes = BsonReaderTool.getBoolean(bson, BUILD_INDEXES_FIELD, false);
            UnsignedInteger slaveDelay = UnsignedInteger.fromIntBits(
                    BsonReaderTool.getNumeric(bson, SLAVE_DELAY_FIELD, DefaultBsonValues.INT32_ZERO).intValue()
            );

            final ImmutableMap<String, String> tags;
            if (!bson.containsKey(TAGS_FIELD.getFieldName())) {
                tags = ImmutableMap.of();
            }
            else {
                ImmutableMap.Builder<String, String> tagsBuilder = ImmutableMap.builder();

                BsonDocument uncastedTags = BsonReaderTool.getDocument(bson, TAGS_FIELD);
                for (Entry<?> entry : uncastedTags) {
                    if (!entry.getValue().isString()) {
                        throw new TypesMismatchException(
                                entry.getKey(),
                                "string",
                                entry.getValue().getType(),
                                "Elements in \"" + TAGS_FIELD + "\" obj of "
                                        + "isMaster response must be of type string "
                                        + " but found type " + entry.getValue().getType().toString().toLowerCase(Locale.ROOT)
                        );
                    }
                    String tagValue = uncastedTags.get(entry.getKey()).asString().getValue();

                    tagsBuilder.put(entry.getKey(), tagValue);
                }
                tags = tagsBuilder.build();
            }

            BsonObjectId electionId = BsonReaderTool.getObjectId(bson, ELECTION_ID_FIELD, null);
            HostAndPort me = BsonReaderTool.getHostAndPort(bson, ME_FIELD, null);

            return new IsMasterReply(
                    master,
                    secondary,
                    setName,
                    setVersion,
                    hosts,
                    passives,
                    arbiters,
                    primary,
                    arbiterOnly,
                    passive,
                    hidden,
                    buildIndexes,
                    slaveDelay,
                    tags,
                    me,
                    electionId
            );
        }

        public static class Builder {
            private MemberState myState;
            private String setName;
            private Integer setVersion;
            private final List<HostAndPort> hosts = new ArrayList<>();
            private final List<HostAndPort> passives = new ArrayList<>();
            private final List<HostAndPort> arbiters = new ArrayList<>();
            private HostAndPort primary;
            private Boolean arbiterOnly;
            private Boolean passive;
            private Boolean hidden;
            private Boolean buildIndexes;
            private UnsignedInteger slaveDelay;
            private final Map<String, String> tags = new HashMap<>();
            private HostAndPort me;
            private BsonObjectId electionId;
            private boolean built = false;

            public Builder setMyState(MemberState myState) {
                Preconditions.checkState(!built);
                this.myState = myState;
                return this;
            }

            public Builder setReplSetName(String setName) {
                Preconditions.checkState(!built);
                this.setName = setName;
                return this;
            }

            public Builder setReplSetVersion(Integer setVersion) {
                Preconditions.checkState(!built);
                this.setVersion = setVersion;
                return this;
            }

            public Builder addHost(HostAndPort host) {
                Preconditions.checkState(!built);
                this.hosts.add(host);
                return this;
            }

            public Builder addPassive(HostAndPort passive) {
                Preconditions.checkState(!built);
                this.passives.add(passive);
                return this;
            }

            public Builder addArbiter(HostAndPort arbiter) {
                Preconditions.checkState(!built);
                this.arbiters.add(arbiter);
                return this;
            }

            public Builder setPrimary(HostAndPort primary) {
                Preconditions.checkState(!built);
                this.primary = primary;
                return this;
            }

            public Builder setArbiterOnly(Boolean arbiterOnly) {
                Preconditions.checkState(!built);
                this.arbiterOnly = arbiterOnly;
                return this;
            }

            public Builder setPassive(Boolean passive) {
                Preconditions.checkState(!built);
                this.passive = passive;
                return this;
            }

            public Builder setHidden(Boolean hidden) {
                Preconditions.checkState(!built);
                this.hidden = hidden;
                return this;
            }

            public Builder setBuildIndexes(Boolean buildIndexes) {
                Preconditions.checkState(!built);
                this.buildIndexes = buildIndexes;
                return this;
            }

            public Builder setSlaveDelay(UnsignedInteger slaveDelay) {
                Preconditions.checkState(!built);
                this.slaveDelay = slaveDelay;
                return this;
            }

            public Builder addTag(String key, String tag) {
                Preconditions.checkState(!built);
                this.tags.put(key, tag);
                return this;
            }

            public Builder setMe(HostAndPort me) {
                Preconditions.checkState(!built);
                this.me = me;
                return this;
            }

            public Builder setElectionId(BsonObjectId electionId) {
                Preconditions.checkState(!built);
                this.electionId = electionId;
                return this;
            }

            public IsMasterReply build() {
                Preconditions.checkState(!built);
                built = true;
                return new IsMasterReply(
                        myState == MemberState.RS_PRIMARY,
                        myState == MemberState.RS_SECONDARY,
                        setName,
                        setVersion,
                        hosts,
                        passives,
                        arbiters,
                        primary,
                        arbiterOnly,
                        passive,
                        hidden,
                        buildIndexes,
                        slaveDelay,
                        tags,
                        me,
                        electionId
                );
            }


        }
    }


}
