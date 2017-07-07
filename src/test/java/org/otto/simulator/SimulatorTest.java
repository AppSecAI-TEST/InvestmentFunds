package org.otto.simulator;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.otto.ifunds.*;
import org.otto.user.FundUser;
import org.otto.xml.RootXmlElem;
import org.otto.xml.XmlHandler;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by tomek on 2016-10-06.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimulatorTest {

    @Before
    public void beforeTest() {
    }

    @After
    public void afterTest() {
    }

    @Test
    public void testSimulation() {
        Simulator simulator = new Simulator();
        IFundsRegistry fundsRegistry = IFundsRegistry.INSTANCE;
        final int times = 5;
        IntStream.range(0, times).forEach(idx -> simulator.runFundsSimulationStep());
        Assertions.assertThat(fundsRegistry.getFund(FundType.BALANCED).priceOfUnit(FundUnitType.UNIT_A).floatValue())
            .isGreaterThan(100.f - times*0.8f);
        Assertions.assertThat(fundsRegistry.getFund(FundType.ACTIONS).priceOfUnit(FundUnitType.UNIT_A).floatValue())
                .isLessThan(100.f + times*1.1f);
    }

    @Mock
    IFundsRegistry mockedFundsRegistry;

    @Mock
    InvestmentFund mockedInvestmentFund;

    @Test
    public void testBuyAndSellUnits() {
        IFundsRegistry.setRegistry(mockedFundsRegistry);
        when(mockedFundsRegistry.getFund(anyObject())).thenReturn(mockedInvestmentFund);
        when(mockedInvestmentFund.priceOfUnit(eq(FundUnitType.UNIT_A))).thenReturn(new BigDecimal(90));
        when(mockedInvestmentFund.priceOfUnit(eq(FundUnitType.UNIT_B))).thenReturn(new BigDecimal(110));

        // buy
        FundUser fundUser = new FundUser(new BigDecimal(1000));
        int numUnitsBought = fundUser.buyFundUnits(FundType.BALANCED, FundUnitType.UNIT_B, 5);
        Assertions.assertThat(numUnitsBought).isEqualTo(5);
        Assertions.assertThat(fundUser.getBudget().floatValue()).isEqualTo(450.f);
        numUnitsBought = fundUser.buyFundUnits(FundType.BALANCED, FundUnitType.UNIT_A, 5);
        Assertions.assertThat(numUnitsBought).isEqualTo(4);
        Assertions.assertThat(fundUser.getBudget().floatValue()).isEqualTo(82.8f);
        Assertions.assertThat(fundUser.getNumUnitTypes(FundAndUnit.of(FundType.BALANCED, FundUnitType.UNIT_A))).isEqualTo(4);
        Assertions.assertThat(fundUser.getNumUnitTypes(FundAndUnit.of(FundType.BALANCED, FundUnitType.UNIT_B))).isEqualTo(5);

        // sell
        int numUnitsSold = fundUser.sellFundUnits(FundType.BALANCED, FundUnitType.UNIT_A, 6);
        Assertions.assertThat(numUnitsSold).isEqualTo(4);
        Assertions.assertThat(fundUser.getBudget().floatValue()).isEqualTo(442.8f);
        Assertions.assertThat(fundUser.getNumUnitTypes(FundAndUnit.of(FundType.BALANCED, FundUnitType.UNIT_A))).isEqualTo(0);

        numUnitsSold = fundUser.sellFundUnits(FundType.BALANCED, FundUnitType.UNIT_B, 4);
        Assertions.assertThat(numUnitsSold).isEqualTo(4);
        Assertions.assertThat(fundUser.getBudget().floatValue()).isEqualTo(874.0f);
        Assertions.assertThat(fundUser.getNumUnitTypes(FundAndUnit.of(FundType.BALANCED, FundUnitType.UNIT_B))).isEqualTo(1);
    }

    @Ignore
    @Test
    public void checkXmlSave_Integration() throws Exception {
        Map<FundAndUnit, Integer> ownedFundUnits1 = new HashMap<>();
        ownedFundUnits1.put(FundAndUnit.of(FundType.BALANCED, FundUnitType.UNIT_A), 2);
        ownedFundUnits1.put(FundAndUnit.of(FundType.BALANCED, FundUnitType.UNIT_B), 3);
        ownedFundUnits1.put(FundAndUnit.of(FundType.ACTIONS, FundUnitType.UNIT_B), 5);

        Map<FundAndUnit, Integer> ownedFundUnits2 = new HashMap<>();
        ownedFundUnits2.put(FundAndUnit.of(FundType.MONEY_MARKET, FundUnitType.UNIT_A), 20);
        ownedFundUnits2.put(FundAndUnit.of(FundType.MONEY_MARKET, FundUnitType.UNIT_B), 30);

        List<FundUser> fundUsers = Arrays.asList(
            new FundUser(new BigDecimal(1000), ownedFundUnits1),
            new FundUser(new BigDecimal(10000), ownedFundUnits2)
        );

        XmlHandler handler = new XmlHandler("C:\\backup\\out1.xml");
        handler.save(fundUsers);
    }

    @Ignore
    @Test
    public void checkXmlLoad_Integration() throws Exception {
        XmlHandler handler = new XmlHandler("C:\\backup\\out1.xml");
        List<FundUser> fundUsers = handler.load();
        Assertions.assertThat(fundUsers.size()).isEqualTo(2);
    }

    @Ignore
    @Test
    public void tesRuntSimulation_Integration() {
        Simulator simulator = new Simulator();
        simulator.runSimulation(10, "C:\\backup\\out2.xml", "C:\\backup\\out3.xml");
    }

}
