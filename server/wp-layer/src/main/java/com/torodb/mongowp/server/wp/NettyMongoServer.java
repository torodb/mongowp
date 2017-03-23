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
package com.torodb.mongowp.server.wp;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.torodb.mongowp.MongoConstants;
import com.torodb.mongowp.annotations.MongoWp;
import com.torodb.mongowp.server.MongoServerConfig;
import com.torodb.mongowp.server.util.LengthFieldPrependerLittleEndian;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteOrder;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 *
 */
//TODO: Modify this server to be able to stop the thread that runs it
public class NettyMongoServer extends AbstractIdleService {

  private static final Logger LOGGER = LogManager.getLogger(NettyMongoServer.class);

  private final int port;
  private EventLoopGroup connectionGroup;
  private EventLoopGroup workerGroup;
  private final RequestMessageObjectHandler requestMessageObjectHandler;
  private final Provider<RequestMessageByteHandler> requestMessageByteHandler;
  private final Provider<ReplyMessageObjectHandler> replyMessageObjectHandler;
  private final LengthFieldPrependerLittleEndian lengthFieldPrependerLittleEndian;
  private final ThreadFactory threadFactory;

  @Inject
  public NettyMongoServer(@MongoWp ThreadFactory threadFactory, MongoServerConfig mongoServerConfig,
      Provider<RequestMessageByteHandler> requestMessageByteHandler,
      Provider<ReplyMessageObjectHandler> replyMessageObjectHandler,
      RequestMessageObjectHandler requestMessageObjectHandler) {
    this.lengthFieldPrependerLittleEndian = new LengthFieldPrependerLittleEndian(
        MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, true);
    this.port = mongoServerConfig.getPort();
    this.requestMessageByteHandler = requestMessageByteHandler;
    this.replyMessageObjectHandler = replyMessageObjectHandler;
    this.requestMessageObjectHandler = requestMessageObjectHandler;
    this.threadFactory = threadFactory;
  }

  @Override
  protected Executor executor() {
    return (Runnable command) -> {
      Thread thread = threadFactory.newThread(command);
      thread.setName(serviceName() + " " + state());
      thread.start();
    };
  }

  private void buildChildHandlerPipeline(ChannelPipeline pipeline) {
    pipeline.addLast(new LengthFieldBasedFrameDecoder(
        ByteOrder.LITTLE_ENDIAN, MongoConstants.MAX_MESSAGE_SIZE_BYTES, 0,
        MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, -MongoConstants.MESSAGE_LENGTH_FIELD_BYTES,
        MongoConstants.MESSAGE_LENGTH_FIELD_BYTES, true
    ));
    pipeline.addLast(requestMessageByteHandler.get());
    pipeline.addLast(lengthFieldPrependerLittleEndian);
    pipeline.addLast(replyMessageObjectHandler.get());
    pipeline.addLast(requestMessageObjectHandler);
  }

  @Override
  protected void startUp() throws Exception {
    LOGGER.info("Listening MongoDB requests on port " + port);

    connectionGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat(
        "netty-connection-%d").build());
    workerGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat(
        "netty-worker-%d").build());

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(connectionGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) throws Exception {
            buildChildHandlerPipeline(socketChannel.pipeline());
          }
        }) // TODO: set TCP channel options?
        ;

    ChannelFuture channelFuture = bootstrap.bind(port).awaitUninterruptibly();
    if (!channelFuture.isSuccess()) {
      workerGroup.shutdownGracefully();
      connectionGroup.shutdownGracefully();
    }
  }

  @Override
  protected void shutDown() throws Exception {
    LOGGER.debug("Shutting down " + this.getClass().getSimpleName());
    if (workerGroup != null) {
      workerGroup.shutdownGracefully().syncUninterruptibly();
    }
    if (connectionGroup != null) {
      connectionGroup.shutdownGracefully().syncUninterruptibly();
    }
  }

  public int getPort() {
    return port;
  }

}
