package org.otto.ifunds;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;
import org.otto.utils.Utils;

/**
 * Created by tomek on 2016-10-06.
 */
public class BalancedIFund extends InvestmentFund {

    public static BalancedIFund INSTANCE = new BalancedIFund();

    public synchronized void runSimulationStep() {
        priceOfA = priceOfA.add( Utils.rangeRandom(-0.80f, +0.96f) );
        priceOfB = priceOfB.add( Utils.rangeRandom(-0.80f, +0.96f) );
        postVerifyPrices();
    }
}
