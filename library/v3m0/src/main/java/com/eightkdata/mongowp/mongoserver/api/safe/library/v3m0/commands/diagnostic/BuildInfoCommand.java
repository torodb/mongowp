
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic;

import com.eightkdata.mongowp.exceptions.*;
import com.eightkdata.mongowp.MongoVersion;
import com.eightkdata.mongowp.mongoserver.api.MarshalException;
import com.eightkdata.mongowp.mongoserver.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.BuildInfoCommand.BuildInfoResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.mongoserver.api.tools.Empty;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonDocumentBuilder;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonField;
import com.eightkdata.mongowp.mongoserver.api.tools.bson.BsonReaderTool;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonInt32;

/**
 *
 */
public class BuildInfoCommand extends AbstractCommand<Empty, BuildInfoResult> {

    public static final BuildInfoCommand INSTANCE = new BuildInfoCommand();

    private BuildInfoCommand() {
        super("buildInfo");
    }

    @Override
    public Class<? extends Empty> getArgClass() {
        return Empty.class;
    }

    @Override
    public Empty unmarshallArg(BsonDocument requestDoc) throws BadValueException,
            TypesMismatchException, NoSuchKeyException, FailedToParseException {
        return Empty.getInstance();
    }

    @Override
    public BsonDocument marshallArg(Empty request) throws MarshalException {
        return EmptyCommandArgumentMarshaller.marshallEmptyArgument(this);
    }

    @Override
    public Class<? extends BuildInfoResult> getResultClass() {
        return BuildInfoResult.class;
    }

    @Override
    public BuildInfoResult unmarshallResult(BsonDocument resultDoc) throws
            BadValueException, TypesMismatchException, NoSuchKeyException,
            FailedToParseException, MongoException {
        return BuildInfoResult.unmarshall(resultDoc);
    }

    @Override
    public BsonDocument marshallResult(BuildInfoResult result) throws
            MarshalException {
        return result.marshall();
    }


    public static class BuildInfoResult {

        private static final BsonField<String> VERSION_FIELD = BsonField.create("version");
        private static final BsonField<String> GIT_VERSION_FIELD = BsonField.create("gitVersion");
        private static final BsonField<String> SYS_INFO_FIELD = BsonField.create("sysInfo");
        private static final BsonField<String> LOADER_FLAGS_FIELD = BsonField.create("loaderFlags");
        private static final BsonField<String> COMPILER_FLAGS_FIELD = BsonField.create("compilerFlags");
        private static final BsonField<String> ALLOCATOR_FIELD = BsonField.create("allocator");
        private static final BsonField<String> JAVASCRIPT_ENGINE_FIELD = BsonField.create("javascriptEngine");
        private static final BsonField<BsonArray> VERSION_ARRAY_FIELD = BsonField.create("versionArrayField");
        private static final BsonField<Number> BITS_FIELD = BsonField.create("bits");
        private static final BsonField<Boolean> DEBUG_FIELD = BsonField.create("debug");
        private static final BsonField<Number> MAX_OBJECT_SIZE_FIELD = BsonField.create("maxObjectSize");

        private final MongoVersion version;
        private final int patchVersion;
        private final String gitVersion;
        private final String sysInfo;
        private final String loaderFlags;
        private final String compilerFlags;
        private final String allocator;
        private final String javascriptEngine;
        private final int bits;
        private final boolean debug;
        private final long maxObjectSize;

        public BuildInfoResult(
                MongoVersion version,
                int patchVersion,
                String gitVersion,
                String sysInfo,
                String loaderFlags,
                String compilerFlags,
                String allocator,
                String javascriptEngine,
                int bits,
                boolean debug,
                long maxObjectSize) {
            this.version = version;
            this.gitVersion = gitVersion;
            this.sysInfo = sysInfo;
            this.loaderFlags = loaderFlags;
            this.compilerFlags = compilerFlags;
            this.allocator = allocator;
            this.javascriptEngine = javascriptEngine;
            this.bits = bits;
            this.debug = debug;
            this.maxObjectSize = maxObjectSize;
            this.patchVersion = patchVersion;
        }

