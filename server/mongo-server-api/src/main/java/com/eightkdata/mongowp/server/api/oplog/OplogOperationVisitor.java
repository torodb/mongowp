/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
