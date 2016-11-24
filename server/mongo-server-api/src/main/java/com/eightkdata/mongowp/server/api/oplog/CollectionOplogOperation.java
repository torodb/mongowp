/*
 * MongoWP
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.server.api.oplog;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class CollectionOplogOperation extends OplogOperation {

  private static final long serialVersionUID = 1L;

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

  @Nullable
  public abstract BsonValue<?> getDocId();

}
