
package com.eightkdata.mongowp.mongoserver.api.impl;

import com.eightkdata.mongowp.mongoserver.api.CommandResult;
import com.eightkdata.mongowp.mongoserver.callback.WriteOpResult;
import javax.annotation.Nonnull;

/**
 *
 */
public class NonWriteCommandResult<R> implements CommandResult<R>{

    @Nonnull 
    private final R result;

    public NonWriteCommandResult(@Nonnull R result) {
        this.result = result;
    }

    @Override
    public WriteOpResult getWriteOpResult() {
        return null;
    }

    @Nonnull
    @Override
    public R getResult() {
        return result;
    }

}
