package org.otto.xml;

import org.otto.ifunds.FundAndUnit;
import org.otto.ifunds.FundType;
import org.otto.ifunds.FundUnitType;
import org.otto.user.FundUser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomek on 2016-10-06.
 */
public class XmlHandler {

    private String fileName;

    public XmlHandler(String fileName) {
        this.fileName = fileName;
    }

    public List<FundUser> load() throws Exception {
        File file = new File(fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(RootXmlElem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        RootXmlElem rootXml = (RootXmlElem) jaxbUnmarshaller.unmarshal(file);
        return convertFromXmlTree(rootXml);
    }

    public void save(List<FundUser> fundUsers) throws Exception {
        final RootXmlElem root = convertToXmlTree(fundUsers);

        File file = new File(fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(RootXmlElem.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(root, file);
        jaxbMarshaller.marshal(root, System.out);
    }

    private RootXmlElem convertToXmlTree(List<FundUser> fundUsers) {
        RootXmlElem rootXml = new RootXmlElem();
        for (FundUser user : fundUsers) {
            UserXmlElem userXml = new UserXmlElem();

            userXml.setBudget(user.getBudget().doubleValue());

            for (FundAndUnit fundUnitType : user.getOwnedFundUnitTypes()) {
                int amount = user.getFundUnitAmount(fundUnitType);
                if (amount > 0) {
                    userXml.addFundUnit(new FundUnitXmlElem(
                            fundUnitType.getFundType().toString(),
                            fundUnitType.getUnitType().toString(),
                            amount
                            ));

                }
            }

            rootXml.addUser(userXml);
        }

        return rootXml;
    }

    public List<FundUser> convertFromXmlTree(RootXmlElem root) {
        List<FundUser> fundUsers = new ArrayList<>();
        int userId = 1;
        for (UserXmlElem userXml : root.getUsers()) {
            Map<FundAndUnit, Integer> ownedFundUnits = new HashMap<>();
            BigDecimal budget = new BigDecimal(userXml.getBudget());

            for (FundUnitXmlElem fundUnitXml : userXml.getFundUnits()) {
                ownedFundUnits.put(FundAndUnit.of(FundType.valueOf(fundUnitXml.getFundType()), FundUnitType.valueOf(fundUnitXml.getFundUnitType())),
                    fundUnitXml.getAmount() );
            }

            fundUsers.add( new FundUser(budget, ownedFundUnits).setName("User" + userId++) );

        }
        return fundUsers;
    }

}
