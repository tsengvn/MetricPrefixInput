package com.tsengvn.prefixinput;

import junit.framework.Assert;

import org.junit.Test;

/**
 *
 * @author Hien Ngo
 * @since 6/30/15
 */
public class NumberUtilTest {

    @Test
    public void testFormatNumber() throws Exception {
        String[] testCases = TEST.split("\n");
        for (String testCase : testCases) {
            if(!testCase.contains("=")) continue;
            String test = testCase.substring(0, testCase.indexOf("=")).trim();
            String expected = testCase.substring(testCase.indexOf("=") + 1).trim().toLowerCase();

            Assert.assertEquals("NumberUtil.formatNumber", expected, NumberUtil.formatNumber(NumberUtil.createDouble(test), true, 4));
        }
    }

    private static final String TEST =
            "123456789 = 123m\n" +
            "1665 = 1.67k\n" +
            "1995 = 2k\n" +
            "0.4 = 0.4\n" +
            "0.234 = 0.23\n" +
            "7 = 7\n" +
            "7.0 = 7\n" +
            "7.1 = 7.1\n" +
            "7.12 = 7.12\n" +
            "7.123 = 7.12\n" +
            "7.245896 = 7.25\n" +
            "12 = 12\n" +
            "12.1 = 12.1\n" +
            "12.36 = 12.36\n" +
            "12.569 = 12.57\n" +
            "\n" +
            "856= 856\n" +
            "856.0 = 856\n" +
            "856.1 = 856.1\n" +
            "856.25 = 856.3\n" +
            "156.234 = 156.2\n" +
            "856.35 = 856.4\n" +
            "122.25 = 122.3\n" +
            "\n" +
            "1000 = 1K\n" +
            "1000.0 = 1K\n" +
            "1000.1 = 1K\n" +
            "1000.23 = 1K\n" +
            "1000.2456 = 1K\n" +
            "1002 = 1K\n" +
            "1050 = 1.05K\n" +
            "1230 = 1.23K\n" +
            "5826 = 5.83K\n" +
            "45223 = 45.2K\n" +
            "45223.0 = 45.2K\n" +
            "45223.1 = 45.2K\n" +
            "45223.123 = 45.2K\n" +
            "\n" +
            "452233 = 452K\n" +
            "452233.0 = 452K\n" +
            "452233.1 = 452K\n" +
            "452233.12 = 452K\n" +
            "452233.125 = 452K\n" +
            "\n" +
            "4555555 = 4.56M\n" +
            "4555555.0 = 4.56M\n" +
            "4555555.1 = 4.56M\n" +
            "4555555.12 = 4.56M\n" +
            "\n" +
            "41235899 = 41.2M\n" +
            "41235899.0 = 41.2M\n" +
            "41235899.1 = 41.2M\n" +
            "41235899.12 = 41.2M\n" +
            "\n" +
            "369852333 = 370M\n" +
            "369852333.0 = 370M\n" +
            "369852333.1 = 370M\n" +
            "369852333.123 = 370M\n" +
            "\n" +
            "3211111111 = 3.21B\n" +
            "3211111111.0 = 3.21B\n" +
            "3211111111.12 = 3.21B\n" +
            "3211111111.123 = 3.21B\n" +
            "\n" +
            "36595246999 = 36.6B\n" +
            "36595246999.0 = 36.6B\n" +
            "36595246999.1 = 36.6B\n" +
            "36595246999.123 = 36.6B\n" +
            "\n" +
            "365974569822 = 366B\n" +
            "365974569822.0 = 366B\n" +
            "365974569822.1 = 366B\n" +
            "365974569822.123 = 366B\n" +
            "\n";
}
