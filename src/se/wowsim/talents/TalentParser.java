package se.wowsim.talents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class TalentParser {

    private int[][] talents;
    private File file;

    public TalentParser(String directory, String filepath) {
        talents = new int[7][4];
        file = new File(directory + "/" + filepath + ".txt");
        parseFile();
    }

    public int[][] getTalents() {
        return talents;
    }

    private void parseFile() {

        try (Scanner scanner = new Scanner(file)) {
            int counter = 0;
            int rowCount = 0;
            while (scanner.hasNextInt()) {
                talents[rowCount][counter] = scanner.nextInt();
                counter++;
                if (counter == 4) {
                    rowCount++;
                    counter = 0;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
