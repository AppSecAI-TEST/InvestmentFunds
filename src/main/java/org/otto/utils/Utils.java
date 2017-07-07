package org.otto.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by tomek on 2016-10-06.
 */
public class Utils {

    public static BigDecimal rangeRandom(float from, float to) {
        BigDecimal rnd = new BigDecimal( new Random().nextFloat() * (to - from) + from );
        rnd.setScale(2, RoundingMode.HALF_EVEN);
        return rnd;
    }

    private static DecimalFormat moneyFormat = new DecimalFormat();

    static {
        moneyFormat.setMaximumFractionDigits(2);
        moneyFormat.setMinimumFractionDigits(0);
        moneyFormat.setGroupingUsed(false);
    }

    public static String printMoney(BigDecimal val) {
        return moneyFormat.format(val);
    }

    public static void sleep(int sec) {
        sleepMs(sec * 1000);
    }

    public static void sleepMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {}
    }

}
