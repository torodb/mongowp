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
package com.torodb.mongowp.bson.impl;

import com.torodb.mongowp.bson.abst.AbstractBsonDateTime;

import java.time.Instant;

/**
 *
 */
public class InstantBsonDateTime extends AbstractBsonDateTime {

  private static final long serialVersionUID = 5915921994779799275L;

  private final Instant delegate;

  public InstantBsonDateTime(Instant delegate) {
    this.delegate = delegate;
  }

  @Override
  public long getMillisFromUnix() {
    return delegate.toEpochMilli();
  }

  @Override
  public Instant getValue() {
    return delegate;
  }

}
