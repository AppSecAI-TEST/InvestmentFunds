package org.otto.ifunds;

import org.otto.utils.Utils;

/**
 * Created by tomek on 2016-10-06.
 */
public class MoneyMarketIFund extends InvestmentFund {
    public static MoneyMarketIFund INSTANCE = new MoneyMarketIFund();

    public synchronized void runSimulationStep() {
        priceOfA = priceOfA.add( Utils.rangeRandom(-0.05f, +0.40f) );
        priceOfB = priceOfB.add( Utils.rangeRandom(-0.05f, +0.40f) );
        postVerifyPrices();
    }
}
