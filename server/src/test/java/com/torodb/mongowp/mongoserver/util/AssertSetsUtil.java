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
package com.torodb.mongowp.mongoserver.util;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 *
 */
public class AssertSetsUtil {

  public static <T> boolean assertSetsEqual(Set<T> a, Set<T> b) {
    if (a == null || b == null) {
      return a == b;
    }
    Set<T> diff1 = Sets.difference(a, b);
    Set<T> diff2 = Sets.difference(b, a);

    return diff1.size() == 0 && diff1.size() == diff2.size();
  }
}
