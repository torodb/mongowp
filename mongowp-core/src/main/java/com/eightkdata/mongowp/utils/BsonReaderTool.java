
package com.eightkdata.mongowp.utils;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument.Entry;
import com.eightkdata.mongowp.bson.*;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.*;
import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;
import com.google.common.primitives.UnsignedInteger;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.threeten.bp.Instant;

/**
 *
 */
public class BsonReaderTool {

    private BsonReaderTool() {}

    public static String toStringBsonType(BsonType type) {
        return type.toString().toLowerCase(Locale.ROOT);
    }

    public static boolean containsField(BsonDocument doc, BsonField<?,?> field) {
        return doc.containsKey(field.getFieldName());
    }

    /**
     * Confirms that the given bson document only contains fields whose names given.
     *
     * @param objectName the container's name, that will be included in the error messages
     * @param bson the document whose fields are going to be checked
     * @param fields the fields we are going to accept
     * @throws BadValueException if an unexpected field is found
     */
    public static void checkOnlyHasFields(
            String objectName,
            BsonDocument bson,
            Set<String> fields) throws BadValueException {
        for (Entry<?> entry : bson) {
            if (!fields.contains(entry.getKey())) {
                throw new BadValueException(
                        "Unexpected field " + entry.getKey() + " in " + objectName
                );
            }
        }
    }

    /**
     * Confirms that the given bson object only contains fields whose names given.
     *
     * @param objectName the container's name, that will be included in the error messages
     * @param bson the object whose fields are going to be checked
     * @param fields the fields we are going to accept
     * @throws BadValueException if an unexpected field is found
     */
    public static void checkOnlyHasFields(
            String objectName, 
            BsonDocument bson,
            String... fields) throws BadValueException {
        checkOnlyHasFields(objectName, bson, Sets.newHashSet(fields));
        
    }
    
