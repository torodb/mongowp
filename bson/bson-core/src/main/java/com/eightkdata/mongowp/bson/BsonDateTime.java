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
package com.eightkdata.mongowp.bson;

import java.time.Instant;

/**
 *
 */
public interface BsonDateTime extends BsonValue<Instant> {

  long getMillisFromUnix();

  /**
   * Two BsonDateTime values are equal if the Instant objects their contain are equal.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

  /**
   * The hashCode of a BsonDateTime is the hashCode of the Instant it contains.
   *
   * @return
   */
  @Override
  public int hashCode();
}
