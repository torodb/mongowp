
package com.eightkdata.mongowp.mongoserver.api.commands;

import com.eightkdata.mongowp.mongoserver.api.callback.MessageReplier;

/**
 *
 */
public interface Reply {

    public void reply(MessageReplier replier);
}
