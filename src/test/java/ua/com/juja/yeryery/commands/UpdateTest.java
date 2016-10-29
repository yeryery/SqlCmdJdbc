package ua.com.juja.yeryery.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UpdateTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Update(view, manager);
    }

    @Test
    public void testUpdate() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("test").thenReturn("22|password|newPass");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                    "1. test, " +
                    "2. ttable, " +
                    "0. cancel (to go back), " +
                    "Enter id you want to update and its new values: id|columnName1|newValue1|columnName2|newValue2..., " +
                    "You have successfully updated table 'test' at id = 22]");
    }

    @Test
    public void testUpdateWrongNumberOfParameters() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("test").thenReturn("22|password|newPass|smth").thenReturn("22|password|newPass");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                    "1. test, " +
                    "2. ttable, " +
                    "0. cancel (to go back), " +
                    "Enter id you want to update and its new values: id|columnName1|newValue1|columnName2|newValue2..., " +
                    "You should enter an odd number of parameters: id|columnName1|newValue1|columnName2|newValue2...\n" +
                    "Please, try again, " +
                    "You have successfully updated table 'test' at id = 22]");
    }

    @Test
    public void testUpdateAndSelectZero() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select 0
                "Table updating canceled]");
    }

    @Test
    public void testUpdateAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("cancel");

        //when
        command.process("update");

        //then
        shouldPrint("[Please enter the name or select number of table you need, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Cancel
                "Table updating canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessUpdate() {
        //when
        boolean canProcess = command.canProcess("update");

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
