package org.otto.ifunds;

import org.otto.utils.Utils;

import java.math.BigDecimal;

/**
 * Created by tomek on 2016-10-06.
 */
public abstract class InvestmentFund {

    protected BigDecimal priceOfA;
    protected BigDecimal priceOfB;

    public InvestmentFund() {
        priceOfA = new BigDecimal(100);
        priceOfB = new BigDecimal(100);
    }

    public abstract void runSimulationStep();

    public BigDecimal priceOfUnitA() {
        return priceOfA;
    }

    public BigDecimal priceOfUnitB() {
        return priceOfB;
    }

    public BigDecimal priceOfUnit(FundUnitType fundUnitType)  {
        switch (fundUnitType) {
            case UNIT_A:
                return priceOfUnitA();
            case UNIT_B:
                return priceOfUnitB();
            default:
                return null;
        }
    }

    protected void postVerifyPrices() {
        if (priceOfA.floatValue() < 0.f) {
            priceOfA = BigDecimal.ZERO;
        }
        if (priceOfB.floatValue() < 0.f) {
            priceOfB = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return "InvestmentFund{" +
                " name=" + getClass().getSimpleName() +
                ", priceOfA=" + Utils.printMoney(priceOfA) +
                ", priceOfB=" + Utils.printMoney(priceOfB) +
                '}';
    }
}
