package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetStepDownCommand.ReplSetStepDownArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

/**
 *
 */
public class ReplSetStepDownCommand extends AbstractCommand<ReplSetStepDownArgument, SimpleReply> {

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
    public ReplSetStepDownArgument unmarshallArg(BsonDocument requestDoc) throws
            MongoServerException {

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

        return new ReplSetStepDownArgument(this, seconds, catchUpPeriodSecs, force);
    }

    @Override
    public Class<? extends SimpleReply> getReplyClass() {
        return SimpleReply.class;
    }

    @Override
    public BsonDocument marshallReply(SimpleReply reply) throws
            MongoServerException {
        return SimpleReplyMarshaller.marshall(reply);
    }

    @Immutable
    public class ReplSetStepDownArgument extends SimpleArgument {

        private final long seconds;
        private final long catchUpPeriodSecs;
        private final boolean force;

        public ReplSetStepDownArgument(ReplSetStepDownCommand command, long seconds, long catchUpPeriodSecs, boolean force) {
            super(command);
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
