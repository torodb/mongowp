
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.admin;

/**
 *
 */
public class DeleteIndexesCommand extends DropIndexesCommand {

    private final static String COMMAND_NAME = "deleteIndexes";
    
    public static final DeleteIndexesCommand INSTANCE = new DeleteIndexesCommand();

    private DeleteIndexesCommand() {
        super(COMMAND_NAME);
    }

}
