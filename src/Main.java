import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import static java.lang.System.out;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import static java.nio.file.StandardOpenOption.CREATE;
import javax.swing.JFileChooser;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        /**
         * the Scanner that will be used to take user input
         */
        Scanner in = new Scanner(System.in);

        /**
         * an ArrayList that will contain all the list items entered by the user
         */
        ArrayList<String> listContainer = new ArrayList<>();

        /**
         * a String that will log the user's input choice based on the menu options Add, Delete, Insert, View, Quit, Move, Open, Save, or Clear
         */
        String menuChoice = "";

        /**
         * a String that will take the value returned by the quitListMaker method
         */
        String menuChoiceContinue = "";

        /**
         * A String that will hold the filename of the current list file
         */
        String listName = "";

        /**
         * A file chooser that will be used to present the user with an interactive file directory
         */
        JFileChooser chooser = new JFileChooser();

        /**
         * A String that will take the list items read from an existing text file
         */
        String listItem = "";

        /**
         * A Path that will hold the filepath to the user's selected file from the openList method
         */
        Path openPath = null;

        /**
         * A boolean that will track when the user has made a modification to their ArrayList
         */
        boolean needsToBeSaved = false;

        /**
         * This algorithm tests if any of the methods run in the do ... while() loop below throw a FileNotFoundException or an IOException and outputs an error report if these exceptions are found
         */
        try {
            /**
             * this algorithm presents users with a menu of choices (Add, Delete, Insert, View, Quit, Move, Open, Save, or Clear) and modifies, prints, or saves the listContainer ArrayList accordingly based on the user's selection (or ends the program if quit is selected)
             */
            do {
                display(listContainer);

                menuChoice = SafeInput.getRegExString(in, "Enter A to add to the list, D to delete from the list, I to insert into the list, V to view the list,\nQ to quit, M to move a list item, O to open a list file from disk, S to save the current list to disk, or C to clear the list", "[AaDdIiVvQqMmOoSsCc]");

                /**
                 * this algorithm tests for which menu choice the user selected and runs the corresponding method, as well as managing when the needsToBeSaved flag changes between true and false
                 */
                switch (menuChoice) {
                    case "a":
                    case "A":
                        addItem(in, listContainer);
                        needsToBeSaved = true;
                        break;
                    case "d":
                    case "D":
                        if (!listContainer.isEmpty()) {
                            deleteItem(in, listContainer);
                            needsToBeSaved = true;
                        } else {
                            out.println("\nYou cannot delete an item from an empty list. Add an item first.");
                        }
                        break;
                    case "i":
                    case "I":
                        if (!listContainer.isEmpty()) {
                            insertItem(in, listContainer);
                            needsToBeSaved = true;
                        } else {
                            out.println("\nYou cannot insert an item into an empty list. Add an item first.");
                        }
                        break;
                    case "v":
                    case "V":
                        printList(listContainer);
                        break;
                    case "q":
                    case "Q":
                        menuChoiceContinue = quitListMaker(in, listContainer, listName, needsToBeSaved);
                        break;
                    case "m":
                    case "M":
                        moveItem(in, listContainer);
                        needsToBeSaved = true;
                        break;
                    case "o":
                    case "O":
                        openPath = openList(in, listContainer, listName, needsToBeSaved);

                        InputStream in2 =
                                new BufferedInputStream(Files.newInputStream(openPath, CREATE));
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(in2));

                        listName = openPath.getFileName().toString();

                        /**
                         * This int tracks how many lines have been read from the given list file
                         */
                        int line = 0;
                        while(reader.ready())
                        {
                            listItem = reader.readLine();
                            line++;

                            listContainer.add(listItem);
                            out.printf("\nItem %-3d: %-20s", line, listItem);
                        }
                        reader.close();
                        out.println("\n\nThe list has been opened.\n");
                        break;
                    case "s":
                    case "S":
                        saveFile(in, listContainer, listName);
                        needsToBeSaved = false;
                        break;
                    case "c":
                    case "C":
                        clearList(in, listContainer, listName, needsToBeSaved);
                        needsToBeSaved = true;
                        break;
                }
            } while (!menuChoiceContinue.equalsIgnoreCase("Q"));
        }
        catch(FileNotFoundException e)
        {
            out.println("The file could not be found.");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            out.println("An exception occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a String from the user to be added to the end of the ArrayList selected
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList to be modified
     */
    private static void addItem(Scanner pipe, ArrayList<String> list)
    {
        /**
         * a String that logs the list item input by the user
         */
        String listItem = "";
        listItem = SafeInput.getNonZeroLenString(pipe, "Please enter your list item");

        list.add(listItem);
    }

    /**
     * Outputs a numbered form of the chosen ArrayList, retrieves a ranged list item index from the user, and deletes the list item at that index of the chosen ArrayList
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList to be modified
     */
    private static void deleteItem(Scanner pipe, ArrayList<String> list)
    {
        /**
         * an int that logs the item index selected by the user
         */
        int itemIndex = 0;
        /**
         * an int that holds the minimum index the user can enter
         */
        int lowRange = 1;
        /**
         * an int that holds the maximum index the user can enter
         */
        int highRange = list.size();

        displayNumberedList(list);

        itemIndex = SafeInput.getRangedInt(pipe, "Enter the number of the item you want to delete", lowRange, highRange);

        itemIndex = itemIndex - 1;

        list.remove(itemIndex);
    }

    /**
     * Retrieves a String from the user, outputs a numbered form of the chosen ArrayList, retrieves a ranged list item index from the user, and inserts the user's String at the index selected
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList to be modified
     */
    private static void insertItem(Scanner pipe, ArrayList<String> list)
    {
        /**
         * a String that logs the list item input by the user
         */
        String listItem = "";
        /**
         * an int that logs the item index selected by the user
         */
        int itemIndex = 0;
        /**
         * an int that holds the minimum index the user can enter
         */
        int lowRange = 1;
        /**
         * an int that holds the maximum index the user can enter
         */
        int highRange = list.size();

        listItem = SafeInput.getNonZeroLenString(pipe, "Please enter your list item");

        displayNumberedList(list);

        itemIndex = SafeInput.getRangedInt(pipe, "Enter the list number where you want your item inserted", lowRange, highRange);

        itemIndex = itemIndex - 1;

        list.add(itemIndex, listItem);
    }

    /**
     * Displays each item in the inputted ArrayList with additional formatting
     *
     * @param list the ArrayList to be printed
     */
    private static void printList(ArrayList<String> list)
    {
        for(int x = 0; x < list.size(); x++)
        {
            out.println(list.get(x));
        }
        out.println("-----------------------");
        out.println();
    }

    /**
     * If the user's ArrayList isn't saved, this method offers the user the option to save the list; the method then asks the user if they are sure they want to quit; if so, ends the program; if not, returns an empty String
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList the user is passing to the saveFile method
     * @param listName the name of the list that the user is passing to the saveFile method
     * @param needsToBeSaved boolean that determines if the user is prompted to save their list
     * @return an empty String (to clear the user's menu input above)
     * @throws FileNotFoundException an error that is thrown if the user's chosen file cannot be found
     * @throws IOException an error that is thrown if any input/output error is found that is not the FileNotFoundException
     */
    private static String quitListMaker(Scanner pipe, ArrayList<String> list, String listName, boolean needsToBeSaved) throws FileNotFoundException, IOException
    {
        /**
         * a boolean that holds the true/false value output by getYNConfirm
         */
        boolean quit = false;

        /**
         * a String variable that holds an empty String to be returned if the program does not end
         */
        String menuChoice = "";

        /**
         * a boolean that tracks whether the user said yes or no to saving the list
         */
        boolean save = false;

        if(needsToBeSaved) {
            save = SafeInput.getYNConfirm(pipe, "Your list isn't saved. If you do not save your list now, you will lose it. Would you like to save your list now?");

            if(save) {
                saveFile(pipe, list, listName);
            }
        }

        quit = SafeInput.getYNConfirm(pipe, "Are you sure you want to quit?");

        if(quit) {
            System.exit(0);
        }

        return menuChoice;
    }

    /**
     * Prints all items in the inputted ArrayList and prints the menu of possible user choices (Add, Delete, Insert, View, Quit, Move, Open, Save, and Clear)
     *
     * @param list the ArrayList to be printed
     */
    private static void display(ArrayList<String> list)
    {
        /**
         * a String to hold the possible menu choices the user can make
         */
        String menu = "\nMenu: Add / Delete / Insert / View / Quit / Move / Open / Save / Clear";

        for(int x = 0; x < list.size(); x++)
        {
            out.println(list.get(x));
        }

        out.println(menu);
    }

    /**
     * Prints all items in the inputted ArrayList with human-understandable indices (indices that start at 1) next to them
     *
     * @param list the ArrayList to be printed
     */
    private static void displayNumberedList(ArrayList<String> list)
    {
        for(int x = 0; x < list.size(); x++)
        {
            int y = 0;
            y = x + 1;
            out.print(y + ". ");
            out.println(list.get(x));
        }
    }

    /**
     * Allows the user to 'move' an item in the selected ArrayList from one index to another by removing the selected item from its original index and adding it to the new index selected by the user
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList that is being modified
     */
    private static void moveItem(Scanner pipe, ArrayList<String> list)
    {
        /**
         * An int that tracks which list item will be moved based on its original index/location in the ArrayList
         */
        int origItemIndex = 0;

        /**
         * An int that tracks which index to move the selected list item to
         */
        int newItemIndex = 0;

        /**
         * A String that holds the name of the list item while it is being moved
         */
        String movedItem = "";

        /**
         * An int that defines the lowest array index the user can select
         */
        int lowRange = 1;

        /**
         * An int that defines the highest array index the user can select
         */
        int highRange = list.size();

        displayNumberedList(list);

        origItemIndex = SafeInput.getRangedInt(pipe, "Enter the number of the item you want to move", lowRange, highRange);

        origItemIndex = origItemIndex - 1;

        movedItem = list.remove(origItemIndex);

        highRange = highRange - 1;

        displayNumberedList(list);

        newItemIndex = SafeInput.getRangedInt(pipe, "Enter the number of the location where you would like to put this list item", lowRange, highRange);

        newItemIndex = newItemIndex - 1;

        list.add(newItemIndex, movedItem);
    }

    /**
     * This method asks the user to save their existing list if it is not saved, clears the existing list in Intellij, allows the user to select a new text file to import, and returns the filepath to the main method
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList that is being saved and cleared
     * @param listName the name of the list that is being saved and cleared
     * @param needsToBeSaved boolean that determines if the user is prompted to save their list
     * @return the filepath that the main method will use to locate the file and retrieve the selected list and filename
     * @throws FileNotFoundException an error that is thrown if the user's chosen file cannot be found
     * @throws IOException an error that is thrown if any input/output error is found that is not the FileNotFoundException
     */
    private static Path openList(Scanner pipe, ArrayList<String> list, String listName, boolean needsToBeSaved) throws FileNotFoundException, IOException
    {
        /**
         * A file chooser that will be used to present the user with an interactive file directory
         */
        JFileChooser chooser = new JFileChooser();

        /**
         * A File that holds the file selected by the user
         */
        File selectedFile;

        /**
         * A Path that holds the Path equivalent of selectedFile
         */
        Path file = null;

        clearList(pipe, list, listName, needsToBeSaved);

        /**
         * The directory that will open when JFileChooser pop-up appears
         */
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            selectedFile = chooser.getSelectedFile();
            file = selectedFile.toPath();

            return file;
        } else
        {
            out.println("\nYou didn't select a list to open. Re-run the program to open a list.");
            System.exit(0);
        }
        return file;
    }

    /**
     * This method writes the user's Intellij list to a text file, 'saving' it; if the listName is empty, also asks the user to name the file
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList that is being saved to a text file
     * @param listName the filename of the list that is being saved
     * @throws FileNotFoundException an error that is thrown if the user's chosen file cannot be found
     * @throws IOException an error that is thrown if any input/output error is found that is not the FileNotFoundException
     */
    private static void saveFile(Scanner pipe, ArrayList<String> list, String listName) throws FileNotFoundException, IOException
    {
        /**
         * The directory that will open when JFileChooser pop-up appears
         */
        File workingDirectory = new File(System.getProperty("user.dir"));

        if(listName.isEmpty())
        {
            listName = SafeInput.getRegExString(pipe, "Please enter the name of your list", "[a-zA-Z0-9_]+");

            /**
             * The Path that holds the working directory and the filename chosen by the user
             */
            Path file = Paths.get(workingDirectory.getPath() + "\\" + listName + ".txt");

            OutputStream out =
                    new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out));

            for(String l:list)
            {
                writer.write(l, 0, l.length());
                writer.flush();
                writer.newLine();
            }
            writer.close();

            System.out.println("\nYour list has been saved!");
        }else {
            /**
             * The Path that holds the working directory and the existing filename
             */
            Path file = Paths.get(workingDirectory.getPath() + "\\" + listName);

            OutputStream out =
                    new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer =
                    new BufferedWriter(new OutputStreamWriter(out));

            BufferedWriter writer2 = Files.newBufferedWriter(file);
            writer2.write("");
            writer2.flush();

            for(String l:list)
            {
                writer.write(l, 0, l.length());
                writer.newLine();
            }
            writer.close();

            System.out.println("\nYour list has been saved!");
        }
    }

    /**
     * This method tests if the user's file needs to be saved and clears the inputted ArrayList
     *
     * @param pipe the Scanner that is used to take user input
     * @param list the ArrayList that is being saved and cleared
     * @param listName the name of the ArrayList that is being passed to the saveFile method
     * @param needsToBeSaved boolean that determines if the user is prompted to save their list
     * @throws FileNotFoundException an error that is thrown if the user's chosen file cannot be found
     * @throws IOException an error that is thrown if any input/output error is found that is not the FileNotFoundException
     */
    private static void clearList(Scanner pipe, ArrayList<String> list, String listName, boolean needsToBeSaved) throws FileNotFoundException, IOException
    {
        /**
         * a boolean that tracks whether the user said yes or no to saving the list
         */
        boolean save = false;

        if(needsToBeSaved) {
            save = SafeInput.getYNConfirm(pipe, "Your list isn't saved. If you do not save your list now, you will lose it. Would you like to save your list now?");

            if(save) {
                saveFile(pipe, list, listName);
            }
        }
        list.clear();
        System.out.println("\nYour list has been cleared!");
    }
}