
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
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.IsMasterCommand.IsMasterReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonArrayBuilder;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

        private static final IsMasterReply NOT_CONFIGURED = new IsMasterReply();
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

        private final Boolean master;
        private final Boolean secondary;
        private final String setName;
        private final Integer setVersion;
        private final ImmutableList<HostAndPort> hosts;
        private final ImmutableList<HostAndPort> passives;
        private final ImmutableList<HostAndPort> arbiters;
        private final HostAndPort primary;
        private final Boolean arbiterOnly;
        private final Boolean passive;
        private final Boolean hidden;
        private final Boolean buildIndexes;
        private final Integer slaveDelay;
        private final ImmutableMap<String, String> tags;
        private final HostAndPort me;
        private final BsonObjectId electionId;
        private final boolean configSet;

        public IsMasterReply(
                boolean master,
                @Nonnull String setName,
                int setVersion,
                @Nullable ImmutableList<HostAndPort> hosts,
                @Nullable ImmutableList<HostAndPort> passives,
                @Nullable ImmutableList<HostAndPort> arbiters,
                HostAndPort primary,
                boolean arbiterOnly,
                boolean passive,
                boolean hidden,
                boolean buildIndexes,
                int slaveDelay,
                @Nullable ImmutableMap<String, String> tags,
                @Nonnull HostAndPort me,
                @Nullable BsonObjectId electionId) {
            this.master = master;
            this.secondary = !master;
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

            this.master = null;
            this.secondary = null;
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
            assert master != null;
            builder.append(IS_MASTER_FIELD, master);
            assert secondary != null;
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
                builder.append(SLAVE_DELAY_FIELD, slaveDelay);
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
            int slaveDelay = BsonReaderTool.getNumeric(bson, SLAVE_DELAY_FIELD, DefaultBsonValues.INT32_ZERO).intValue();

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
