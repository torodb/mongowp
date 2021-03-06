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
package com.torodb.mongowp.commands;

import com.torodb.mongowp.Status;

import javax.annotation.Nonnull;

/**
 *
 */
public interface CommandImplementation<ArgT, ResultT, ContextT> {

  @Nonnull
  public Status<ResultT> apply(
      Request req,
      Command<? super ArgT, ? super ResultT> command,
      ArgT arg,
      ContextT context);

}
