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
package com.eightkdata.mongowp.bson.netty.annotations;

import io.netty.buffer.ByteBuf;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to identify when the indexes of a {@link ByteBuf} parameter will be
 * modified by the method that recives the parameter or the constructor (and the created object)
 * that recives the parameter.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER})
@Documented
public @interface ModifiesIndexes {

  boolean value() default true;
}
