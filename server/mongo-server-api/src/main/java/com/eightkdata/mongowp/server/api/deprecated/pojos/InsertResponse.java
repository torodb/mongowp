
package com.eightkdata.mongowp.server.api.deprecated.pojos;

import com.eightkdata.mongowp.server.callback.WriteOpResult;
import com.eightkdata.mongowp.server.callback.MessageReplier;
import com.google.common.collect.ImmutableList;
import javax.annotation.concurrent.Immutable;

/**
 *
 */
@Immutable
public abstract class InsertResponse {

    private final boolean ok;
    private final int n;
    private final ImmutableList<WriteError> writeErrors;
    private final ImmutableList<WriteConcernError> writeConcernErrors;

    public InsertResponse(boolean ok, int n, ImmutableList<WriteError> writeErrors, ImmutableList<WriteConcernError> writeConcernErrors) {
        this.ok = ok;
        this.n = n;
        this.writeErrors = writeErrors;
        this.writeConcernErrors = writeConcernErrors;
    }

    public boolean isOk() {
        return ok;
    }

    public int getN() {
        return n;
    }

    public ImmutableList<WriteError> getWriteErrors() {
        return writeErrors;
    }

    public ImmutableList<WriteConcernError> getWriteConcernErrors() {
        return writeConcernErrors;
    }
    
    public abstract WriteOpResult renderizeAsLastError();
    
    public abstract void renderize(MessageReplier replier);

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.ok ? 1 : 0);
        hash = 79 * hash + this.n;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InsertResponse other = (InsertResponse) obj;
        if (this.ok != other.ok) {
            return false;
        }
        if (this.n != other.n) {
            return false;
        }
        if (this.writeErrors != other.writeErrors &&
                (this.writeErrors == null ||
                !this.writeErrors.equals(other.writeErrors))) {
            return false;
        }
        return !(this.writeConcernErrors != other.writeConcernErrors &&
                (this.writeConcernErrors == null ||
                !this.writeConcernErrors.equals(other.writeConcernErrors)));
    }
    
    @Immutable
    public static class WriteError {
        private final int index;
        private final int code;
        private final String errmsg;

        public WriteError(int index, int code, String errmsg) {
            this.index = index;
            this.code = code;
            this.errmsg = errmsg;
        }

        public int getIndex() {
            return index;
        }

        public int getCode() {
            return code;
        }

        public String getErrmsg() {
            return errmsg;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 19 * hash + this.index;
            hash = 19 * hash + this.code;
            hash
                    = 19 * hash +
                    (this.errmsg != null ? this.errmsg.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WriteError other = (WriteError) obj;
            if (this.index != other.index) {
                return false;
            }
            if (this.code != other.code) {
                return false;
            }
            return !((this.errmsg == null) ? (other.errmsg != null)
                    : !this.errmsg.equals(other.errmsg));
        }
    }
    
    @Immutable
    public static class WriteConcernError {
        private final int code;
        private final String errmsg;

        public WriteConcernError(int code, String errmsg) {
            this.code = code;
            this.errmsg = errmsg;
        }

        public int getCode() {
            return code;
        }

        public String getErrmsg() {
            return errmsg;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + this.code;
            hash
                    = 79 * hash +
                    (this.errmsg != null ? this.errmsg.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final WriteConcernError other = (WriteConcernError) obj;
            if (this.code != other.code) {
                return false;
            }
            return !((this.errmsg == null) ? (other.errmsg != null)
                    : !this.errmsg.equals(other.errmsg));
        }
    }
}
