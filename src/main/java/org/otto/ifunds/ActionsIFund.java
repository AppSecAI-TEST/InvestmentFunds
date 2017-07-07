package org.otto.ifunds;

import org.otto.utils.Utils;

/**
 * Created by tomek on 2016-10-06.
 */
public class ActionsIFund extends InvestmentFund {

    public static ActionsIFund INSTANCE = new ActionsIFund();

    public synchronized void runSimulationStep() {
        priceOfA = priceOfA.add( Utils.rangeRandom(-1.00f, +1.10f) );
        priceOfB = priceOfB.add( Utils.rangeRandom(-1.00f, +1.10f) );
        postVerifyPrices();
    }
}
