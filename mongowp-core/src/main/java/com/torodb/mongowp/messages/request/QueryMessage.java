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
package com.torodb.mongowp.messages.request;


import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.AWAIT_DATA;
import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.EXHAUST;
import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.NO_CURSOR_TIMEOUT;
import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.OPLOG_REPLAY;
import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.PARTIAL;
import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.SLAVE_OK;
import static com.torodb.mongowp.messages.request.QueryMessage.QueryOption.TAILABLE_CURSOR;

import com.google.common.base.Preconditions;
import com.torodb.mongowp.annotations.Ethereal;
import com.torodb.mongowp.bson.BsonDocument;

import java.util.Set;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

public class QueryMessage extends AbstractRequestMessage {

  public static final RequestOpCode REQUEST_OP_CODE = RequestOpCode.OP_QUERY;

  @Override
  public RequestOpCode getOpCode() {
    return REQUEST_OP_CODE;
  }

  @Nonnull
  private final String database;
  @Nonnull
  private final String collection;
  @Nonnegative
  private final int numberToSkip;
  private final int numberToReturn;
  @Nonnull
  private final QueryOptions queryOptions;
  @Nonnull
  @Ethereal("getDataContext")
  private final BsonDocument query;
  @Nullable
  @Ethereal("getDataContext")
  private final BsonDocument returnFieldsSelector;

  @Nullable
  private final String comment;
  @Nonnull
  private final ExplainOption explainOption;
  @Nullable
  @Ethereal("getDataContext")
  private final BsonDocument hint;
  private final long maxScan;
  private final int maxTimeMs;
  @Nullable
  @Ethereal("getDataContext")
  private final BsonDocument max;
  @Nullable
  @Ethereal("getDataContext")
  private final BsonDocument min;
  @Nullable
  @Ethereal("getDataContext")
  private final BsonDocument orderBy;
  private final boolean returnKey;
  private final boolean showDiscLoc;
  private final boolean snapshot;

  public QueryMessage(
      RequestBaseMessage requestBaseMessage, @Nonnull BsonContext context,
      AutoCloseable documentContext, String database, String collection,
      int numberToSkip, int numberToReturn, QueryOptions queryOptions,
      @Ethereal("context") BsonDocument query,
      @Ethereal("context") BsonDocument returnFieldsSelector,
      String comment, ExplainOption explainOption,
      @Ethereal("context") BsonDocument hint, long maxScan, int maxTimeMs,
      @Ethereal("context") BsonDocument max,
      @Ethereal("context") BsonDocument min,
      @Ethereal("context") BsonDocument orderBy,
      boolean returnKey, boolean showDiscLoc,
      boolean snapshot) {
    super(requestBaseMessage, context);
    this.database = database;
    this.collection = collection;
    this.numberToSkip = numberToSkip;
    this.numberToReturn = numberToReturn;
    this.queryOptions = queryOptions;
    this.query = query;
    this.returnFieldsSelector = returnFieldsSelector;
    this.comment = comment;
    this.explainOption = explainOption;
    this.hint = hint;
    this.maxScan = maxScan;
    this.maxTimeMs = maxTimeMs;
    this.max = max;
    this.min = min;
    this.orderBy = orderBy;
    this.returnKey = returnKey;
    this.showDiscLoc = showDiscLoc;
    this.snapshot = snapshot;
  }

  @Nonnull
  public String getDatabase() {
    return database;
  }

  @Nonnull
  public String getCollection() {
    return collection;
  }

  @Nonnegative
  public int getNumberToSkip() {
    return numberToSkip;
  }

  public int getNumberToReturn() {
    return numberToReturn;
  }

  @Nonnull
  public QueryOptions getQueryOptions() {
    return queryOptions;
  }

  @Nonnull
  @Ethereal("this")
  public BsonDocument getQuery() {
    return query;
  }

  /**
   * Makes it possible to attach a comment to a query.
   *
   * @return the comment or null if it is absent
   */
  @Nullable
  public String getComment() {
    return comment;
  }

