package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.tools;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.bson.*;

/**
 *
 */
public class BsonValueComparator implements Comparator<BsonValue>, Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean considerFieldName;

    public BsonValueComparator(boolean considerFieldName) {
        this.considerFieldName = considerFieldName;
    }

    @Override
    public int compare(BsonValue v1, BsonValue v2) {
        int d;
        if (!v1.isNumber() || !v2.isNumber()) {
            d = v1.getBsonType().compareTo(v2.getBsonType());
            if (d != 0) {
                return d;
            }
        }
        switch (v1.getBsonType()) {

            case JAVASCRIPT:
                return compareJavascript(v1.asJavaScript(), v2.asJavaScript());
            case JAVASCRIPT_WITH_SCOPE:
                return compareJavascriptWithScope(v1.asJavaScriptWithScope(), v2.asJavaScriptWithScope());
            case REGULAR_EXPRESSION:
                return compareRegularExpression(v1.asRegularExpression(), v2.asRegularExpression());
            case SYMBOL:
                return compareSymbol(v1.asSymbol(), v2.asSymbol());
            case ARRAY:
                return compareArray(v1.asArray(), v2.asArray());
            case BINARY:
                return compareBinary(v1.asBinary(), v2.asBinary());
            case BOOLEAN:
                return v1.asBoolean().compareTo(v2.asBoolean());
            case DATE_TIME:
                return v1.asDateTime().compareTo(v2.asDateTime());
            case DB_POINTER:
                return compareDBPointer(v1.asDBPointer(), v2.asDBPointer());
            case DOCUMENT:
                return compareDocument(v1.asDocument(), v2.asDocument());
            case MAX_KEY:
            case MIN_KEY:
            case NULL:
            case UNDEFINED:
                return 0;
            case OBJECT_ID:
                return v1.asObjectId().compareTo(v2.asObjectId());
            case STRING:
                return v1.asString().compareTo(v2.asString());
            case TIMESTAMP:
                return v1.asTimestamp().compareTo(v2.asTimestamp());

            case INT32:
                switch (v2.getBsonType()) {
                    case INT32:
                        return v1.asInt32().compareTo(v2.asInt32());
                    case INT64:
                        return Longs.compare(v1.asInt32().intValue(), v2.asInt64().longValue());
                    case DOUBLE:
                        return Doubles.compare(v1.asInt32().intValue(), v2.asDouble().doubleValue());
                }
                throw new AssertionError();
            case INT64:
                switch (v2.getBsonType()) {
                    case INT32:
                        return Longs.compare(v1.asInt64().longValue(), v2.asInt32().intValue());
                    case INT64:
                        return v1.asInt64().compareTo(v2.asInt64());
                    case DOUBLE:
                        return Doubles.compare(v1.asInt64().longValue(), v2.asDouble().doubleValue());
                }
                throw new AssertionError();
            case DOUBLE:
                switch (v2.getBsonType()) {
                    case INT32:
                        return Doubles.compare(v1.asDouble().doubleValue(), v2.asInt32().intValue());
                    case INT64:
                        return Doubles.compare(v1.asDouble().doubleValue(), v2.asInt64().longValue());
                    case DOUBLE:
                        return v1.asDouble().compareTo(v2.asDouble());
                }
                throw new AssertionError();
            default:
                throw new AssertionError(getClass() + " does not define how to compare values of type " + v1.getBsonType());
        }
    }

    public int compareDocument(BsonDocument o1, BsonDocument o2) {
        Iterator<Entry<String, BsonValue>> it1 = o1.entrySet().iterator();
        Iterator<Entry<String, BsonValue>> it2 = o2.entrySet().iterator();
        while (true) {
            if (!it1.hasNext()) {
                return it2.hasNext() ? -1 : 0;
            }
            if (!it2.hasNext()) {
                return 1;
            }
            Entry<String, BsonValue> e1 = it1.next();
            Entry<String, BsonValue> e2 = it2.next();

            int d = compareEntry(e1, e2);
            if (d != 0) {
                return d;
            }
        }
    }

    private int compareEntry(Map.Entry<String, BsonValue> e1, Map.Entry<String, BsonValue> e2) {
        BsonValue v1 = e1.getValue();
        BsonValue v2 = e2.getValue();
        int d;
        if (!v1.isNumber() || !v2.isNumber()) {
            d = v1.getBsonType().compareTo(v2.getBsonType());
            if (d != 0) {
                return d;
            }
        }
        if (considerFieldName) {
            d = e1.getKey().compareTo(e2.getKey());
            if (d != 0) {
                return d;
            }
        }
        return compare(v1, v2);
    }

    private int compareArray(BsonArray a1, BsonArray a2) {
        Iterator<BsonValue> it1 = a1.iterator();
        Iterator<BsonValue> it2 = a2.iterator();
        while (true) {
            if (!it1.hasNext()) {
                return it2.hasNext() ? -1 : 0;
            }
            if (!it2.hasNext()) {
                return 1;
            }
            int d = compare(it1.next(), it2.next());
            if (d != 0) {
                return 0;
            }
        }
    }

    private int compareBinary(BsonBinary b1, BsonBinary b2) {
        byte[] d1 = b1.getData();
        byte[] d2 = b2.getData();
        int diff = d1.length - d2.length;
        if (diff != 0) {
            return diff;
        }
        for (int i = 0; i < d1.length; i++) {
            diff = d1[i] - d2[i];
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }

    private int compareDBPointer(BsonDbPointer p1, BsonDbPointer p2) {
        String s1 = p1.getNamespace();
        String s2 = p2.getNamespace();
        int diff = s1.compareTo(s2);
        if (diff != 0) {
            return diff;
        }
        return p1.getId().compareTo(p2.getId());
    }

    private int compareJavascriptWithScope(BsonJavaScriptWithScope j1, BsonJavaScriptWithScope j2) {
        int diff = j1.getCode().compareTo(j2.getCode());
        if (diff != 0) {
            return diff;
        }
        return compareDocument(j1.getScope(), j2.getScope());
    }

    private int compareJavascript(BsonJavaScript j1, BsonJavaScript j2) {
        return j1.getCode().compareTo(j2.getCode());
    }

    private int compareRegularExpression(BsonRegularExpression r1, BsonRegularExpression r2) {
        int diff = r1.getPattern().compareTo(r2.getPattern());
        if (diff != 0) {
            return diff;
        }
        return r1.getPattern().compareTo(r2.getPattern());
    }

    private int compareSymbol(BsonSymbol s1, BsonSymbol s2) {
        return s1.getSymbol().compareTo(s2.getSymbol());
    }
}
