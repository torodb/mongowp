package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.mongoserver.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetStepDownCommand.ReplSetStepDownArgument;
import com.eightkdata.mongowp.mongoserver.api.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonReaderTool;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

/**
 *
 */
public class ReplSetStepDownCommand extends AbstractCommand<ReplSetStepDownArgument, Empty> {

    private static final BsonInt32 DEFAULT_STEP_DOWN_SECS = new BsonInt32(60);
    public static final ReplSetStepDownCommand INSTANCE = new ReplSetStepDownCommand();

    private ReplSetStepDownCommand() {
        super("replSetStepDown");
    }

    @Override
    public Class<? extends ReplSetStepDownArgument> getArgClass() {
        return ReplSetStepDownArgument.class;
    }

    @Override
    public boolean canChangeReplicationState() {
        return true;
    }

    @Override
    public ReplSetStepDownArgument unmarshallArg(BsonDocument requestDoc) throws TypesMismatchException {

        boolean force = BsonReaderTool.getBoolean(requestDoc, "force", false);
        long seconds = BsonReaderTool.getNumeric(
                requestDoc,
                getCommandName(),
                DEFAULT_STEP_DOWN_SECS)
                .longValue();

        long defaultSecondaryCatchUpPeriodSecs;
        if (force) {
            defaultSecondaryCatchUpPeriodSecs = 0;
        } else {
            defaultSecondaryCatchUpPeriodSecs = 10;
        }
        long catchUpPeriodSecs = BsonReaderTool.getLong(
                requestDoc,
                "secondaryCatchUpPeriodSecs",
                defaultSecondaryCatchUpPeriodSecs
        );

        return new ReplSetStepDownArgument(seconds, catchUpPeriodSecs, force);
    }

    @Override
    public BsonDocument marshallArg(ReplSetStepDownArgument request) {
        throw new UnsupportedOperationException("Not supported yet."); //TODO
    }

    @Override
    public Class<? extends Empty> getResultClass() {
        return Empty.class;
    }

    @Override
    public BsonDocument marshallResult(Empty reply) {
        return null;
    }

    @Override
    public Empty unmarshallResult(BsonDocument resultDoc) {
        return Empty.getInstance();
    }

    @Immutable
    public class ReplSetStepDownArgument {

        private final long seconds;
        private final long catchUpPeriodSecs;
        private final boolean force;

        public ReplSetStepDownArgument(long seconds, long catchUpPeriodSecs, boolean force) {
            this.seconds = seconds;
            this.catchUpPeriodSecs = catchUpPeriodSecs;
            this.force = force;
        }

        public long getSeconds() {
            return seconds;
        }

        public long getCatchUpPeriodSecs() {
            return catchUpPeriodSecs;
        }

        public boolean isForce() {
            return force;
        }

    }

}
