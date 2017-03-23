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
package com.eightkdata.mongowp.client.wrapper;

import com.eightkdata.mongowp.MongoVersion;
import com.eightkdata.mongowp.client.core.MongoClient;
import com.eightkdata.mongowp.client.core.MongoConnection;
import com.eightkdata.mongowp.client.core.UnreachableMongoServerException;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;
import com.google.inject.Inject;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 *
 */
public class MongoClientWrapper implements MongoClient {

  private static final Logger LOGGER =
      LogManager.getLogger(MongoClientWrapper.class);
  private boolean closed;
  private final MongoClientConfiguration configuration;
  private final MongoVersion version;
  private final com.mongodb.MongoClient driverClient;
  private final CodecRegistry codecRegistry;

  @Inject
  public MongoClientWrapper(MongoClientConfiguration configuration) throws
      UnreachableMongoServerException {
    try {
      MongoClientOptions options = toMongoClientOptions(configuration);
      ImmutableList<MongoCredential> credentials = toMongoCredentials(configuration);

      testAddress(configuration.getHostAndPort(), options);

      this.configuration = configuration;

      this.driverClient = new com.mongodb.MongoClient(
          new ServerAddress(
              configuration.getHostAndPort().getHostText(),
              configuration.getHostAndPort().getPort()),
          credentials,
          options
      );

      version = calculateVersion();
      codecRegistry = CodecRegistries.fromCodecs(new DocumentCodec());
      closed = false;
    } catch (com.mongodb.MongoException ex) {
      throw new UnreachableMongoServerException(configuration.getHostAndPort(), ex);
    }
  }

  private MongoClientOptions toMongoClientOptions(MongoClientConfiguration configuration) {
    MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
    if (configuration.isSslEnabled()) {
      optionsBuilder.sslEnabled(configuration.isSslEnabled());
      if (configuration.getSocketFactory() != null) {
        optionsBuilder.socketFactory(configuration.getSocketFactory());
        optionsBuilder.sslInvalidHostNameAllowed(configuration.isSslAllowInvalidHostnames());
      }
    }
    return optionsBuilder.build();
  }

  private ImmutableList<MongoCredential> toMongoCredentials(
      MongoClientConfiguration configuration) {
    ImmutableList.Builder<MongoCredential> credentialsBuilder = ImmutableList.builder();
    for (MongoAuthenticationConfiguration authConfiguration : configuration
        .getAuthenticationConfigurations()) {
      credentialsBuilder.add(toMongoCredential(authConfiguration));
    }
    return credentialsBuilder.build();
  }

  private MongoCredential toMongoCredential(MongoAuthenticationConfiguration authConfiguration) {
    switch (authConfiguration.getMechanism()) {
      case cr:
        return MongoCredential.createMongoCRCredential(authConfiguration.getUser(),
            authConfiguration.getSource(), authConfiguration.getPassword().toCharArray());
      case scram_sha1:
        return MongoCredential.createScramSha1Credential(authConfiguration.getUser(),
            authConfiguration.getSource(), authConfiguration.getPassword().toCharArray());
      case negotiate:
        return MongoCredential.createCredential(authConfiguration.getUser(), authConfiguration
            .getSource(), authConfiguration.getPassword().toCharArray());
      case x509:
        return MongoCredential.createMongoX509Credential(authConfiguration.getUser());
      default:
        throw new UnsupportedOperationException("Authentication mechanism " + authConfiguration
            .getMechanism() + " not supported");
    }
  }

  @Override
  public boolean isRemote() {
    return true;
  }

  private void testAddress(HostAndPort address, MongoClientOptions options) throws
      UnreachableMongoServerException {
    SocketAddress sa = new InetSocketAddress(address.getHostText(), address.getPort());
    try (Socket s = options.getSocketFactory().createSocket()) {
      s.connect(sa, 3000);
    } catch (IOException ex) {
      throw new UnreachableMongoServerException(address, ex);
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
    return configuration.getHostAndPort();
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
    try {
      driverClient.close();
    } catch (RuntimeException ex) {
      LOGGER.debug("Ignored an error while closing the client to "
          + getAddress(), ex);
    }
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

}
