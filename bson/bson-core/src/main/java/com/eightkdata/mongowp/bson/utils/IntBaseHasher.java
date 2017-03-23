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
package com.eightkdata.mongowp.bson.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 */
public class IntBaseHasher {

  private static final Logger LOGGER = LogManager.getLogger(IntBaseHasher.class);
  private static final HashFunction FUNCTION = Hashing.goodFastHash(32);

  public static int hash(int lenght) {
    int hash = FUNCTION.newHasher().putInt(lenght).hash().asInt();
    if (hash == 0) {
      LOGGER.warn("Hash function returns 0");
      hash = 1;
    }
    return hash;
  }

  private IntBaseHasher() {
  }

}
