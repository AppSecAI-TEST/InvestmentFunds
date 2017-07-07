package org.otto.ifunds;

import org.otto.utils.Utils;

/**
 * Created by tomek on 2016-10-06.
 */
public class BondIFund extends InvestmentFund {

    public static BondIFund INSTANCE = new BondIFund();

    public synchronized void runSimulationStep() {
        priceOfA = priceOfA.add( Utils.rangeRandom(-0.15f, +0.60f) );
        priceOfB = priceOfB.add( Utils.rangeRandom(-0.15f, +0.60f) );
        postVerifyPrices();
    }
}
