package ua.com.juja.yeryery.commands.dialogs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SelectTableTest {

    private View view;
    private Dialog dialog;
    private final String ACTION = "display";

    @Before
    public void setup() {
        view = mock(View.class);
        dialog = new SelectTable();
    }

    @Test
    public void testSelectTableByNumber() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(view.read()).thenReturn("1");

        //when
        String actual = dialog.askUser(tableNames, view, ACTION);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //1
        assertEquals("test", actual);
    }

    @Test
    public void testSelectTableByName() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(view.read()).thenReturn("test");

        //when
        String actual = dialog.askUser(tableNames, view, ACTION);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //test
        assertEquals("test", actual);
    }

    @Test
    public void testSelectCancel() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(view.read()).thenReturn("cancel");

        //when
        String actual = dialog.askUser(tableNames, view, ACTION);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
        assertEquals("cancel", actual);
    }

    @Test
    public void testSelectZero() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(view.read()).thenReturn("0");

        //when
        String actual = dialog.askUser(tableNames, view, ACTION);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //0
        assertEquals("cancel", actual);
    }

    @Test
    public void testSelectNotExistTable() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(view.read()).thenReturn("notExist").thenReturn("cancel");

        //when
        String actual = dialog.askUser(tableNames, view, ACTION);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //notExist
                "Table with name 'notExist' doesn't exists! Try again., " +
                "Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
        assertEquals("cancel", actual);
    }

    @Test
    public void testSelectOutOfBoundsTableNumber() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        when(view.read()).thenReturn("3").thenReturn("cancel");

        //when
        String actual = dialog.askUser(tableNames, view, ACTION);

        //then
        shouldPrint("[Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back), " +
                //notExist
                "There is no table with this number! Try again., " +
                "Please enter the name or select number of table you want to display, " +
                "1. test, " +
                "2. ttable, " +
                "0. cancel (to go back)]");
                //cancel
        assertEquals("cancel", actual);
    }


    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
