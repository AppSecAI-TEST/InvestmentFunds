package org.otto.simulator;

import org.otto.ifunds.*;
import org.otto.user.FundUser;
import org.otto.utils.Utils;
import org.otto.xml.XmlHandler;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Created by tomek on 2016-10-06.
 */
public class Simulator {
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private List<FundUser> fundUsers = new ArrayList<>();

    public Simulator addFundUser(BigDecimal budget, Map<FundAndUnit, Integer> ownedFundUnits) {
        fundUsers.add(new FundUser(budget, ownedFundUnits));
        return this;
    }

    public FundUser getFundUser(int idx) {
        return fundUsers.get(idx);
    }

    public List<FundUser> getFundUsers() {
        return fundUsers;
    }

    /**
     * Run single simulation step for all investment funds
     */
    public void runFundsSimulationStep() {
        List<InvestmentFund> funds = IFundsRegistry.INSTANCE.getAllFunds();
        funds.forEach(fund -> {
            System.out.println("Before simulation step: " + fund);
            fund.runSimulationStep();
            System.out.println("After simulation step: " + fund);
        });
    }

    /**
     * Run whole simulation
     *
     * @param times how many times simulation will be run
     * @param inputFile xml with input configuration
     * @param outputFile xml with output configuration
     */
    public void runSimulation(final int times, final String inputFile, final String outputFile) {
        loadFundUsers(inputFile);
        final int numUsers = fundUsers.size();

        CountDownLatch latch = new CountDownLatch(2);

        final Random rnd = new Random();
        executorService.execute(() -> {

            IntStream.range(0, 2*times).forEach(step -> {
                FundUser selectedUser = fundUsers.get( rnd.nextInt(numUsers) );
                boolean willBuy = (rnd.nextInt(2) == 0);
                FundType[] fundTypes = FundType.values();
                FundUnitType[] fundUnitTypes = FundUnitType.values();
                if (willBuy) {
                    selectedUser.buyFundUnits(fundTypes[rnd.nextInt(fundTypes.length)], fundUnitTypes[rnd.nextInt(fundUnitTypes.length)],
                            rnd.nextInt(10) + 1);
                } else {
                    selectedUser.sellFundUnits(fundTypes[rnd.nextInt(fundTypes.length)], fundUnitTypes[rnd.nextInt(fundUnitTypes.length)],
                            rnd.nextInt(10) + 1);
                }
                Utils.sleepMs(rnd.nextInt(500) + 500);
            });

            saveFundUsers(outputFile);
            latch.countDown();
        });

        executorService.execute(() -> {

            IntStream.range(0, times).forEach(step -> {
                System.out.println("*** Step: " + (step + 1) + " ***");
                runFundsSimulationStep();
                Utils.sleep(1);
            });

            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load fund users from xml file
     *
     * @param file
     */
    public void loadFundUsers(String file) {
        if (file == null) {
            fundUsers = loadSampleConfiguration();
            return;
        }

        XmlHandler handler = new XmlHandler(file);
        try {
            fundUsers = handler.load();
        } catch (Exception e) {
            e.printStackTrace();
            fundUsers = loadSampleConfiguration();
        }
    }

    /**
     * Save fund users in xml file
     *
     * @param file
     */
    public void saveFundUsers(String file) {
        XmlHandler handler = new XmlHandler(file);
        try {
            handler.save(fundUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<FundUser> loadSampleConfiguration() {
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

        return fundUsers;
    }
}
