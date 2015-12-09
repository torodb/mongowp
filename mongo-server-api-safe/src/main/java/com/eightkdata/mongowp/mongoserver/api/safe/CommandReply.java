/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.api.safe.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP;
import com.eightkdata.mongowp.mongoserver.protocol.MongoWP.ErrorCode;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.CommandFailed;
import com.eightkdata.mongowp.mongoserver.protocol.exceptions.MongoException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import org.bson.BsonDocument;

/**
 *
 * @param <Result> the type of the result of the command this reply represents
 */
@Immutable
public interface CommandReply<Result> {
    public static final BsonField<String> ERR_MSG_FIELD = BsonField.create("errmsg");
    public static final BsonField<Double> OK_FIELD = BsonField.create("ok");

    public boolean isOk();

    @Nonnull
    public MongoWP.ErrorCode getErrorCode();

    /**
     *
     * @return
     * @throws IllegalStateException if {@linkplain #isOk() } returns {@linkplain ErrorCode#OK}
     */
    @Nullable
    public String getErrorMessage() throws IllegalStateException;

    /**
     *
     * @return
     * @throws IllegalStateException if {@linkplain #isOk() } returns {@linkplain ErrorCode#OK}
     */
    @Nonnull
    public MongoException getErrorAsException() throws IllegalStateException;

    /**
     * @return null if the command is not a write command
     */
    public @Nullable WriteOpResult getWriteOpResult();

    /**
     *
     * @return
     * @throws IllegalStateException if {@linkplain #isOk() } returns something
     *         different than {@linkplain ErrorCode#OK}
     */
    /**
     *
     * @return the result of the command execution
     * @throws CommandFailed if the command execution failed because its own
     *         semantic
     * @throws MongoException if something else went wrong during the execution
     */
    @Nonnull
    public Result getResult() throws IllegalStateException;

    @Nonnull
    public BsonDocument marshall() throws MongoException;
}
