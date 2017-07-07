package org.otto.ifunds;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tomek on 2016-10-06.
 */
public class IFundsRegistry {
    public static IFundsRegistry INSTANCE = new IFundsRegistry();

    public InvestmentFund getFund(FundType type) {
        switch (type) {
            case ACTIONS:
                return ActionsIFund.INSTANCE;
            case BALANCED:
                return BalancedIFund.INSTANCE;
            case BOND:
                return BondIFund.INSTANCE;
            case MONEY_MARKET:
                return MoneyMarketIFund.INSTANCE;
            case STABLE_GROWTH:
                return StableGrowthFund.INSTANCE;
            default:
                return null;
        }
    }

    public List<InvestmentFund> getAllFunds() {
        return Arrays.asList(ActionsIFund.INSTANCE, BalancedIFund.INSTANCE, BondIFund.INSTANCE,
                MoneyMarketIFund.INSTANCE, StableGrowthFund.INSTANCE);
    }

    public static void setRegistry(IFundsRegistry registry) { // for mocking singleton
        INSTANCE = registry;
    }

}
