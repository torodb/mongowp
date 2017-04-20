Mongo Wire Protocol project
===========================

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.torodb/mongowp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.torodb/mongowp)

MongoWP is a Java layer that enables the development of 
[MongoDB wire protocol][1] applications (client or server side).

Any application designed to act as a mongo server could rely on this layer to 
implement the wire protocol. Examples of such applications may be mongo proxies,
connection poolers or in-memory implementations, to name a few.

This layer is divided into several projects:

* `bson`: provides an abstraction layer over a BSON document. It provides an API
that hides the concrete library used to process BSON documents.
* `mongowp-core`: contains common POJOs, exceptions, enums and annotations used
by other projects.
* `server`: an aggregation project that contains other projects used to
implement servers that speak the mongo wire protocol. It includes a 
[Netty][2]-based implementation.
* `server`: an aggregation project that contains other projects used to
implement clients that speak the mongo wire protocol. Right now there is only
one implementation (`driver-wrapper`) that adapts the official MongoDB driver.

How to use it
-------------

MongoWP is built with Maven. Released artifacts are hosted on [Maven Central][4]
and snapshot artifacts on [Nexus Repository Management][5]. 
You may build the source code by running "mvn package" on the root directory.

## Code QA
 * Master branch build status: [![Master branch build status](https://travis-ci.org/torodb/mongowp.svg?branch=master)](https://travis-ci.org/torodb/mongowp)
 * Devel branch build status :  [![Devel branch build status](https://travis-ci.org/torodb/mongowp.svg?branch=devel)](https://travis-ci.org/torodb/mongowp)

## Code QA
 * Master branch: 
[![Build Status](https://travis-ci.org/torodb/mongowp.svg?branch=master)](https://travis-ci.org/torodb/mongowp)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/0ab3aedf5eb7411aad8536fe3d447ee1?branch=master)](https://www.codacy.com/app/torodb/mongowp?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=torodb/mongowp&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/coverage/0ab3aedf5eb7411aad8536fe3d447ee1?branch=master)](https://www.codacy.com/app/torodb/mongowp?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=torodb/mongowp&amp;utm_campaign=Badge_Grade)
 * Devel branch:
[![Build Status](https://travis-ci.org/torodb/mongowp.svg?branch=devel)](https://travis-ci.org/torodb/mongowp)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/0ab3aedf5eb7411aad8536fe3d447ee1?branch=devel)](https://www.codacy.com/app/torodb/mongowp?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=torodb/mongowp&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/coverage/0ab3aedf5eb7411aad8536fe3d447ee1?branch=devel)](https://www.codacy.com/app/torodb/mongowp?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=torodb/mongowp&amp;utm_campaign=Badge_Grade)

[1]: http://docs.mongodb.org/meta-driver/latest/legacy/mongodb-wire-protocol/
[2]: http://netty.io/
[3]: http://www.torodb.com
[4]: http://search.maven.org/#search%7Cga%7C1%7Cg:%22com.torodb%22%20AND%20a:%22mongowp)%22
[5]: https://oss.sonatype.org/content/groups/public/com/torodb/mongowp/
