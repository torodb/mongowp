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

package com.eightkdata.mongowp;

/**
 *
 */
class OkStatus<R> implements Status<R> {

  static final Status<?> OK = new OkStatus<>(null);
  private static final long serialVersionUID = -2094761991032243073L;
  private final R result;

  public OkStatus(R result) {
    this.result = result;
  }

  @Override
  public ErrorCode getErrorCode() {
    return ErrorCode.OK;
  }

  @Override
  public R getResult() throws IllegalStateException {
    return result;
  }

  @Override
  public boolean isOk() {
    return true;
  }

  @Override
  public String getErrorMsg() throws IllegalStateException {
    throw new IllegalStateException("This status is OK, so there is no error msg");
  }

}
