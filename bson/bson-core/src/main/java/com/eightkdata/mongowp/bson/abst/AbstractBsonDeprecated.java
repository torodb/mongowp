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

import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.bson.utils.BsonTypeComparator;

public abstract class AbstractBsonDeprecated extends AbstractBsonValue<String>
    implements BsonDeprecated {

  @Override
  public Class<? extends String> getValueClass() {
    return String.class;
  }

  @Override
  public BsonType getType() {
    return BsonType.DEPRECATED;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof BsonDeprecated)) {
      return false;
    }
    return this.getValue().equals(((BsonDeprecated) obj).getValue());
  }

  @Override
  public BsonDeprecated asDeprecated() {
    return this;
  }

  @Override
  public boolean isDeprecated() {
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

    if (obj.isNull()) {
      return 1;
    }

    if (obj.isUndefined()) {
      return 1;
    }

    assert obj.isDeprecated();
    return 0;
  }

  @Override
  public final int hashCode() {
    return getValue().hashCode();
  }

  @Override
  public <R, A> R accept(BsonValueVisitor<R, A> visitor, A arg) {
    return visitor.visit(this, arg);
  }

}
