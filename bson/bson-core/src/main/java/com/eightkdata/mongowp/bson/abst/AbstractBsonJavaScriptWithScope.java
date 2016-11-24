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
