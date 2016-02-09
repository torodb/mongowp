
package com.eightkdata.mongowp.client.core;

import com.eightkdata.mongowp.MongoVersion;
import com.google.common.net.HostAndPort;
import java.io.Closeable;
import javax.annotation.Nullable;

/**
 *
 */
public interface MongoClient extends Closeable {

    /**
     * Returns the {@link HostAndPort} of the server or null if it is a local client.
     *
     * @return
     */
    @Nullable
    public HostAndPort getAddress();

    public MongoVersion getMongoVersion();

    public MongoConnection openConnection();

//    public CommandsLibrary getCommandsLibrary();

    @Override
    public void close();

    public boolean isClosed();
}
