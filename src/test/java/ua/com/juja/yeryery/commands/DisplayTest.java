package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DisplayTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Display(view, manager);
    }

    @Test
    public void testPrintTableDataSelectNumberOfTable() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1");
        when(manager.getTableColumns("test")).thenReturn(new String[] {"id", "name", "password"});

        DataSet user1 = new DataSet();
        user1.put("id", 12);
        user1.put("name", "username1");
        user1.put("password", "pass1");

        DataSet user2 = new DataSet();
        user2.put("id", 22);
        user2.put("name", "username2");
        user2.put("password", "pass2");

        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getDataContent("test")).thenReturn(data);

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "| id | name | password | , " +
                "-------------------------, " +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------]");
    }

    @Test
    public void testPrintTableDataTypeTableName() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("test");
        when(manager.getTableColumns("test")).thenReturn(new String[] {"id", "name", "password"});

        DataSet user1 = new DataSet();
        user1.put("id", 12);
        user1.put("name", "username1");
        user1.put("password", "pass1");

        DataSet user2 = new DataSet();
        user2.put("id", 22);
        user2.put("name", "username2");
        user2.put("password", "pass2");

        DataSet[] data = new DataSet[] {user1, user2};
        when(manager.getDataContent("test")).thenReturn(data);

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "| id | name | password | , " +
                "-------------------------, " +
                "| 12 | username1 | pass1 | \n" +
                "| 22 | username2 | pass2 | \n" +
                "------------------------]");
    }

    @Test
    public void testPrintEmptyTableData() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("test");
        when(manager.getTableColumns("test")).thenReturn(new String[] {"id", "name", "password"});

        DataSet user = new DataSet();

        DataSet[] data = new DataSet[] {user};
        when(manager.getDataContent("test")).thenReturn(data);

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                "| id | name | password | , " +
                "-------------------------, " +
                "| \n" +
                "------------------------]");
    }

    @Test
    public void testSelectCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        command.process("display");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
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
