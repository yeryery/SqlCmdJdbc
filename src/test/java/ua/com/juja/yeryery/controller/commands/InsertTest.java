package ua.com.juja.yeryery.controller.commands;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InsertTest {

    private DatabaseManager manager;
    private View view;
    private Command command;
    private Set<String> tableColumns;
    private List<DataSet> tableContent;
    private String selectedTable;

    @Before
    public void setup() {
        Logger.getRootLogger().setLevel(Level.OFF);
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Insert(view, manager);

        Set<String> tableNames = new LinkedHashSet<>(Arrays.asList("test", "users"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";

        TestTable testTable = new TestTable();
        tableColumns = testTable.getTableColumns();
        tableContent = testTable.getTableContent();
    }

    @Test
    public void testInsert() throws SQLException {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("id|5|name|Bob");
        when(manager.getTableColumns(selectedTable)).thenReturn(tableColumns);
        when(manager.getDataContent(selectedTable)).thenReturn(tableContent);

        //when
        command.process("insert");

        //then
        shouldPrint("[Select the table you need for 'insert' command, " +
                "1. test, " +
                "2. users, " +
                "0. cancel (to go back), " +
                //test
                "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back, " +
                //id|5|name|Bob
                "You have successfully entered new data into the table 'test', " +
                "+--+----+\n" +
                "|id|name|\n" +
                "+--+----+\n" +
                "|1 |John|\n" +
                "+--+----+\n" +
                "|2 |Mike|\n" +
                "+--+----+]");
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

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
