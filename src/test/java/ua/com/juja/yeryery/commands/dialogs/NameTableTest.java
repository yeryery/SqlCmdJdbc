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

public class NameTableTest {

    private View view;
    private Dialog dialog;
    private final String ACTION = "create";

    @Before
    public void setup() {
        view = mock(View.class);
        dialog = new DialogImpl(view);
    }

    @Test
    public void testNameTableWithOriginalName() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        String message = String.format("Please enter the name of table you want to %s or 'cancel' to go back", ACTION);
        when(view.read()).thenReturn("myTable");

        //when
        String actual = dialog.NameTable(tableNames, message);

        //then
        verify(view).write("Please enter the name of table you want to create or 'cancel' to go back");
        //myTable
        assertEquals("myTable", actual);
    }

    @Test
    public void testNameTableWithExistingName() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        String message = String.format("Please enter the name of table you want to %s or 'cancel' to go back", ACTION);
        when(view.read()).thenReturn("test").thenReturn("cancel");

        //when
        String actual = dialog.NameTable(tableNames, message);

        //then
        shouldPrint("[Please enter the name of table you want to create or 'cancel' to go back, " +
                //test
                "Table with name 'test' already exists!, " +
                "[test, ttable], " +
                "Try again., " +
                "Please enter the name of table you want to create or 'cancel' to go back]");
        //cancel
    }

    @Test
    public void testNameTableWhenNameStartsWithNumber() {
        //given
        Set<String> tableNames = new LinkedHashSet<String>(Arrays.asList("test", "ttable"));
        String message = String.format("Please enter the name of table you want to %s or 'cancel' to go back", ACTION);
        when(view.read()).thenReturn("1name").thenReturn("cancel");

        //when
        String actual = dialog.NameTable(tableNames, message);

        //then
        verify(view).write("Table name must begin with a letter! Try again.");
    }

    private void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
