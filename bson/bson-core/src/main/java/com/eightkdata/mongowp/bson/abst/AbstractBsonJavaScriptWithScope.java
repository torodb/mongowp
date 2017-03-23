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
package com.eightkdata.mongowp.bson.abst;

import com.eightkdata.mongowp.bson.BsonJavaScriptWithScope;
import com.eightkdata.mongowp.bson.BsonType;
import com.eightkdata.mongowp.bson.BsonValue;
import com.eightkdata.mongowp.bson.BsonValueVisitor;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

public abstract class AbstractBsonJavaScriptWithScope
    extends AbstractBsonValue<BsonJavaScriptWithScope> implements BsonJavaScriptWithScope {

  @Override
  public Class<? extends BsonJavaScriptWithScope> getValueClass() {
    return this.getClass();
  }

  @Override
  public BsonJavaScriptWithScope getValue() {
    return this;
  }

  @Override
  public BsonType getType() {
    return BsonType.JAVA_SCRIPT_WITH_SCOPE;
  }

  @Override
  public BsonJavaScriptWithScope asJavaScriptWithScope() {
    return this;
  }

  @Override
  public boolean isJavaScriptWithScope() {
    return true;
  }

  @Override
  public int compareTo(BsonValue<?> obj) {
    if (obj == this) {
      return 0;
    }
    int diff = BsonTypeComparator.INSTANCE.compare(getType(), obj.getType());
    if (diff != 0) {
      return diff;
    }

    assert obj.isJavaScriptWithScope();
    BsonJavaScriptWithScope other = obj.asJavaScriptWithScope();
    //TODO: Check how MongoDB compares js with scope!

    diff = this.getValue().compareTo(other.getValue());
    if (diff != 0) {
      return diff;
    }

    return this.getScope().compareTo(other.getScope());
  }

  @Override
  public final boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonJavaScriptWithScope)) {
      return false;
    }
    BsonJavaScriptWithScope other = (BsonJavaScriptWithScope) obj;
    if (!this.getJavaScript().equals(other.getJavaScript())) {
      return false;
    }
    return this.getScope().equals(other.getScope());
  }

  @Override
  public final int hashCode() {
    return getJavaScript().hashCode();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
