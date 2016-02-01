package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class CollectionOplogOperation extends OplogOperation {

    private static final StringField NS_FIELD = new StringField("ns");
    private final String collection;

    public CollectionOplogOperation(
            String database,
            String collection,
            OpTime optime,
            long h,
            OplogVersion version,
            boolean fromMigrate) {
        super(database, optime, h, version, fromMigrate);
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        BsonDocumentBuilder result = super.toDescriptiveBson();
        result.append(NS_FIELD, getDatabase() + '.' + collection);
        return result;
    }

}
