import javax.imageio.IIOException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * MyAnalyzer
 * @author John Haley
 *
 * Uses the data from CharacterFrequencyAnalyzer to manipulate the substitution cipher. This allows characters to be
 * substituted without overriding the changed characters.
 */
public class MyAnalyzer {

    private static String newCipher;
    private static String mainCipher;
    private static char[] ciphArr;

    private static HashMap<Character, Character> map;
    private static TreeMap<Character, Character> pairMap;

    private static boolean isRunning = true;

    /**
     * main: main method that initializes variables and objects
     *       contains the continous process needed for substitution
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanIn = new Scanner(System.in);
        System.out.print("Please enter a filename: ");
        String fileName = scanIn.nextLine();
        CharacterFrequencyAnalyzer fa = new CharacterFrequencyAnalyzer(fileName);
        map = new HashMap<Character, Character>();
        mainCipher = fa.getCipher().toLowerCase();
        ciphArr = mainCipher.toCharArray();

        Set<Character> allChars = fa.getKeySet();
        Iterator<Character> allCharsIterator = allChars.iterator();
        while (allCharsIterator.hasNext()) {          //Add all available characters to HashMap
            char chtmp = allCharsIterator.next();
            map.put(chtmp, chtmp);
        }
        pairMap = new TreeMap<>();
        while (isRunning) {
            substitute(parseInput(scanIn));
        }
    }

    /**
     * substitute: method that implements the substitution of chosen characters and maps them in a HashMap.
     * @param rpl - replacement strings used for substitution.
     */
    private static void substitute(String[] rpl) {
        if (rpl[0].equals(""))
            return;
        final String ANSI_RESET = "\u001B[0m";          //For coloring text to make it more visible
        final String ANSI_YELLOW = "\u001B[33m";
        int subSize;
        if (rpl[0].length() >= rpl[1].length())
            subSize = rpl[0].length();
        else
            subSize = rpl[1].length();

        for (int i = 0; i < subSize; i++) {             //Implementing pairings and substitutions.
            map.put(rpl[0].charAt(i), rpl[1].charAt(i));
            if (rpl[0].charAt(i) == rpl[1].charAt(i)) {
                pairMap.remove(rpl[0].charAt(i));
            } else {
                pairMap.put(rpl[0].charAt(i), rpl[1].charAt(i));
            }
            newCipher = "";

        }
        System.out.print("Current pairings: ");
        pairMap.forEach((key, value) -> {
            System.out.print("(" + key + ", " + value + ")  ");
        });

        System.out.println();

        for (int i = 0; i < mainCipher.length(); i++) { //Creates the string of substituted cipher and prints cipher.
            newCipher += map.get(ciphArr[i]);
            if (map.get(ciphArr[i]).equals(ciphArr[i])) {
                System.out.print(ciphArr[i]);
            } else {
                System.out.print(ANSI_YELLOW + map.get(ciphArr[i]) + ANSI_RESET); //If there's a substitution, highlight it.
            }
        }
        System.out.println();
    }

    /**
     * parseInput: parses input into two types: a command, or a string of keys and a string of values.
     * @param scanIn scanner used for reading input
     * @return Strings used for substitution
     */
    private static String[] parseInput(Scanner scanIn){
        System.out.println("Enter values to replace: ");
        String input1 = scanIn.nextLine();
        if (input1.charAt(0) == ':'){
            commands(input1);
            return new String[]{""};
        }
        System.out.println("Enter replacing values: ");
        String input2 = scanIn.nextLine();


        return new String[]{input1, input2};
    }

    /**
     * commands: method used for ease to view data in certain ways, or to print the current key.
     * @param input the command from the user
     */
    private static void commands(String input) {
        if (input.equals(":list3")) {
            String[] tmpS = newCipher.split(" ");
            HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
            for (int i = 0; i < tmpS.length; i++) {
                if (tmpS[i].length() == 3) {
                    tmpMap.put(tmpS[i], tmpMap.getOrDefault(tmpS[i], 0) + 1);
                }
            }
            tmpMap.forEach((key, value) -> {
                System.out.println(key + " : " + value + " times ");
            });

        } else if (input.equals(":list2")) {
            String[] tmpS = newCipher.split(" ");
            HashMap<String, Integer> tmpMap = new HashMap<String, Integer>();
            for (int i = 0; i < tmpS.length; i++) {
                if (tmpS[i].length() == 2) {
                    tmpMap.put(tmpS[i], tmpMap.getOrDefault(tmpS[i], 0) + 1);
                }
            }
            tmpMap.forEach((key, value) -> {
                System.out.println(key + " : " + value + " times ");
            });
        } else if (input.equals(":quit")) {
            isRunning = false;
        } else if (input.equals(":printKey")) {
            System.out.print("Key: " + "\u001B[33m");
            pairMap.forEach((key, value) -> {
                System.out.print(value);
            });
            System.out.print("\u001B[0m" + " for Set: " + "\u001B[33m");
            pairMap.forEach((key, value) -> {
                        System.out.print(key);
            });
            System.out.println("\u001B[0m");
        }else{
            System.out.print("Here are a list of commands:\n:quit - quit the application\n:list2 - lists all words that are exactly two characters in length\n:list3 - lists all words that are exactly three characters in length\n:printkey - prints current key for cipher\n:help - lists this help document\n");

        }
    }
}
