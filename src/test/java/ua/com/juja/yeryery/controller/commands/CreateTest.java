package ua.com.juja.yeryery.controller.commands;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Create.class)
public class CreateTest {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;
    private DataSet inputColumns;
    private Command command;
    private static final String TEST_TABLE = "test";
    private static final String ACTION = "create";

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        dialog = mock(Dialog.class);
        inputColumns = new DataSetImpl();
        command = new Create(view, manager);
    }

    @Test
    public void testCreateCommand() throws Exception {
        //given
        PowerMockito.whenNew(Dialog.class).withArguments(view, manager).thenReturn(dialog);
        when(dialog.nameTable()).thenReturn(TEST_TABLE);
        when(dialog.getNewColumns(TEST_TABLE)).thenReturn(inputColumns);

        //when
        command.process(ACTION);

        //then
        verify(view).write("Your table 'test' have successfully created!");
    }

}
