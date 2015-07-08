package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.Command;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetMaintenanceCommand.ReplSetMaintenanceArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.SimpleReplyMarshaller;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 */
public class ReplSetMaintenanceCommand extends AbstractCommand<ReplSetMaintenanceArgument, SimpleReply>{

    public static final ReplSetMaintenanceCommand INSTANCE = new ReplSetMaintenanceCommand();

    private ReplSetMaintenanceCommand() {
        super("replSetMaintenance");
    }

    @Override
    public Class<? extends ReplSetMaintenanceArgument> getArgClass() {
        return ReplSetMaintenanceArgument.class;
    }

    @Override
    public ReplSetMaintenanceArgument unmarshallArg(BsonDocument requestDoc)
            throws MongoServerException {

        return new ReplSetMaintenanceArgument(
                this,
                BsonReaderTool.getBoolean(requestDoc, getCommandName())
        );

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
    public static class ReplSetMaintenanceArgument extends SimpleArgument {

        private final boolean value;

        public ReplSetMaintenanceArgument(Command command, boolean value) {
            super(command);
            this.value = value;
        }

        public boolean isValue() {
            return value;
        }

    }

}
