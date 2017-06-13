package ua.com.juja.yeryery.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ua.com.juja.yeryery.model.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testApplicationContext.xml")

public class ServiceImplTest {

    @Autowired
    private Service service;

    @Test
    public void test() throws SQLException {
        //given
        DatabaseManager manager = service.connect("database", "login", "password");

        DataEntry entry = new DataEntryImpl();
        DataSet columns = new DataSetImpl();
        DataSet input = new DataSetImpl();
        input.put("name", "John");
        input.put("password", "pass");
        input.put("id", "21");
        manager.create("users", entry, columns);
        manager.insert("users", input);

        //when
        List<List<String>> users = service.constructTable(manager, "users");

        //then
        assertEquals("[[name, password, id], [John, pass, 21]]", users.toString());

    }
}
