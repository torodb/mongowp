/*
 *     This file is part of ToroDB.
 *
 *     ToroDB is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ToroDB is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with ToroDB. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */

package com.eightkdata.mongowp.messages.utils;

import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;

public class NamespaceUtils {
    
    private NamespaceUtils() {
    }

    public static String get(String database, String collection) {
        return new StringBuilder(database)
                .append('.')
                .append(collection).toString();
    }

    public static String getDatabase(String namespace) throws InvalidNamespaceException {
        int firstDotIndex = getAndCheckFirstDot(namespace);
        if (firstDotIndex == namespace.length()) { //if there is no dot
            return namespace; //then all namespace is the database
        }
        return namespace.substring(0, getAndCheckFirstDot(namespace));
    }

    public static String getCollection(String namespace) throws InvalidNamespaceException {
        int firstDotIndex = getAndCheckFirstDot(namespace);
        if (firstDotIndex == namespace.length()) { //if there is no dot
            throw new InvalidNamespaceException( //then there throw InvalidNamespaceException
                    namespace,
                    "Does not have collection part"
            );
        }
        return namespace.substring(firstDotIndex + 1);
    }

    private static int getAndCheckFirstDot(String namespace) throws InvalidNamespaceException {
        int firstDot = namespace.indexOf('.');
        if (firstDot == 0) {
            throw new InvalidNamespaceException(namespace, "The first character shall not be a dot");
        }
        if (firstDot < 0) {
            return namespace.length();
        }
        if (firstDot == namespace.length() - 1) {
            throw new InvalidNamespaceException(namespace, "The last character shall not be the first dot");
        }
        return firstDot;
    }

}
