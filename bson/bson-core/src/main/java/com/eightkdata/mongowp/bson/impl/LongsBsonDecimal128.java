/*
 * Copyright 2014 8Kdata Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eightkdata.mongowp.bson.impl;

import com.eightkdata.mongowp.bson.abst.AbstractBsonDecimal128;

import java.math.BigDecimal;
import java.math.BigInteger;

public class LongsBsonDecimal128 extends AbstractBsonDecimal128 {

  private static final long serialVersionUID = 3772367120072257318L;

  private static final long INFINITY_MASK = 0x7800000000000000L;
  private static final long NaN_MASK = 0x7c00000000000000L;
  private static final long SIGN_BIT_MASK = 1L << 63;
  private static final int EXPONENT_OFFSET = 6176;
  private static final int MIN_EXPONENT = -6176;
  private static final int MAX_EXPONENT = 6111;
  private static final int MAX_BIT_LENGTH = 113;

  private static final BigInteger BIG_INT_TEN = new BigInteger("10");
  private static final BigInteger BIG_INT_ONE = new BigInteger("1");
  private static final BigInteger BIG_INT_ZERO = new BigInteger("0");
  
  private long high;
  private long low;

  public LongsBsonDecimal128(long high, long low) {
    this.high = high;
    this.low = low;
  }

  /**
   * Returns a {@link LongsBsonDecimal128} instance with the given value.
   */
  public static LongsBsonDecimal128 newInstance(long high, long low) {
    return new LongsBsonDecimal128(high, low);
  }
  
  private LongsBsonDecimal128(BigDecimal value) {
    bigDecimalToDecimal128(value, value.signum() == -1);
  }
  
  /**
   * Returns a {@link LongsBsonDecimal128} instance with the given value.
   */
  public static LongsBsonDecimal128 newInstance(BigDecimal value) {
    return new LongsBsonDecimal128(value);
  }

  @Override
  public long getHigh() {
    return this.high;
  }

  @Override
  public long getLow() {
    return low;
  }

  /**
   * Get a value of this {@link LongsBsonDecimal128} in an equivalent BigDecimal
   * 
   * @return BigDecimal equivalent to this Decimal128
   */
  @Override
  public BigDecimal getValue() {

    if (isNaN()) {
      throw new ArithmeticException("Can not get value of NaN");
    }

    if (isInfinite()) {
      throw new ArithmeticException("Can not get value of Infinite");
    }

    BigDecimal value = getBigDecimal();

    // If the BigDecimal is 0, but the Decimal128 is negative, that means we have -0.
    if (isNegative() && value.signum() == 0) {
      throw new ArithmeticException("Negative zero can not be converted to a BigDecimal");
    }

    return value;
  }

  private boolean isNaN() {
    return (high & NaN_MASK) == NaN_MASK;
  }

  private boolean isInfinite() {
    return (high & INFINITY_MASK) == INFINITY_MASK;
  }

  private BigDecimal getBigDecimal() {
    int scale = -getExponent();

    if (twoHighestCombinationBitsAreSet()) {
      return BigDecimal.valueOf(0, scale);
    }

    return new BigDecimal(new BigInteger(isNegative() ? -1 : 1, getBytes()), scale);
  }

  /**
   * Convert longs high and low to array of 16 bytes.
   * May have leading zeros.
   *  
   * @return byte[] 
   */
  @Override
  public byte[] getBytes() {
    byte[] bytes = new byte[15];

    long mask = 0x00000000000000ff;
    for (int i = 14; i >= 7; i--) {
      bytes[i] = (byte) ((low & mask) >>> ((14 - i) << 3));
      mask = mask << 8;
    }

    mask = 0x00000000000000ff;
    for (int i = 6; i >= 1; i--) {
      bytes[i] = (byte) ((high & mask) >>> ((6 - i) << 3));
      mask = mask << 8;
    }

    mask = 0x0001000000000000L;
    bytes[0] = (byte) ((high & mask) >>> 48);
    return bytes;
  }

  private int getExponent() {
    if (twoHighestCombinationBitsAreSet()) {
      return (int) ((high & 0x1fffe00000000000L) >>> 47) - EXPONENT_OFFSET;
    } else {
      return (int) ((high & 0x7fff800000000000L) >>> 49) - EXPONENT_OFFSET;
    }
  }

  private boolean twoHighestCombinationBitsAreSet() {
    return (high & 3L << 61) == 3L << 61;
  }

  /**
   * Returns true if this Decimal128 is negative.
   *
   * @return true if this Decimal128 is negative
   */
  private boolean isNegative() {
    return (high & SIGN_BIT_MASK) == SIGN_BIT_MASK;
  }

  /**
   * Convert a bigDecimal in two longs which represent a Decimal128
   * @param initialValue bigDecimal to convert
   * @param isNegative boolean necessary to detect -0, 
   *          which can't be represented with a BigDecimal 
   */
  private void bigDecimalToDecimal128(final BigDecimal initialValue, final boolean isNegative) {
    long localHigh = 0;
    long localLow = 0;

    BigDecimal value = clampAndRound(initialValue);

    long exponent = -value.scale();

    if ((exponent < MIN_EXPONENT) || (exponent > MAX_EXPONENT)) {
      throw new AssertionError("Exponent is out of range for Decimal128 encoding: " + exponent);
    }

    if (value.unscaledValue().bitLength() > MAX_BIT_LENGTH) {
      throw new AssertionError("Unscaled roundedValue is out of range for Decimal128 encoding:"
          + value.unscaledValue());
    }

    BigInteger significand = value.unscaledValue().abs();
    int bitLength = significand.bitLength();

    for (int i = 0; i < Math.min(64, bitLength); i++) {
      if (significand.testBit(i)) {
        localLow |= 1L << i;
      }
    }

    for (int i = 64; i < bitLength; i++) {
      if (significand.testBit(i)) {
        localHigh |= 1L << (i - 64);
      }
    }

    long biasedExponent = exponent + EXPONENT_OFFSET;

    localHigh |= biasedExponent << 49;

    if (value.signum() == -1 || isNegative) {
      localHigh |= SIGN_BIT_MASK;
    }

    high = localHigh;
    low = localLow;
  }

  private BigDecimal clampAndRound(final BigDecimal initialValue) {
    BigDecimal value;
    if (-initialValue.scale() > MAX_EXPONENT) {
      int diff = -initialValue.scale() - MAX_EXPONENT;
      if (initialValue.unscaledValue().equals(BIG_INT_ZERO)) {
        value = new BigDecimal(initialValue.unscaledValue(), -MAX_EXPONENT);
      } else if (diff + initialValue.precision() > 34) {
        throw new NumberFormatException(
            "Exponent is out of range for Decimal128 encoding of " + initialValue);
      } else {
        BigInteger multiplier = BIG_INT_TEN.pow(diff);
        value = new BigDecimal(initialValue.unscaledValue().multiply(multiplier),
            initialValue.scale() + diff);
      }
    } else if (-initialValue.scale() < MIN_EXPONENT) {
      // Increasing a very negative exponent may require decreasing precision, which is rounding
      // Only round exactly (by removing precision that is all zeroes).  
      // An exception is thrown if the rounding would be inexact:
      int diff = initialValue.scale() + MIN_EXPONENT;
      int undiscardedPrecision = ensureExactRounding(initialValue, diff);
      BigInteger divisor = undiscardedPrecision == 0 ? BIG_INT_ONE : BIG_INT_TEN.pow(diff);
      value = new BigDecimal(initialValue.unscaledValue().divide(divisor),
          initialValue.scale() - diff);
    } else {
      value = initialValue.round(java.math.MathContext.DECIMAL128);
      int extraPrecision = initialValue.precision() - value.precision();
      if (extraPrecision > 0) {
        // only round exactly
        ensureExactRounding(initialValue, extraPrecision);
      }
    }
    return value;
  }

  private int ensureExactRounding(final BigDecimal initialValue, final int extraPrecision) {
    String significand = initialValue.unscaledValue().abs().toString();
    int undiscardedPrecision = Math.max(0, significand.length() - extraPrecision);
    for (int i = undiscardedPrecision; i < significand.length(); i++) {
      if (significand.charAt(i) != '0') {
        throw new NumberFormatException(
            "Conversion to Decimal128 would require inexact rounding of " + initialValue);
      }
    }
    return undiscardedPrecision;
  }

}
