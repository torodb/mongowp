
package com.eightkdata.mongowp.server.api;

import com.eightkdata.mongowp.server.callback.WriteOpResult;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public interface CommandResult<R> {

    @Nullable
    public WriteOpResult getWriteOpResult();

    @Nonnull
    public R getResult();

}
