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
package com.eightkdata.mongowp.exceptions;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.bson.BsonDocument;
import javax.annotation.Nullable;

/**
 *
 */
public class NoMatchingDocumentException extends MongoException {

    private static final long serialVersionUID = -3667547491696671346L;

    private final String database;
    private final String collection;
    private final BsonDocument query;

    public NoMatchingDocumentException(@Nullable String database, @Nullable String collection, @Nullable BsonDocument query) {
        super(ErrorCode.NO_MATCHING_DOCUMENT, database, collection, query);
        this.database = database;
        this.collection = collection;
        this.query = query;
    }

    public NoMatchingDocumentException(String customMessage, @Nullable String database, @Nullable String collection, @Nullable BsonDocument query) {
        super(customMessage, ErrorCode.NO_MATCHING_DOCUMENT);
        this.database = database;
        this.collection = collection;
        this.query = query;
    }

    @Nullable
    public String getDatabase() {
        return database;
    }

    @Nullable
    public String getCollection() {
        return collection;
    }

    @Nullable
    public BsonDocument getQuery() {
        return query;
    }
}
