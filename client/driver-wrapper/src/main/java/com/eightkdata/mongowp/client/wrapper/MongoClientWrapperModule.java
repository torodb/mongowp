/*
 * This file is part of MongoWP.
 *
 * MongoWP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MongoWP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with driver-wrapper. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.eightkdata.mongowp.client.wrapper;

import com.eightkdata.mongowp.client.core.MongoClientFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 *
 */
public class MongoClientWrapperModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MongoClientWrapperFactory.class)
                .in(Singleton.class);
        bind(MongoClientFactory.class)
                .to(MongoClientWrapperFactory.class);

    }

}
