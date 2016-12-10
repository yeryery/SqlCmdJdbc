package ua.com.juja.yeryery.commands;

import org.junit.Test;

import static org.junit.Assert.*;

public class HelpTest {

    private FakeView view = new FakeView();
    private Command command = new Help(view);

    @Test
    public void TestCanProcessHelpString() {
        //given

        //when
        boolean canProcess = command.canProcess("help");

        //then
        assertTrue(canProcess);
    }

    @Test
    public void TestCantProcessHelpWrongString() {
        //given

        //when
        boolean canProcess = command.canProcess("wrong");

        //then
        assertFalse(canProcess);
    }

    @Test
    public void TestProcessHelp() {
        //given

        //when
        command.process("help");

        //then
        assertEquals("Content of commands:\n" +
                      "\tconnect|database|username|password\n" +
                      "\t\tConnect to Database\n" +
                      "\tcontent\n" +
                      "\t\tContent of tables\n" +
                      "\tcreate\n" +
                      "\t\tCreate new table\n" +
                      "\tdisplay\n" +
                      "\t\tDisplay require table\n" +
                      "\tinsert\n" +
                      "\t\tInsert new data in require table\n" +
                      "\tclear\n" +
                      "\t\tClear require table\n" +
                      "\tdrop\n" +
                      "\t\tDrop require table\n" +
                      "\texit\n" +
                      "\t\tProgram exit\n" +
                      "\thelp\n" +
                      "\t\tAll commands\n", view.getContent());
    }
}