        private static BuildInfoResult unmarshall(BsonDocument resultDoc) throws TypesMismatchException, NoSuchKeyException {
            MongoVersion version = MongoVersion.fromMongoString(
                    BsonReaderTool.getString(resultDoc, VERSION_FIELD, null)
            );
            BsonArray versionArr = BsonReaderTool.getArray(resultDoc, VERSION_ARRAY_FIELD, null);
            int pathVersion = 0;
            if (versionArr != null && versionArr.size() > 2 && versionArr.get(2).isNumber()) {
                pathVersion = versionArr.get(2).asNumber().intValue();
            }
            int bits;
            try {
                bits = BsonReaderTool.getNumeric(resultDoc, BITS_FIELD).intValue();
            } catch (NoSuchKeyException ex) {
                bits = 0;
            }
            long maxObjectSize;
            try {
                maxObjectSize = BsonReaderTool.getNumeric(resultDoc, MAX_OBJECT_SIZE_FIELD).longValue();
            } catch (NoSuchKeyException ex) {
                maxObjectSize = 0;
            }

            return new BuildInfoResult(
                    version,
                    pathVersion,
                    BsonReaderTool.getString(resultDoc, GIT_VERSION_FIELD, null),
                    BsonReaderTool.getString(resultDoc, SYS_INFO_FIELD, null),
                    BsonReaderTool.getString(resultDoc, LOADER_FLAGS_FIELD, null),
                    BsonReaderTool.getString(resultDoc, COMPILER_FLAGS_FIELD, null),
                    BsonReaderTool.getString(resultDoc, ALLOCATOR_FIELD, null),
                    BsonReaderTool.getString(resultDoc, JAVASCRIPT_ENGINE_FIELD, null),
                    bits,
                    BsonReaderTool.getBoolean(resultDoc, DEBUG_FIELD, false),
                    maxObjectSize
            );
        }

        private BsonDocument marshall() {
            BsonArray versionArray = null;
            String versionString = null;
            if (version != null) {
                versionArray = new BsonArray();
                versionArray.add(new BsonInt32(version.getMajor()));
                versionArray.add(new BsonInt32(version.getMinor()));
                versionArray.add(new BsonInt32(patchVersion));

                versionString = version.toString();
            }

            return new BsonDocumentBuilder()
                    .append(VERSION_FIELD, versionString)
                    .append(GIT_VERSION_FIELD, gitVersion)
                    .append(SYS_INFO_FIELD, sysInfo)
                    .append(LOADER_FLAGS_FIELD, loaderFlags)
                    .append(COMPILER_FLAGS_FIELD, compilerFlags)
                    .append(ALLOCATOR_FIELD, allocator)
                    .append(JAVASCRIPT_ENGINE_FIELD, javascriptEngine)
                    .append(VERSION_ARRAY_FIELD, versionArray)
                    .appendNumber(BITS_FIELD, bits)
                    .append(DEBUG_FIELD, debug)
                    .appendNumber(MAX_OBJECT_SIZE_FIELD, maxObjectSize)
                    .build();
        }

        public MongoVersion getVersion() {
            return version;
        }

        public String getGitVersion() {
            return gitVersion;
        }

        public String getSysInfo() {
            return sysInfo;
        }

        public String getLoaderFlags() {
            return loaderFlags;
        }

        public String getCompilerFlags() {
            return compilerFlags;
        }

        public String getAllocator() {
            return allocator;
        }

        public String getJavascriptEngine() {
            return javascriptEngine;
        }

        public int getBits() {
            return bits;
        }

        public boolean isDebug() {
            return debug;
        }

        public long getMaxObjectSize() {
            return maxObjectSize;
        }

        public int getParchVersion() {
            return patchVersion;
        }
    }

}
