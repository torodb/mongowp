
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
    public static Entry<?> getEntry(BsonDocument doc, String fieldId) throws NoSuchKeyException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            throw new NoSuchKeyException(fieldId);
        }
        return entry;
    }

    @Nonnull
    public static Entry<?> getEntry(BsonDocument doc, BsonField field) throws NoSuchKeyException {
        return getEntry(doc, field.getFieldName());
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
        return getDocument(getEntry(doc, fieldId), fieldId);
    }

    @Nonnull
    public static BsonDocument getDocument(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue<?> object = entry.getValue();
        if (!object.isDocument()) {
            throw new TypesMismatchException(fieldId, BsonType.DOCUMENT, object.getType());
        }
        return object.asDocument();
    }

    @Nonnull
    public static BsonDocument getDocument(Entry<?> entry, DocField field) throws TypesMismatchException {
        return getDocument(entry, field.getFieldName());
    }

    @Nullable
    public static BsonDocument getDocument(BsonDocument doc, String fieldId, BsonDocument defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getDocument(entry, fieldId);
    }

    @Nonnull
    public static BsonArray getArray(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue<?> object = entry.getValue();
        if (!object.isArray()) {
            throw new TypesMismatchException(fieldId, BsonType.ARRAY, object.getType());
        }
        return object.asArray();
    }

    @Nonnull
    public static BsonArray getArray(Entry<?> entry, ArrayField field) throws TypesMismatchException {
        return getArray(entry, field.getFieldName());
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
        return getArray(getEntry(doc, fieldId), fieldId);
    }

    @Nullable
    public static BsonArray getArray(BsonDocument doc, String fieldId, BsonArray defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getArray(entry, fieldId);
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
    public static BsonNumber<?> getNumeric(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue<?> object = entry.getValue();
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
    public static BsonNumber getNumeric(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        return getNumeric(getEntry(doc, fieldId), fieldId);
    }

    @Nullable
    public static BsonNumber getNumeric(BsonDocument doc, String fieldId, BsonNumber defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getNumeric(entry, fieldId);
    }

    @Nonnull
    public static Instant getInstant(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue<?> value = entry.getValue();
        if (!value.isDateTime()) {
            throw new TypesMismatchException(
                    fieldId, "date", value.getType(),
                    "Expected date type for field " + fieldId + ". Found" + value
            );
        }
        return value.asDateTime().getValue();
    }

    @Nonnull
    public static Instant getInstant(Entry<?> entry, DateTimeField field) throws TypesMismatchException {
        return getInstant(entry, field.getFieldName());
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
        return getInstant(getEntry(doc, fieldId), fieldId);
    }

    @Nullable
    public static Instant getInstant(BsonDocument doc, String fieldId, Instant defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getInstant(entry, fieldId);
    }

    @Nonnull
    public static OpTime getOpTime(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue object = entry.getValue();
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
    public static OpTime getOpTime(Entry<?> entry, TimestampField field) throws TypesMismatchException {
        return getOpTime(entry, field.getFieldName());
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
        return getOpTime(getEntry(doc, fieldId), fieldId);
    }

    @Nullable
    public static OpTime getOpTime(BsonDocument doc, String fieldId, OpTime defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getOpTime(entry, fieldId);
    }

    public static int getInteger(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue<?> object = entry.getValue();
        if (!object.isInt32()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected integer type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asInt32().intValue();
    }

    public static int getInteger(Entry<?> entry, IntField field) throws TypesMismatchException {
        return getInteger(entry, field.getFieldName());
    }

    @Nonnull
    public static int getInteger(BsonDocument doc, IntField field) throws TypesMismatchException, NoSuchKeyException {
        return getInteger(doc, field.getFieldName());
    }

    public static int getInteger(BsonDocument doc, IntField field, int defaultValue) throws TypesMismatchException {
        return getInteger(doc, field.getFieldName(), defaultValue);
    }

    public static int getInteger(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        return getInteger(doc.getEntry(fieldId), fieldId);
    }

    public static int getInteger(BsonDocument doc, String fieldId, int defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getInteger(entry, fieldId);
    }

    public static long getLong(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue object = entry.getValue();
        if (!object.isNumber()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected long type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asNumber().longValue();
    }

    public static long getLong(Entry<?> entry, LongField field) throws TypesMismatchException {
        return getLong(entry, field.getFieldName());
    }

    @Nonnull
    public static long getLong(BsonDocument doc, LongField field) throws TypesMismatchException, NoSuchKeyException {
        return getLong(doc, field.getFieldName());
    }

    public static long getLong(BsonDocument doc, LongField field, long defaultValue) throws TypesMismatchException {
        return getLong(doc, field.getFieldName(), defaultValue);
    }
    
    public static long getLong(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        return getLong(getEntry(doc, fieldId), fieldId);
    }
    
    public static long getLong(BsonDocument doc, String fieldId, long defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getLong(entry, fieldId);
    }

    public static double getDouble(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue object = entry.getValue();
        if (!object.isDouble()) {
            String foundType = toStringBsonType(object.getType());
            throw new TypesMismatchException(
                    fieldId, "integer", object.getType(),
                    "Expected double type for field " + fieldId + ". Found" + foundType
            );
        }
        return object.asDouble().doubleValue();
    }

    public static double getDouble(Entry<?> entry, DoubleField field) throws TypesMismatchException {
        return getDouble(entry, field.getFieldName());
    }

    @Nonnull
    public static double getDouble(BsonDocument doc, DoubleField field) throws TypesMismatchException, NoSuchKeyException {
        return getDouble(doc, field.getFieldName());
    }

    public static double getDouble(BsonDocument doc, DoubleField field, double defaultValue) throws TypesMismatchException {
        return getDouble(doc, field.getFieldName(), defaultValue);
    }

    public static double getDouble(BsonDocument doc, String fieldId) throws TypesMismatchException, NoSuchKeyException {
        return getDouble(getEntry(doc, fieldId), fieldId);
    }

    public static double getDouble(BsonDocument doc, String fieldId, double defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getDouble(entry, fieldId);
    }

    public static boolean getBooleanOrNumeric(Entry<?> entry, String fieldId) throws TypesMismatchException{
        BsonValue object = entry.getValue();
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
    
    public static boolean getBooleanOrNumeric(Entry<?> entry, BooleanField field) throws TypesMismatchException {
        return getBooleanOrNumeric(entry, field.getFieldName());
    }

    public static boolean getBooleanOrNumeric(BsonDocument doc, String fieldId, boolean defaultValue)
            throws TypesMismatchException{
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getBooleanOrNumeric(entry, fieldId);
    }

    public static boolean getBooleanOrNumeric(BsonDocument doc, BooleanField field, boolean defaultValue)
            throws TypesMismatchException{
        return getBooleanOrNumeric(doc, field.getFieldName(), defaultValue);
    }

    public static boolean getBoolean(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue object = entry.getValue();
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
    public static boolean getBoolean(Entry<?> entry, BooleanField field) throws TypesMismatchException {
        return getBoolean(entry, field.getFieldName());
    }

    public static boolean getBoolean(BsonDocument doc, BooleanField field, boolean defaultValue)
            throws TypesMismatchException {
        return getBoolean(doc, field.getFieldName(), defaultValue);
    }

    public static boolean getBoolean(BsonDocument doc, String fieldId)
            throws TypesMismatchException, NoSuchKeyException{
        return getBoolean(getEntry(doc, fieldId), fieldId);
    }

    public static boolean getBoolean(BsonDocument doc, BooleanField field)
            throws TypesMismatchException, NoSuchKeyException{
        return getBoolean(doc, field.getFieldName());
    }
    
    public static boolean getBoolean( BsonDocument doc, String fieldId, boolean defaultValue) 
            throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getBoolean(entry, fieldId);
    }

    @Nonnull
    public static String getString(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue object = entry.getValue();
        if (!object.isString()) {
            throw new TypesMismatchException(fieldId, "string", object.getType());
        }
        return object.asString().getValue();
    }

    @Nonnull
    public static String getString(Entry<?> entry, StringField field) throws TypesMismatchException {
        return getString(entry, field.getFieldName());
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
        return getString(getEntry(doc, fieldId), fieldId);
    }

    @Nullable
    public static String getString(BsonDocument doc, String fieldId, String defaultValue) throws TypesMismatchException{
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getString(entry, fieldId);
    }

    @Nonnull
    public static HostAndPort getHostAndPort(Entry<?> entry, String fieldId) throws TypesMismatchException {
        String string = getString(entry, fieldId);
        return HostAndPort.fromString(string);
    }

    @Nonnull
    public static HostAndPort getHostAndPort(Entry<?> entry, HostAndPortField field) throws TypesMismatchException {
        return getHostAndPort(entry, field.getFieldName());
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
        return getHostAndPort(getEntry(doc, fieldName), fieldName);
    }

    @Nullable
    public static HostAndPort getHostAndPort(BsonDocument doc, String fieldId, HostAndPort defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getHostAndPort(entry, fieldId);
    }

    @Nonnull
    public static BsonObjectId getObjectId(Entry<?> entry, String fieldId) throws TypesMismatchException {
        BsonValue<?> object = entry.getValue();
        if (!object.isObjectId()) {
            throw new TypesMismatchException(fieldId, "objectId", object.getType());
        }
        return object.asObjectId();
    }

    @Nonnull
    public static BsonObjectId getObjectId(Entry<?> entry, ObjectIdField field) throws TypesMismatchException {
        return getObjectId(entry, field.getFieldName());
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
        return getObjectId(getEntry(doc, fieldId), fieldId);
    }

    @Nullable
    public static BsonObjectId getObjectId(BsonDocument doc, String fieldId, BsonObjectId defaultValue) throws TypesMismatchException {
        Entry<?> entry = doc.getEntry(fieldId);
        if (entry == null) {
            return defaultValue;
        }
        return getObjectId(entry, fieldId);
    }
}
