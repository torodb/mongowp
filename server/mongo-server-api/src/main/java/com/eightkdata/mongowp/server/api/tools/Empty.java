
package com.eightkdata.mongowp.server.api.tools;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class is like {@link Void} but it has one instance.
 * <p/>
 * This can be useful when you want an implementation of a generic class that
 * returns non null instances of the generic type. For instance, commands that
 * do not need argument or do not return information, can use this class.
 */
public class Empty {

    private Empty() {
    }

    public static Empty getInstance() {
        return EmptyHolder.INSTANCE;
    }

    private static class EmptyHolder {
        private static final Empty INSTANCE = new Empty();
    }

    @SuppressFBWarnings(value = "UPM_UNCALLED_PRIVATE_METHOD")
    private Object readResolve()  {
        return Empty.getInstance();
    }
 }
