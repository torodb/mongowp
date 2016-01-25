package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.types.ObjectId;
import org.threeten.bp.Instant;

import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.ServerStatusCommand.ServerStatusArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.ServerStatusCommand.ServerStatusReply;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonReaderTool;
import com.google.common.collect.ImmutableList;

/**
 *
 */
public class ServerStatusCommand extends AbstractCommand<ServerStatusArgument, ServerStatusReply> {

    public static final ServerStatusCommand INSTANCE = new ServerStatusCommand();

    private ServerStatusCommand() {
        super("serverStatus");
    }

    @Override
    public boolean isSlaveOk() {
        return true;
    }

    @Override
    public Class<? extends ServerStatusArgument> getArgClass() {
        return ServerStatusArgument.class;
    }

    @Override
    public ServerStatusArgument unmarshallArg(BsonDocument requestDoc) 
            throws TypesMismatchException, BadValueException, NoSuchKeyException {
        return ServerStatusArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ServerStatusArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends ServerStatusReply> getResultClass() {
        return ServerStatusReply.class;
    }

    @Override
    public BsonDocument marshallResult(ServerStatusReply reply) {
        return reply.marshall();
    }

    @Override
    public ServerStatusReply unmarshallResult(BsonDocument resultDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Immutable
    public static class ServerStatusArgument {

        private static final BsonField<Boolean> HOST_FIELD = BsonField.create("host");
        private static final BsonField<Boolean> VERSION_FIELD = BsonField.create("version");
        private static final BsonField<Boolean> PROCESS_FIELD = BsonField.create("process");
        private static final BsonField<Boolean> PID_FIELD = BsonField.create("pid");
        private static final BsonField<Boolean> UPTIME_FIELD = BsonField.create("uptime");
        private static final BsonField<Boolean> UPTIME_ESTIMATE_FIELD = BsonField.create("uptimeEstimate");
        private static final BsonField<Boolean> LOCAL_TIME_FIELD = BsonField.create("localTime");
        private static final BsonField<Boolean> LOCKS_FIELD = BsonField.create("locks");
        private static final BsonField<Boolean> GLOBAL_LOCK_FIELD = BsonField.create("globalLock");
        private static final BsonField<Boolean> MEM_FIELD = BsonField.create("mem");
        private static final BsonField<Boolean> CONNECTIONS_FIELD = BsonField.create("connections");
        private static final BsonField<Boolean> EXTRA_INFO_FIELD = BsonField.create("extra_info");
        private static final BsonField<Boolean> BACKGROUND_FLUSHING_FIELD = BsonField.create("backgroundFlushing");
        private static final BsonField<Boolean> CURSORS_FIELD = BsonField.create("cursors");
        private static final BsonField<Boolean> NETWORK_FIELD = BsonField.create("network");
        private static final BsonField<Boolean> REPL_FIELD = BsonField.create("repl");
        private static final BsonField<Boolean> OPCOUNTERS_REPL_FIELD = BsonField.create("opcountersRepl");
        private static final BsonField<Boolean> OPCOUNTERS_FIELD = BsonField.create("opcounters");
        private static final BsonField<Boolean> RANGE_DELETER_FIELD = BsonField.create("rangeDeleter");
        private static final BsonField<Boolean> SECURITY_FIELD = BsonField.create("security");
        private static final BsonField<Boolean> STORAGE_ENGINE_FIELD = BsonField.create("storageEngine");
        private static final BsonField<Boolean> ASSERTS_FIELD = BsonField.create("asserts");
        private static final BsonField<Boolean> WRITE_BACKS_QUEUED_FIELD = BsonField.create("writeBacksQueued");
        private static final BsonField<Boolean> DUR_FIELD = BsonField.create("dur");
        private static final BsonField<Boolean> METRICS_FIELD = BsonField.create("metrics");
        private static final BsonField<Boolean> WIRED_TIGER_FIELD = BsonField.create("wiredTiger");
        
        private final boolean host;
        private final boolean version;
        private final boolean process;
        private final boolean pid;
        private final boolean uptime;
        private final boolean uptimeEstimate;
        private final boolean localTime;
        private final boolean locks;
        private final boolean globalLock;
        private final boolean mem;
        private final boolean connections;
        private final boolean extraInfo;
        private final boolean backgroundFlushing;
        private final boolean cursors;
        private final boolean network;
        private final boolean repl;
        private final boolean opcountersRepl;
        private final boolean opcounters;
        private final boolean rangeDeleter;
        private final boolean security;
        private final boolean storageEngine;
        private final boolean asserts;
        private final boolean writeBacksQueued;
        private final boolean dur;
        private final boolean metrics;
        private final boolean wiredTiger;

        public ServerStatusArgument(boolean host, boolean version, boolean process, boolean pid, boolean uptime,
                boolean uptimeEstimate, boolean localTime, boolean locks, boolean globalLock, boolean mem,
                boolean connections, boolean extraInfo, boolean backgroundFlushing, boolean cursors, boolean network,
                boolean repl, boolean opcountersRepl, boolean opcounters, boolean rangeDeleter, boolean security,
                boolean storageEngine, boolean asserts, boolean writeBacksQueued, boolean dur, boolean metrics,
                boolean wiredTiger) {
            super();
            this.host = host;
            this.version = version;
            this.process = process;
            this.pid = pid;
            this.uptime = uptime;
            this.uptimeEstimate = uptimeEstimate;
            this.localTime = localTime;
            this.locks = locks;
            this.globalLock = globalLock;
            this.mem = mem;
            this.connections = connections;
            this.extraInfo = extraInfo;
            this.backgroundFlushing = backgroundFlushing;
            this.cursors = cursors;
            this.network = network;
            this.repl = repl;
            this.opcountersRepl = opcountersRepl;
            this.opcounters = opcounters;
            this.rangeDeleter = rangeDeleter;
            this.security = security;
            this.storageEngine = storageEngine;
            this.asserts = asserts;
            this.writeBacksQueued = writeBacksQueued;
            this.dur = dur;
            this.metrics = metrics;
            this.wiredTiger = wiredTiger;
        }

        public boolean isHost() {
            return host;
        }

        public boolean isVersion() {
            return version;
        }

        public boolean isProcess() {
            return process;
        }

        public boolean isPid() {
            return pid;
        }

        public boolean isUptime() {
            return uptime;
        }

        public boolean isUptimeEstimate() {
            return uptimeEstimate;
        }

        public boolean isLocalTime() {
            return localTime;
        }

        public boolean isLocks() {
            return locks;
        }

        public boolean isGlobalLock() {
            return globalLock;
        }

        public boolean isMem() {
            return mem;
        }

        public boolean isConnections() {
            return connections;
        }

        public boolean isExtraInfo() {
            return extraInfo;
        }

        public boolean isBackgroundFlushing() {
            return backgroundFlushing;
        }

        public boolean isCursors() {
            return cursors;
        }

        public boolean isNetwork() {
            return network;
        }

        public boolean isRepl() {
            return repl;
        }

        public boolean isOpcountersRepl() {
            return opcountersRepl;
        }

        public boolean isOpcounters() {
            return opcounters;
        }

        public boolean isRangeDeleter() {
            return rangeDeleter;
        }

        public boolean isSecurity() {
            return security;
        }

        public boolean isStorageEngine() {
            return storageEngine;
        }

        public boolean isAsserts() {
            return asserts;
        }

        public boolean isDur() {
            return dur;
        }

        public boolean isWriteBacksQueued() {
            return writeBacksQueued;
        }

        public boolean isMetrics() {
            return metrics;
        }

        public boolean isWiredTiger() {
            return wiredTiger;
        }

        protected static ServerStatusArgument unmarshall(BsonDocument doc)
                throws TypesMismatchException, BadValueException, NoSuchKeyException {
            boolean host = BsonReaderTool.getBooleanOrNumeric(doc, HOST_FIELD, true);
            boolean version = BsonReaderTool.getBooleanOrNumeric(doc, VERSION_FIELD, true);
            boolean process = BsonReaderTool.getBooleanOrNumeric(doc, PROCESS_FIELD, true);
            boolean pid = BsonReaderTool.getBooleanOrNumeric(doc, PID_FIELD, true);
            boolean uptime = BsonReaderTool.getBooleanOrNumeric(doc, UPTIME_FIELD, true);
            boolean uptimeEstimate = BsonReaderTool.getBooleanOrNumeric(doc, UPTIME_ESTIMATE_FIELD, true);
            boolean localTime = BsonReaderTool.getBooleanOrNumeric(doc, LOCAL_TIME_FIELD, true);
            boolean locks = BsonReaderTool.getBooleanOrNumeric(doc, LOCKS_FIELD, true);
            boolean globalLock = BsonReaderTool.getBooleanOrNumeric(doc, GLOBAL_LOCK_FIELD, true);
            boolean mem = BsonReaderTool.getBooleanOrNumeric(doc, MEM_FIELD, true);
            boolean connections = BsonReaderTool.getBooleanOrNumeric(doc, CONNECTIONS_FIELD, true);
            boolean extraInfo = BsonReaderTool.getBooleanOrNumeric(doc, EXTRA_INFO_FIELD, true);
            boolean backgroundFlushing = BsonReaderTool.getBooleanOrNumeric(doc, BACKGROUND_FLUSHING_FIELD, true);
            boolean cursors = BsonReaderTool.getBooleanOrNumeric(doc, CURSORS_FIELD, true);
            boolean network = BsonReaderTool.getBooleanOrNumeric(doc, NETWORK_FIELD, true);
            boolean repl = BsonReaderTool.getBooleanOrNumeric(doc, REPL_FIELD, true);
            boolean opcountersRepl = BsonReaderTool.getBooleanOrNumeric(doc, OPCOUNTERS_REPL_FIELD, true);
            boolean opcounters = BsonReaderTool.getBooleanOrNumeric(doc, OPCOUNTERS_FIELD, true);
            boolean rangeDeleter = BsonReaderTool.getBooleanOrNumeric(doc, RANGE_DELETER_FIELD, false);
            boolean security = BsonReaderTool.getBooleanOrNumeric(doc, SECURITY_FIELD, true);
            boolean storageEngine = BsonReaderTool.getBooleanOrNumeric(doc, STORAGE_ENGINE_FIELD, true);
            boolean asserts = BsonReaderTool.getBooleanOrNumeric(doc, ASSERTS_FIELD, true);
            boolean writeBacksQueued = BsonReaderTool.getBooleanOrNumeric(doc, WRITE_BACKS_QUEUED_FIELD, true);
            boolean dur = BsonReaderTool.getBooleanOrNumeric(doc, DUR_FIELD, true);
            boolean metrics = BsonReaderTool.getBooleanOrNumeric(doc, METRICS_FIELD, true);
            boolean wiredTiger = BsonReaderTool.getBooleanOrNumeric(doc, WIRED_TIGER_FIELD, true);

            return new ServerStatusArgument(host, version, process, pid, uptime, uptimeEstimate, localTime, 
                    locks, globalLock, mem, connections, extraInfo, backgroundFlushing, cursors, 
                    network, repl, opcountersRepl, opcounters, rangeDeleter, security, storageEngine, 
                    asserts, writeBacksQueued, dur, metrics, wiredTiger);
        }

    }

    //TODO(gortiz): This reply is not prepared to respond on error cases!
    public static class ServerStatusReply {

        private static final BsonField<String> HOST_FIELD = BsonField.create("host");
        private static final BsonField<String> VERSION_FIELD = BsonField.create("version");
        private static final BsonField<String> PROCESS_FIELD = BsonField.create("process");
        private static final BsonField<Integer> PID_FIELD = BsonField.create("pid");
        private static final BsonField<Long> UPTIME_FIELD = BsonField.create("uptime");
        private static final BsonField<Long> UPTIME_ESTIMATE_FIELD = BsonField.create("uptimeEstimate");
        private static final BsonField<Instant> LOCAL_TIME_FIELD = BsonField.create("localTime");
        private static final BsonField<BsonDocument> LOCKS_FIELD = BsonField.create("locks");
        private static final BsonField<BsonDocument> GLOBAL_LOCK_FIELD = BsonField.create("globalLock");
        private static final BsonField<BsonDocument> MEM_FIELD = BsonField.create("mem");
        private static final BsonField<BsonDocument> CONNECTIONS_FIELD = BsonField.create("connections");
        private static final BsonField<BsonDocument> EXTRA_INFO_FIELD = BsonField.create("extra_info");
        private static final BsonField<BsonDocument> BACKGROUND_FLUSHING_FIELD = BsonField.create("backgroundFlushing");
        private static final BsonField<BsonDocument> CURSORS_FIELD = BsonField.create("cursors");
        private static final BsonField<BsonDocument> NETWORK_FIELD = BsonField.create("network");
        private static final BsonField<BsonDocument> REPL_FIELD = BsonField.create("repl");
        private static final BsonField<BsonDocument> OPCOUNTERS_REPL_FIELD = BsonField.create("opcountersRepl");
        private static final BsonField<BsonDocument> OPCOUNTERS_FIELD = BsonField.create("opcounters");
        private static final BsonField<BsonDocument> RANGE_DELETER_FIELD = BsonField.create("rangeDeleter");
        private static final BsonField<BsonDocument> SECURITY_FIELD = BsonField.create("security");
        private static final BsonField<BsonDocument> STORAGE_ENGINE_FIELD = BsonField.create("storageEngine");
        private static final BsonField<BsonDocument> ASSERTS_FIELD = BsonField.create("asserts");
        private static final BsonField<Integer> WRITE_BACKS_QUEUED_FIELD = BsonField.create("writeBacksQueued");
        private static final BsonField<BsonDocument> DUR_FIELD = BsonField.create("dur");
        private static final BsonField<BsonDocument> METRICS_FIELD = BsonField.create("metrics");
        private static final BsonField<BsonDocument> WIRED_TIGER_FIELD = BsonField.create("wiredTiger");

        private final String host;
        private final String version;
        private final String process;
        private final Integer pid;
        private final Long uptime;
        private final Long uptimeEstimate;
        private final Instant localTime;
        private final Locks locks;
        private final GlobalLock globalLock;
        private final Mem mem;
        private final Connections connections;
        private final ExtraInfo extraInfo;
        private final BackgroundFlushing backgroundFlushing;
        private final Cursors cursors;
        private final Network network;
        private final Repl repl;
        private final Opcounters opcountersRepl;
        private final Opcounters opcounters;
        private final RangeDeleter rangeDeleter;
        private final Security security;
        private final StorageEngine storageEngine;
        private final Asserts asserts;
        private final Dur dur;
        private final Integer writebacksQueued;
        private final Metrics metrics;
        private final WiredTiger wiredTiger;

        public ServerStatusReply(String host, String version, String process, Integer pid, Long uptime,
                Long uptimeEstimate, Instant localTime, Locks locks, GlobalLock globalLock, Mem mem,
                Connections connections, ExtraInfo extraInfo, BackgroundFlushing backgroundFlushing, Cursors cursors,
                Network network, Repl repl, Opcounters opcountersRepl, Opcounters opcounters, RangeDeleter rangeDeleter,
                Security security, StorageEngine storageEngine, Asserts asserts, Dur dur, Integer writebacksQueued,
                Metrics metrics, WiredTiger wiredTiger) {
            super();
            this.host = host;
            this.version = version;
            this.process = process;
            this.pid = pid;
            this.uptime = uptime;
            this.uptimeEstimate = uptimeEstimate;
            this.localTime = localTime;
            this.locks = locks;
            this.globalLock = globalLock;
            this.mem = mem;
            this.connections = connections;
            this.extraInfo = extraInfo;
            this.backgroundFlushing = backgroundFlushing;
            this.cursors = cursors;
            this.network = network;
            this.repl = repl;
            this.opcountersRepl = opcountersRepl;
            this.opcounters = opcounters;
            this.rangeDeleter = rangeDeleter;
            this.security = security;
            this.storageEngine = storageEngine;
            this.asserts = asserts;
            this.dur = dur;
            this.writebacksQueued = writebacksQueued;
            this.metrics = metrics;
            this.wiredTiger = wiredTiger;
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            if (host != null) builder.append(HOST_FIELD, host);
            if (version != null) builder.append(VERSION_FIELD, version);
            if (process != null) builder.append(PROCESS_FIELD, process);
            if (pid != null) builder.append(PID_FIELD, pid);
            if (uptime != null) builder.append(UPTIME_FIELD, uptime);
            if (uptimeEstimate != null) builder.append(UPTIME_ESTIMATE_FIELD, uptimeEstimate);
            if (localTime != null) builder.append(LOCAL_TIME_FIELD, localTime);
            if (locks != null) builder.append(LOCKS_FIELD, locks.marshall());
            if (globalLock != null) builder.append(GLOBAL_LOCK_FIELD, globalLock.marshall());
            if (mem != null) builder.append(MEM_FIELD, mem.marshall());
            if (connections != null) builder.append(CONNECTIONS_FIELD, connections.marshall());
            if (extraInfo != null) builder.append(EXTRA_INFO_FIELD, extraInfo.marshall());
            if (backgroundFlushing != null) builder.append(BACKGROUND_FLUSHING_FIELD, backgroundFlushing.marshall());
            if (cursors != null) builder.append(CURSORS_FIELD, cursors.marshall());
            if (network != null) builder.append(NETWORK_FIELD, network.marshall());
            if (repl != null) builder.append(REPL_FIELD, repl.marshall());
            if (opcountersRepl != null) builder.append(OPCOUNTERS_REPL_FIELD, opcountersRepl.marshall());
            if (opcounters != null) builder.append(OPCOUNTERS_FIELD, opcounters.marshall());
            if (rangeDeleter != null) builder.append(RANGE_DELETER_FIELD, rangeDeleter.marshall());
            if (security != null) builder.append(SECURITY_FIELD, security.marshall());
            if (storageEngine != null) builder.append(STORAGE_ENGINE_FIELD, storageEngine.marshall());
            if (asserts != null) builder.append(ASSERTS_FIELD, asserts.marshall());
            if (dur != null) builder.append(DUR_FIELD, dur.marshall());
            if (writebacksQueued != null) builder.append(WRITE_BACKS_QUEUED_FIELD, writebacksQueued);
            if (metrics != null) builder.append(METRICS_FIELD, metrics.marshall());
            if (wiredTiger != null) builder.append(WIRED_TIGER_FIELD, wiredTiger.marshall());
            return builder.build();
        }

        public String getHost() {
            return host;
        }

        public String getVersion() {
            return version;
        }

        public String getProcess() {
            return process;
        }

        public Integer getPid() {
            return pid;
        }

        public Long getUptime() {
            return uptime;
        }

        public Long getUptimeEstimate() {
            return uptimeEstimate;
        }

        public Instant getLocalTime() {
            return localTime;
        }

        public Locks getLocks() {
            return locks;
        }

        public GlobalLock getGlobalLock() {
            return globalLock;
        }

        public Mem getMem() {
            return mem;
        }

        public Connections getConnections() {
            return connections;
        }

        public ExtraInfo getExtraInfo() {
            return extraInfo;
        }

        public BackgroundFlushing getBackgroundFlushing() {
            return backgroundFlushing;
        }

        public Cursors getCursors() {
            return cursors;
        }

        public Network getNetwork() {
            return network;
        }

        public Repl getRepl() {
            return repl;
        }

        public Opcounters getOpcountersRepl() {
            return opcountersRepl;
        }

        public Opcounters getOpcounters() {
            return opcounters;
        }

        public RangeDeleter getRangeDeleter() {
            return rangeDeleter;
        }

        public Security getSecurity() {
            return security;
        }

        public StorageEngine getStorageEngine() {
            return storageEngine;
        }

        public Asserts getAsserts() {
            return asserts;
        }

        public Dur getDur() {
            return dur;
        }

        public Integer getWritebacksQueued() {
            return writebacksQueued;
        }

        public Metrics getMetrics() {
            return metrics;
        }

        public WiredTiger getWiredTiger() {
            return wiredTiger;
        }

        public static class Builder {

            private String host;
            private String version;
            private String process;
            private Integer pid;
            private Long uptime;
            private Long uptimeEstimate;
            private Instant localTime;
            private Locks locks;
            private GlobalLock globalLock;
            private Mem mem;
            private Connections connections;
            private ExtraInfo extraInfo;
            private BackgroundFlushing backgroundFlushing;
            private Cursors cursors;
            private Network network;
            private Repl repl;
            private Opcounters opcountersRepl;
            private Opcounters opcounters;
            private RangeDeleter rangeDeleter;
            private Security security;
            private StorageEngine storageEngine;
            private Asserts asserts;
            private Dur dur;
            private Integer writebacksQueued;
            private Metrics metrics;
            private WiredTiger wiredTiger;

            public Builder() {
            }

            public String getHost() {
                return host;
            }

            public Builder setHost(String host) {
                this.host = host;
                return this;
            }

            public String getVersion() {
                return version;
            }

            public Builder setVersion(String version) {
                this.version = version;
                return this;
            }

            public String getProcess() {
                return process;
            }

            public Builder setProcess(String process) {
                this.process = process;
                return this;
            }

            public Integer getPid() {
                return pid;
            }

            public Builder setPid(Integer pid) {
                this.pid = pid;
                return this;
            }

            public Long getUptime() {
                return uptime;
            }

            public Builder setUptime(Long uptime) {
                this.uptime = uptime;
                return this;
            }

            public Long getUptimeEstimate() {
                return uptimeEstimate;
            }

            public Builder setUptimeEstimate(Long uptimeEstimate) {
                this.uptimeEstimate = uptimeEstimate;
                return this;
            }

            public Instant getLocalTime() {
                return localTime;
            }

            public Builder setLocalTime(Instant localTime) {
                this.localTime = localTime;
                return this;
            }

            public Locks getLocks() {
                return locks;
            }

            public Builder setLocks(Locks locks) {
                this.locks = locks;
                return this;
            }

            public GlobalLock getGlobalLock() {
                return globalLock;
            }

            public Builder setGlobalLock(GlobalLock globalLock) {
                this.globalLock = globalLock;
                return this;
            }

            public Mem getMem() {
                return mem;
            }

            public Builder setMem(Mem mem) {
                this.mem = mem;
                return this;
            }

            public Connections getConnections() {
                return connections;
            }

            public Builder setConnections(Connections connections) {
                this.connections = connections;
                return this;
            }

            public ExtraInfo getExtraInfo() {
                return extraInfo;
            }

            public Builder setExtraInfo(ExtraInfo extraInfo) {
                this.extraInfo = extraInfo;
                return this;
            }

            public BackgroundFlushing getBackgroundFlushing() {
                return backgroundFlushing;
            }

            public Builder setBackgroundFlushing(BackgroundFlushing backgroundFlushing) {
                this.backgroundFlushing = backgroundFlushing;
                return this;
            }

            public Cursors getCursors() {
                return cursors;
            }

            public Builder setCursors(Cursors cursors) {
                this.cursors = cursors;
                return this;
            }

            public Network getNetwork() {
                return network;
            }

            public Builder setNetwork(Network network) {
                this.network = network;
                return this;
            }

            public Repl getRepl() {
                return repl;
            }

            public Builder setRepl(Repl repl) {
                this.repl = repl;
                return this;
            }

            public Opcounters getOpcountersRepl() {
                return opcountersRepl;
            }

            public Builder setOpcountersRepl(Opcounters opcountersRepl) {
                this.opcountersRepl = opcountersRepl;
                return this;
            }

            public Opcounters getOpcounters() {
                return opcounters;
            }

            public Builder setOpcounters(Opcounters opcounters) {
                this.opcounters = opcounters;
                return this;
            }

            public RangeDeleter getRangeDeleter() {
                return rangeDeleter;
            }

            public Builder setRangeDeleter(RangeDeleter rangeDeleter) {
                this.rangeDeleter = rangeDeleter;
                return this;
            }

            public Security getSecurity() {
                return security;
            }

            public Builder setSecurity(Security security) {
                this.security = security;
                return this;
            }

            public StorageEngine getStorageEngine() {
                return storageEngine;
            }

            public Builder setStorageEngine(StorageEngine storageEngine) {
                this.storageEngine = storageEngine;
                return this;
           }

            public Asserts getAsserts() {
                return asserts;
            }

            public Builder setAsserts(Asserts asserts) {
                this.asserts = asserts;
                return this;
            }

            public Dur getDur() {
                return dur;
            }

            public Builder setDur(Dur dur) {
                this.dur = dur;
                return this;
            }

            public Integer getWritebacksQueued() {
                return writebacksQueued;
            }

            public Builder setWritebacksQueued(Integer writebacksQueued) {
                this.writebacksQueued = writebacksQueued;
                return this;
            }

            public Metrics getMetrics() {
                return metrics;
            }

            public Builder setMetrics(Metrics metrics) {
                this.metrics = metrics;
                return this;
            }

            public WiredTiger getWiredTiger() {
                return wiredTiger;
            }

            public Builder setWiredTiger(WiredTiger wiredTiger) {
                this.wiredTiger = wiredTiger;
                return this;
            }

            public ServerStatusReply build() {
                return new ServerStatusReply(
                        host, 
                        version, 
                        process, 
                        pid, 
                        uptime, 
                        uptimeEstimate, 
                        localTime, 
                        locks, 
                        globalLock, 
                        mem, 
                        connections, 
                        extraInfo, 
                        backgroundFlushing, 
                        cursors, 
                        network, 
                        repl, 
                        opcountersRepl, 
                        opcounters, 
                        rangeDeleter, 
                        security, 
                        storageEngine, 
                        asserts, 
                        dur, 
                        writebacksQueued, 
                        metrics, 
                        wiredTiger);
            }
        }

    }

    public static class Locks {
        private static final BsonField<BsonDocument> LOCKS_GLOBAL_FIELD = BsonField.create("Global");
        private static final BsonField<BsonDocument> LOCKS_MMAPV1_JOURNAL_FIELD = BsonField.create("MMAPV1Journal");
        private static final BsonField<BsonDocument> LOCKS_DATABASE_FIELD = BsonField.create("Database");
        private static final BsonField<BsonDocument> LOCKS_COLLECTION_FIELD = BsonField.create("Collection");
        private static final BsonField<BsonDocument> LOCKS_METADATA_FIELD = BsonField.create("Metadata");
        private static final BsonField<BsonDocument> LOCKS_OPLOG_FIELD = BsonField.create("oplog");

        private final Lock global;
        private final Lock mmapv1Journal;
        private final Lock database;
        private final Lock collection;
        private final Lock metadata;
        private final Lock oplog;

        public Locks(Lock global, Lock mmapv1Journal, Lock database, Lock collection, Lock metadata, Lock oplog) {
            super();
            this.global = global;
            this.mmapv1Journal = mmapv1Journal;
            this.database = database;
            this.collection = collection;
            this.metadata = metadata;
            this.oplog = oplog;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(LOCKS_GLOBAL_FIELD, global.marshall())
                    .append(LOCKS_MMAPV1_JOURNAL_FIELD, mmapv1Journal.marshall())
                    .append(LOCKS_DATABASE_FIELD, database.marshall())
                    .append(LOCKS_COLLECTION_FIELD, collection.marshall())
                    .append(LOCKS_METADATA_FIELD, metadata.marshall())
                    .append(LOCKS_OPLOG_FIELD, oplog.marshall())
                    .build();
        }

        public Lock getGlobal() {
            return global;
        }

        public Lock getMmapv1journal() {
            return mmapv1Journal;
        }

        public Lock getDatabase() {
            return database;
        }

        public Lock getCollection() {
            return collection;
        }

        public Lock getMetadata() {
            return metadata;
        }

        public Lock getOplog() {
            return oplog;
        }
        
        public static class Lock {
            private static final BsonField<BsonDocument> LOCKS_LOCK_AQUIRE_COUNT_FIELD = BsonField.create("acquireCount");
            private static final BsonField<BsonDocument> LOCKS_LOCK_AQUIRE_WAIT_COUNT_FIELD = BsonField.create("acquireWaitCount");
            private static final BsonField<BsonDocument> LOCKS_LOCK_TIME_ACQUIRING_MICROS_COUNT_FIELD = BsonField.create("timeAcquiringMicros");
            private static final BsonField<BsonDocument> LOCKS_LOCK_DEADLOCK_COUNT_FIELD = BsonField.create("deadlockCount");

            private final Count acquireCount;
            private final Count acquireWaitCount;
            private final Count timeAcquiringMicros;
            private final Count deadlockCount;

            public Lock(Count acquireCount, Count acquireWaitCount, Count timeAcquiringMicros, Count deadlockCount) {
                super();
                this.acquireCount = acquireCount;
                this.acquireWaitCount = acquireWaitCount;
                this.timeAcquiringMicros = timeAcquiringMicros;
                this.deadlockCount = deadlockCount;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(LOCKS_LOCK_AQUIRE_COUNT_FIELD, acquireCount.marshall())
                        .append(LOCKS_LOCK_AQUIRE_WAIT_COUNT_FIELD, acquireWaitCount.marshall())
                        .append(LOCKS_LOCK_TIME_ACQUIRING_MICROS_COUNT_FIELD, timeAcquiringMicros.marshall())
                        .append(LOCKS_LOCK_DEADLOCK_COUNT_FIELD, deadlockCount.marshall())
                        .build();
            }

            public Count getAcquireCount() {
                return acquireCount;
            }

            public Count getAcquireWaitCount() {
                return acquireWaitCount;
            }

            public Count getTimeAcquiringMicros() {
                return timeAcquiringMicros;
            }

            public Count getDeadlockCount() {
                return deadlockCount;
            }
        }
        
        public static class Count {
            private static final BsonField<Integer> LOCKS_LOCK_R_LOWER_FIELD = BsonField.create("r");
            private static final BsonField<Integer> LOCKS_LOCK_W_LOWER_FIELD = BsonField.create("w");
            private static final BsonField<Integer> LOCKS_LOCK_R_UPPER_FIELD = BsonField.create("R");
            private static final BsonField<Integer> LOCKS_LOCK_W_UPPER_FIELD = BsonField.create("W");

            private final int intentShared;
            private final int intentExclusive;
            private final int shared;
            private final int exclusive;

            public Count(int intentShared, int intentExclusive, int shared, int exclusive) {
                super();
                this.intentShared = intentShared;
                this.intentExclusive = intentExclusive;
                this.shared = shared;
                this.exclusive = exclusive;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(LOCKS_LOCK_R_LOWER_FIELD, intentShared)
                        .append(LOCKS_LOCK_W_LOWER_FIELD, intentExclusive)
                        .append(LOCKS_LOCK_R_UPPER_FIELD, shared)
                        .append(LOCKS_LOCK_W_UPPER_FIELD, exclusive)
                        .build();
            }

            public int getIntentShared() {
                return intentShared;
            }

            public int getIntentExclusive() {
                return intentExclusive;
            }

            public int getExclusive() {
                return exclusive;
            }
        }
    }

    public static class GlobalLock {
        private static final BsonField<Long> GLOBAL_LOCK_TOTAL_TIME_FIELD = BsonField.create("totalTime");
        private static final BsonField<BsonDocument> GLOBAL_LOCK_CURRENT_QUEUE_FIELD = BsonField.create("currentQueue");
        private static final BsonField<BsonDocument> GLOBAL_LOCK_ACTIVE_CLIENTS_FIELD = BsonField.create("activeClients");

        private final long totalTime;
        private final GlobalLockStats currentQueue;
        private final GlobalLockStats activeClients;

        public GlobalLock(long totalTime, GlobalLockStats currentQueue, GlobalLockStats activeClients) {
            super();
            this.totalTime = totalTime;
            this.currentQueue = currentQueue;
            this.activeClients = activeClients;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(GLOBAL_LOCK_TOTAL_TIME_FIELD, totalTime)
                    .append(GLOBAL_LOCK_CURRENT_QUEUE_FIELD, currentQueue.marshall())
                    .append(GLOBAL_LOCK_ACTIVE_CLIENTS_FIELD, activeClients.marshall())
                    .build();
        }
        
        public long getTotalTime() {
            return totalTime;
        }

        public GlobalLockStats getCurrentQueue() {
            return currentQueue;
        }

        public GlobalLockStats getActiveClients() {
            return activeClients;
        }

        public static class GlobalLockStats {
            private static final BsonField<Integer> GLOBAL_LOCK_TOTAL_FIELD = BsonField.create("total");
            private static final BsonField<Integer> GLOBAL_LOCK_READERS_FIELD = BsonField.create("readers");
            private static final BsonField<Integer> GLOBAL_LOCK_WRITERS_FIELD = BsonField.create("writers");

            private final int total;
            private final int readers;
            private final int writers;

            public GlobalLockStats(int total, int readers, int writers) {
                super();
                this.total = total;
                this.readers = readers;
                this.writers = writers;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(GLOBAL_LOCK_TOTAL_FIELD, total)
                        .append(GLOBAL_LOCK_READERS_FIELD, readers)
                        .append(GLOBAL_LOCK_WRITERS_FIELD, writers)
                        .build();
            }

            public int getTotal() {
                return total;
            }

            public int getReaders() {
                return readers;
            }

            public int getWriters() {
                return writers;
            }
        }
    }

    public static class Mem {
        private static final BsonField<Integer> MEM_BITS_FIELD = BsonField.create("bits");
        private static final BsonField<Long> MEM_RESIDENT_FIELD = BsonField.create("resident");
        private static final BsonField<Long> MEM_VIRTUAL_FIELD = BsonField.create("virtual");
        private static final BsonField<Boolean> MEM_SUPPORTED_FIELD = BsonField.create("supported");
        private static final BsonField<Long> MEM_MAPPED_FIELD = BsonField.create("mapped");
        private static final BsonField<Long> MEM_MAPPED_WITH_JOURNAL_FIELD = BsonField.create("mappedWithJournal");
        private static final BsonField<String> MEM_NOTE_FIELD = BsonField.create("note");

        private final int bits;
        private final long resident;
        private final long virtual;
        private final boolean supported;
        private final long mapped;
        private final long mappedWithJournal;
        private final String note;

        public Mem(int bits, long resident, long virtual, boolean supported, long mapped, long mappedWithJournal,
                String note) {
            super();
            this.bits = bits;
            this.resident = resident;
            this.virtual = virtual;
            this.supported = supported;
            this.mapped = mapped;
            this.mappedWithJournal = mappedWithJournal;
            this.note = note;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(MEM_BITS_FIELD, bits)
                    .append(MEM_RESIDENT_FIELD, resident)
                    .append(MEM_VIRTUAL_FIELD, virtual)
                    .append(MEM_SUPPORTED_FIELD, supported)
                    .append(MEM_MAPPED_FIELD, mapped)
                    .append(MEM_MAPPED_WITH_JOURNAL_FIELD, mappedWithJournal)
                    .append(MEM_NOTE_FIELD, note)
                    .build();
        }

        public int getBits() {
            return bits;
        }

        public long getResident() {
            return resident;
        }

        public long getVirtual() {
            return virtual;
        }

        public boolean isSupported() {
            return supported;
        }

        public long getMapped() {
            return mapped;
        }

        public long getMappedWithJournal() {
            return mappedWithJournal;
        }

        public String getNote() {
            return note;
        }
    }

    public static class Connections {
        private static final BsonField<Integer> CONNECTIONS_CURRENT_FIELD = BsonField.create("current");
        private static final BsonField<Integer> CONNECTIONS_AVAILABLE_FIELD = BsonField.create("available");
        private static final BsonField<Integer> CONNECTIONS_TOTAL_CREATED_FIELD = BsonField.create("totalCreated");

        private final int current;
        private final int available;
        private final int totalCreated;

        public Connections(int current, int available, int totalCreated) {
            super();
            this.current = current;
            this.available = available;
            this.totalCreated = totalCreated;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(CONNECTIONS_CURRENT_FIELD, current)
                    .append(CONNECTIONS_AVAILABLE_FIELD, available)
                    .append(CONNECTIONS_TOTAL_CREATED_FIELD, totalCreated)
                    .build();
        }

        public int getCurrent() {
            return current;
        }

        public int getAvailable() {
            return available;
        }

        public int getTotalCreated() {
            return totalCreated;
        }
    }

    public static class ExtraInfo {
        private static final BsonField<String> EXTRA_INFO_NOTE_FIELD = BsonField.create("note");
        private static final BsonField<Long> EXTRA_INFO_HEAP_USAGE_BYTES_FIELD = BsonField.create("heap_usage_bytes");
        private static final BsonField<Integer> EXTRA_INFO_FAULTS_FIELD = BsonField.create("page_faults");

        private final String note;
        private final long heapUsageBytes;
        private final int pageFaults;

        public ExtraInfo(String note, long heapUsageBytes, int pageFaults) {
            super();
            this.note = note;
            this.heapUsageBytes = heapUsageBytes;
            this.pageFaults = pageFaults;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(EXTRA_INFO_NOTE_FIELD, note)
                    .append(EXTRA_INFO_HEAP_USAGE_BYTES_FIELD, heapUsageBytes)
                    .append(EXTRA_INFO_FAULTS_FIELD, pageFaults)
                    .build();
        }

        public String getNote() {
            return note;
        }

        public long getHeapUsageBytes() {
            return heapUsageBytes;
        }

        public int getPageFaults() {
            return pageFaults;
        }
    }
    
    public static class BackgroundFlushing {
        private static final BsonField<Integer> BACKGROUND_FLUSHING_FLUSHES_FIELD = BsonField.create("flushes");
        private static final BsonField<Long> BACKGROUND_FLUSHING_TOTAL_MS_FIELD = BsonField.create("total_ms");
        private static final BsonField<Long> BACKGROUND_FLUSHING_AVERAGE_MS_FIELD = BsonField.create("average_ms");
        private static final BsonField<Long> BACKGROUND_FLUSHING_LAST_MS_FIELD = BsonField.create("last_ms");
        private static final BsonField<Instant> BACKGROUND_FLUSHING_LAST_FINISHED_FIELD = BsonField.create("last_finished");

        private final int flushes;
        private final long totalMs;
        private final long averageMs;
        private final long lastMs;
        private final Instant lastFinished;

        public BackgroundFlushing(int flushes, long totalMs, long averageMs, long lastMs, @Nonnull Instant lastFinished) {
            super();
            this.flushes = flushes;
            this.totalMs = totalMs;
            this.averageMs = averageMs;
            this.lastMs = lastMs;
            this.lastFinished = lastFinished;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(BACKGROUND_FLUSHING_FLUSHES_FIELD, flushes)
                    .append(BACKGROUND_FLUSHING_TOTAL_MS_FIELD, totalMs)
                    .append(BACKGROUND_FLUSHING_AVERAGE_MS_FIELD, averageMs)
                    .append(BACKGROUND_FLUSHING_LAST_MS_FIELD, lastMs)
                    .append(BACKGROUND_FLUSHING_LAST_FINISHED_FIELD, lastFinished)
                    .build();
        }

        public int getFlushes() {
            return flushes;
        }

        public long getTotalMs() {
            return totalMs;
        }

        public long getAverageMs() {
            return averageMs;
        }

        public long getLastMs() {
            return lastMs;
        }

        public Instant getLastFinished() {
            return lastFinished;
        }
    }

    public static class Cursors {
        private static final BsonField<String> CURSORS_NOTE_FIELD = BsonField.create("note");
        private static final BsonField<Integer> CURSORS_TOTAL_OPEN_FIELD = BsonField.create("totalOpen");
        private static final BsonField<Long> CURSORS_CLIENT_CUSRORS_SIZE_FIELD = BsonField.create("clientCursors_size");
        private static final BsonField<Integer> CURSORS_TIMED_OUT_FIELD = BsonField.create("timedOut");
        private static final BsonField<Integer> CURSORS_TOTAL_NO_TIMEOUT_FIELD = BsonField.create("totalNoTimeout");
        private static final BsonField<Integer> CURSORS_PINNED_FIELD = BsonField.create("pinned");

        private final String note;
        private final int totalOpen;
        private final long clientCursorsSize;
        private final int timedOut;
        private final int totalNoTimeout;
        private final int pinned;

        public Cursors(String note, int totalOpen, long clientCursorsSize, int timedOut, int totalNoTimeout,
                int pinned) {
            super();
            this.note = note;
            this.totalOpen = totalOpen;
            this.clientCursorsSize = clientCursorsSize;
            this.timedOut = timedOut;
            this.totalNoTimeout = totalNoTimeout;
            this.pinned = pinned;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(CURSORS_NOTE_FIELD, note)
                    .append(CURSORS_TOTAL_OPEN_FIELD, totalOpen)
                    .append(CURSORS_CLIENT_CUSRORS_SIZE_FIELD, clientCursorsSize)
                    .append(CURSORS_TIMED_OUT_FIELD, timedOut)
                    .append(CURSORS_TOTAL_NO_TIMEOUT_FIELD, totalNoTimeout)
                    .append(CURSORS_PINNED_FIELD, pinned)
                    .build();
        }

        public String getNote() {
            return note;
        }

        public int getTotalOpen() {
            return totalOpen;
        }

        public int getTimedOut() {
            return timedOut;
        }

        public int getTotalNoTimeout() {
            return totalNoTimeout;
        }

        public int getPinned() {
            return pinned;
        }
    }

    public static class Network {
        private static final BsonField<Long> NETWORK_BYTES_IN_FIELD = BsonField.create("bytesIn");
        private static final BsonField<Long> NETWORK_BYTES_OUT_FIELD = BsonField.create("bytesOut");
        private static final BsonField<Integer> NETWORK_NUM_REQUESTS_FIELD = BsonField.create("numRequests");

        private final long bytesIn;
        private final long bytesOut;
        private final int numRequests;

        public Network(long bytesIn, long bytesOut, int numRequests) {
            super();
            this.bytesIn = bytesIn;
            this.bytesOut = bytesOut;
            this.numRequests = numRequests;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(NETWORK_BYTES_IN_FIELD, bytesIn)
                    .append(NETWORK_BYTES_OUT_FIELD, bytesOut)
                    .append(NETWORK_NUM_REQUESTS_FIELD, numRequests)
                    .build();
        }

        public long getBytesIn() {
            return bytesIn;
        }

        public long getBytesOut() {
            return bytesOut;
        }

        public int getNumRequests() {
            return numRequests;
        }
    }
    
    public static class Repl {
        private static final BsonField<String> REPL_SET_NAME_FIELD = BsonField.create("setName");
        private static final BsonField<Boolean> REPL_ISMASTER_FIELD = BsonField.create("ismaster");
        private static final BsonField<Boolean> REPL_SECONDARY_FIELD = BsonField.create("secondary");
        private static final BsonField<String> REPL_PRIMARY_FIELD = BsonField.create("primary");
        private static final BsonField<BsonArray> REPL_HOSTS_FIELD = BsonField.create("hosts");
        private static final BsonField<String> REPL_ME_FIELD = BsonField.create("me");
        private static final BsonField<ObjectId> REPL_ELECTION_ID_FIELD = BsonField.create("electionId");
        private static final BsonField<Long> REPL_RBID_FIELD = BsonField.create("rbid");
        private static final BsonField<BsonArray> REPL_SLAVES_FIELD = BsonField.create("slaves");
        
        private final String setName;
        private final boolean ismaster;
        private final boolean secondary;
        private final String primary;
        private final ImmutableList<String> hosts;
        private final String me;
        private final ObjectId electionId;
        private final long rbid;
        private final ImmutableList<Slave> slaves;
        
        public Repl(String setName, boolean ismaster, boolean secondary, String primary, ImmutableList<String> hosts,
                String me, ObjectId electionId, long rbid, ImmutableList<Slave> slaves) {
            super();
            this.setName = setName;
            this.ismaster = ismaster;
            this.secondary = secondary;
            this.primary = primary;
            this.hosts = hosts;
            this.me = me;
            this.electionId = electionId;
            this.rbid = rbid;
            this.slaves = slaves;
        }

        private BsonDocument marshall() {
            BsonArray hostsArr = new BsonArray();
            for (String host : hosts) {
                hostsArr.add(new BsonString(host));
            }
            
            BsonArray slavesArr = new BsonArray();
            for (Slave slave : slaves) {
                slavesArr.add(slave.marshall());
            }
            
            return new BsonDocumentBuilder()
                    .append(REPL_SET_NAME_FIELD, setName)
                    .append(REPL_ISMASTER_FIELD, ismaster)
                    .append(REPL_SECONDARY_FIELD, secondary)
                    .append(REPL_PRIMARY_FIELD, primary)
                    .append(REPL_HOSTS_FIELD, hostsArr)
                    .append(REPL_ME_FIELD, me)
                    .append(REPL_ELECTION_ID_FIELD, electionId)
                    .append(REPL_RBID_FIELD, rbid)
                    .append(REPL_SLAVES_FIELD, slavesArr)
                    .build();
        }
        
        public String getSetName() {
            return setName;
        }

        public boolean isIsmaster() {
            return ismaster;
        }

        public boolean isSecondary() {
            return secondary;
        }

        public String getPrimary() {
            return primary;
        }

        public ImmutableList<String> getHosts() {
            return hosts;
        }

        public String getMe() {
            return me;
        }

        public ObjectId getElectionId() {
            return electionId;
        }

        public long getRbid() {
            return rbid;
        }

        public ImmutableList<Slave> getSlaves() {
            return slaves;
        }

        public static class Slave {
            private static final BsonField<Long> REPL_SLAVES_RID_FIELD = BsonField.create("rid");
            private static final BsonField<String> REPL_SLAVES_HOST_FIELD = BsonField.create("host");
            private static final BsonField<Long> REPL_SLAVES_OPTIME_FIELD = BsonField.create("optime");
            private static final BsonField<Long> REPL_SLAVES_MEMBER_ID_FIELD = BsonField.create("memberID");
    
            private final long rid;
            private final String host;
            private final long optime;
            private final long memeberId;
    
            public Slave(long rid, String host, long optime, long memeberId) {
                super();
                this.rid = rid;
                this.host = host;
                this.optime = optime;
                this.memeberId = memeberId;
            }
    
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(REPL_SLAVES_RID_FIELD, rid)
                        .append(REPL_SLAVES_HOST_FIELD, host)
                        .append(REPL_SLAVES_OPTIME_FIELD, optime)
                        .append(REPL_SLAVES_MEMBER_ID_FIELD, memeberId)
                        .build();
            }
    
            public long getRid() {
                return rid;
            }
    
            public String getHost() {
                return host;
            }
    
            public long getOptime() {
                return optime;
            }
    
            public long getMemeberId() {
                return memeberId;
            }
        }
    }

    public static class Opcounters {
        private static final BsonField<Integer> OPCOUNTERS_INSERT_FIELD = BsonField.create("insert");
        private static final BsonField<Integer> OPCOUNTERS_QUERY_FIELD = BsonField.create("query");
        private static final BsonField<Integer> OPCOUNTERS_UPDATE_FIELD = BsonField.create("update");
        private static final BsonField<Integer> OPCOUNTERS_DELETE_FIELD = BsonField.create("delete");
        private static final BsonField<Integer> OPCOUNTERS_GETMORE_FIELD = BsonField.create("getmore");
        private static final BsonField<Integer> OPCOUNTERS_COMMAND_FIELD = BsonField.create("command");
        
        private final int insert;
        private final int query;
        private final int update;
        private final int delete;
        private final int getmore;
        private final int command;

        public Opcounters(int insert, int query, int update, int delete, int getmore,
                int command) {
            super();
            this.insert = insert;
            this.query = query;
            this.update = update;
            this.delete = delete;
            this.getmore = getmore;
            this.command = command;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(OPCOUNTERS_INSERT_FIELD, insert)
                    .append(OPCOUNTERS_QUERY_FIELD, query)
                    .append(OPCOUNTERS_UPDATE_FIELD, update)
                    .append(OPCOUNTERS_DELETE_FIELD, delete)
                    .append(OPCOUNTERS_GETMORE_FIELD, getmore)
                    .append(OPCOUNTERS_COMMAND_FIELD, command)
                    .build();
        }

        public int getInsert() {
            return insert;
        }

        public int getQuery() {
            return query;
        }

        public int getUpdate() {
            return update;
        }

        public int getDelete() {
            return delete;
        }

        public int getGetmore() {
            return getmore;
        }

        public int getCommand() {
            return command;
        }
    }

    public static class RangeDeleter {
        private static final BsonField<BsonArray> RANGE_DELETER_LAST_DELETE_STATS_FIELD = BsonField.create("lastDeleteStats");

        private final ImmutableList<LastDeletedStat> lastDeleteStats;

        public RangeDeleter(ImmutableList<LastDeletedStat> lastDeleteStats) {
            super();
            this.lastDeleteStats = lastDeleteStats;
        }

        private BsonDocument marshall() {
            BsonArray lastDeleteStatsArr = new BsonArray();
            for (LastDeletedStat lastDeletedStat : lastDeleteStats) {
                lastDeleteStatsArr.add(lastDeletedStat.marshall());
            }
            
            return new BsonDocumentBuilder()
                    .append(RANGE_DELETER_LAST_DELETE_STATS_FIELD, lastDeleteStatsArr)
                    .build();
        }

        public ImmutableList<LastDeletedStat> getLastDeleteStats() {
            return lastDeleteStats;
        }

        public static class LastDeletedStat {
            private static final BsonField<Integer> RANGE_DELETER_LAST_DELETE_STATS_DELETED_DOCS_FIELD = BsonField.create("deletedDocs");
            private static final BsonField<Instant> RANGE_DELETER_LAST_DELETE_STATS_QUEUE_START_FIELD = BsonField.create("queueStart");
            private static final BsonField<Instant> RANGE_DELETER_LAST_DELETE_STATS_QUEUE_END_FIELD = BsonField.create("queueEnd");
            private static final BsonField<Instant> RANGE_DELETER_LAST_DELETE_STATS_DELETE_START_FIELD = BsonField.create("deleteStart");
            private static final BsonField<Instant> RANGE_DELETER_LAST_DELETE_STATS_DELETE_END_FIELD = BsonField.create("deleteEnd");
            private static final BsonField<Instant> RANGE_DELETER_LAST_DELETE_STATS_WAIT_FOR_START_FIELD = BsonField.create("waitForReplStart");
            private static final BsonField<Instant> RANGE_DELETER_LAST_DELETE_STATS_WAIT_FOR_END_FIELD = BsonField.create("waitForReplEnd");

            private final int deletedDocs;
            private final Instant queueStart;
            private final Instant queueEnd;
            private final Instant deleteStart;
            private final Instant deleteEnd;
            private final Instant waitForReplStart;
            private final Instant waitForReplEnd;

            public LastDeletedStat(int deletedDocs, Instant queueStart, Instant queueEnd, Instant deleteStart,
                    Instant deleteEnd, Instant waitForReplStart, Instant waitForReplEnd) {
                super();
                this.deletedDocs = deletedDocs;
                this.queueStart = queueStart;
                this.queueEnd = queueEnd;
                this.deleteStart = deleteStart;
                this.deleteEnd = deleteEnd;
                this.waitForReplStart = waitForReplStart;
                this.waitForReplEnd = waitForReplEnd;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(RANGE_DELETER_LAST_DELETE_STATS_DELETED_DOCS_FIELD, deletedDocs)
                        .append(RANGE_DELETER_LAST_DELETE_STATS_QUEUE_START_FIELD, queueStart)
                        .append(RANGE_DELETER_LAST_DELETE_STATS_QUEUE_END_FIELD, queueEnd)
                        .append(RANGE_DELETER_LAST_DELETE_STATS_DELETE_START_FIELD, deleteStart)
                        .append(RANGE_DELETER_LAST_DELETE_STATS_DELETE_END_FIELD, deleteEnd)
                        .append(RANGE_DELETER_LAST_DELETE_STATS_WAIT_FOR_START_FIELD, waitForReplStart)
                        .append(RANGE_DELETER_LAST_DELETE_STATS_WAIT_FOR_END_FIELD, waitForReplEnd)
                        .build();
            }

            public int getDeletedDocs() {
                return deletedDocs;
            }

            public Instant getQueueStart() {
                return queueStart;
            }

            public Instant getQueueEnd() {
                return queueEnd;
            }

            public Instant getDeleteStart() {
                return deleteStart;
            }

            public Instant getDeleteEnd() {
                return deleteEnd;
            }

            public Instant getWaitForReplStart() {
                return waitForReplStart;
            }

            public Instant getWaitForReplEnd() {
                return waitForReplEnd;
            }
        }
    }

    public static class Security {
        private static final BsonField<String> SECURITY_SSL_SERVER_SUBJECT_NAME_FIELD = BsonField.create("SSLServerSubjectName");
        private static final BsonField<Boolean> SECURITY_SSL_SERVER_HAS_CERTIFICATE_AUTHORITY_FIELD = BsonField.create("SSLServerHasCertificateAuthority");
        private static final BsonField<Instant> SECURITY_SSL_SERVER_CERTIFICATE_EXPIRATION_DATE_FIELD = BsonField.create("SSLServerCertificateExpirationDate");

        private final String sslServerSubjectName;
        private final boolean sslServerHasCertificateAuthority;
        private final Instant sslServerCertificateExpirationDate;

        public Security(String sSLServerSubjectName, boolean sSLServerHasCertificateAuthority,
                Instant sSLServerCertificateExpirationDate) {
            super();
            sslServerSubjectName = sSLServerSubjectName;
            sslServerHasCertificateAuthority = sSLServerHasCertificateAuthority;
            sslServerCertificateExpirationDate = sSLServerCertificateExpirationDate;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(SECURITY_SSL_SERVER_SUBJECT_NAME_FIELD, sslServerSubjectName)
                    .append(SECURITY_SSL_SERVER_HAS_CERTIFICATE_AUTHORITY_FIELD, sslServerHasCertificateAuthority)
                    .append(SECURITY_SSL_SERVER_CERTIFICATE_EXPIRATION_DATE_FIELD, sslServerCertificateExpirationDate)
                    .build();
        }

        public String getSSLServerSubjectName() {
            return sslServerSubjectName;
        }

        public boolean isSSLServerHasCertificateAuthority() {
            return sslServerHasCertificateAuthority;
        }

        public Instant getSSLServerCertificateExpirationDate() {
            return sslServerCertificateExpirationDate;
        }
    }

    public static class StorageEngine {
        private static final BsonField<String> STORAGE_ENGINE_NAME_FIELD = BsonField.create("name");

        private final String name;

        public StorageEngine(String name) {
            super();
            this.name = name;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(STORAGE_ENGINE_NAME_FIELD, name)
                    .build();
        }

        public String getName() {
            return name;
        }
    }

    public static class Asserts {
        private static final BsonField<Integer> ASSERTS_REGULAR_FIELD = BsonField.create("regular");
        private static final BsonField<Integer> ASSERTS_WARNING_FIELD = BsonField.create("warning");
        private static final BsonField<Integer> ASSERTS_MSG_FIELD = BsonField.create("msg");
        private static final BsonField<Integer> ASSERTS_USER_FIELD = BsonField.create("user");
        private static final BsonField<Integer> ASSERTS_ROLLOVERS_FIELD = BsonField.create("rollovers");

        private final Integer regular;
        private final Integer warning;
        private final Integer msg;
        private final Integer user;
        private final Integer rollovers;

        public Asserts(Integer regular, Integer warning, Integer msg, Integer user, Integer rollovers) {
            super();
            this.regular = regular;
            this.warning = warning;
            this.msg = msg;
            this.user = user;
            this.rollovers = rollovers;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(ASSERTS_REGULAR_FIELD, regular)
                    .append(ASSERTS_WARNING_FIELD, warning)
                    .append(ASSERTS_MSG_FIELD, msg)
                    .append(ASSERTS_USER_FIELD, user)
                    .append(ASSERTS_ROLLOVERS_FIELD, rollovers)
                    .build();
        }

        public Integer getRegular() {
            return regular;
        }

        public Integer getWarning() {
            return warning;
        }

        public Integer getMsg() {
            return msg;
        }

        public Integer getUser() {
            return user;
        }

        public Integer getRollovers() {
            return rollovers;
        }
    }

    public static class Dur {
        private static final BsonField<Integer> DUR_COMMITS_FIELD = BsonField.create("commits");
        private static final BsonField<Long> DUR_JOURNALED_MB_FIELD = BsonField.create("journaledMB");
        private static final BsonField<Long> DUR_WRITE_TO_DATA_FILES_MB_FIELD = BsonField.create("writeToDataFilesMB");
        private static final BsonField<Integer> DUR_COMPRESSION_FIELD = BsonField.create("compression");
        private static final BsonField<Integer> DUR_COMMITS_IN_WRITE_LOCK_FIELD = BsonField.create("commitsInWriteLock");
        private static final BsonField<Integer> DUR_EARLY_COMMITS_FIELD = BsonField.create("earlyCommits");
        private static final BsonField<BsonDocument> DUR_TIME_MS_FIELD = BsonField.create("timeMS");

        private final int commits;
        private final long journaledMB;
        private final long writeToDataFilesMB;
        private final int compression;
        private final int commitsInWriteLock;
        private final int earlyCommits;
        private final TimeMS timeMS;

        public Dur(int commits, long journaledMB, long writeToDataFilesMB, int compression, int commitsInWriteLock,
                int earlyCommits, TimeMS timeMS) {
            super();
            this.commits = commits;
            this.journaledMB = journaledMB;
            this.writeToDataFilesMB = writeToDataFilesMB;
            this.compression = compression;
            this.commitsInWriteLock = commitsInWriteLock;
            this.earlyCommits = earlyCommits;
            this.timeMS = timeMS;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(DUR_COMMITS_FIELD, commits)
                    .append(DUR_JOURNALED_MB_FIELD, journaledMB)
                    .append(DUR_WRITE_TO_DATA_FILES_MB_FIELD, writeToDataFilesMB)
                    .append(DUR_COMPRESSION_FIELD, compression)
                    .append(DUR_COMMITS_IN_WRITE_LOCK_FIELD, commitsInWriteLock)
                    .append(DUR_EARLY_COMMITS_FIELD, earlyCommits)
                    .append(DUR_TIME_MS_FIELD, timeMS.marshall())
                    .build();
        }
        
        public int getCommits() {
            return commits;
        }

        public long getJournaledMB() {
            return journaledMB;
        }

        public long getWriteToDataFilesMB() {
            return writeToDataFilesMB;
        }

        public int getCompression() {
            return compression;
        }

        public int getCommitsInWriteLock() {
            return commitsInWriteLock;
        }

        public int getEarlyCommits() {
            return earlyCommits;
        }

        public TimeMS getTimeMS() {
            return timeMS;
        }

        public static class TimeMS {
            private static final BsonField<Long> DUR_TIME_MS_DT_FIELD = BsonField.create("dt");
            private static final BsonField<Long> DUR_TIME_MS_PREP_LOG_BUFFER_FIELD = BsonField.create("prepLogBuffer");
            private static final BsonField<Long> DUR_TIME_MS_WRITE_TO_JOURNAL_FIELD = BsonField.create("writeToJournal");
            private static final BsonField<Long> DUR_TIME_MS_WRITE_DATA_FILES_FIELD = BsonField.create("writeToDataFiles");
            private static final BsonField<Long> DUR_TIME_MS_REMAP_PRIVATE_VIEW_FIELD = BsonField.create("remapPrivateView");
            private static final BsonField<Integer> DUR_TIME_MS_COMMITS_FIELD = BsonField.create("commits");
            private static final BsonField<Integer> DUR_TIME_MS_COMMITS_IN_WRITE_LOCK_FIELD = BsonField.create("commitsInWriteLock");

            private final long dt;
            private final long prepLogBuffer;
            private final long writeToJournal;
            private final long writeToDataFiles;
            private final long remapPrivateView;
            private final int commits;
            private final int commitsInWriteLock;

            public TimeMS(long dt, long prepLogBuffer, long writeToJournal, long writeToDataFiles,
                    long remapPrivateView, int commits, int commitsInWriteLock) {
                super();
                this.dt = dt;
                this.prepLogBuffer = prepLogBuffer;
                this.writeToJournal = writeToJournal;
                this.writeToDataFiles = writeToDataFiles;
                this.remapPrivateView = remapPrivateView;
                this.commits = commits;
                this.commitsInWriteLock = commitsInWriteLock;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(DUR_TIME_MS_DT_FIELD, dt)
                        .append(DUR_TIME_MS_PREP_LOG_BUFFER_FIELD, prepLogBuffer)
                        .append(DUR_TIME_MS_WRITE_TO_JOURNAL_FIELD, writeToJournal)
                        .append(DUR_TIME_MS_WRITE_DATA_FILES_FIELD, writeToDataFiles)
                        .append(DUR_TIME_MS_REMAP_PRIVATE_VIEW_FIELD, remapPrivateView)
                        .append(DUR_TIME_MS_COMMITS_FIELD, commits)
                        .append(DUR_TIME_MS_COMMITS_IN_WRITE_LOCK_FIELD, commitsInWriteLock)
                        .build();
            }

            public long getDt() {
                return dt;
            }

            public long getPrepLogBuffer() {
                return prepLogBuffer;
            }

            public long getWriteToJournal() {
                return writeToJournal;
            }

            public long getWriteToDataFiles() {
                return writeToDataFiles;
            }

            public long getRemapPrivateView() {
                return remapPrivateView;
            }

            public int getCommits() {
                return commits;
            }

            public int getCommitsInWriteLock() {
                return commitsInWriteLock;
            }
        }
    }

    public static class Metrics {
        private static final BsonField<BsonArray> METRICS_COMMANDS_FIELD = BsonField.create("commands");
        private static final BsonField<BsonDocument> METRICS_DOCUMENT_FIELD = BsonField.create("document");
        private static final BsonField<BsonDocument> METRICS_GET_LAST_ERROR_FIELD = BsonField.create("getLastError");
        private static final BsonField<BsonDocument> METRICS_OPERATION_FIELD = BsonField.create("operation");
        private static final BsonField<BsonDocument> METRICS_QUERY_EXECUTOR_FIELD = BsonField.create("queryExecutor");
        private static final BsonField<BsonDocument> METRICS_RECORD_FIELD = BsonField.create("record");
        private static final BsonField<BsonDocument> METRICS_REPL_FIELD = BsonField.create("repl");
        private static final BsonField<BsonDocument> METRICS_STORAGE_FIELD = BsonField.create("storage");
        private static final BsonField<BsonDocument> METRICS_TTL_FIELD = BsonField.create("ttl");

        private final ImmutableList<Command> commands;
        private final Document document;
        private final GetLastError getLastError;
        private final Operation operation;
        private final QueryExecutor queryExecutor;
        private final Record record;
        private final Repl repl;
        private final Storage storage;
        private final Ttl ttl;

        public Metrics(ImmutableList<Command> commands, Document document, GetLastError getLastError,
                Operation operation, QueryExecutor queryExecutor, Record record, Repl repl, Storage storage, Ttl ttl) {
            super();
            this.commands = commands;
            this.document = document;
            this.getLastError = getLastError;
            this.operation = operation;
            this.queryExecutor = queryExecutor;
            this.record = record;
            this.repl = repl;
            this.storage = storage;
            this.ttl = ttl;
        }

        private BsonDocument marshall() {
            BsonArray commandsArr = new BsonArray();
            for (Command command : commands) {
                commandsArr.add(command.marshall());
            }
            
            return new BsonDocumentBuilder()
                    .append(METRICS_COMMANDS_FIELD, commandsArr)
                    .append(METRICS_DOCUMENT_FIELD, document.marshall())
                    .append(METRICS_GET_LAST_ERROR_FIELD, getLastError.marshall())
                    .append(METRICS_OPERATION_FIELD, operation.marshall())
                    .append(METRICS_QUERY_EXECUTOR_FIELD, queryExecutor.marshall())
                    .append(METRICS_RECORD_FIELD, record.marshall())
                    .append(METRICS_REPL_FIELD, repl.marshall())
                    .append(METRICS_STORAGE_FIELD, storage.marshall())
                    .append(METRICS_TTL_FIELD, ttl.marshall())
                    .build();
        }
        
        public ImmutableList<Command> getCommands() {
            return commands;
        }

        public Document getDocument() {
            return document;
        }

        public GetLastError getGetLastError() {
            return getLastError;
        }

        public Operation getOperation() {
            return operation;
        }

        public QueryExecutor getQueryExecutor() {
            return queryExecutor;
        }

        public Record getRecord() {
            return record;
        }

        public Repl getRepl() {
            return repl;
        }

        public Storage getStorage() {
            return storage;
        }

        public Ttl getTtl() {
            return ttl;
        }

        public static class Command {
            private static final BsonField<Integer> METRICS_COMMANDS_FAILED_FIELD = BsonField.create("failed");
            private static final BsonField<Integer> METRICS_COMMANDS_TOTAL_FIELD = BsonField.create("total");
            
            private final String command;
            private final int failed;
            private final int total;

            public Command(String command, int failed, int total) {
                super();
                this.command = command;
                this.failed = failed;
                this.total = total;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .appendUnsafe(command, 
                                new BsonDocumentBuilder()
                                    .append(METRICS_COMMANDS_FAILED_FIELD, failed)
                                    .append(METRICS_COMMANDS_TOTAL_FIELD, total).build())
                        .build();
            }

            public String getCommand() {
                return command;
            }

            public int getFailed() {
                return failed;
            }

            public int getTotal() {
                return total;
            }
        }
        
        public static class Document {
            private static final BsonField<Integer> METRICS_DOCUMENT_DELETED_FIELD = BsonField.create("deleted");
            private static final BsonField<Integer> METRICS_DOCUMENT_INSERTED_FIELD = BsonField.create("inserted");
            private static final BsonField<Integer> METRICS_DOCUMENT_RETURNED_FIELD = BsonField.create("returned");
            private static final BsonField<Integer> METRICS_DOCUMENT_UPDATED_FIELD = BsonField.create("updated");
            
            private final int deleted;
            private final int inserted;
            private final int returned;
            private final int updated;
            
            public Document(int deleted, int inserted, int returned, int updated) {
                super();
                this.deleted = deleted;
                this.inserted = inserted;
                this.returned = returned;
                this.updated = updated;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_DOCUMENT_DELETED_FIELD, deleted)
                        .append(METRICS_DOCUMENT_INSERTED_FIELD, inserted)
                        .append(METRICS_DOCUMENT_RETURNED_FIELD, returned)
                        .append(METRICS_DOCUMENT_UPDATED_FIELD, updated)
                        .build();
            }

            public int getDeleted() {
                return deleted;
            }


            public int getInserted() {
                return inserted;
            }


            public int getReturned() {
                return returned;
            }


            public int getUpdated() {
                return updated;
            }
        }
        
        public static class GetLastError {
            private static final BsonField<BsonDocument> METRICS_GET_LAST_ERROR_WTIME_FIELD = BsonField.create("wtime");
            private static final BsonField<Integer> METRICS_GET_LAST_ERROR_WTIMEOUTS_FIELD = BsonField.create("wtimeouts");
            
            private final Stats wtime;
            private final int wtimeouts;
            
            public GetLastError(Stats wtime, int wtimeouts) {
                super();
                this.wtime = wtime;
                this.wtimeouts = wtimeouts;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_GET_LAST_ERROR_WTIME_FIELD, wtime.marshall())
                        .append(METRICS_GET_LAST_ERROR_WTIMEOUTS_FIELD, wtimeouts)
                        .build();
            }

            public Stats getWtime() {
                return wtime;
            }

            public int getWtimeouts() {
                return wtimeouts;
            }
        }
        
