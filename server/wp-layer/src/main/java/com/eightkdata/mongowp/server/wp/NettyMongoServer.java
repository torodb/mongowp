/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.server.wp;

import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.annotations.MongoWp;
import com.eightkdata.mongowp.server.MongoServerConfig;
import com.eightkdata.mongowp.server.util.LengthFieldPrependerLittleEndian;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
