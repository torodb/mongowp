/*
 *     This file is part of mongowp.
 *
 *     mongowp is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     mongowp is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with mongowp. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */


package com.eightkdata.mongowp.messages.request;

import javax.annotation.Nonnull;
import java.net.InetAddress;

/**
 *
 */
public abstract class AbstractRequestMessage {
    @Nonnull private final RequestBaseMessage requestBaseMessage;

    protected AbstractRequestMessage(@Nonnull RequestBaseMessage requestBaseMessage) {
        this.requestBaseMessage = requestBaseMessage;
    }

    @Nonnull
    public RequestBaseMessage getBaseMessage() {
        return requestBaseMessage;
    }

    @Nonnull
    public InetAddress getClientAddress() {
        return requestBaseMessage.getClientAddress();
    }

    @Nonnull
    public String getClientAddressString() {
        return requestBaseMessage.getClientAddressString();
    }

    public int getClientPort() {
        return requestBaseMessage.getClientPort();
    }

    public int getRequestId() {
        return requestBaseMessage.getRequestId();
    }

	protected String[] splitFullCollectionName(String fullCollectionName) {
		String[] splittedFullCollectionName = new String[2];
		
		int indexOfSeparator = fullCollectionName.indexOf(".");
        if(indexOfSeparator == -1) {
            throw new IllegalArgumentException("Invalid full collection name '" + fullCollectionName + "'");
        }
        splittedFullCollectionName[0] = fullCollectionName.substring(0, indexOfSeparator);
        splittedFullCollectionName[1] = fullCollectionName.substring(indexOfSeparator + 1);
        
		return splittedFullCollectionName;
	}

    @Override
    public String toString() {
        return "clientAddress=" + requestBaseMessage.getClientAddressString() +
                ", clientPort=" + getClientPort() +
                ", requestId=" + getRequestId();
    }
}
