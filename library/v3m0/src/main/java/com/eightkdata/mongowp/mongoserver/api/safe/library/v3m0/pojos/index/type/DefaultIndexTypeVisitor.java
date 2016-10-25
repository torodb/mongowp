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

package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.index.type;

public abstract class DefaultIndexTypeVisitor<Arg, Result> implements IndexTypeVisitor<Arg, Result> {

    protected abstract Result defaultVisit(IndexType indexType, Arg arg);
    
    @Override
    public Result visit(AscIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(DescIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(TextIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(HashedIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(TwoDIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(TwoDSphereIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(GeoHaystackIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

    @Override
    public Result visit(UnknownIndexType indexType, Arg arg) {
        return defaultVisit(indexType, arg);
    }

}
