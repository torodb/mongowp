package com.eightkdata.mongowp.server.api.oplog;

/**
 *
 * @param <Result> the result of the visit methods.
 * @param <Arg> An argument the visit method can take. If it is not needed, use {@link Void}
 */
public interface OplogOperationVisitor<Result, Arg> {

    public Result visit(DbCmdOplogOperation op, Arg arg);

    public Result visit(DbOplogOperation op, Arg arg);

    public Result visit(DeleteOplogOperation op, Arg arg);

    public Result visit(InsertOplogOperation op, Arg arg);

    public Result visit(NoopOplogOperation op, Arg arg);

    public Result visit(UpdateOplogOperation op, Arg arg);

}
