/*
 * MongoWP
 * Copyright © 2014 8Kdata Technology (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.abst.AbstractBsonJavaScriptWithScope;

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
