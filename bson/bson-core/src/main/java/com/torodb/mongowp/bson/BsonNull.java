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
package com.torodb.mongowp.bson;

public interface BsonNull extends BsonValue<BsonNull> {

  public static final int HASH = BsonType.NULL.hashCode();

  /**
   * Two BsonNull values are always equal.
   */
  @Override
  public boolean equals(Object obj);

  /**
   * The hashCode of a BsonNull is {@link #HASH}.
   */
  @Override
  public int hashCode();
}
