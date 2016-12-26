package ua.com.juja.yeryery.commands;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DisplayTest {

    private DatabaseManager manager;
    private View view;
    private Command command;
    private String selectedTable;

    @Before
    public void setup() {
        Logger.getRootLogger().setLevel(Level.OFF);
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Display(view, manager);
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";
    }

    @Test
    public void testDisplayTableData() {
        //given
        when(view.read()).thenReturn(selectedTable);
        when(manager.getTableColumns(selectedTable)).thenReturn(new LinkedHashSet<String>(Arrays.asList("name", "age")));

        DataSet user1 = new DataSetImpl();
        user1.put("name", "Mike");
        user1.put("age", "25");

        DataSet user2 = new DataSetImpl();
        user2.put("name", "Jack");
        user2.put("age", "28");

        List<DataSet> dataSets = new LinkedList<DataSet>();
        dataSets.add(user1);
        dataSets.add(user2);
        when(manager.getDataContent(selectedTable)).thenReturn(dataSets);

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "+----+---+\n" +
                "|name|age|\n" +
                "+----+---+\n" +
                "|Mike|25 |\n" +
                "+----+---+\n" +
                "|Jack|28 |\n" +
                "+----+---+]");
    }

    @Test
    public void testDisplayEmptyTableData() {
        //given
        when(view.read()).thenReturn(selectedTable);
        when(manager.getTableColumns(selectedTable)).thenReturn(new LinkedHashSet<String>(Arrays.asList("name", "age")));

        DataSet user = new DataSetImpl();

        List<DataSet> dataSets = new LinkedList<DataSet>();
        dataSets.add(user);
        when(manager.getDataContent(selectedTable)).thenReturn(dataSets);

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //test
                "+----+---+\n" +
                "|name|age|\n" +
                "+----+---+]");
    }

    @Test
    public void testDisplayAndSelectCancel() {
        //given
        when(view.read()).thenReturn("cancel");

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Table displaying canceled]");
    }

    @Test
    public void testDisplayAndSelectZero() {
        //given
        when(view.read()).thenReturn("0");

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //0
                "Table displaying canceled]");
    }

    @Test
    public void testDisplayAndSelectNotExistingTable() {
        //given
        when(view.read()).thenReturn("notExistingTable").thenReturn("cancel");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select notExistingTable
                "Table with name 'notExistingTable' doesn't exist!, " +
                "Try again., " +
                "Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Table displaying canceled]");
    }

    @Test
    public void testDisplayAndSelectWrongTableNumber() {
        //given
        when(view.read()).thenReturn("22").thenReturn("cancel");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select 22
                "There is no table with number 22!, " +
                "Try again., " +
                "Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //cancel
                "Table displaying canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessDisplay() {
        //when
        boolean canProcess = command.canProcess("display");

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
