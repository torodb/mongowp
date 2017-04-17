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

import java.util.Arrays;

/**
 *
 */
public interface BsonDbPointer extends BsonValue<BsonDbPointer> {

  String getNamespace();

  BsonObjectId getId();

  /**
   * The hashCode of a BsonDbPointer is the hash code of its object id.
   *
   * @return
   * @see Arrays#hashCode(byte[])
   */
  @Override
  public int hashCode();

  /**
   * Two BsonDbPointer values are equal if their namespace and id properties are the equal.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

}