        public static class Operation {
            private static final BsonField<Integer> METRICS_OPERATION_FASTMOD_FIELD = BsonField.create("fastmod");
            private static final BsonField<Integer> METRICS_OPERATION_IDHACK_FIELD = BsonField.create("idhack");
            private static final BsonField<Integer> METRICS_OPERATION_SCAN_AND_ORDER_FIELD = BsonField.create("scanAndOrder");
            
            private final int fastmod;
            private final int idhack;
            private final int scanAndOrder;
            
            public Operation(int fastmod, int idhack, int scanAndOrder) {
                super();
                this.fastmod = fastmod;
                this.idhack = idhack;
                this.scanAndOrder = scanAndOrder;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_OPERATION_FASTMOD_FIELD, fastmod)
                        .append(METRICS_OPERATION_IDHACK_FIELD, idhack)
                        .append(METRICS_OPERATION_SCAN_AND_ORDER_FIELD, scanAndOrder)
                        .build();
            }

            public int getFastmod() {
                return fastmod;
            }

            public int getIdhack() {
                return idhack;
            }

            public int getScanAndOrder() {
                return scanAndOrder;
            }
        }
        
        public static class QueryExecutor {
            private static final BsonField<Integer> METRICS_QUERY_EXECUTOR_SCANNED_FIELD = BsonField.create("scanned");
            
            private final int scanned;
            
            public QueryExecutor(int scanned) {
                super();
                this.scanned = scanned;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_QUERY_EXECUTOR_SCANNED_FIELD, scanned)
                        .build();
            }

            public int getScanned() {
                return scanned;
            }
        }
        
        public static class Record {
            private static final BsonField<Integer> METRICS_RECORD_MOVES_FIELD = BsonField.create("moves");
            
            private final int moves;
            
            public Record(int moves) {
                super();
                this.moves = moves;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_RECORD_MOVES_FIELD, moves)
                        .build();
            }

            public int getMoves() {
                return moves;
            }
        }
        
        public static class Repl {
            private static final BsonField<BsonDocument> METRICS_REPL_APPLY_FIELD = BsonField.create("apply");
            private static final BsonField<BsonDocument> METRICS_REPL_BUFFER_FIELD = BsonField.create("buffer");
            private static final BsonField<BsonDocument> METRICS_REPL_NETWORK_FIELD = BsonField.create("network");
            private static final BsonField<BsonDocument> METRICS_REPL_OPLOG_FIELD = BsonField.create("oplog");
            private static final BsonField<BsonDocument> METRICS_REPL_PRELOAD_FIELD = BsonField.create("preload");
            
            private final Apply apply;
            private final Buffer buffer;
            private final Network network;
            private final Oplog oplog;
            private final Preload preload;
            
            public Repl(Apply apply, Buffer buffer, Network network, Oplog oplog, Preload preload) {
                super();
                this.apply = apply;
                this.buffer = buffer;
                this.network = network;
                this.oplog = oplog;
                this.preload = preload;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_REPL_APPLY_FIELD, apply.marshall())
                        .append(METRICS_REPL_BUFFER_FIELD, buffer.marshall())
                        .append(METRICS_REPL_NETWORK_FIELD, network.marshall())
                        .append(METRICS_REPL_OPLOG_FIELD, oplog.marshall())
                        .append(METRICS_REPL_PRELOAD_FIELD, preload.marshall())
                        .build();
            }
            
            public Apply getApply() {
                return apply;
            }

            public Buffer getBuffer() {
                return buffer;
            }

            public Network getNetwork() {
                return network;
            }

            public Oplog getOplog() {
                return oplog;
            }

            public Preload getPreload() {
                return preload;
            }

            public static class Apply {
                private static final BsonField<BsonDocument> METRICS_REPL_APPLY_BATCHES_FIELD = BsonField.create("batches");
                private static final BsonField<Integer> METRICS_REPL_APPLY_OPS_FIELD = BsonField.create("ops");
                
                private final Stats batches;
                private final int ops;
                
                public Apply(Stats batches, int ops) {
                    super();
                    this.batches = batches;
                    this.ops = ops;
                }

                private BsonDocument marshall() {
                    return new BsonDocumentBuilder()
                            .append(METRICS_REPL_APPLY_BATCHES_FIELD, batches.marshall())
                            .append(METRICS_REPL_APPLY_OPS_FIELD, ops)
                            .build();
                }

                public Stats getBatches() {
                    return batches;
                }

                public int getOps() {
                    return ops;
                }
            }
            
            public static class Buffer {
                private static final BsonField<Integer> METRICS_REPL_BUFFER_COUNT_FIELD = BsonField.create("count");
                private static final BsonField<Long> METRICS_REPL_BUFFER_MAX_SIZE_BYTES_FIELD = BsonField.create("maxSizeBytes");
                private static final BsonField<Long> METRICS_REPL_BUFFER_SIZE_BYTES_FIELD = BsonField.create("sizeBytes");
                
                private final int count;
                private final long maxSizeBytes;
                private final long sizeBytes;
                
                public Buffer(int count, long maxSizeBytes, long sizeBytes) {
                    super();
                    this.count = count;
                    this.maxSizeBytes = maxSizeBytes;
                    this.sizeBytes = sizeBytes;
                }

                private BsonDocument marshall() {
                    return new BsonDocumentBuilder()
                            .append(METRICS_REPL_BUFFER_COUNT_FIELD, count)
                            .append(METRICS_REPL_BUFFER_MAX_SIZE_BYTES_FIELD, maxSizeBytes)
                            .append(METRICS_REPL_BUFFER_SIZE_BYTES_FIELD, sizeBytes)
                            .build();
                }

                public int getCount() {
                    return count;
                }

                public long getMaxSizeBytes() {
                    return maxSizeBytes;
                }

                public long getSizeBytes() {
                    return sizeBytes;
                }
            }
            
            public static class Network {
                private static final BsonField<Long> METRICS_REPL_NETWORK_BYTES_FIELD = BsonField.create("bytes");
                private static final BsonField<BsonDocument> METRICS_REPL_NETWORK_GETMORES_FIELD = BsonField.create("getmores");
                private static final BsonField<Integer> METRICS_REPL_NETWORK_OPS_FIELD = BsonField.create("ops");
                private static final BsonField<Integer> METRICS_REPL_NETWORK_READERS_CREATED_FIELD = BsonField.create("readersCreated");
                
                private final long bytes;
                private final Stats getmores;
                private final int ops;
                private final int readersCreated;
                
                public Network(long bytes, Stats getmores, int ops, int readersCreated) {
                    super();
                    this.bytes = bytes;
                    this.getmores = getmores;
                    this.ops = ops;
                    this.readersCreated = readersCreated;
                }

                private BsonDocument marshall() {
                    return new BsonDocumentBuilder()
                            .append(METRICS_REPL_NETWORK_BYTES_FIELD, bytes)
                            .append(METRICS_REPL_NETWORK_GETMORES_FIELD, getmores.marshall())
                            .append(METRICS_REPL_NETWORK_OPS_FIELD, ops)
                            .append(METRICS_REPL_NETWORK_READERS_CREATED_FIELD, readersCreated)
                            .build();
                }

                public long getBytes() {
                    return bytes;
                }

                public Stats getGetmores() {
                    return getmores;
                }

                public int getOps() {
                    return ops;
                }

                public int getReadersCreated() {
                    return readersCreated;
                }
            }
            
            public static class Oplog {
                private static final BsonField<BsonDocument> METRICS_REPL_OPLOG_INSERT_FIELD = BsonField.create("insert");
                private static final BsonField<Long> METRICS_REPL_OPLOG_INSERT_BYTES_FIELD = BsonField.create("insertBytes");
                
                private final Stats insert;
                private final long insertBytes;
                
                public Oplog(Stats insert, long insertBytes) {
                    super();
                    this.insert = insert;
                    this.insertBytes = insertBytes;
                }

                private BsonDocument marshall() {
                    return new BsonDocumentBuilder()
                            .append(METRICS_REPL_OPLOG_INSERT_FIELD, insert.marshall())
                            .append(METRICS_REPL_OPLOG_INSERT_BYTES_FIELD, insertBytes)
                            .build();
                }

                public Stats getInsert() {
                    return insert;
                }

                public long getInsertBytes() {
                    return insertBytes;
                }
            }
            
            public static class Preload {
                private static final BsonField<BsonDocument> METRICS_REPL_PRELOAD_DOCS_FIELD = BsonField.create("docs");
                private static final BsonField<BsonDocument> METRICS_REPL_PRELOAD_INDEXES_FIELD = BsonField.create("indexes");
                
                private final Stats docs;
                private final Stats indexes;
                
                public Preload(Stats docs, Stats indexes) {
                    super();
                    this.docs = docs;
                    this.indexes = indexes;
                }

                private BsonDocument marshall() {
                    return new BsonDocumentBuilder()
                            .append(METRICS_REPL_PRELOAD_DOCS_FIELD, docs.marshall())
                            .append(METRICS_REPL_PRELOAD_INDEXES_FIELD, indexes.marshall())
                            .build();
                }

                public Stats getDocs() {
                    return docs;
                }

                public Stats getIndexes() {
                    return indexes;
                }
            }
        }
        
        public static class Storage {
            private static final BsonField<BsonDocument> METRICS_STORAGE_FREELIST_FIELD = BsonField.create("freelist");
            
            private final Freelist freelist;
            
            public Storage(Freelist freelist) {
                super();
                this.freelist = freelist;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_STORAGE_FREELIST_FIELD, freelist.marshall())
                        .build();
            }
            
            public Freelist getFreelist() {
                return freelist;
            }

            public static class Freelist {
                private static final BsonField<BsonDocument> METRICS_STORAGE_FREELIST_SEARCH_FIELD = BsonField.create("search");
                
                private final Search search;
                
                public Freelist(Search search) {
                    super();
                    this.search = search;
                }

                private BsonDocument marshall() {
                    return new BsonDocumentBuilder()
                            .append(METRICS_STORAGE_FREELIST_SEARCH_FIELD, search.marshall())
                            .build();
                }
                
                public Search getSearch() {
                    return search;
                }

                public static class Search {
                    private static final BsonField<Integer> METRICS_STORAGE_FREELIST_SEARCH_BUCKET_EXHAUSTED_FIELD = BsonField.create("bucketExhausted");
                    private static final BsonField<Integer> METRICS_STORAGE_FREELIST_SEARCH_REQUESTS_FIELD = BsonField.create("requests");
                    private static final BsonField<Integer> METRICS_STORAGE_FREELIST_SEARCH_SCANNED_FIELD = BsonField.create("scanned");
                    
                    private final int bucketExhausted;
                    private final int requests;
                    private final int scanned;
                    
                    public Search(int bucketExhausted, int requests, int scanned) {
                        super();
                        this.bucketExhausted = bucketExhausted;
                        this.requests = requests;
                        this.scanned = scanned;
                    }

                    private BsonDocument marshall() {
                        return new BsonDocumentBuilder()
                                .append(METRICS_STORAGE_FREELIST_SEARCH_BUCKET_EXHAUSTED_FIELD, bucketExhausted)
                                .append(METRICS_STORAGE_FREELIST_SEARCH_REQUESTS_FIELD, requests)
                                .append(METRICS_STORAGE_FREELIST_SEARCH_SCANNED_FIELD, scanned)
                                .build();
                    }

                    public int getBucketExhausted() {
                        return bucketExhausted;
                    }

                    public int getRequests() {
                        return requests;
                    }

                    public int getScanned() {
                        return scanned;
                    }
                }
            }
        }
        
        public static class Ttl {
            private static final BsonField<Integer> METRICS_TTL_DELETED_DOCUMENTS_FIELD = BsonField.create("deletedDocuments");
            private static final BsonField<Integer> METRICS_TTL_PASSES_FIELD = BsonField.create("passes");
            
            private final int deletedDocuments;
            private final int passes;
            
            public Ttl(int deletedDocuments, int passes) {
                super();
                this.deletedDocuments = deletedDocuments;
                this.passes = passes;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_TTL_DELETED_DOCUMENTS_FIELD, deletedDocuments)
                        .append(METRICS_TTL_PASSES_FIELD, passes)
                        .build();
            }

            public int getDeletedDocuments() {
                return deletedDocuments;
            }

            public int getPasses() {
                return passes;
            }
        }
        
        public static class Stats {
            private static final BsonField<Integer> METRICS_NUM_FIELD = BsonField.create("num");
            private static final BsonField<Long> METRICS_TOTAL_MILLIS_FIELD = BsonField.create("totalMillis");
            
            private final int num;
            private final long totalMillis;

            public Stats(int num, long totalMillis) {
                super();
                this.num = num;
                this.totalMillis = totalMillis;
            }

            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .append(METRICS_NUM_FIELD, num)
                        .append(METRICS_TOTAL_MILLIS_FIELD, totalMillis)
                        .build();
            }

            public int getNum() {
                return num;
            }

            public long getTotalMillis() {
                return totalMillis;
            }
        }
    }

    public static class WiredTiger {
        private static final BsonField<String> WIRED_TIGER_URI_FIELD = BsonField.create("uri");
        private static final BsonField<BsonDocument> WIRED_TIGER_LSM_FIELD = BsonField.create("LSM");
        private static final BsonField<BsonDocument> WIRED_TIGER_ASYNC_FIELD = BsonField.create("async");
        private static final BsonField<BsonDocument> WIRED_TIGER_BLOCK_MANAGER_FIELD = BsonField.create("block-manager");
        private static final BsonField<BsonDocument> WIRED_TIGER_CACHE_FIELD = BsonField.create("cache");
        private static final BsonField<BsonDocument> WIRED_TIGER_CONNECTION_FIELD = BsonField.create("connection");
        private static final BsonField<BsonDocument> WIRED_TIGER_CURSOR_FIELD = BsonField.create("cursor");
        private static final BsonField<BsonDocument> WIRED_TIGER_DATA_HANDLE_FIELD = BsonField.create("data-handle");
        private static final BsonField<BsonDocument> WIRED_TIGER_LOG_FIELD = BsonField.create("log");
        private static final BsonField<BsonDocument> WIRED_TIGER_RECONCILIATION_FIELD = BsonField.create("reconciliation");
        private static final BsonField<BsonDocument> WIRED_TIGER_SESSION_FIELD = BsonField.create("session");
        private static final BsonField<BsonDocument> WIRED_TIGER_THREAD_YIELD_FIELD = BsonField.create("thread-yield");
        private static final BsonField<BsonDocument> WIRED_TIGER_TRANSACTION_FIELD = BsonField.create("transaction");
        private static final BsonField<BsonDocument> WIRED_TIGER_CONCURRENT_TRANSACTIONS_FIELD = BsonField.create("concurrentTransactions");

        private final String uri;
        private final Lsm lsm;
        private final Async async;
        private final BlockManager blockManager;
        private final Cache cache;
        private final Connection connection;
        private final Cursor cursor;
        private final DataHandle dataHandle;
        private final Log log;
        private final Reconciliation reconciliation;
        private final Session session;
        private final ThreadYield threadYield;
        private final Transaction transaction;
        private final ConcurrentTransaction concurrentTransactions;

        public WiredTiger(String uri, Lsm lsm, Async async, BlockManager blockManager, Cache cache,
                Connection connection, Cursor cursor, DataHandle dataHandle, Log log, Reconciliation reconciliation,
                Session session, ThreadYield threadYield, Transaction transaction,
                ConcurrentTransaction concurrentTransactions) {
            super();
            this.uri = uri;
            this.lsm = lsm;
            this.async = async;
            this.blockManager = blockManager;
            this.cache = cache;
            this.connection = connection;
            this.cursor = cursor;
            this.dataHandle = dataHandle;
            this.log = log;
            this.reconciliation = reconciliation;
            this.session = session;
            this.threadYield = threadYield;
            this.transaction = transaction;
            this.concurrentTransactions = concurrentTransactions;
        }
        
        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(WIRED_TIGER_URI_FIELD, uri)
                    .append(WIRED_TIGER_LSM_FIELD, lsm.marshall())
                    .append(WIRED_TIGER_ASYNC_FIELD, async.marshall())
                    .append(WIRED_TIGER_BLOCK_MANAGER_FIELD, blockManager.marshall())
                    .append(WIRED_TIGER_CACHE_FIELD, cache.marshall())
                    .append(WIRED_TIGER_CONNECTION_FIELD, connection.marshall())
                    .append(WIRED_TIGER_CURSOR_FIELD, cursor.marshall())
                    .append(WIRED_TIGER_DATA_HANDLE_FIELD, dataHandle.marshall())
                    .append(WIRED_TIGER_LOG_FIELD, log.marshall())
                    .append(WIRED_TIGER_RECONCILIATION_FIELD, reconciliation.marshall())
                    .append(WIRED_TIGER_SESSION_FIELD, session.marshall())
                    .append(WIRED_TIGER_THREAD_YIELD_FIELD, threadYield.marshall())
                    .append(WIRED_TIGER_TRANSACTION_FIELD, transaction.marshall())
                    .append(WIRED_TIGER_CONCURRENT_TRANSACTIONS_FIELD, concurrentTransactions.marshall())
                    .build();
        }

        public String getUri() {
            return uri;
        }

        public Lsm getLsm() {
            return lsm;
        }

        public Async getAsync() {
            return async;
        }

        public BlockManager getBlockManager() {
            return blockManager;
        }

        public Cache getCache() {
            return cache;
        }

        public Connection getConnection() {
            return connection;
        }

        public Cursor getCursor() {
            return cursor;
        }

        public DataHandle getDataHandle() {
            return dataHandle;
        }

        public Log getLog() {
            return log;
        }

        public Reconciliation getReconciliation() {
            return reconciliation;
        }

        public Session getSession() {
            return session;
        }

        public ThreadYield getThreadYield() {
            return threadYield;
        }

        public Transaction getTransaction() {
            return transaction;
        }

        public ConcurrentTransaction getConcurrentTransactions() {
            return concurrentTransactions;
        }


        //TODO: Complete
        
        /*
       "LSM" : {
             "sleep for LSM checkpoint throttle" : <num>,
             "sleep for LSM merge throttle" : <num>,
             "rows merged in an LSM tree" : <num>,
             "application work units currently queued" : <num>,
             "merge work units currently queued" : <num>,
             "tree queue hit maximum" : <num>,
             "switch work units currently queued" : <num>,
             "tree maintenance operations scheduled" : <num>,
             "tree maintenance operations discarded" : <num>,
             "tree maintenance operations executed" : <num>
       },
         */
        public static class Lsm {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "async" : {
              "number of allocation state races" : <num>,
              "number of operation slots viewed for allocation" : <num>,
              "current work queue length" : <num>,
              "number of flush calls" : <num>,
              "number of times operation allocation failed" : <num>,
              "maximum work queue length" : <num>,
              "number of times worker found no work" : <num>,
              "total allocations" : <num>,
              "total compact calls" : <num>,
              "total insert calls" : <num>,
              "total remove calls" : <num>,
              "total search calls" : <num>,
              "total update calls" : <num>
        },
         */
        public static class Async {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "block-manager" : {
              "mapped bytes read" : <num>,
              "bytes read" : <num>,
              "bytes written" : <num>,
              "mapped blocks read" : <num>,
              "blocks pre-loaded" : <num>,
              "blocks read" : <num>,
              "blocks written" : <num>
        },
         */
        public static class BlockManager {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "cache" : {
              "tracked dirty bytes in the cache" : <num>,
              "bytes currently in the cache" : <num>,
              "maximum bytes configured" : <num>,
              "bytes read into cache" : <num>,
              "bytes written from cache" : <num>,
              "pages evicted by application threads" : <num>,
              "checkpoint blocked page eviction" : <num>,
              "unmodified pages evicted" : <num>,
              "page split during eviction deepened the tree" : <num>,
              "modified pages evicted" : <num>,
              "pages selected for eviction unable to be evicted" : <num>,
              "pages evicted because they exceeded the in-memory maximum" : <num>,
              "pages evicted because they had chains of deleted items" : <num>,
              "failed eviction of pages that exceeded the in-memory maximum" : <num>,
              "hazard pointer blocked page eviction" : <num>,
              "internal pages evicted" : <num>,
              "maximum page size at eviction" : <num>,
              "eviction server candidate queue empty when topping up" : <num>,
              "eviction server candidate queue not empty when topping up" : <num>,
              "eviction server evicting pages" : <num>,
              "eviction server populating queue, but not evicting pages" : <num>,
              "eviction server unable to reach eviction goal" : <num>,
              "pages split during eviction" : <num>,
              "pages walked for eviction" : <num>,
              "eviction worker thread evicting pages" : <num>,
              "in-memory page splits" : <num>,
              "percentage overhead" : <num>,
              "tracked dirty pages in the cache" : <num>,
              "pages currently held in the cache" : <num>,
              "pages read into cache" : <num>,
              "pages written from cache" : <num>
        },
         */
        public static class Cache {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "connection" : {
              "pthread mutex condition wait calls" : <num>,
              "files currently open" : <num>,
              "memory allocations" : <num>,
              "memory frees" : <num>,
              "memory re-allocations" : <num>,
              "total read I/Os" : <num>,
              "pthread mutex shared lock read-lock calls" : <num>,
              "pthread mutex shared lock write-lock calls" : <num>,
              "total write I/Os" : <num>
        },
         */
        public static class Connection {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "cursor" : {
              "cursor create calls" : <num>,
              "cursor insert calls" : <num>,
              "cursor next calls" : <num>,
              "cursor prev calls" : <num>,
              "cursor remove calls" : <num>,
              "cursor reset calls" : <num>,
              "cursor search calls" : <num>,
              "cursor search near calls" : <num>,
              "cursor update calls" : <num>
        },
         */
        public static class Cursor {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "data-handle" : {
              "connection dhandles swept" : <num>,
              "connection candidate referenced" : <num>,
              "connection sweeps" : <num>,
              "connection time-of-death sets" : <num>,
              "session dhandles swept" : <num>,
              "session sweep attempts" : <num>
        },
         */
        public static class DataHandle {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "log" : {
              "log buffer size increases" : <num>,
              "total log buffer size" : <num>,
              "log bytes of payload data" : <num>,
              "log bytes written" : <num>,
              "yields waiting for previous log file close" : <num>,
              "total size of compressed records" : <num>,
              "total in-memory size of compressed records" : <num>,
              "log records too small to compress" : <num>,
              "log records not compressed" : <num>,
              "log records compressed" : <num>,
              "maximum log file size" : <num>,
              "pre-allocated log files prepared" : <num>,
              "number of pre-allocated log files to create" : <num>,
              "pre-allocated log files used" : <num>,
              "log read operations" : <num>,
              "log release advances write LSN" : <num>,
              "records processed by log scan" : <num>,
              "log scan records requiring two reads" : <num>,
              "log scan operations" : <num>,
              "consolidated slot closures" : <num>,
              "logging bytes consolidated" : <num>,
              "consolidated slot joins" : <num>,
              "consolidated slot join races" : <num>,
              "slots selected for switching that were unavailable" : <num>,
              "record size exceeded maximum" : <num>,
              "failed to find a slot large enough for record" : <num>,
              "consolidated slot join transitions" : <num>,
              "log sync operations" : <num>,
              "log sync_dir operations" : <num>,
              "log server thread advances write LSN" : <num>,
              "log write operations" : <num>
        },
         */
        public static class Log {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "reconciliation" : {
              "page reconciliation calls" : <num>,
              "page reconciliation calls for eviction" : <num>,
              "split bytes currently awaiting free" : <num>,
              "split objects currently awaiting free" : <num>
        },
         */
        public static class Reconciliation {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "session" : {
              "open cursor count" : <num>,
              "open session count" : <num>
        },
         */
        public static class Session {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "thread-yield" : {
              "page acquire busy blocked" : <num>,
              "page acquire eviction blocked" : <num>,
              "page acquire locked blocked" : <num>,
              "page acquire read blocked" : <num>,
              "page acquire time sleeping (usecs)" : <num>
        },
         */
        public static class ThreadYield {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "transaction" : {
              "transaction begins" : <num>,
              "transaction checkpoints" : <num>,
              "transaction checkpoint currently running" : <num>,
              "transaction checkpoint max time (msecs)" : <num>,
              "transaction checkpoint min time (msecs)" : <num>,
              "transaction checkpoint most recent time (msecs)" : <num>,
              "transaction checkpoint total time (msecs)" : <num>,
              "transactions committed" : <num>,
              "transaction failures due to cache overflow" : <num>,
              "transaction range of IDs currently pinned" : <num>,
              "transactions rolled back" : <num>
        },
         */
        public static class Transaction {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
        /*
        "concurrentTransactions" : {
              "write" : {
                 "out" : <num>,
                 "available" : <num>,
                 "totalTickets" : <num>
              },
              "read" : {
                 "out" : <num>,
                 "available" : <num>,
                 "totalTickets" : <num>
              }
        }
         */
        public static class ConcurrentTransaction {
            private BsonDocument marshall() {
                return new BsonDocumentBuilder()
                        .build();
            }
        }
    }

}

