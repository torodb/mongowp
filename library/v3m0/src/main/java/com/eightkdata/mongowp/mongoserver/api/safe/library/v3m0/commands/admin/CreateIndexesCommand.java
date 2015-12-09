
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

import com.eightkdata.mongowp.mongoserver.api.safe.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin.CreateIndexesCommand.CreateIndexesArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.IndexOptions;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonReaderTool;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.BadValueException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.TypesMismatchException;
import com.google.common.collect.Lists;
import java.util.List;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

/**
 *
 */
public class CreateIndexesCommand extends AbstractCommand<CreateIndexesArgument, Empty> {
    private static final String COMMAND_NAME = "createIndexes";
    public static final CreateIndexesCommand INSTANCE = new CreateIndexesCommand();

    public CreateIndexesCommand() {
        super(COMMAND_NAME);
    }

    @Override
    public Class<? extends CreateIndexesArgument> getArgClass() {
        return CreateIndexesArgument.class;
    }

    @Override
    public CreateIndexesArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return CreateIndexesArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(CreateIndexesArgument request) {
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
    public Empty unmarshallResult(BsonDocument replyDoc) {
        return Empty.getInstance();
    }

    public static class CreateIndexesArgument {
        private final static BsonField<String> COLLECTION_FIELD = BsonField.create(COMMAND_NAME);
        private final static BsonField<BsonArray> INDEXES_FIELD = BsonField.create("indexes");

        private final String collection;
        private final List<IndexOptions> indexesToCreate;

        public CreateIndexesArgument(String collection, List<IndexOptions> indexesToCreate) {
            this.collection = collection;
            this.indexesToCreate = indexesToCreate;
        }

        public String getCollection() {
            return collection;
        }

        public List<IndexOptions> getIndexesToCreate() {
            return indexesToCreate;
        }

        private BsonDocument marshall() {
            BsonArray indexesArr = new BsonArray();
            for (IndexOptions indexToCreate : indexesToCreate) {
                indexesArr.add(indexToCreate.marshall());
            }

            return new BsonDocumentBuilder()
                    .append(COLLECTION_FIELD, collection)
                    .append(INDEXES_FIELD, indexesArr)
                    .build();
        }

        private static CreateIndexesArgument unmarshall(BsonDocument requestDoc)
                throws TypesMismatchException, NoSuchKeyException, BadValueException {
            String collection = BsonReaderTool.getString(requestDoc, COLLECTION_FIELD);

            BsonArray optionsArray = BsonReaderTool.getArray(requestDoc, INDEXES_FIELD);
            List<IndexOptions> indexes = Lists.newArrayListWithCapacity(optionsArray.size());
            for (BsonValue element : optionsArray) {
                if (!element.isDocument()) {
                    throw new BadValueException("The element " + element
                            + " inside " + INDEXES_FIELD + " array is not a document");
                }
                IndexOptions options = IndexOptions.unmarshall(element.asDocument());
                indexes.add(options);
            }
            return new CreateIndexesArgument(collection, indexes);
        }
    }
}
