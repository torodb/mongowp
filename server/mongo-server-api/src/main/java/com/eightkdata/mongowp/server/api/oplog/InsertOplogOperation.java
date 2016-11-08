/*
 * MongoWP - Mongo Server: API
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonString;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public class InsertOplogOperation extends CollectionOplogOperation {
    private static final long serialVersionUID = 1L;
    
    private static final DocField DOC_TO_INSERT_FIELD = new DocField("o");

    private final BsonDocument docToInsert;

    public InsertOplogOperation(
            BsonDocument docToInsert,
            String database,
            String collection,
            OpTime optime,
            long h,
            OplogVersion version,
            boolean fromMigrate) {
        super(database, collection, optime, h, version, fromMigrate);
        this.docToInsert = docToInsert;
    }

    public BsonDocument getDocToInsert() {
        return docToInsert;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.INSERT;
    }

    @Override
    public BsonValue<?> getDocId() {
        return getDocToInsert().get("_id");
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        return super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(DOC_TO_INSERT_FIELD, docToInsert);
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
