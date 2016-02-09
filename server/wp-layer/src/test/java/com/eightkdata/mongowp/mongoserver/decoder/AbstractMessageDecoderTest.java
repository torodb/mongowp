package com.eightkdata.mongowp.mongoserver.decoder;

import com.eightkdata.mongowp.server.decoder.AbstractMessageDecoder;
import com.eightkdata.mongowp.exceptions.InvalidNamespaceException;
import com.eightkdata.mongowp.exceptions.MongoException;
import com.eightkdata.mongowp.messages.request.RequestBaseMessage;
import com.eightkdata.mongowp.messages.request.RequestMessage;
import io.netty.buffer.ByteBuf;
import org.junit.Test;

/**
 *
 * @author gortiz
 */
public class AbstractMessageDecoderTest {

    private MockedMessageDecoder decoder = new MockedMessageDecoder();

    @Test
    public void testGetDatabase_standard() throws Exception {
        assert decoder.testGetDatabase("database.collection").equals("database");
    }

    @Test
    public void testGetDatabase_noDot() throws Exception {
        assert decoder.testGetDatabase("database").equals("database");
    }

    @Test(expected = InvalidNamespaceException.class)
    public void testGetDatabase_startDot() throws Exception {
        decoder.testGetDatabase(".database");
    }

    @Test(expected = InvalidNamespaceException.class)
    public void testGetDatabase_endsDot() throws Exception {
        decoder.testGetDatabase("database.");
    }

    @Test
    public void testGetDatabase_severalDot() throws Exception {
        String recived = decoder.testGetDatabase("database.collection.afterdot");
        assert recived.equals("database") :
                "Expected 'database' but recived'" + recived + "'";
    }

    @Test
    public void testGetCollection_standard() throws Exception {
        String recived = decoder.testGetCollection("database.collection");
        assert recived.equals("collection") : "Expected 'collection', recived '"
                + recived + "'";
    }

    @Test(expected = InvalidNamespaceException.class)
    public void testGetCollection_noDot() throws Exception {
        decoder.testGetCollection("database");
    }

    @Test(expected = InvalidNamespaceException.class)
    public void testGetCollection_startDot() throws Exception {
        decoder.testGetCollection(".database");
    }

    @Test(expected = InvalidNamespaceException.class)
    public void testGetCollection_endsDot() throws Exception {
        decoder.testGetCollection("database.");
    }

    @Test
    public void testGetCollection_severalDot() throws Exception {
        String recived = decoder.testGetCollection("database.collection.afterdot");
        assert recived.equals("collection.afterdot") :
                "Expected 'collection.afterdot' but recived'" + recived + "'";
    }
    public static class MockedMessageDecoder extends AbstractMessageDecoder {

        @Override
        public RequestMessage decode(ByteBuf buffer, RequestBaseMessage requestBaseMessage)
                throws MongoException, InvalidNamespaceException {
            return null;
        }

        private String testGetDatabase(String namespace) throws InvalidNamespaceException {
            return super.getDatabase(namespace);
        }

        private String testGetCollection(String namespace) throws InvalidNamespaceException {
            return super.getCollection(namespace);
        }
    }
}
