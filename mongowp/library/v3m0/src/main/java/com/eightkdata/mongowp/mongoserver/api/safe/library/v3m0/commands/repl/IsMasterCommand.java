
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.CommandReply;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.IsMasterCommand.IsMasterReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleArgumentMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.*;
import org.bson.types.ObjectId;

/**
 *
 */
public class IsMasterCommand extends AbstractCommand<SimpleArgument, IsMasterReply> {

    public static final IsMasterCommand INSTANCE = new IsMasterCommand();

    private IsMasterCommand() {
        super("isMaster");
    }

    @Override
    public Class<? extends SimpleArgument> getArgClass() {
        return SimpleArgument.class;
    }

    @Override
    public SimpleArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {
        return new SimpleArgument(this);
    }

    @Override
    public BsonDocument marshallArg(SimpleArgument request) throws
            MongoServerException, UnsupportedOperationException {
        return SimpleArgumentMarshaller.marshall(request);
    }

    @Override
    public Class<? extends IsMasterReply> getReplyClass() {
        return IsMasterReply.class;
    }

    @Override
    public BsonDocument marshallReply(IsMasterReply reply) throws
            MongoServerException {
        return reply.toBson();
    }

    @Override
    public IsMasterReply unmarshallReply(BsonDocument replyDoc) throws
            MongoServerException, UnsupportedOperationException {
        return IsMasterReply.fromDocument(replyDoc, this);
    }

    @Immutable
    public static class IsMasterReply implements CommandReply {

        private static final BsonField<Boolean> IS_MASTER_FIELD = BsonField.create("ismaster");
        private static final BsonField<Boolean> SECONDARY_FIELD = BsonField.create("secondary");
        private static final BsonField<String> SET_NAME_FIELD = BsonField.create("setName");
        private static final BsonField<Integer> SET_VERSION_FIELD = BsonField.create("setVersion");
        private static final BsonField<BsonArray> HOSTS_FIELD = BsonField.create("hosts");
        private static final BsonField<BsonArray> PASSIVES_FIELD = BsonField.create("passives");
        private static final BsonField<BsonArray> ARBITERS_FIELD = BsonField.create("arbiters");
        private static final BsonField<HostAndPort> PRIMARY_FIELD = BsonField.create("primary");
        private static final BsonField<Boolean> ARBITER_ONLY_FIELD = BsonField.create("arbiterOnly");
        private static final BsonField<Boolean> PASSIVE_FIELD = BsonField.create("passive");
        private static final BsonField<Boolean> HIDDEN_FIELD = BsonField.create("hidden");
        private static final BsonField<Boolean> BUILD_INDEXES_FIELD = BsonField.create("buildIndexes");
        private static final BsonField<Integer> SLAVE_DELAY_FIELD = BsonField.create("slaveDelay");
        private static final BsonField<BsonDocument> TAGS_FIELD = BsonField.create("tags");
        private static final BsonField<HostAndPort> ME_FIELD = BsonField.create("me");
        private static final BsonField<ObjectId> ELECTION_ID_FIELD = BsonField.create("electionId");
        private static final BsonField<String> INFO_FIELD = BsonField.create("info");
        private static final BsonField<Boolean> IS_REPLICA_SET_FIELD = BsonField.create("isreplicaset");

        private final IsMasterCommand command;
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
        private final ObjectId electionId;
        private final boolean configSet;
        private final boolean shutdownInProgress;

        public IsMasterReply(
                IsMasterCommand command,
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
                @Nullable ObjectId electionId) {
            this.command = command;
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
            shutdownInProgress = false;
        }

