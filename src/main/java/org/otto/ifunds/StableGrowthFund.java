package org.otto.ifunds;

import org.otto.utils.Utils;

/**
 * Created by tomek on 2016-10-06.
 */
public class StableGrowthFund extends InvestmentFund {

    public static StableGrowthFund INSTANCE = new StableGrowthFund();

    public synchronized void runSimulationStep() {
        priceOfA = priceOfA.add( Utils.rangeRandom(-0.65f, +0.85f) );
        priceOfB = priceOfB.add( Utils.rangeRandom(-0.65f, +0.85f) );
        postVerifyPrices();
    }
}
