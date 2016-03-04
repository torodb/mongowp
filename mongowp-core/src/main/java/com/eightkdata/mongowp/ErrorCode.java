
package com.eightkdata.mongowp;

import java.util.ResourceBundle;

/**
 *
 */
public enum ErrorCode {
    OK(0, "error.code.ok"),
    INTERNAL_ERROR(1, "error.code.internal.error"),
    BAD_VALUE(2, "error.code.bad.value"),
    OBSOLETE_DUPLICATE_KEY(3, "error.code.obsolete.duplicate.key"),
    NO_SUCH_KEY(4, "error.code.no.such.key"),
    GRAPH_CONTAINS_CYCLE(5, "error.code.graph.contains.cycle"),
    HOST_UNREACHABLE(6, "error.code.host.unreachable"),
    HOST_NOT_FOUND(7, "error.code.host.not.found"),
    UNKNOWN_ERROR(8, "error.code.unknown.error"),
    FAILED_TO_PARSE(9, "error.code.failed.to.parse"),
    CANNOT_MUTATE_OBJECT(10, "error.code.cannot.mutate.object"),
    USER_NOT_FOUND(11, "error.code.user.not.found"),
    UNSUPPORTED_FORMAT(12, "error.code.unsupported.format"),
    UNAUTHORIZED(13, "error.code.unauthorized"),
    TYPE_MISMATCH(14, "error.code.type.mismatch"),
    OVERFLOW(15, "error.code.overflow"),
    INVALID_LENGHT(16, "error.code.invalid.lenght"),
    PROTOCOL_ERROR(17, "error.code.protocol.error"),
    AUTHENTICATION_FAILED(18, "error.code.authentication.failed"),
    CANNOT_REUSE_OBJECT(19, "error.code.cannot.reuse.object"),
    ILLEGAL_OPERATION(20, "error.code.illegal.operation"),
    EMPTY_ARRAY_OPERATION(21, "error.code.empty.array.operation"),
    INVALID_BSON(22, "error.code.invalid.bson"),
    ALREADY_INITIALIZED(23, "error.code.already.initialized"),
    LOCK_TIMEOUT(24, "error.code.lock.timeout"),
    REMOTE_VALIDATION_ERROR(25, "error.code.remote.validation.error"),
    NAMESPACE_NOT_FOUND(26, "error.code.namespace.not.found"),
    INDEX_NOT_FOUND(27, "error.code.index.not.found"),
    PATH_NOT_VIABLE(28, "error.code.path.not.viable"),
    NON_EXISTENT_PATH(29, "error.code.not.existent.path"),
    INVALID_PATH(30, "error.code.invalid.path"),
    ROLE_NOT_FOUND(31, "error.code.role.not.found"),
    ROLES_NOT_RELATED(32, "error.code.roles.not.related"),
    PRIVILEGE_NOT_FOUND(33, "error.code.privilege.not.found"),
    CANNOT_BACKFILL_ARRAY(34, "error.code.cannot.backfill.array"),
    USER_MODIFICATION_FAILED(35, "error.code.user.modification.failed"),
    REMOTE_CHANGE_DETECTED(36, "error.code.remote.change.detected"),
    FILE_RENAME_FAILED(37, "error.code.file.rename.failed"),
    FILE_NOT_OPEN(38, "error.code.file.not.open"),
    FILE_STREAM_FAILED(39, "error.code.file.stream.failed"),
    CONFLICTING_UPDATE_OPERATORS(40, "error.code.conflicting.update.operators"),
    FILE_ALREADY_OPEN(41, "error.code.file.already.open"),
    LOG_WRITE_FAILED(42, "error.code.log.write.failed"),
    CURSOR_NOT_FOUND(43, "error.code.cursor.not.found"),
    USER_DATA_INCONSISTENT(45, "error.code.user.data.inconsistent"),
    LOCK_BUSY(45, "error.code.lock.busy"),
    NO_MATCHING_DOCUMENT(47, "error.code.no.matching.document"),
    NAMESPACE_EXISTS(48, "error.code.namespace.exists"),
    INVALID_ROLE_MODIFICATION(49, "error.code.invalid.role.modification"),
    EXCEEDED_TIME_LIMIT(50, "error.code.exceeded.time.limit"),
    MANUAL_INTERVENTION_REQUIRED(51, "error.code.manual.intervention.required"),
    DOLLAR_PREFIXED_FIELD_NAME(52, "error.code.dollar.prefixed.field.name"),
    INVALID_FIELD(53, "error.code.invalid.field"),
    NOT_SINGLE_VALUE_FIELD(54, "error.code.not.single.value.field"),
    INVALID_DBREF(55, "error.code.invalid.dbref"),
    EMPTY_FIELD_NAME(56, "error.code.empty.field.name"),
    DOTTED_FIELD_NAME(57, "error.code.dotted.field.name"),
    ROLE_MODIFICATION_FAILED(58, "error.code.role.modification.failed"),
    COMMAND_NOT_FOUND(59, "error.code.command.not.found"),
    DATABASE_NOT_FOUND(60, "error.code.database.not.found"),
    SHARD_KEY_NOT_FOUND(61, "error.code.shard.key.not.found"),
    OPLOG_OPERATION_UNSUPPORTED(62, "error.code.oplog.operation.unsupported"),
    STALE_SHARD_VERSION(63, "error.code.stale.shard.version"),
    WRITE_CONCERN_FAILED(64, "error.code.write.concern.failed"),
    MULTIPLE_ERRORS_OCCURRED(65, "error.code.multiple.errors.occurred"),
    IMMUTABLE_FIELD(66, "error.code.immutable.field"),
    CANNOT_CREATE_INDEX(67, "error.code.cannot.create.index"),
    INDEX_ALREADY_EXISTS(68, "error.code.index.already.exists"),
    AUTH_SCHEMA_INCOMPATIBLE(69, "error.code.auth.schema.incompatible"),
    SHARD_NOT_FOUND(70, "error.code.shard.not.found"),
    REPLICA_SET_NOT_FOUND(71, "error.code.replica.set.not.found"),
    INVALID_OPTIONS(72, "error.code.invalid.options"),
    INVALID_NAMESPACE(73, "error.code.invalid.namespace"),
    NODE_NOT_FOUND(74, "error.code.node.not.found"),
    WRITE_CONCERN_LEGACY_OK(75, "error.code.write.concern.legacy.ok"),
    NOT_REPLICATION_ENABLED(76, "error.code.not.replication.enabled"),
    OPERATION_INCOMPLETE(77, "error.code.operation.incomplete"),
    COMMAND_RESULT_SCHEMA_VIOLATION(78, "error.code.command.result.schema.violation"),
    UNKNOWN_REPL_WRITE_CONCERN(79, "error.code.unknown.repl.write.concern"),
    ROLE_DATA_INCONSISTENT(80, "error.code.role.data.inconsistent"),
    NO_WHERE_PARSE_CONTEXT(81, "error.code.no.where.parse.context"),
    NO_PROGRESS_MADE(82, "error.code.no.progress.made"),
    REMOTE_RESULTS_UNAVAILABLE(83, "error.code.remote.results.unavailable"),
    DUPLICATE_KEY_VALUE(84, "error.code.duplciate.key.value"),
    INDEX_OPTIONS_CONFLICT(85, "error.code.index.options.conflict"),
    INDEX_KEY_SPECS_CONFLICT(86, "error.code.index.key.specs.conflict"),
    CANNOT_SPLIT(87, "error.code.cannot.split"),
    SPLIT_FAILED(88, "error.code.split.failed"),
    NETWORK_TIMEOUT(89, "error.code.network.timeout"),
    CALLBACK_CANCELED(90, "error.code.callback.cancelled"),
    SHUTDOWN_IN_PROGRESS(91, "error.code.shutdown.in.progress"),
    SECONDARY_AHEAD_OF_PRIMARY(92, "error.code.secondary.ahead.of.primary"),
    INVALID_REPLICA_SET_CONFIG(93, "error.code.invalid.replica.set.config"),
    NOT_YET_INITIALIZED(94, "error.code.not.yet.initialized"),
    NOT_SECONDARY(95, "error.code.not.secondary"),
    OPERATION_FAILED(96, "error.code.operation.failed"),
    NOT_PROJECTION_FOUND(97, "error.code.not.projection.found"),
    DB_PATH_IN_USE(98, "error.code.db.path.in.use"),
    WRITE_CONCERN_NOT_DEFINED(99, "error.code.write.concern.not.defined"),
    CANNOT_SATISFY_WRITE_CONCERN(100, "error.code.cannot.satisfy.write.concern"),
    OUTDATED_CLIENT(101, "error.code.outdated.client"),
    INCOMPATIBLE_AUDIT_METADATA(102, "error.code.incompatible.audit.metadata"),
    NEW_REPLICA_SET_CONFIGURATION_INCOMPATIBLE(103, "error.code.new.replica.set.configuration.incompatible"),
    NODE_NOT_ELECTABLE(104, "error.code.node.not.electable"),
    INCOMPATIBLE_SHARDING_METADATA(105, "error.code.incompatible.sharding.metadata"),
    DISTRIBUTED_CLOCK_SKEWED(106, "error.code.distributed.clock.skewed"),
    LOCK_FAILED(107, "error.code.lock.failed"),
    INCONSISTENT_REPLICA_SET_NAMES(108, "error.code.inconsistent.replica.set.names"),
    CONFIGURATION_IN_PROGRESS(109, "error.code.configuration.in.progress"),
    CANNOT_INITIALIZE_NODE_WITH_DATA(110, "error.code.cannot.initialize.node.with.data"),
    NOT_EXACT_VALUE_FIELD(111, "error.code.not.exact.value.field"),
    WRITE_CONFLICT(112, "error.code.write.conflict"),
    INITIAL_SYNC_FAILURE(113, "error.code.initial.sync.failure"),
    INITIAL_SYNC_OPLOG_SOURCE_MISSING(114, "error.code.initial.sync.oplog.source.missing"),
    COMMAND_NOT_SUPPORTED(115, "error.code.command.not.supported"),
    DOC_TOO_LARGE_FOR_CAPPED(116, "error.code.doc.too.large.for.capped"),
    CONFLICTING_OPERATION_IN_PROGRESS(117, "error.code.conflicting.operation.in.progress"),
    NAMESPACE_NOT_SHARDED(118, "error.code.namespace.not.sharded"),
    INVALID_SYNC_SOURCE(119, "error.code.invalid.sync.source"),
    OPLOG_START_MISSING(120, "error.code.oplog.start.missing"),
    DOCUMENT_VALIDATION_FAILURE(121, "error.code.document.validation.failure"),
    READ_AFTER_OPTIME_TIMEOUT(122, "error.code.read.after.optime.timeout"),
    NOT_A_REPLICA_SET(123, "error.code.not.a.replica.set"),
    INCOMPATIBLE_ELECTION_PROTOCOL(124, "error.code.incompatible.election.protocol"),
    COMMAND_FAILED(125, "error.code.command.failed"),
    RPC_PROTOCOL_NEGOTIATION_FAILED(126, "error.code.rpc.protocol.negotiation.failed"),
    NOT_MASTER(10107, "error.code.not.master"),
    DUPLICATE_KEY(11000, "error.code.duplicate.key"),
    INTERRUPTED_AT_SHUTDOWN(11600, "error.code.interrupted.at.shutdown"),
    INTERRUPTED(11601, "error.code.interrupted"),
    BACKGROUND_OPERATION_IN_PROGRESS_FOR_DATABASE(12586, "error.code.background.operation.in.progress.for.database"),
    BACKGROUND_OPERATION_IN_PROGRESS_FOR_NAMESPACE(12587, "error.code.background.operation.in.progress.for.namespace"),
    DATABASE_DIFFER_CASE(1313297, "error.code.database.differ.case"),
    SHARD_KEY_TOO_BIG(13334, "error.code.shard.key.too.big"),
    SEND_STALE_CONFIG(13388, "error.code.send.stale.config"),
    NOT_MASTER_NO_SLAVE_OK_CODE(13435, "error.code.not.master.no.slave.ok.code"),
    NOT_MASTER_OR_SECONDARY_CODE(13436, "error.code.not.master.or.secondary.code"),
    OUT_OF_DISK_SPACE(14031, "error.code.out.of.disk.space"),
    KEY_TOO_LONG(117280, "error.code.key.too.long"),
    // TODO: the following are not true Mongo error codes.
    WRONG_FIELD_TYPE(100009, "error.code.wrong.field.name"),
    MUST_RUN_ON_ADMIN(1000013, "error.code.must.run.on.admin"),
    UNIMPLEMENTED_FLAG(1000003, "error.code.unimplemented.flag"),
    INVALID_GET_LOG_LOG(1000004, "error.code.invalid.get.log.log"),
    MAX_ERROR(Integer.MAX_VALUE, "error.code.max.error");
    private final int errorCode;
    private final String errorMessageToken;

    ErrorCode(int errorCode, String errorMessageToken) {
        this.errorCode = errorCode;
        this.errorMessageToken = errorMessageToken;
    }

    public int getErrorCode() {
        return errorCode;
    }

    /**
     *
     * @param id
     * @return
     * @throws IllegalArgumentException if there is no error code with the given id
     */
    public static ErrorCode fromErrorCode(int id) throws
            IllegalArgumentException {
        for (ErrorCode error : ErrorCode.values()) {
            if (error.getErrorCode() == id) {
                return error;
            }
        }
        throw new IllegalArgumentException("There is no error with the error code '" +
                id + "'");
    }

    public String getErrorMessage() {
        return ResourceBundle.getBundle("com/eightkdata/mongowp/Bundle").getString(errorMessageToken);
    }

}
