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

    private View view;
    private Command command;
    private String selectedTable;

    @Before
    public void setup() {
        DatabaseManager manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Drop(view, manager);
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(manager.getTableNames()).thenReturn(tableNames);
        selectedTable = "test";
    }

    @Test
    public void testDropSelectTableNameAndConfirm() {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("y");

        //when
        command.process("drop");

        //then
        shouldPrint("[Select the table you need for 'drop' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "Table 'test' will be dropped! Continue? (y/n), " +
                //yes
                "Table 'test' successfully dropped!]");
    }

    @Test
    public void testDropSelectTableNumberAndConfirm() {
        //given
        when(view.read()).thenReturn("1").thenReturn("y");

        //when
        command.process("drop");

        //then
        shouldPrint("[Select the table you need for 'drop' command, " +
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
        when(view.read()).thenReturn(selectedTable).thenReturn("n");

        //when
        try {
            command.process("drop");
        } catch (CancelException e) {
        }

        //then
        shouldPrint("[Select the table you need for 'drop' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "Table 'test' will be dropped! Continue? (y/n)]");
                //no
    }

    @Test
    public void testDropSelectTableAndNeitherInput() {
        //given
        when(view.read()).thenReturn(selectedTable).thenReturn("neither").thenReturn("y");

        //when
        command.process("drop");

        //then
        shouldPrint("[Select the table you need for 'drop' command, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //Select table 'test'
                "Table 'test' will be dropped! Continue? (y/n), " +
                //neither 'y' nor 'n'
                "Table 'test' will be dropped! Continue? (y/n), " +
                //yes
                "Table 'test' successfully dropped!]");
    }

    @Test
    public void testCanProcessDrop() {
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

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
