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

public class DropTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Drop(view, manager);
    }

    @Test
    public void testDropSelectTableAndConfirm() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("y").thenReturn("0");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you want to drop, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Table 'test' will be dropped! Continue? (y/n), " +
                //yes
                "Table 'test' successfully dropped!]");
    }

    @Test
    public void testDropSelectTableAndDontConfirm() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("n").thenReturn("0");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you want to drop, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Table 'test' will be dropped! Continue? (y/n), " +
                //no
                "Table dropping canceled]");
    }

    @Test
    public void testDropSelectTableAndNeitherInput() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("1").thenReturn("neither").thenReturn("y");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you want to drop, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table number 1
                "Table 'test' will be dropped! Continue? (y/n), " +
                //neither 'y' nor 'n'
                "Table 'test' will be dropped! Continue? (y/n), " +
                //yes
                "Table 'test' successfully dropped!]");
    }

    @Test
    public void testDropAndCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        when(view.read()).thenReturn("0");

        //when
        command.process("drop");

        //then
        shouldPrint("[Please enter the name or select number of table you want to drop, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select cancel
                "Table dropping canceled]");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessList() {
        //when
        boolean canProcess = command.canProcess("drop");

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
