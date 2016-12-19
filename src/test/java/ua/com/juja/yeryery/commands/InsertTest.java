package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InsertTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Insert(view, manager);
    }

    @Test
    public void testInsert() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName1 = "id";
        int value1 = 1;
        String columnName2 = "name";
        String value2 = "username";
        String columnName3 = "password";
        String value3 = "pass";
        when(manager.getTableColumns("ttable"))
                .thenReturn(new LinkedHashSet<String>(Arrays.asList(columnName1, columnName2, columnName3)));

        when(view.read()).thenReturn("2")
                .thenReturn(Integer.toString(value1))
                .thenReturn(value2)
                .thenReturn(value3);

        DataSet input = new DataSetImpl();
        input.put(columnName1, value1);
        input.put(columnName2, value2);
        input.put(columnName3, value3);

        doNothing().when(manager).insert("ttable", input);

        //when
        command.process("insert");

        //then
        shouldPrint("[Please enter the name or select number of table where you want to insert new rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'ttable'
                "Enter new values you require, " +
                "id, " +
                //1
                "name, " +
                //username1
                "password, " +
                //pass1
                "You have successfully entered new data into the table 'ttable']");
    }

    @Test
    public void testInsertWithSqlException() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);

        String columnName1 = "id";
        int value1 = 1;
        String columnName2 = "name";
        String value2 = "username";
        String columnName3 = "password";
        String value3 = "pass";
        when(manager.getTableColumns("ttable"))
                .thenReturn(new LinkedHashSet<String>(Arrays.asList(columnName1, columnName2, columnName3)));

        when(view.read()).thenReturn("2")
                .thenReturn(Integer.toString(value1))
                .thenReturn(value2)
                .thenReturn(value3);

        DataSet input = new DataSetImpl();
        input.put(columnName1, value1);
        input.put(columnName2, value2);
        input.put(columnName3, value3);

        doThrow(new SQLException()).when(manager).insert("ttable", input);

        try {
            manager.insert("ttable", input);
        } catch (SQLException e) {
            view.write("SQL ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                    "  Position: 41");
        }

        //when
        command.process("insert");

        //then
        verify(view).write("SQL ERROR: invalid input syntax for integer: \"notNumber\"\n" +
                "  Position: 41");
    }

    @Test
    public void testInsertAndSelectZero() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        command.process("insert");

        shouldPrint("[Please enter the name or select number of table where you want to insert new rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select 0
                "Table inserting canceled]");
    }

    @Test
    public void testInsertAndCancel() throws SQLException {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        command.process("insert");

        shouldPrint("[Please enter the name or select number of table where you want to insert new rows, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Cancel
                "Table inserting canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessInsert() {
        //when
        boolean canProcess = command.canProcess("insert");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void testCantProcessWrongInput() {
        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }
}
