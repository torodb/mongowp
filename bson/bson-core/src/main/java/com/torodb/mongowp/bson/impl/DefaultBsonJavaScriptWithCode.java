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

import com.torodb.mongowp.bson.BsonDocument;
import com.torodb.mongowp.bson.abst.AbstractBsonJavaScriptWithScope;

/**
 *
 */
public class DefaultBsonJavaScriptWithCode extends AbstractBsonJavaScriptWithScope {

  private static final long serialVersionUID = 786221599968366452L;
  private final String js;
  private final BsonDocument scope;

  public DefaultBsonJavaScriptWithCode(String js, BsonDocument scope) {
    this.js = js;
    this.scope = scope;
  }

  @Override
  public BsonDocument getScope() {
    return scope;
  }

  @Override
  public String getJavaScript() {
    return js;
  }
}
