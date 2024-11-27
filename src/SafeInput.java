import java.util.Scanner;

public class SafeInput
{
    /**
     * Retrieves a String value from the user longer than 0 characters
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a String response that is not zero length
     */

    public static String getNonZeroLenString(Scanner pipe, String prompt)
    {
        String retString = ""; // Set this to zero length. Loop runs until it isn't
        do
        {
            System.out.print("\n" + prompt + ": "); // show prompt add space
            retString = pipe.nextLine();
        }while(retString.length() == 0);

        return retString;
    }

    /**
     *Retrieves an int value from the user with no limitations on range
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return an integer value with no limitations on range
     */

    public static int getInt(Scanner pipe, String prompt)
    {
        int retInt = 0;
        String trash = "";
        boolean done = false;

        do
        {
            System.out.print("\n" + prompt + ": ");
            if(pipe.hasNextInt()) {
                retInt = pipe.nextInt();
                pipe.nextLine();
                done = true;
            }else {
                trash = pipe.nextLine();
                System.out.println("\nPlease enter an integer. You entered: " + trash + ".");
            }
        }while(!done);
        return retInt;
    }

    /**
     *Retrieves a double value from the user with no limitations on range
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a double value with no limitations on range
     */

    public static double getDouble(Scanner pipe, String prompt)
    {
        double retDouble = 0.0;
        String trash = "";
        boolean done = false;

        do
        {
            System.out.print("\n" + prompt + ": ");
            if(pipe.hasNextDouble()) {
                retDouble = pipe.nextDouble();
                pipe.nextLine();
                done = true;
            }else {
                trash = pipe.nextLine();
                System.out.println("\nPlease enter a numeric value. You entered: " + trash + ".");
            }
        }while(!done);
        return retDouble;
    }

    /**
     *Retrieves a range-limited integer value from the user
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param low the lowest value that the user can validly enter
     * @param high the highest value that the user can validly enter
     * @return an integer value within the range established by the programmer
     */

    public static int getRangedInt(Scanner pipe, String prompt, int low, int high)
    {
        int retRangedInt = 0;
        String trash = "";
        boolean done = false;

        do
        {
            System.out.print("\n" + prompt + "[" + low + "-" + high + "]: ");
            if(pipe.hasNextInt())
            {
                retRangedInt = pipe.nextInt();
                pipe.nextLine();
                if(retRangedInt >= low && retRangedInt <= high) {
                    done = true;
                }else {
                    System.out.println("\nThe number you input is out of range. Please enter a number at or between [" + low + "-" + high + "]. You entered: " + retRangedInt + ".");
                }
            }else {
                trash = pipe.nextLine();
                System.out.println("\nPlease enter an integer at or between [" + low + "-" + high + "]. You entered: " + trash + ".");
            }
        }while(!done);
        return retRangedInt;
    }

    /**
     *Retrieves a range-limited double value from the user
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param low the lowest value that the user can validly enter
     * @param high the highest value that the user can validly enter
     * @return a double value within the range established by the programmer
     */

    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high)
    {
        double retRangedDouble = 0.0;
        String trash = "";
        boolean done = false;

        do
        {
            System.out.print("\n" + prompt + "[" + low + "-" + high + "]: ");
            if(pipe.hasNextDouble())
            {
                retRangedDouble = pipe.nextDouble();
                pipe.nextLine();
                if(retRangedDouble >= low && retRangedDouble <= high)
                {
                    done = true;
                }else {
                    System.out.println("\nThe number you input is out of range. Please enter a number at or between [" + low + "-" + high + "]. You entered: " + retRangedDouble + ".");
                }
            }else {
                trash = pipe.nextLine();
                System.out.println("\nPlease enter a double at or between [" + low + "-" + high + "]. You entered: " + trash + ".");
            }
        }while(!done);
        return retRangedDouble;
    }

    /**
     *Retrieves a Y or N String value from the user and produces a true or false boolean value
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a boolean value (true/false) corresponding to the Y or N answer provided by the user
     */

    public static boolean getYNConfirm(Scanner pipe, String prompt)
    {
        String yesNo = "";
        boolean retBoolean = true;
        boolean done = false;

        do
        {
            System.out.print("\n" + prompt + " [Y/N]: ");
            yesNo = pipe.nextLine();
            if(yesNo.equalsIgnoreCase("Y"))
            {
                retBoolean = true;
                done = true;
            }else if(yesNo.equalsIgnoreCase("N"))
            {
                retBoolean = false;
                done = true;
            }else
            {
                System.out.println("\nPlease enter Y or N [Y/N]. You entered: " + yesNo + ".");
            }
        }while(!done);
        return retBoolean;
    }

    /**
     *Retrieves a String value from the user adhering to the regEx pattern provided by the programmer
     *
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param regEx the regEx String that the user's input must follow
     * @return a String that follows the regEx pattern provided
     */

    public static String getRegExString(Scanner pipe, String prompt, String regEx)
    {
        String userInput = "";
        boolean done = false;

        do
        {
            System.out.print("\n" + prompt + ": ");
            userInput = pipe.nextLine();

            if(userInput.matches(regEx))
            {
                done = true;
            }else
            {
                System.out.println("\nPlease enter an answer matching the regEx pattern: " + regEx + ". You entered: " + userInput + ".");
            }
        }while(!done);
        return userInput;
    }
}