    @Nonnull
    public static BsonValue getValue(BsonDocument doc, String fieldId) throws NoSuchKeyException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            throw new NoSuchKeyException(fieldId);
        }
        return object;
    }

    @Nonnull
    public static BsonValue getValue(BsonDocument doc, BsonField field) throws NoSuchKeyException {
        return getValue(doc, field.getFieldName());
    }


    @Nonnull
    public static BsonDocument getDocument(BsonDocument doc, DocField field) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getDocument(doc, field.getFieldName());
    }

    @Nullable
    public static BsonDocument getDocument(BsonDocument doc, DocField field, BsonDocument defaultValue) throws TypesMismatchException {
        return getDocument(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static BsonDocument getDocument(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isDocument()) {
            throw new TypesMismatchException(fieldId, BsonType.DOCUMENT, object.getType());
        }
        return object.asDocument();
    }

    @Nullable
    public static BsonDocument getDocument(BsonDocument doc, String fieldId, BsonDocument defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isDocument()) {
            throw new TypesMismatchException(fieldId, BsonType.DOCUMENT, object.getType());
        }
        return object.asDocument();
    }

    @Nonnull
    public static BsonArray getArray(BsonDocument doc, ArrayField field) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getArray(doc, field.getFieldName());
    }

    @Nullable
    public static BsonArray getArray(BsonDocument doc, ArrayField field, BsonArray defaultValue) throws TypesMismatchException {
        return getArray(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static BsonArray getArray(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isArray()) {
            throw new TypesMismatchException(fieldId, BsonType.ARRAY, object.getType());
        }
        return object.asArray();
    }

    @Nullable
    public static BsonArray getArray(BsonDocument doc, String fieldId, BsonArray defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isArray()) {
            throw new TypesMismatchException(fieldId, BsonType.ARRAY, object.getType());
        }
        return object.asArray();
    }

    @Nonnull
    public static BsonNumber getNumeric(BsonDocument doc, NumberField<?> field) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getNumeric(doc, field.getFieldName());
    }

    @Nullable
    public static BsonNumber getNumeric(BsonDocument doc, NumberField<?> field, BsonNumber defaultValue) throws TypesMismatchException {
        return getNumeric(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static BsonNumber getNumeric(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isNumber()) {
            String foundType = object.getType().toString().toLowerCase(Locale.ROOT);
            throw new TypesMismatchException(
                    fieldId, "numeric", object.getType(),
                    fieldId + " field has non-numeric type " + foundType
            );
        }
        return object.asNumber();
    }

    @Nullable
    public static BsonNumber getNumeric(BsonDocument doc, String fieldId, BsonNumber defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isNumber()) {
            String foundType = object.getType().toString().toLowerCase(Locale.ROOT);
            throw new TypesMismatchException(
                    fieldId, "numeric", object.getType(),
                    fieldId + " field has non-numeric type " + foundType
            );
        }
        return object.asNumber();
    }

    @Nonnull
    public static Instant getInstant(BsonDocument doc, DateTimeField field) throws TypesMismatchException, NoSuchKeyException {
        return getInstant(doc, field.getFieldName());
    }

    @Nullable
    public static Instant getInstant(BsonDocument doc, DateTimeField field, Instant defaultValue) throws TypesMismatchException {
        return getInstant(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static Instant getInstant(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (object.isDateTime()) {
            return object.asDateTime().getValue();
        }
//        TODO: Check if we to remove that is correct
//        if (object.isTimestamp()) {
//            return Instant.ofEpochSecond(object.asTimestamp().getSecondsSinceEpoch()); //TODO: Check this implementation
//        }
        String foundType = object.getType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nullable
    public static Instant getInstant(BsonDocument doc, String fieldId, Instant defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (object.isDateTime()) {
            return object.asDateTime().getValue();
        }
//         TODO: Check if we to remove that is correct
//        if (object.isTimestamp()) {
//            return Instant.ofEpochSecond(object.asTimestamp().getSecondsSinceEpoch()); //TODO: Check this implementation
//        }
        String foundType = object.getType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nonnull
    public static OpTime getOpTime(BsonDocument doc, TimestampField field) throws TypesMismatchException, NoSuchKeyException {
        return getOpTime(doc, field.getFieldName());
    }

    @Nullable
    public static OpTime getOpTime(BsonDocument doc, TimestampField field, OpTime defaultValue) throws TypesMismatchException {
        return getOpTime(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static OpTime getOpTime(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
//        TODO: Check if to remove that is correct
//        if (object.isDateTime()) {
//            BsonDateTime asDateTime = object.asDateTime();
//            UnsignedInteger secs = UnsignedInteger.valueOf(asDateTime.getMillisFromUnix() / 1000);
//            UnsignedInteger term = UnsignedInteger.fromIntBits((int) (asDateTime.getMillisFromUnix() % 1000));
//            return new OpTime(secs, term);
//        }
        if (object.isTimestamp()) {
            BsonTimestamp asTimestamp = object.asTimestamp();
            UnsignedInteger secs = UnsignedInteger.fromIntBits(asTimestamp.getSecondsSinceEpoch());
            UnsignedInteger term = UnsignedInteger.fromIntBits(asTimestamp.getOrdinal());
            return new OpTime(secs, term);
        }
        String foundType = object.getType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nullable
    public static OpTime getOpTime(BsonDocument doc, String fieldId, OpTime defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (object.isDateTime()) {
            BsonDateTime asDateTime = object.asDateTime();
            UnsignedInteger secs = UnsignedInteger.valueOf(asDateTime.getMillisFromUnix() / 1000);
            UnsignedInteger term = UnsignedInteger.fromIntBits((int) (asDateTime.getMillisFromUnix() % 1000));
            return new OpTime(secs, term);
        }
        if (object.isTimestamp()) {
            BsonTimestamp asTimestamp = object.asTimestamp();
            UnsignedInteger secs = UnsignedInteger.fromIntBits(asTimestamp.getSecondsSinceEpoch());
            UnsignedInteger term = UnsignedInteger.fromIntBits(asTimestamp.getOrdinal());
            return new OpTime(secs, term);
        }
        String foundType = object.getType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nonnull
    public static int getInteger(BsonDocument doc, IntField field) throws TypesMismatchException, NoSuchKeyException {
        return getInteger(doc, field.getFieldName());
    }

    public static int getInteger(BsonDocument doc, IntField field, int defaultValue) throws TypesMismatchException {
        return getInteger(doc, field.getFieldName(), defaultValue);
    }

    public static int getInteger(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isInt32()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected integer type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asInt32().intValue();
    }

    public static int getInteger(BsonDocument doc, String fieldId, int defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isInt32()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected integer type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asInt32().intValue();
    }

    @Nonnull
    public static long getLong(BsonDocument doc, LongField field) throws TypesMismatchException, NoSuchKeyException {
        return getLong(doc, field.getFieldName());
    }

    public static long getLong(BsonDocument doc, LongField field, long defaultValue) throws TypesMismatchException {
        return getLong(doc, field.getFieldName(), defaultValue);
    }
    
    public static long getLong(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isNumber()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected long type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asNumber().longValue();
    }
    
    public static long getLong(BsonDocument doc, String fieldId, long defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isNumber()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected long type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asNumber().longValue();
    }

    @Nonnull
    public static double getDouble(BsonDocument doc, DoubleField field) throws TypesMismatchException, NoSuchKeyException {
        return getDouble(doc, field.getFieldName());
    }

    public static double getDouble(BsonDocument doc, DoubleField field, double defaultValue) throws TypesMismatchException {
        return getDouble(doc, field.getFieldName(), defaultValue);
    }

    public static double getDouble(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isDouble()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected double type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asDouble().doubleValue();
    }

    public static double getDouble(BsonDocument doc, String fieldId, double defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isDouble()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected double type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asDouble().doubleValue();
    }

    @Nonnull
    public static boolean getBoolean(BsonDocument doc, BooleanField field) throws TypesMismatchException, NoSuchKeyException {
        return getBoolean(doc, field.getFieldName());
    }

    public static boolean getBoolean(BsonDocument doc, BooleanField field, boolean defaultValue) throws TypesMismatchException {
        return getBoolean(doc, field.getFieldName(), defaultValue);
    }

    public static boolean getBooleanOrNumeric(
            BsonDocument doc,
            String fieldId,
            boolean defaultValue) throws TypesMismatchException{
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (object.isNumber()) {
            return object.asNumber().intValue() != 0;
        }
        if (object.isBoolean()) {
            return object.asBoolean().getPrimitiveValue();
        }
        throw new TypesMismatchException(
                fieldId, BsonType.BOOLEAN, object.getType(),
                "Expected boolean or numeric type for field " + fieldId
                + ". Found " + object.getType().toString().toLowerCase(Locale.ROOT)
        );
    }

    public static boolean getBooleanOrNumeric(
            BsonDocument doc,
            BooleanField field,
            boolean defaultValue) throws TypesMismatchException{
        return getBooleanOrNumeric(doc, field.getFieldName(), defaultValue);
    }

    public static boolean getBoolean(
            BsonDocument doc,
            String fieldId) throws TypesMismatchException, NoSuchKeyException{
        BsonValue object = getValue(doc, fieldId);
        if (object.isBoolean()) {
            return object.asBoolean().getValue();
        }
        String foundType = toStringBsonType(object.getType());
        throw new TypesMismatchException(
                fieldId, "boolean", object.getType(),
                "Expected boolean type for field " + fieldId + ". Found " + foundType
        );
    }
    
    public static boolean getBoolean(
            BsonDocument doc,
            String fieldId,
            boolean defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (object.isBoolean()) {
            return object.asBoolean().getValue();
        }
        String foundType = toStringBsonType(object.getType());
        throw new TypesMismatchException(
                fieldId, "boolean", object.getType(),
                "Expected boolean type for field " + fieldId + ". Found " + foundType
        );
    }

    @Nonnull
    public static String getString(BsonDocument doc, StringField field) throws TypesMismatchException, NoSuchKeyException {
        return getString(doc, field.getFieldName());
    }

    @Nullable
    public static String getString(BsonDocument doc, StringField field, String defaultValue) throws TypesMismatchException {
        return getString(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static String getString(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isString()) {
            throw new TypesMismatchException(fieldId, "string", object.getType());
        }
        return object.asString().getValue();
    }

    @Nullable
    public static String getString(BsonDocument doc, String fieldId, String defaultValue) throws TypesMismatchException{
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isString()) {
            throw new TypesMismatchException(fieldId, "string", object.getType());
        }
        return object.asString().getValue();
    }

    @Nonnull
    public static HostAndPort getHostAndPort(BsonDocument doc, HostAndPortField field) throws TypesMismatchException, NoSuchKeyException {
        return getHostAndPort(doc, field.getFieldName());
    }

    @Nullable
    public static HostAndPort getHostAndPort(BsonDocument doc, HostAndPortField field, HostAndPort defaultValue) throws TypesMismatchException {
        return getHostAndPort(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static HostAndPort getHostAndPort(BsonDocument doc, String fieldName) throws TypesMismatchException, NoSuchKeyException {
        String stringValue = BsonReaderTool.getString(doc, fieldName).trim();
        return HostAndPort.fromString(stringValue);
    }

    @Nullable
    public static HostAndPort getHostAndPort(BsonDocument doc, String fieldId, HostAndPort defaultValue) throws TypesMismatchException {
        Object object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        String stringValue;
        try {
            stringValue = BsonReaderTool.getString(doc, fieldId).trim();
        }
        catch (NoSuchKeyException ex) {
            throw new AssertionError();
        }
        return HostAndPort.fromString(stringValue);
    }

    @Nonnull
    public static BsonObjectId getObjectId(BsonDocument doc, ObjectIdField field) throws TypesMismatchException, NoSuchKeyException {
        return getObjectId(doc, field.getFieldName());
    }

    @Nullable
    public static BsonObjectId getObjectId(BsonDocument doc, ObjectIdField field, BsonObjectId defaultValue) throws TypesMismatchException {
        return getObjectId(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static BsonObjectId getObjectId(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isObjectId()) {
            throw new TypesMismatchException(fieldId, "objectId", object.getType());
        }
        return object.asObjectId().getValue();
    }

    @Nullable
    public static BsonObjectId getObjectId(BsonDocument doc, String fieldId, BsonObjectId defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isObjectId()) {
            throw new TypesMismatchException(fieldId, "objectId", object.getType());
        }
        return object.asObjectId().getValue();
    }
}
