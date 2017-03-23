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
package com.torodb.mongowp.server.api.tools;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class is like {@link Void} but it has one instance.
 * <p/>
 * This can be useful when you want an implementation of a generic class that returns non null
 * instances of the generic type. For instance, commands that do not need argument or do not return
 * information, can use this class.
 */
public class Empty {

  private Empty() {
  }

  public static Empty getInstance() {
    return EmptyHolder.INSTANCE;
  }

  private static class EmptyHolder {

    private static final Empty INSTANCE = new Empty();
  }

  @SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
  private Object readResolve() {
    return Empty.getInstance();
  }
}
