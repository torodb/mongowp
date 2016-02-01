
package com.eightkdata.mongowp.client.wrapper;

import com.eightkdata.mongowp.MongoVersion;
import com.eightkdata.mongowp.client.core.MongoClient;
import com.eightkdata.mongowp.client.core.MongoConnection;
import com.eightkdata.mongowp.client.core.UnreachableMongoServerException;
import com.google.common.base.Preconditions;
import com.google.common.net.HostAndPort;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

/**
 *
 */
public class MongoClientWrapper implements MongoClient {

    private boolean closed;
    private final HostAndPort address;
    private final MongoVersion version;
    private final com.mongodb.MongoClient driverClient;
    private final CodecRegistry codecRegistry;

    public MongoClientWrapper(HostAndPort address) throws UnreachableMongoServerException {
        testAddress(address);

        this.address = address.withDefaultPort(27017);

        this.driverClient = new com.mongodb.MongoClient(
                this.address.getHostText(),
                this.address.getPort()
        );

        version = calculateVersion();
        codecRegistry = CodecRegistries.fromCodecs(new DocumentCodec());
        closed = false;
    }

    private void testAddress(HostAndPort address) throws UnreachableMongoServerException {
        SocketAddress sa = new InetSocketAddress(address.getHostText(), address.getPort());
        Socket s = null;
        try {
            s = new Socket();
            s.connect(sa, 3000);
        } catch (IOException ex) {
            throw new UnreachableMongoServerException(address, ex);
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private MongoVersion calculateVersion() {
        Document buildInfo = driverClient
                .getDatabase("admin")
                .runCommand(new BsonDocument("buildInfo", new BsonDouble(1.0)));

        String versionString = buildInfo.getString("version");

        return MongoVersion.fromMongoString(versionString);
    }

    @Override
    public HostAndPort getAddress() {
        return address;
    }

    @Override
    public MongoVersion getMongoVersion() {
        return version;
    }

    @Override
    public MongoConnection openConnection() {
        Preconditions.checkState(!closed, "This client is closed");
        return new MongoConnectionWrapper(codecRegistry, this);
    }

    protected com.mongodb.MongoClient getDriverClient() {
        return driverClient;
    }

    @Override
    public void close() {
        closed = true;
        driverClient.close();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

}
