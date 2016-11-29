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
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;

public class DbOplogOperation extends OplogOperation {

  private static final long serialVersionUID = 1L;

  public DbOplogOperation(String database, OpTime optime, long h, OplogVersion version,
      boolean fromMigrate) {
    super(database, optime, h, version, fromMigrate);
  }

  @Override
  public OplogOperationType getType() {
    return OplogOperationType.DB;
  }

  @Override
  public BsonDocumentBuilder toDescriptiveBson() {
    return super.toDescriptiveBson()
        .append(OP_FIELD, getType().getOplogName());
  }

  @Override
  public <R, A> R accept(OplogOperationVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
