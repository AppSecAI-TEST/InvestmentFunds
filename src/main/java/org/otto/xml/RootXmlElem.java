package org.otto.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 2016-10-06.
 */
@XmlRootElement(name = "users")
public class RootXmlElem {

    @XmlElement(name = "user")
    private List<UserXmlElem> users = new ArrayList<>();

    public List<UserXmlElem> getUsers() {
        return users;
    }

    public void addUser(UserXmlElem user) {
        users.add(user);
    }
}
