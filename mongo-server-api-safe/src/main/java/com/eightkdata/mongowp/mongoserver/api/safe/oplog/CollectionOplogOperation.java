package com.eightkdata.mongowp.mongoserver.api.safe.oplog;

import com.eightkdata.mongowp.mongoserver.pojos.OpTime;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 *
 */
@Immutable
public abstract class CollectionOplogOperation extends OplogOperation {

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
    public BsonDocument toDescriptiveBson() {
        BsonDocument result = super.toDescriptiveBson();
        result.append("ns", new BsonString(getDatabase() + '.' + collection));
        return result;
    }

}
