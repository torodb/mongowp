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
package com.eightkdata.mongowp.server.decoder;

import com.eightkdata.mongowp.bson.netty.annotations.ConservesIndexes;
import com.eightkdata.mongowp.bson.netty.annotations.Loose;
import com.eightkdata.mongowp.bson.netty.annotations.Retains;
import com.eightkdata.mongowp.messages.request.BsonContext;
import io.netty.buffer.ByteBuf;

/**
 *
 */
public class MyBsonContext implements BsonContext {

  private boolean closed = false;
  private final ByteBuf byteBuf;

  public MyBsonContext(@Loose @Retains @ConservesIndexes ByteBuf byteBuf) {
    this.byteBuf = byteBuf.retain();
  }

  @Override
  public void close() {
    if (!closed) {
      closed = true;
      this.byteBuf.release();
    }
  }

  @Override
  public boolean isValid() {
    return !closed;
  }

}
