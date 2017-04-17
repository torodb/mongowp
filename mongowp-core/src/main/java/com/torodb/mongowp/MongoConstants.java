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
package com.torodb.mongowp;

import com.google.common.primitives.Ints;
import com.torodb.mongowp.bson.BsonDouble;
import com.torodb.mongowp.bson.impl.PrimitiveBsonDouble;

public class MongoConstants {

  public static final int DEFAULT_PORT = 27017;
  /*
   * TODO: MAX_MESSAGE_SIZE_BYTES is a value that the server could change, so it should be
   * configurable
   */
  public static final int MAX_MESSAGE_SIZE_BYTES = 48 * 1000 * 1000;
  public static final int MESSAGE_LENGTH_FIELD_BYTES = Ints.BYTES;
  public static final int MESSAGE_HEADER_WITHOUT_LENGTH_FIELD_BYTES = Ints.BYTES + Ints.BYTES
      + Ints.BYTES;
  public static final int MESSAGE_HEADER_BYTES = MESSAGE_LENGTH_FIELD_BYTES
      + MESSAGE_HEADER_WITHOUT_LENGTH_FIELD_BYTES;

  public static final Double KO = (double) 0;
  public static final Double OK = (double) 1;

  public static final BsonDouble BSON_KO = PrimitiveBsonDouble.newInstance(0);
  public static final BsonDouble BSON_OK = PrimitiveBsonDouble.newInstance(1);

  private MongoConstants() {
  }

}
