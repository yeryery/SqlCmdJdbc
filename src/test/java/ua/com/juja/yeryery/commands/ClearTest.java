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

public class ClearTest {

    private DatabaseManager manager;
    private View view;
    private Command command;
    private Set<String> tableNames;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Clear(view, manager);
        tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
    }

    @Test
    public void testClearSelectTableAndConfirm() {
        //given
        when(view.read()).thenReturn("1").thenReturn("y");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to clear, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Table 'test' will be cleared! Continue? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearSelectTableAndDontConfirm() {
        //given
        when(view.read()).thenReturn("1").thenReturn("n");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to clear, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Table 'test' will be cleared! Continue? (y/n), " +
                //no
                "Table clearing canceled]");
    }

    @Test
    public void testClearSelectTableAndNeitherInput() {
        //given
        when(view.read()).thenReturn("1").thenReturn("neither").thenReturn("y");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to clear, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Table 'test' will be cleared! Continue? (y/n), " +
                //neither 'y' nor 'n'
                "Table 'test' will be cleared! Continue? (y/n), " +
                //yes
                "Table 'test' successfully cleared!]");
    }

    @Test
    public void testClearAndSelectNull() {
        //given
        when(view.read()).thenReturn("0");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to clear, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select cancel
                "Table clearing canceled]");
    }

    @Test
    public void testClearAndSelectCancel() {
        //given
        when(view.read()).thenReturn("cancel");

        //when
        command.process("clear");

        //then
        shouldPrint("[Please enter the name or select number of table you want to clear, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select cancel
                "Table clearing canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
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
}
