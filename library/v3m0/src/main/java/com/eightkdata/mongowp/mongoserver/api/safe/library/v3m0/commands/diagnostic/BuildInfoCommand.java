
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic;

import com.eightkdata.mongowp.MongoVersion;
import com.eightkdata.mongowp.bson.BsonArray;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.utils.DefaultBsonValues;
import com.eightkdata.mongowp.exceptions.*;
import com.eightkdata.mongowp.fields.ArrayField;
import com.eightkdata.mongowp.fields.BooleanField;
import com.eightkdata.mongowp.fields.NumberField;
import com.eightkdata.mongowp.fields.StringField;
import com.eightkdata.mongowp.server.api.MarshalException;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.diagnostic.BuildInfoCommand.BuildInfoResult;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools.EmptyCommandArgumentMarshaller;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonArrayBuilder;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;

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

        private static final StringField VERSION_FIELD = new StringField("version");
        private static final StringField GIT_VERSION_FIELD = new StringField("gitVersion");
        private static final StringField SYS_INFO_FIELD = new StringField("sysInfo");
        private static final StringField LOADER_FLAGS_FIELD = new StringField("loaderFlags");
        private static final StringField COMPILER_FLAGS_FIELD = new StringField("compilerFlags");
        private static final StringField ALLOCATOR_FIELD = new StringField("allocator");
        private static final StringField JAVASCRIPT_ENGINE_FIELD = new StringField("javascriptEngine");
        private static final ArrayField VERSION_ARRAY_FIELD = new ArrayField("versionArrayField");
        private static final NumberField<?> BITS_FIELD = new NumberField<>("bits");
        private static final BooleanField DEBUG_FIELD = new BooleanField("debug");
        private static final NumberField<?> MAX_OBJECT_SIZE_FIELD = new NumberField<>("maxObjectSize");

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
            BsonArray versionArray = DefaultBsonValues.EMPTY_ARRAY;
            String versionString = null;
            if (version != null) {
                BsonArrayBuilder arrBuilder = new BsonArrayBuilder(3);
                arrBuilder.add(version.getMajor());
                arrBuilder.add(version.getMinor());
                arrBuilder.add(patchVersion);

                versionArray = arrBuilder.build();

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
