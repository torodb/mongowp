
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.impl.SimpleReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetFreezeCommand.ReplSetFreezeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetFreezeCommand.ReplSetFreezeReply;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoServerException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.*;

/**
 * <em>Freeze</em> or <em>unfreeze</em> this node.
 *
 * This node will not attempt to become primary until the time period specified
 * expires. Calling {replSetFreeze:0} will unfreeze the node.
 */
public class ReplSetFreezeCommand extends AbstractCommand<ReplSetFreezeArgument, ReplSetFreezeReply>{

    public static final ReplSetFreezeCommand INSTANCE = new ReplSetFreezeCommand();

    private ReplSetFreezeCommand() {
        super("replSetFreeze");
    }
    
    @Override
    public Class<? extends ReplSetFreezeArgument> getArgClass() {
        return ReplSetFreezeArgument.class;
    }

    @Override
    public ReplSetFreezeArgument unmarshallArg(BsonDocument requestDoc)
            throws MongoServerException {
        int freezeSecs = requestDoc.getInt32("replSetFreeze").getValue();
        
        return new ReplSetFreezeArgument(this, freezeSecs);
    }

    @Override
    public Class<? extends ReplSetFreezeReply> getReplyClass() {
        return ReplSetFreezeReply.class;
    }

    @Override
    public BsonDocument marshallReply(ReplSetFreezeReply reply) throws
            MongoServerException {
        BsonDocument doc = new BsonDocument();

        String info = reply.getInfo();
        if (info != null) {
            doc.append("info", new BsonString(info));
        }
        String warning = reply.getWarning();
        if (warning != null) {
            doc.append("warning", new BsonString(warning));
        }
        
        return doc;
    }
    
    public static class ReplSetFreezeArgument extends SimpleArgument {

        private final int freezeSecs;

        public ReplSetFreezeArgument(ReplSetFreezeCommand command, int freezeSecs) {
            super(command);
            this.freezeSecs = freezeSecs;
        }

        public int getFreezeSecs() {
            return freezeSecs;
        }
    }
    
    public static class ReplSetFreezeReply extends SimpleReply {

        private final String info;
        private final String warning;

        public ReplSetFreezeReply(@Nonnull ReplSetFreezeCommand command, @Nullable String info, @Nullable String warning) {
            super(command);
            this.info = info;
            this.warning = warning;
        }

        public String getInfo() {
            return info;
        }

        public String getWarning() {
            return warning;
        }

    }

}
