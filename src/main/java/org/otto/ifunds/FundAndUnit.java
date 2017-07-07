package org.otto.ifunds;

import org.otto.user.FundUser;

/**
 * Created by tomek on 2016-10-06.
 */
public class FundAndUnit {
    private FundType fundType;
    private FundUnitType unitType;

    public FundAndUnit(FundType fundType, FundUnitType unitType) {
        this.fundType = fundType;
        this.unitType = unitType;
    }

    public static FundAndUnit of(FundType fundType, FundUnitType unitType) {
        return new FundAndUnit(fundType, unitType);
    }

    public FundType getFundType() {
        return fundType;
    }

    public FundUnitType getUnitType() {
        return unitType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FundAndUnit that = (FundAndUnit) o;

        if (fundType != that.fundType) return false;
        return unitType == that.unitType;

    }

    @Override
    public int hashCode() {
        int result = fundType != null ? fundType.hashCode() : 0;
        result = 31 * result + (unitType != null ? unitType.hashCode() : 0);
        return result;
    }
}
