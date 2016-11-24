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

/**
 * A visitor that visits {@link OplogOperation oplog operations}.
 *
 * @param <R> the result of the visit methods.
 * @param <A>    An argument the visit method can take. If it is not needed, use {@link Void}
 */
public interface OplogOperationVisitor<R, A> {

  public R visit(DbCmdOplogOperation op, A arg);

  public R visit(DbOplogOperation op, A arg);

  public R visit(DeleteOplogOperation op, A arg);

  public R visit(InsertOplogOperation op, A arg);

  public R visit(NoopOplogOperation op, A arg);

  public R visit(UpdateOplogOperation op, A arg);

}