        public IsMasterReply(IsMasterCommand command, boolean configSet, boolean shutdownInProgress) {
            this.command = command;
            if (configSet && !shutdownInProgress) {
                throw new AssertionError("This constructor must be used when "
                        + "config set is not correctly set or when a shutdown is "
                        + "in progress");
            }
            this.configSet = configSet;
            this.shutdownInProgress = shutdownInProgress;

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

        @Override
        public boolean isOk() {
            return getErrorCode().equals(ErrorCode.OK);
        }

        @Override
        public ErrorCode getErrorCode() {
            if (shutdownInProgress) {
                return ErrorCode.SHUTDOWN_IN_PROGRESS;
            }
            return ErrorCode.OK;
        }

        @Override
        public Command getCommand() {
            return command;
        }

        @Override
        public WriteOpResult getWriteOpResult() {
            return null;
        }

        private BsonDocument toBson() throws ShutdownInProgressException {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            if (shutdownInProgress) {
                throw new ShutdownInProgressException();
            }
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
            BsonArray bsonArray = new BsonArray();
            for (HostAndPort hostAndPort : hostsAndPortList) {
                bsonArray.add(new BsonString(hostAndPort.toString()));
            }
            return bsonArray;
        }

        private BsonDocument toBsonDocument(Map<String, String> map) {
            BsonDocument doc = new BsonDocument();
            for (Entry<String, String> entrySet : map.entrySet()) {
                doc.append(entrySet.getKey(), new BsonString(entrySet.getValue()));
            }
            return doc;
        }

        private static ImmutableList<HostAndPort> fromBSONArray(BsonDocument bson, BsonField field)
                throws TypesMismatchException, NoSuchKeyException {
            if (!bson.containsKey(field.getFieldName())) {
                return ImmutableList.of();
            }
            else {
                ImmutableList.Builder<HostAndPort> resultBuilder = ImmutableList.builder();
                BsonDocument uncastedList = BsonReaderTool.getDocument(bson, field);
                for (String subField : uncastedList.keySet()) {
                    try {
                        resultBuilder.add(HostAndPort.fromString(BsonReaderTool.getString(bson, subField)
                                )
                        );
                    } catch (TypesMismatchException ex) {
                        throw new TypesMismatchException(
                                subField,
                                "string",
                                ex.getFoundType(),
                                "Elements in \"" + field + "\" array of isMaster "
                                        + "response must be of type string but "
                                        + "found type " + ex.getFoundType()
                        );
                    }
                }
                return resultBuilder.build();
            }
        }

        private static IsMasterReply fromDocument(BsonDocument bson, IsMasterCommand command) throws TypesMismatchException, NoSuchKeyException, FailedToParseException {
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
                return new IsMasterReply(command, false, false);
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
            int slaveDelay = BsonReaderTool.getNumeric(bson, SLAVE_DELAY_FIELD, new BsonInt32(0)).intValue();

            final ImmutableMap<String, String> tags;
            if (!bson.containsKey(TAGS_FIELD.getFieldName())) {
                tags = ImmutableMap.of();
            }
            else {
                ImmutableMap.Builder<String, String> tagsBuilder = ImmutableMap.builder();

                BsonDocument uncastedTags = BsonReaderTool.getDocument(bson, TAGS_FIELD);
                for (Entry<String, BsonValue> entry : uncastedTags.entrySet()) {
                    if (!entry.getValue().isString()) {
                        throw new TypesMismatchException(
                                entry.getKey(),
                                "string",
                                entry.getValue().getBsonType(),
                                "Elements in \"" + TAGS_FIELD + "\" obj of "
                                        + "isMaster response must be of type string "
                                        + " but found type " + entry.getValue().getBsonType().toString().toLowerCase(Locale.ROOT)
                        );
                    }
                    String tagValue = uncastedTags.get(entry.getKey()).asString().getValue();

                    tagsBuilder.put(entry.getKey(), tagValue);
                }
                tags = tagsBuilder.build();
            }

            ObjectId electionId = BsonReaderTool.getObjectId(bson, ELECTION_ID_FIELD, null);
            HostAndPort me = BsonReaderTool.getHostAndPort(bson, ME_FIELD, null);

            return new IsMasterReply(
                    command,
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
