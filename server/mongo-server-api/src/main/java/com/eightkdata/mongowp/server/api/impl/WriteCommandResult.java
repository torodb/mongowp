
package com.eightkdata.mongowp.server.api.impl;

import com.eightkdata.mongowp.server.api.CommandResult;
import com.eightkdata.mongowp.server.callback.WriteOpResult;
import javax.annotation.Nonnull;

/**
 *
 */
public class WriteCommandResult<R> implements CommandResult<R> {

    @Nonnull 
    private final R result;
    private final WriteOpResult writeOpResult;

    public WriteCommandResult(@Nonnull R result, @Nonnull WriteOpResult writeOpResult) {
        this.result = result;
        this.writeOpResult = writeOpResult;
    }

    @Nonnull 
    @Override
    public WriteOpResult getWriteOpResult() {
        return writeOpResult;
    }

    @Override
    @Nonnull 
    public R getResult() {
        return result;
    }

}
