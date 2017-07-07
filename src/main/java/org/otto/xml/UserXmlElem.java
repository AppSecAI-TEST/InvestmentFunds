package org.otto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 2016-10-06.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserXmlElem {

    @XmlElement(name = "budget")
    private Double budget;

    @XmlElement(name = "fund-unit")
    private List<FundUnitXmlElem> fundUnits = new ArrayList<>();

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public List<FundUnitXmlElem> getFundUnits() {
        return fundUnits;
    }

    public void addFundUnit(FundUnitXmlElem fundUnit) {
        fundUnits.add(fundUnit);
    }
}
