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
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import javax.annotation.Nullable;

/**
 *
 */
public class NoopOplogOperation extends OplogOperation {
    private static final long serialVersionUID = 1L;
    
    private static final StringField NS_FIELD = new StringField("ns");
    private static final DocField MSG_FIELD = new DocField("o");
    private final @Nullable BsonDocument msg;

    public NoopOplogOperation(@Nullable BsonDocument msg, String database, OpTime optime, long h, OplogVersion version, boolean fromMigrate) {
        super(database, optime, h, version, fromMigrate);
        this.msg = msg;
    }

    @Override
    public OplogOperationType getType() {
        return OplogOperationType.NOOP;
    }

    @Override
    public BsonDocumentBuilder toDescriptiveBson() {
        BsonDocumentBuilder doc = super.toDescriptiveBson()
                .append(OP_FIELD, getType().getOplogName())
                .append(NS_FIELD, "");
        if (msg != null) {
            doc.append(MSG_FIELD, msg);
        }
        return doc;
    }

    @Override
    public <Result, Arg> Result accept(OplogOperationVisitor<Result, Arg> visitor, Arg arg) {
        return visitor.visit(this, arg);
    }

}
