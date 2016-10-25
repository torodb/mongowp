
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl;

import javax.annotation.Nullable;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.IntField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetFreezeCommand.ReplSetFreezeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.repl.ReplSetFreezeCommand.ReplSetFreezeReply;
import com.eightkdata.mongowp.server.api.impl.AbstractNotAliasableCommand;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 * <em>Freeze</em> or <em>unfreeze</em> this node.
 *
 * This node will not attempt to become primary until the time period specified
 * expires. Calling {replSetFreeze:0} will unfreeze the node.
 */
public class ReplSetFreezeCommand extends AbstractNotAliasableCommand<ReplSetFreezeArgument, ReplSetFreezeReply>{
	private static final IntField COMMAND_FIELD = new IntField("replSetFreeze");

	public static final ReplSetFreezeCommand INSTANCE = new ReplSetFreezeCommand();

    private ReplSetFreezeCommand() {
        super(COMMAND_FIELD.getFieldName());
    }
    
    @Override
    public Class<? extends ReplSetFreezeArgument> getArgClass() {
        return ReplSetFreezeArgument.class;
    }

    @Override
    public ReplSetFreezeArgument unmarshallArg(BsonDocument requestDoc) 
    		throws TypesMismatchException, NoSuchKeyException {
        return ReplSetFreezeArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ReplSetFreezeArgument request) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Class<? extends ReplSetFreezeReply> getResultClass() {
        return ReplSetFreezeReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetFreezeReply reply) {
        return reply.marshall();
    }

    @Override
    public ReplSetFreezeReply unmarshallResult(BsonDocument resultDoc) {
        throw new UnsupportedOperationException("Not supported");
    }
    
    public static class ReplSetFreezeArgument {

        private final int freezeSecs;

        public ReplSetFreezeArgument(int freezeSecs) {
            this.freezeSecs = freezeSecs;
        }

        public int getFreezeSecs() {
            return freezeSecs;
        }
        
        private static ReplSetFreezeArgument unmarshall(BsonDocument doc) 
        		throws TypesMismatchException, NoSuchKeyException {
            int freezeSecs = BsonReaderTool.getInteger(doc, COMMAND_FIELD);
            
            return new ReplSetFreezeArgument(freezeSecs);
        }
    }
    
    public static class ReplSetFreezeReply {
        private static final StringField INFO_FIELD = new StringField("info");
        private static final StringField WARNING_FIELD = new StringField("warning");

        private final String info;
        private final String warning;

        public ReplSetFreezeReply(@Nullable String info, @Nullable String warning) {
            this.info = info;
            this.warning = warning;
        }

        public String getInfo() {
            return info;
        }

        public String getWarning() {
            return warning;
        }

        private BsonDocument marshall() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();

            if (info != null) {
                builder.append(INFO_FIELD, info);
            }
            if (warning != null) {
                builder.append(WARNING_FIELD, warning);
            }
            
            return builder.build();
        }
    }

}
