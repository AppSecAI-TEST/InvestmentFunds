package org.otto.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Created by tomek on 2016-10-06.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FundUnitXmlElem {
    private String fundType;

    private String fundUnitType;

    private Integer amount;

    public FundUnitXmlElem() {
    }

    public FundUnitXmlElem(String fundType, String fundUnitType, Integer amount) {
        this.fundType = fundType;
        this.fundUnitType = fundUnitType;
        this.amount = amount;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundUnitType() {
        return fundUnitType;
    }

    public void setFundUnitType(String fundUnitType) {
        this.fundUnitType = fundUnitType;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
