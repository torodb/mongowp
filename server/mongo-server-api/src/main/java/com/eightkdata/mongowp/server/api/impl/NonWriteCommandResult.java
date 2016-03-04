
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandResult;
import com.eightkdata.mongowp.server.callback.WriteOpResult;
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