  /**
   * Provides information on the query plan.
   *
   * When enabled, the request returns a document that describes the process and indexes used to
   * return the query.
   *
   * @return the requested explain level
   */
  @Nonnull
  public ExplainOption getExplainOption() {
    return explainOption;
  }

  /**
   * Forces the query optimizer to use a specific index to fulfill the query.
   *
   * @return A document with the indexes that must be used or null if absent
   */
  @Nullable
  @Ethereal("this")
  public BsonDocument getHint() {
    return hint;
  }

  /**
   * Constrains the query to only scan the specified number of documents when fulfilling the query.
   *
   * @return the max number of documents to scan or negative if there is no limit
   */
  public long getMaxScan() {
    return maxScan;
  }

  /**
   * Specifies a cumulative time limit in milliseconds for processing operations on the cursor.
   *
   * @return the maximum number of millis or negative if there is no limit
   * @since MongoDB 2.6
   */
  public int getMaxTimeMs() {
    return maxTimeMs;
  }

  /**
   * Specifies the exclusive upper bound for a specific index in order to constrain the results.
   *
   * @return a document with the max value for each attribute or null if absent
   */
  @Nullable
  @Ethereal("this")
  public BsonDocument getMax() {
    return max;
  }

  /**
   * Specifies a the inclusive lower bound for a specific index in order to constrain the results.
   *
   * @return a document with the min value for each attribute or null if absent
   */
  @Nullable
  @Ethereal("this")
  public BsonDocument getMin() {
    return min;
  }

  @Nullable
  @Ethereal("this")
  public BsonDocument getOrderBy() {
    return orderBy;
  }

  /**
   * Only return the index field or fields for the results of the query.
   *
   * @return
   */
  public boolean isReturnKey() {
    return returnKey;
  }

  /**
   * Adds a field $diskLoc to the returned documents.
   * <p>
   * The value of the added $diskLoc field is a document that contains the disk location
   * information.
   *
   * @return
   */
  public boolean isShowDiscLoc() {
    return showDiscLoc;
  }

  /**
   * Prevents the cursor from returning a document more than once because an intervening write
   * operation results in a move of the document.
   *
   * @return
   */
  public boolean isSnapshot() {
    return snapshot;
  }

  @Override
  public String toString() {
    return "QueryMessage{" + super.toString() + ", database='" + database + '\'' + ", collection='"
        + collection + '\'' + ", numberToSkip=" + numberToSkip + ", numberToReturn="
        + numberToReturn + ", query=" + query + ", returnFieldsSelector=" + returnFieldsSelector
        + '}';
  }

  @Immutable
  public static class QueryOptions {

    final boolean tailable;
    final boolean slaveOk;
    final boolean oplogReplay;
    final boolean noCursorTimeout;
    final boolean awaitData;
    final boolean exhaust;
    final boolean partial;

    public QueryOptions(Set<QueryOption> queryOptions) {
      this.tailable = queryOptions.contains(TAILABLE_CURSOR);
      this.slaveOk = queryOptions.contains(SLAVE_OK);
      this.oplogReplay = queryOptions.contains(OPLOG_REPLAY);
      this.noCursorTimeout = queryOptions.contains(NO_CURSOR_TIMEOUT);
      this.awaitData = queryOptions.contains(AWAIT_DATA);
      this.exhaust = queryOptions.contains(EXHAUST);
      this.partial = queryOptions.contains(PARTIAL);
    }

    public boolean isTailable() {
      return tailable;
    }

    public boolean isSlaveOk() {
      return slaveOk;
    }

    public boolean isOplogReplay() {
      return oplogReplay;
    }

    public boolean isNoCursorTimeout() {
      return noCursorTimeout;
    }

    public boolean isAwaitData() {
      return awaitData;
    }

    public boolean isExhaust() {
      return exhaust;
    }

    public boolean isPartial() {
      return partial;
    }

  }

  public static enum QueryOption {
    TAILABLE_CURSOR,
    SLAVE_OK,
    OPLOG_REPLAY,
    NO_CURSOR_TIMEOUT,
    AWAIT_DATA,
    EXHAUST,
    PARTIAL;
  }

