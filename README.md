Mongo Wire Protocol project
===========================


mongowp is a Java layer that enables the development of server-side [MongoDB wire protocol][1] implementations.

Any application designed to act as a mongo server could rely on this layer to implement the wire protocol. Examples of such applications may be mongo proxies, connection poolers or in-memory implementations, to name a few.

This layer is divided into two main projects:

* `netty-bson`: provides an abstraction layer over a BSON document. It provides an API that hides the concrete library used to process BSON documents.
* `mongowp`: a [Netty][2]-based implementation of the mongo wire protocol. It offers a callback-like interface (RequestProcessor interface, located in subproject `mongo-server-api`) that should be implemented to write a mongo server.

mongowp is the basis for the [ToroDB][3] database, an open source, document-oriented, JSON, mongo-compatible database that uses PostgreSQL to store the data relationally. This technique provides high performance gains and a significant reduction of the storage requirements.


How to use it
-------------

mongowp is built with maven, and the artifacts are currently hosted on a [public repository][4]. You may build the source code by running "mvn package" on the root directory of each project.


[1]: http://docs.mongodb.org/meta-driver/latest/legacy/mongodb-wire-protocol/
[2]: http://netty.io/
[3]: http://www.torodb.com
[4]: https://oss.sonatype.org/content/groups/public/com/8kdata/mongowp/