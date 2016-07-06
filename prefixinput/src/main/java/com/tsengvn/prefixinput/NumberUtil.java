package com.tsengvn.prefixinput;

/**
 *
 * @author Hien Ngo
 * @since 1/20/15
 */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class NumberUtil {
    // International System of Units
    // more detail http://en.wikipedia.org/wiki/International_System_of_Units
    // TODO: Standardlize SI prefix http://en.wikipedia.org/wiki/Metric_prefix#List_of_SI_prefixes
    private static final String[] SI_PREFIXS = new String[]{"", "k", "m", "b", "t"};
    private static final Map<String, BigDecimal> SI_PREFIXS_MAP;

    static {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("", createBigDecimal("1"));
        map.put("k", createBigDecimal("1000"));
        map.put("m", createBigDecimal("1000000"));
        map.put("b", createBigDecimal("1000000000"));
        map.put("t", createBigDecimal("1000000000000"));
        SI_PREFIXS_MAP = Collections.unmodifiableMap(map);
    }

    public static String formatSignificant(double value, int significant) {
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(value));
        bigDecimal = bigDecimal.round(new MathContext(significant));
        return bigDecimal.stripTrailingZeros().toPlainString();
    }

    public static String formatDouble(Double value) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(value);
    }

    public static String formatNumber(Double value, boolean scale0, Integer precision) {
        if (value == null || value == 0) return "0";

        int order = 0;


        boolean flag = false;
        while (value >= 1000) {
            value /= 1000;
            order += 1;
            flag = true;
        }

        if (flag) {
            value = NumberUtil.createDouble(formatDouble(value));
            precision--;
        }

        // In case rounded number ~ 1000 (Ex: 1000k)
        String result = (precision > 0 ? formatSignificant(value, precision) : formatDouble(value));
        value = Double.valueOf(result);
        while (value >= 1000.0) {
            value /= 1000.0;
            order += 1;
        }

        return (precision > 0 ? formatSignificant(value, precision) : formatDouble(value)) + SI_PREFIXS[order];
    }

    public static String formatNumber(Double value) {
        return formatNumber(value, false, 0);
    }

    public static Long createLong(String value, Long defaultValue) {
        if (value == null || value.trim() == "") return defaultValue;
        if (!isNumeric(value)) return defaultValue;

        return Long.valueOf(value);
    }

    public static Long createLong(Object value) {
        if (value == null) return 0L;
        return NumberUtil.createLong(value.toString(), 0L);
    }

    public static BigDecimal createBigDecimal(String value, BigDecimal defaultValue) {
        if (value == null || value.trim().length() == 0) return defaultValue;
        //if(!NumberUtils.isNumber(value)) return defaultValue;

        return createNumber(value, 2, defaultValue);
    }

    public static BigDecimal createBigDecimal(String value) {
        return createBigDecimal(value, new BigDecimal("0"));
    }

    public static BigInteger createBigInteger(String value, BigInteger defaultValue) {
        if (value == null || value.trim().length() == 0) return defaultValue;
        if (!isNumeric(value)) return defaultValue;

        return new BigInteger(value);
    }

    public static BigInteger createBigInteger(String value) {
        return createBigInteger(value, new BigInteger("0"));
    }

    public static BigInteger createBigInteger(Object value) {
        if (value == null) return new BigInteger("0");
        return createBigInteger(value.toString());
    }

    public static Integer createInteger(String value, Integer defaultValue) {
        if (value == null || value.trim() == "") return defaultValue;
        if (!isNumeric(value)) return defaultValue;

        return Integer.valueOf(value);
    }

    public static Integer createInteger(String value) {
        return NumberUtil.createInteger(value, 0);
    }

    public static Integer createInteger(Object value) {
        if (value == null) return 0;
        return NumberUtil.createInteger(value.toString(), 0);
    }

    private static BigDecimal createNumber(String number, int scale, BigDecimal defaultValue) {
        if (number == null || number.trim().length() == 0) return defaultValue;
        number = number.trim();

        String tail = number.substring(number.length()-1);
        if (Arrays.asList(SI_PREFIXS).contains(tail)) {
            String head = number.substring(0, number.length() - 1);
            if (isNumeric(head)) {
                return round(new BigDecimal(head).multiply(SI_PREFIXS_MAP.get(tail)), scale);
            } else {
                return defaultValue;
            }
        }

        return isNumeric(number) ? round(number, scale) : defaultValue;
    }

    public static BigDecimal createNumber(String number) {
        return createNumber(number, 2, createBigDecimal(number));
    }

    public static BigDecimal round(String value, int scale) {
        BigDecimal number = new BigDecimal(value);
        return round(number, scale);
    }

    public static BigDecimal round(BigDecimal value, int scale) {
        if (scale < 0) throw new IllegalArgumentException();
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    public static Double createDouble(Object number) {
        if (number == null) return 0D;
        return createNumber(number.toString(), 2, createBigDecimal(number.toString())).doubleValue();
    }

    public static boolean isValidBigInteger(String number) {
        if (number == null) {
            return false;
        }
        try {
            new BigInteger(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str){
        return str.matches("^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$");
    }

}