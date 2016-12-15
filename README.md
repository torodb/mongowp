Mongo Wire Protocol project
===========================

[![Build status](https://travis-ci.org/SonarSource/sq-com_example_java-maven-travis.svg?branch=master)](https://travis-ci.org/SonarSource/sq-com_example_java-maven-travis) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.8kdata.mongowp:mongowp-parent)](https://sonarqube.com/dashboard/index/com.8kdata.mongowp:mongowp-parent)

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

mongowp is built with maven, and the artifacts are currently hosted on a 
[public repository][4]. You may build the source code by running "mvn package" 
on the root directory of each project.

## Code QA
 * Master branch build status: [![Master branch build status](https://travis-ci.org/8kdata/mongowp.svg?branch=master)](https://travis-ci.org/8kdata/mongowp) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.8kdata.mongowp:mongowp-parent)](https://sonarqube.com/dashboard/index/com.8kdata.mongowp:mongowp-parent)
 * Devel branch build status :  [![Devel branch build status](https://travis-ci.org/8kdata/mongowp.svg?branch=devel)](https://travis-ci.org/8kdata/mongowp) [![Quality Gate](https://sonarqube.com/api/badges/gate?key=com.8kdata.mongowp:mongowp-parent:devel)](https://sonarqube.com/dashboard/index/com.8kdata.mongowp:mongowp-parent:devel)

[1]: http://docs.mongodb.org/meta-driver/latest/legacy/mongodb-wire-protocol/
[2]: http://netty.io/
[3]: http://www.torodb.com
[4]: https://oss.sonatype.org/content/groups/public/com/8kdata/mongowp/