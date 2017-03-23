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
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonBoolean;

/**
 *
 */
public class TrueBsonBoolean extends AbstractBsonBoolean {

  private static final long serialVersionUID = 2065588109899170349L;

  private TrueBsonBoolean() {
  }

  @Override
  public boolean getPrimitiveValue() {
    return true;
  }

  public static TrueBsonBoolean getInstance() {
    return SimpleBsonTrueHolder.INSTANCE;
  }

  private static class SimpleBsonTrueHolder {

    private static final TrueBsonBoolean INSTANCE = new TrueBsonBoolean();
  }

  // @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return TrueBsonBoolean.getInstance();
  }
}
