
package com.eightkdata.mongowp.mongoserver.api.safe;

import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
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