  public static enum ExplainOption {
    NONE,
    QUERY_PLANNER,
    EXECUTION_STATS,
    ALL_PLANS_EXECUTION;
  }

  public static enum NaturalOrder {
    NONE,
    DESC,
    ASC
  }

  public static class Builder {

    @Nonnull
    private final BsonContext context;
    @Nonnull
    private final RequestBaseMessage requestBaseMessage;
    @Nonnull
    private final String database;
    @Nonnull
    private final String collection;
    @Nonnegative
    private int numberToSkip = 0;
    private int numberToReturn = 0;
    @Nonnull
    private final QueryOptions queryOptions;
    @Ethereal("context")
    private BsonDocument query;

    @Ethereal("context")
    @Nullable
    private BsonDocument returnFieldsSelector;
    @Nullable
    private String comment;
    @Nonnull
    private ExplainOption explainOption = ExplainOption.NONE;
    @Nullable
    @Ethereal("context")
    private BsonDocument hint;
    private long maxScan = -1;
    private int maxTimeMs = -1;
    @Nullable
    @Ethereal("context")
    private BsonDocument max;
    @Nullable
    @Ethereal("context")
    private BsonDocument min;
    @Nullable
    @Ethereal("context")
    private BsonDocument orderBy;
    private boolean returnKey = false;
    private boolean showDiscLoc = false;
    private boolean snapshot = false;

    public Builder(@Nonnull RequestBaseMessage requestBaseMessage,
        BsonContext context, @Nonnull String database,
        @Nonnull String collection, @Nonnull QueryOptions queryOptions) {
      this.requestBaseMessage = requestBaseMessage;
      this.database = database;
      this.collection = collection;
      this.queryOptions = queryOptions;
      this.context = context;
    }

    public Builder setQuery(@Ethereal("context") @Nonnull BsonDocument query) {
      this.query = query;
      return this;
    }

    public Builder setNumberToSkip(@Nonnegative int numberToSkip) {
      this.numberToSkip = numberToSkip;
      return this;
    }

    public Builder setNumberToReturn(int numberToReturn) {
      this.numberToReturn = numberToReturn;
      return this;
    }

    public Builder setComment(String comment) {
      this.comment = comment;
      return this;
    }

    public Builder setExplainOption(ExplainOption explainOption) {
      this.explainOption = explainOption;
      return this;
    }

    public Builder setHint(@Ethereal("context") @Nullable BsonDocument hint) {
      this.hint = hint;
      return this;
    }

    public Builder setMaxScan(long maxScan) {
      this.maxScan = maxScan;
      return this;
    }

    public Builder setMaxTimeMs(int maxTimeMs) {
      this.maxTimeMs = maxTimeMs;
      return this;
    }

    public Builder setMax(@Nullable @Ethereal("context") BsonDocument max) {
      this.max = max;
      return this;
    }

    public Builder setMin(@Nullable @Ethereal("context") BsonDocument min) {
      this.min = min;
      return this;
    }

    public Builder setOrderBy(@Nullable @Ethereal("context") BsonDocument orderBy) {
      this.orderBy = orderBy;
      return this;
    }

    public Builder setReturnKey(boolean returnKey) {
      this.returnKey = returnKey;
      return this;
    }

    public Builder setShowDiscLoc(boolean showDiscLoc) {
      this.showDiscLoc = showDiscLoc;
      return this;
    }

    public Builder setSnapshot(boolean snapshot) {
      this.snapshot = snapshot;
      return this;
    }

    public Builder setReturnFieldsSelector(
        @Nullable @Ethereal("context") BsonDocument returnFieldsSelector) {
      this.returnFieldsSelector = returnFieldsSelector;
      return this;
    }

    public QueryMessage build() {
      Preconditions.checkNotNull(query, "Query shall not be null");
      return new QueryMessage(requestBaseMessage, context, context,
          database, collection, numberToSkip, numberToReturn,
          queryOptions, query, returnFieldsSelector, comment,
          explainOption, hint, maxScan, maxTimeMs, max, min, orderBy,
          returnKey, showDiscLoc, snapshot);
    }

    @Ethereal("context")
    public BsonDocument getQuery() {
      return query;
    }
  }
}
