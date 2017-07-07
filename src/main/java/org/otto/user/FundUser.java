package org.otto.user;

import org.otto.ifunds.*;
import org.otto.utils.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by tomek on 2016-10-06.
 */
public class FundUser {
    private BigDecimal budget;
    private Map<FundAndUnit, Integer> ownedFundUnits;

    private String name;

    public FundUser() {
        this(null, null);
    }

    public FundUser(BigDecimal budget) {
        this(budget, null);
    }

    public FundUser(BigDecimal budget, Map<FundAndUnit, Integer> ownedFundUnits) {
        this.budget = (budget != null) ? new BigDecimal(1000) : budget;
        this.budget.setScale(2, RoundingMode.HALF_EVEN);
        this.ownedFundUnits = (ownedFundUnits != null) ? ownedFundUnits : new HashMap<>();
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    private static BigDecimal provision = new BigDecimal(0.02f);

    /**
     * Buy some amount of fund units
     *
     * @param fundType types of investment fund
     * @param unitType unit type of investment fund
     * @param amount how many units will be bought
     * @return
     */
    public synchronized int buyFundUnits(FundType fundType, FundUnitType unitType, int amount) {
        final InvestmentFund fund = IFundsRegistry.INSTANCE.getFund(fundType);
        BigDecimal unitPrice = fund.priceOfUnit(unitType);
        FundAndUnit fundAndUnit = FundAndUnit.of(fundType, unitType);

        if (unitType == FundUnitType.UNIT_A) {
            unitPrice = unitPrice.add(unitPrice.multiply(provision));
        }

        final int numUnitsToBuy = Math.min(amount, budget.divide(unitPrice, BigDecimal.ROUND_FLOOR).intValue());

        BigDecimal moneyToSpend = unitPrice.multiply(new BigDecimal(numUnitsToBuy));

        BigDecimal prevBudget = budget;
        budget = budget.subtract(moneyToSpend).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("***** User: " + name + " bought " + numUnitsToBuy + " units of fund " + fundType
                + " and fund unit type " + unitType);
        System.out.println("***** User: " + name + " budget was: " + Utils.printMoney(prevBudget)
                + " and is: " + Utils.printMoney(budget));

        if (ownedFundUnits.containsKey(fundAndUnit)) {
            int numAlreadyOwned = ownedFundUnits.get(fundAndUnit);
            ownedFundUnits.put(fundAndUnit, numAlreadyOwned + numUnitsToBuy);
        } else {
            ownedFundUnits.put(fundAndUnit, numUnitsToBuy);
        }

        return numUnitsToBuy;
    }

    /**
     * Sell some amount of fund units
     *
     * @param fundType types of investment fund
     * @param unitType unit type of investment fund
     * @param amount how many units will be bought
     * @return
     */
    public synchronized int sellFundUnits(FundType fundType, FundUnitType unitType, int amount) {
        final InvestmentFund fund = IFundsRegistry.INSTANCE.getFund(fundType);
        final BigDecimal unitPrice = fund.priceOfUnit(unitType);
        FundAndUnit fundAndUnit = FundAndUnit.of(fundType, unitType);

        final int numAlreadyOwned = getNumUnitTypes(fundAndUnit);
        final int numUnitsToSell = Math.min(amount, getNumUnitTypes(fundAndUnit));

        if (numUnitsToSell > 0) {
            BigDecimal moneyToEarn = unitPrice.multiply(new BigDecimal(numUnitsToSell));

            if (unitType == FundUnitType.UNIT_B) {
                moneyToEarn = moneyToEarn.subtract( moneyToEarn.multiply(provision) );
            }

            BigDecimal prevBudget = budget;
            budget = budget.add(moneyToEarn).setScale(2, BigDecimal.ROUND_HALF_UP);
            ownedFundUnits.put(fundAndUnit, numAlreadyOwned - numUnitsToSell);

            System.out.println("***** User: " + name + " sold " + numUnitsToSell + " units of fund " + fundType
                    + " and fund unit type " + unitType);
            System.out.println("***** User: " + name + " budget was: " + Utils.printMoney(prevBudget)
                    + " and is: " + Utils.printMoney(budget));
        }

        return numUnitsToSell;
    }

    public int getNumUnitTypes(FundAndUnit fundAndUnit) {
        if (ownedFundUnits.containsKey(fundAndUnit)) {
            return ownedFundUnits.get(fundAndUnit);
        }
        return 0;
    }

    public boolean hasFundAndUnit(FundAndUnit fundAndUnit) {
        return ownedFundUnits.containsKey(fundAndUnit);
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public Set<FundAndUnit> getOwnedFundUnitTypes() {
        return this.ownedFundUnits.keySet();
    }

    public int getFundUnitAmount(FundAndUnit fundAndUnit) {
        return ownedFundUnits.get(fundAndUnit);
    }

    public FundUser setName(String name) {
        this.name = name;
        return this;
    }
}
