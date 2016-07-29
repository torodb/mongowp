
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.messages.utils.NamespaceUtils;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.RenameCollectionCommand.RenameCollectionArgument;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;

/**
 *
 */
public class RenameCollectionCommand extends AbstractCommand<RenameCollectionArgument, Empty> {
    private static final String COMMAND_NAME = "renameCollection";
    public static final RenameCollectionCommand INSTANCE = new RenameCollectionCommand();

    public RenameCollectionCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public Class<? extends RenameCollectionArgument> getArgClass() {
        return RenameCollectionArgument.class;
    }

    @Override
    public RenameCollectionArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return RenameCollectionArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(RenameCollectionArgument request) {
        return request.marshall();
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
    public Empty unmarshallResult(BsonDocument replyDoc) throws TypesMismatchException, NoSuchKeyException {
        return Empty.getInstance();
    }

    public static class RenameCollectionArgument {
        private final static StringField FROM_NAMESPACE_FIELD = new StringField(COMMAND_NAME);
        private final static StringField TO_NAMESPACE_FIELD = new StringField("to");
        private final static BooleanField DROP_TARGET_FIELD = new BooleanField("dropTarget");

        private final String fromDatabase;
        private final String fromCollection;
        private final String toDatabase;
        private final String toCollection;
        private final boolean dropTarget;

        public RenameCollectionArgument(String fromDatabase, String fromCollection, String toDatabase, String toCollection, boolean dropTarget) {
            this.fromDatabase = fromDatabase;
            this.fromCollection = fromCollection;
            this.toDatabase = toDatabase;
            this.toCollection = toCollection;
            this.dropTarget = dropTarget;
        }

        public String getFromDatabase() {
            return fromDatabase;
        }

        public String getFromCollection() {
            return fromCollection;
        }

        public String getToDatabase() {
            return toDatabase;
        }

        public String getToCollection() {
            return toCollection;
        }

        public boolean isDropTarget() {
            return dropTarget;
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                    .append(FROM_NAMESPACE_FIELD, NamespaceUtils.get(fromDatabase, fromCollection))
                    .append(TO_NAMESPACE_FIELD, NamespaceUtils.get(toDatabase, toCollection))
                    .append(DROP_TARGET_FIELD, dropTarget)
                    .build();
        }

        private static RenameCollectionArgument unmarshall(BsonDocument requestDoc)
                throws TypesMismatchException, NoSuchKeyException, BadValueException {
            String fromNamespace = BsonReaderTool.getString(requestDoc, FROM_NAMESPACE_FIELD);
            String toNamespace = BsonReaderTool.getString(requestDoc, TO_NAMESPACE_FIELD);
            boolean dropTarget = BsonReaderTool.getBooleanOrUndefined(requestDoc, DROP_TARGET_FIELD, false);

            String fromCollection, fromDatabase;
            try {
                fromDatabase = NamespaceUtils.getDatabase(fromNamespace);
                fromCollection = NamespaceUtils.getCollection(fromNamespace);
            } catch (InvalidNamespaceException invalidNamespaceException) {
                throw new BadValueException(invalidNamespaceException.getMessage(), invalidNamespaceException);
            }

            String toCollection, toDatabase;
            try {
                toDatabase = NamespaceUtils.getDatabase(toNamespace);
                toCollection = NamespaceUtils.getCollection(toNamespace);
            } catch (InvalidNamespaceException invalidNamespaceException) {
                throw new BadValueException(invalidNamespaceException.getMessage(), invalidNamespaceException);
            }
            
            return new RenameCollectionArgument(
                    fromDatabase, fromCollection, toDatabase, toCollection, dropTarget);
        }
    }

}
