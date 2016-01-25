
package com.eightkdata.mongowp.mongoserver.api.tools.bson;

import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;
import com.google.common.primitives.UnsignedInteger;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bson.*;
import org.bson.types.ObjectId;
import org.threeten.bp.Instant;

/**
 *
 */
public class BsonReaderTool {

    private BsonReaderTool() {}

    public static String toStringBsonType(BsonType type) {
        return type.toString().toLowerCase(Locale.ROOT);
    }

    public static boolean containsField(BsonDocument doc, BsonField<?> field) {
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
        for (String field : bson.keySet()) {
            if (!fields.contains(field)) {
                throw new BadValueException(
                        "Unexpected field " + field + " in " + objectName
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
    public static BsonDocument getDocument(BsonDocument doc, BsonField<BsonDocument> field) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getDocument(doc, field.getFieldName());
    }

    @Nullable
    public static BsonDocument getDocument(BsonDocument doc, BsonField<BsonDocument> field, BsonDocument defaultValue) throws TypesMismatchException {
        return getDocument(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static BsonDocument getDocument(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isDocument()) {
            throw new TypesMismatchException(fieldId, BsonType.DOCUMENT, object.getBsonType());
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
            throw new TypesMismatchException(fieldId, BsonType.DOCUMENT, object.getBsonType());
        }
        return object.asDocument();
    }

    @Nonnull
    public static BsonArray getArray(BsonDocument doc, BsonField<BsonArray> field) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getArray(doc, field.getFieldName());
    }

    @Nullable
    public static BsonArray getArray(BsonDocument doc, BsonField<BsonArray> field, BsonArray defaultValue) throws TypesMismatchException {
        return getArray(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static BsonArray getArray(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isArray()) {
            throw new TypesMismatchException(fieldId, BsonType.ARRAY, object.getBsonType());
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
            throw new TypesMismatchException(fieldId, BsonType.ARRAY, object.getBsonType());
        }
        return object.asArray();
    }

    @Nonnull
    public static BsonNumber getNumeric(BsonDocument doc, BsonField<? extends Number> field) throws TypesMismatchException, NoSuchKeyException {
        return BsonReaderTool.getNumeric(doc, field.getFieldName());
    }

    @Nullable
    public static BsonNumber getNumeric(BsonDocument doc, BsonField<? extends Number> field, BsonNumber defaultValue) throws TypesMismatchException {
        return getNumeric(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static BsonNumber getNumeric(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isNumber()) {
            String foundType = object.getBsonType().toString().toLowerCase(Locale.ROOT);
            throw new TypesMismatchException(
                    fieldId, "numeric", object.getBsonType(),
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
            String foundType = object.getBsonType().toString().toLowerCase(Locale.ROOT);
            throw new TypesMismatchException(
                    fieldId, "numeric", object.getBsonType(),
                    fieldId + " field has non-numeric type " + foundType
            );
        }
        return object.asNumber();
    }

    @Nonnull
    public static Instant getInstant(BsonDocument doc, BsonField<Instant> field) throws TypesMismatchException, NoSuchKeyException {
        return getInstant(doc, field.getFieldName());
    }

    @Nullable
    public static Instant getInstant(BsonDocument doc, BsonField<Instant> field, Instant defaultValue) throws TypesMismatchException {
        return getInstant(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static Instant getInstant(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (object.isDateTime()) {
            return Instant.ofEpochMilli(object.asDateTime().getValue());
        }
        if (object.isTimestamp()) {
            return Instant.ofEpochSecond(object.asTimestamp().getTime(), object.asTimestamp().getInc()); //TODO: Check this implementation
        }
        String foundType = object.getBsonType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getBsonType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nullable
    public static Instant getInstant(BsonDocument doc, String fieldId, Instant defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object.isDateTime()) {
            return Instant.ofEpochMilli(object.asDateTime().getValue());
        }
        if (object.isTimestamp()) {
            return Instant.ofEpochSecond(object.asTimestamp().getTime(), object.asTimestamp().getInc()); //TODO: Check this implementation
        }
        String foundType = object.getBsonType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getBsonType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nonnull
    public static OpTime getOpTime(BsonDocument doc, BsonField<OpTime> field) throws TypesMismatchException, NoSuchKeyException {
        return getOpTime(doc, field.getFieldName());
    }

    @Nullable
    public static OpTime getOpTime(BsonDocument doc, BsonField<OpTime> field, OpTime defaultValue) throws TypesMismatchException {
        return getOpTime(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static OpTime getOpTime(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (object.isDateTime()) {
            BsonDateTime asDateTime = object.asDateTime();
            UnsignedInteger secs = UnsignedInteger.valueOf(asDateTime.getValue() / 1000);
            UnsignedInteger term = UnsignedInteger.fromIntBits((int) (asDateTime.getValue() % 1000));
            return new OpTime(secs, term);
        }
        if (object.isTimestamp()) {
            BsonTimestamp asTimestamp = object.asTimestamp();
            UnsignedInteger secs = UnsignedInteger.fromIntBits(asTimestamp.getTime());
            UnsignedInteger term = UnsignedInteger.fromIntBits(asTimestamp.getInc());
            return new OpTime(secs, term);
        }
        String foundType = object.getBsonType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getBsonType(),
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
            Instant.ofEpochMilli(object.asDateTime().getValue());
        }
        if (object.isTimestamp()) {
            Instant.ofEpochSecond(object.asTimestamp().getTime(), object.asTimestamp().getInc()); //TODO: Check this implementation
        }
        String foundType = object.getBsonType().toString().toLowerCase(Locale.ROOT);
        throw new TypesMismatchException(
                fieldId, "date", object.getBsonType(),
                "Expected date type for field " + fieldId + ". Found" + foundType
        );
    }

    @Nonnull
    public static int getInteger(BsonDocument doc, BsonField<Integer> field) throws TypesMismatchException, NoSuchKeyException {
        return getInteger(doc, field.getFieldName());
    }

    public static int getInteger(BsonDocument doc, BsonField<Integer> field, int defaultValue) throws TypesMismatchException {
        return getInteger(doc, field.getFieldName(), defaultValue);
    }

    public static int getInteger(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isInt32()) {
            String foundType = toStringBsonType(object.getBsonType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getBsonType(),
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
            String foundType = toStringBsonType(object.getBsonType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getBsonType(),
                    "Expected integer type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asInt32().intValue();
    }

    @Nonnull
    public static long getLong(BsonDocument doc, BsonField<Long> field) throws TypesMismatchException, NoSuchKeyException {
        return getLong(doc, field.getFieldName());
    }

    public static long getLong(BsonDocument doc, BsonField<Long> field, long defaultValue) throws TypesMismatchException {
        return getLong(doc, field.getFieldName(), defaultValue);
    }
    
    public static long getLong(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isNumber()) {
            String foundType = toStringBsonType(object.getBsonType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getBsonType(),
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
            String foundType = toStringBsonType(object.getBsonType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getBsonType(),
                    "Expected long type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asNumber().longValue();
    }

    @Nonnull
    public static double getDouble(BsonDocument doc, BsonField<Double> field) throws TypesMismatchException, NoSuchKeyException {
        return getDouble(doc, field.getFieldName());
    }

    public static double getDouble(BsonDocument doc, BsonField<Double> field, double defaultValue) throws TypesMismatchException {
        return getDouble(doc, field.getFieldName(), defaultValue);
    }

    public static double getDouble(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isDouble()) {
            String foundType = toStringBsonType(object.getBsonType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getBsonType(),
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
            String foundType = toStringBsonType(object.getBsonType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getBsonType(),
                    "Expected double type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asDouble().doubleValue();
    }

    @Nonnull
    public static boolean getBoolean(BsonDocument doc, BsonField<Boolean> field) throws TypesMismatchException, NoSuchKeyException {
        return getBoolean(doc, field.getFieldName());
    }

    public static boolean getBoolean(BsonDocument doc, BsonField<Boolean> field, boolean defaultValue) throws TypesMismatchException {
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
            return object.asBoolean().getValue();
        }
        throw new TypesMismatchException(
                fieldId, BsonType.BOOLEAN, object.getBsonType(),
                "Expected boolean or numeric type for field " + fieldId
                + ". Found " + object.getBsonType().toString().toLowerCase(Locale.ROOT)
        );
    }

    public static boolean getBooleanOrNumeric(
            BsonDocument doc,
            BsonField<Boolean> field,
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
        String foundType = toStringBsonType(object.getBsonType());
        throw new TypesMismatchException(
                fieldId, "boolean", object.getBsonType(),
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
        String foundType = toStringBsonType(object.getBsonType());
        throw new TypesMismatchException(
                fieldId, "boolean", object.getBsonType(),
                "Expected boolean type for field " + fieldId + ". Found " + foundType
        );
    }

    @Nonnull
    public static String getString(BsonDocument doc, BsonField<String> field) throws TypesMismatchException, NoSuchKeyException {
        return getString(doc, field.getFieldName());
    }

    @Nullable
    public static String getString(BsonDocument doc, BsonField<String> field, String defaultValue) throws TypesMismatchException {
        return getString(doc, field.getFieldName(), defaultValue);
    }

    @Nonnull
    public static String getString(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isString()) {
            throw new TypesMismatchException(fieldId, "string", object.getBsonType());
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
            throw new TypesMismatchException(fieldId, "string", object.getBsonType());
        }
        return object.asString().getValue();
    }

    @Nonnull
    public static HostAndPort getHostAndPort(BsonDocument doc, BsonField<HostAndPort> field) throws TypesMismatchException, NoSuchKeyException {
        return getHostAndPort(doc, field.getFieldName());
    }

    @Nullable
    public static HostAndPort getHostAndPort(BsonDocument doc, BsonField<HostAndPort> field, HostAndPort defaultValue) throws TypesMismatchException {
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
    public static ObjectId getObjectId(BsonDocument doc, BsonField<ObjectId> field) throws TypesMismatchException, NoSuchKeyException {
        return getObjectId(doc, field.getFieldName());
    }

    @Nullable
    public static ObjectId getObjectId(BsonDocument doc, BsonField<ObjectId> field, ObjectId defaultValue) throws TypesMismatchException {
        return getObjectId(doc, field.getFieldName(), defaultValue);
    }
    
    @Nonnull
    public static ObjectId getObjectId(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        BsonValue object = getValue(doc, fieldId);
        if (!object.isObjectId()) {
            throw new TypesMismatchException(fieldId, "objectId", object.getBsonType());
        }
        return object.asObjectId().getValue();
    }

    @Nullable
    public static ObjectId getObjectId(BsonDocument doc, String fieldId, ObjectId defaultValue) throws TypesMismatchException {
        BsonValue object = doc.get(fieldId);
        if (object == null) {
            return defaultValue;
        }
        if (!object.isObjectId()) {
            throw new TypesMismatchException(fieldId, "objectId", object.getBsonType());
        }
        return object.asObjectId().getValue();
    }
}
