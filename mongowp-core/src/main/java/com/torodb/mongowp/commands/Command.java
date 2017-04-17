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
package com.torodb.mongowp.commands;

import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.exceptions.BadValueException;
import com.torodb.mongowp.exceptions.FailedToParseException;
import com.torodb.mongowp.exceptions.MongoException;
import com.torodb.mongowp.exceptions.NoSuchKeyException;
import com.torodb.mongowp.exceptions.TypesMismatchException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Command represents a MongoDB command as a function signature represents a function in
 * programming languages like C++ or Java.
 *
 * @param <A> the class of the command argument
 * @param <R> the class of the command response
 */
public interface Command<A, R> {

  public String getCommandName();

  /**
   * @return iff the command must be executed on the admin namespace
   */
  public boolean isAdminOnly();

  /**
   * @return iff can be directly executed on a slave
   */
  public boolean isSlaveOk();

  /**
   * @return iff clients can force the execution of this command on slave nodes with
   * <em>slaveOk</em> option
   */
  public boolean isSlaveOverrideOk();

  /**
   * @return iff the command execution should increment the command counter
   */
  public boolean shouldAffectCommandCounter();

  /**
   * @return iff the command can be executed while the server is in recovering state
   */
  public boolean isAllowedOnMaintenance();

  public default boolean supportsReadConcern() {
    return false;
  }

  /**
   *
   * @return iff the command can change the replication state
   */
  public boolean canChangeReplicationState();

  public Class<? extends A> getArgClass();

  @Nonnull
  public default A unmarshallArg(@Nonnull BsonDocument requestDoc) throws MongoException {
    return unmarshallArg(requestDoc, requestDoc.getFirstEntry().getKey());
  }

  @Nonnull
  public A unmarshallArg(@Nonnull BsonDocument requestDoc, String aliasedAs) throws MongoException;

  @Nonnull
  public BsonDocument marshallArg(A request, String aliasedAs) throws MarshalException;

  public Class<? extends R> getResultClass();

  @Nonnull
  public R unmarshallResult(@Nonnull BsonDocument resultDoc) throws BadValueException,
      TypesMismatchException, NoSuchKeyException, FailedToParseException, MongoException;

  /**
   * Translate the result to a bson document.
   *
   * The fields
   *
   * @param result
   * @return
   * @throws com.torodb.mongowp.commands.MarshalException
   * @throws MongoException
   */
  @Nullable
  public BsonDocument marshallResult(R result) throws MarshalException;

  /**
   * Two commands are equal iff their class is the same.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);
}
