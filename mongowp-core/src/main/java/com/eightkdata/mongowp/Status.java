/*
 * MongoWP - MongoWP: Core
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © 2014 8Kdata (www.8kdata.com)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © 2014 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © ${project.inceptionYear} 8Kdata (www.8kdata.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * MongoWP: Core - mongowp is a Java layer that enables the development of server-side MongoDB wire protocol implementations.
        Any application designed to act as a mongo server could rely on this layer to implement the wire protocol.
        Examples of such applications may be mongo proxies, connection poolers or in-memory implementations,
        to name a few.
 * Copyright © ${project.inceptionYear} ${owner} (${email})
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.eightkdata.mongowp;

import com.eightkdata.mongowp.exceptions.MongoException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 *
 */
public interface Status<Result> extends Serializable{
    public ErrorCode getErrorCode();

    public default boolean isOk() {
        return getErrorCode() == ErrorCode.OK;
    }

    @Nullable
    public Result getResult() throws IllegalStateException;

    @Nullable
    public String getErrorMsg() throws IllegalStateException;

    public default Optional<Result> asOptional() {
        if (isOk()) {
            return Optional.ofNullable(getResult());
        }
        return Optional.empty();
    }

    public static <T> Status<T> ok() {
        return (Status<T>) OkStatus.OK;
    }

    public static <T> Status<T> ok(T result) {
        return new OkStatus<>(result);
    }

    public static <T> Status<T> from(ErrorCode errorCode) {
        return new ErrorStatus<>(errorCode);
    }

    public static <T> Status<T> from(ErrorCode errorCode, String errorMsg) {
        return new ErrorStatus<>(errorCode, errorMsg);
    }

    public static <T> Status<T> withDefaultMsg(ErrorCode errorCode, Object... args) {
        String errorMsg;
        try {
            errorMsg = MessageFormat.format(errorCode.getErrorMessage(), args);
        } catch (IllegalArgumentException ex) {
            errorMsg = "Unknown error message";
        }

        return new ErrorStatus<>(errorCode, errorMsg);
    }

    public static <T> Status<T> from(MongoException ex) {
        return new ErrorStatus<>(ex.getErrorCode(), ex.getLocalizedMessage());
    }

}
