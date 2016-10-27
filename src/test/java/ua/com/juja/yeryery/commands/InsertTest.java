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
    public void testInsertSelectTableAndConfirm() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(manager.getTableColumns("ttable")).thenReturn(new String[] {"id", "name", "password"});

        DataSet user = new DataSet();
        user.put("id", 1);
        user.put("name", "username1");
        user.put("password", "pass1");
        when(view.read()).thenReturn("2")
                         .thenReturn(user.getValues()[0].toString())
                         .thenReturn(user.getValues()[1].toString())
                         .thenReturn(user.getValues()[2].toString());

        DataSet[] data = new DataSet[] {user};
        when(manager.getDataContent("test")).thenReturn(data);

        //when
        command.process("insert");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Enter the values you require, " +
                "id, " +
                //1
                "name, " +
                //username1
                "password, " +
                //pass1
                "You have successfully entered new data!]");
    }

    @Test
    public void testInsertAndSelectZero() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        command.process("insert");

        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select 0
                "Table inserting canceled]");
    }

    @Test
    public void testInsertAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        command.process("insert");

        shouldPrint("[Please enter the name or select number of table you need, " +
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
    public void testCanProcessList() {
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
