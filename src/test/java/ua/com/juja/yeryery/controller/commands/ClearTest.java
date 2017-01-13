package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.controller.commands.CancelException;
import ua.com.juja.yeryery.controller.commands.Clear;
import ua.com.juja.yeryery.controller.commands.Command;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClearTest {

    private View view;
    private Command command;
    private String selectedTable;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(view, manager);
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";
    }

    @Test
    public void testClearSelectTableNameAndConfirm() {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("y");

        //when
        command.process("clear");

        //then
        shouldPrint("[Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "Are you sure you want to clear table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearSelectTableNumberAndConfirm() {
        //given
        when(view.read()).thenReturn("1").thenReturn("y");

        //when
        command.process("clear");

        //then
        shouldPrint("[Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Are you sure you want to clear table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearSelectTableAndDontConfirm() {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("n");

        //when
        try {
            command.process("clear");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "Are you sure you want to clear table 'test'? (y/n)]");
                //no
    }

    @Test
    public void testClearSelectTableAndNeitherInput() {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("neither").thenReturn("y");

        //when
        command.process("clear");

        //then
        shouldPrint("[Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "Are you sure you want to clear table 'test'? (y/n), " +
                //neither 'y' nor 'n'
                "Are you sure you want to clear table 'test'? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearAndSelectCancel() {
        //given
        when(view.read()).thenReturn("cancel");

        //when
        boolean checkException = false;
        try {
            command.process("clear");
            fail("Expected CancelException");
        } catch (Exception e) {
            checkException = e instanceof CancelException;
        }

        //then
        assertTrue(checkException);
    }

    @Test
    public void testClearAndSelectZero() {
        //given
        when(view.read()).thenReturn("0");

        //when
        boolean checkException = false;
        try {
            command.process("clear");
            fail("Expected CancelException");
        } catch (Exception e) {
            checkException = e instanceof CancelException;
        }

        //then
        assertTrue(checkException);
    }

    @Test
    public void testClearAndSelectNotExistingTable() {
        //given
        when(view.read()).thenReturn("notExistingTable").thenReturn("cancel");

        //when
        try {
            command.process("clear");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select notExistingTable
                "Error! Table with name 'notExistingTable' doesn't exist\n" +
                "Try again, " +
                "Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel)
    }

    @Test
    public void testClearAndSelectWrongTableNumber() {
        //given
        when(view.read()).thenReturn("22").thenReturn("cancel");

        //when
        try {
            command.process("clear");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select 22
                "Error! There is no table with number 22\n" +
                "Try again, " +
                "Select the table you need for 'clear' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
    }

    @Test
    public void testCanProcessClear() {
        //when
        boolean canProcess = command.canProcess("clear");

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