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
         * a String that will log the user's input choice based on the menu options Add, Delete, Insert, Print, or Quit
         */
        String menuChoice = "";
        /**
         * a String that will take the value returned by the quitListMaker method
         */
        String menuChoiceContinue = "";

        /**
         * this algorithm presents users with a menu of choices (Add, Delete, Insert, Print, or Quit) and modifies the listContainer ArrayList accordingly based on the user's selection (or ends the program if quit is selected)
         */
        do {
            display(listContainer);

            menuChoice = SafeInput.getRegExString(in, "Enter A to add to the list, D to delete from the list, I to insert into the list, P to print the list, or Q to quit", "[AaDdIiPpQq]");

            /**
             * this algorithm tests for which menu choice the user selected and runs the corresponding method
             */
            switch (menuChoice)
            {
                case "a":
                case "A":
                    addItem(in, listContainer);
                    break;
                case "d":
                case "D":
                    if(!listContainer.isEmpty())
                    {
                        deleteItem(in, listContainer);
                    }else
                    {
                        System.out.println("\nYou cannot delete an item from an empty list. Add an item first.");
                    }
                    break;
                case "i":
                case "I":
                    if(!listContainer.isEmpty())
                    {
                        insertItem(in, listContainer);
                    }else
                    {
                        System.out.println("\nYou cannot insert an item into an empty list. Add an item first.");
                    }
                    break;
                case "p":
                case "P":
                    printList(listContainer);
                    break;
                case "q":
                case "Q":
                    menuChoiceContinue = quitListMaker(in);
                    break;
            }
        }while(!menuChoiceContinue.equalsIgnoreCase("Q"));
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
            System.out.println(list.get(x));
        }
        System.out.println("-----------------------");
        System.out.println();
    }

    /**
     * Asks the user if they are sure they want to quit; if so, ends the program; if not, returns an empty String
     *
     * @param pipe the Scanner that is used to take user input
     */
    private static String quitListMaker(Scanner pipe)
    {
        /**
         * a boolean that holds the true/false value output by getYNConfirm
         */
        boolean quit = false;
        /**
         * a String variable that holds an empty String to be returned if the program does not end
         */
        String menuChoice = "";

        quit = SafeInput.getYNConfirm(pipe, "Are you sure you want to quit?");

        if(quit) {
            System.exit(0);
        }

        return menuChoice;
    }

    /**
     * Prints all items in the inputted ArrayList and prints the menu of possible user choices (Add, Delete, Insert, Print, and Quit)
     *
     * @param list the ArrayList to be printed
     */
    private static void display(ArrayList<String> list)
    {
        /**
         * a String to hold the possible menu choices the user can make
         */
        String menu = "\nMenu: Add / Delete / Insert / Print / Quit";

        for(int x = 0; x < list.size(); x++)
        {
            System.out.println(list.get(x));
        }

        System.out.println(menu);
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
            System.out.print(y + ". ");
            System.out.println(list.get(x));
        }
    }
}