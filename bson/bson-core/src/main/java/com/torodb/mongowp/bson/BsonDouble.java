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

/**
 *
 */
public interface BsonDouble extends BsonNumber<Double> {

  /**
   * Return true if the difference between this value and the given one is less than a error
   * <em>delta</em>
   *
   * @param other
   * @param delta the acceptable error
   * @return true if they are close enough
   */
  boolean simmilar(BsonDouble other, double delta);

  /**
   * Two BsonDouble values are equal if the double values their contain are equal.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj);

  /**
   * The hashCode of a BsonDouble is the hashCode of the value it contains.
   *
   * @return
   * @see Double#hashCode()
   */
  @Override
  public int hashCode();
}
