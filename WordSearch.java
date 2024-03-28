import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;


public class WordSearch {
    public WordSearch() throws IOException {
        puzzleStream = openFile("Enter puzzle file");
        wordStream = openFile("Enter dictionary name");
        System.out.println("Reading files...");
        readPuzzle();
        readWords();
    }
    public int solvePuzzle() {
        int matches = 0;

        for( int r = 0; r < rows; r++ )
            for( int c = 0; c < columns; c++ )
                for( int rd = -1; rd <= 1; rd++ )
                    for( int cd = -1; cd <= 1; cd++ )
                        if( rd != 0 || cd != 0 )
                            matches += solveDirection( r, c, rd, cd );

        return matches;
        }

    private int rows;
    private int columns;
    private char theBoard[][];
    private String[] theWords;
    private BufferedReader puzzleStream;
    private BufferedReader wordStream;
    private BufferedReader in = new
            BufferedReader(new InputStreamReader(System.in));

    private static int prefixSearch(String[] a, String x) {
        int index = Arrays.binarySearch(a, x);
        if (index < 0) {
            return (-index - 1);
        }
        else {
            return index;
        }
    }

    private BufferedReader openFile( String message )
    {
        String fileName = "";
        FileReader theFile;
        BufferedReader fileIn = null;

        do
        {
            System.out.println( message + ": " );

            try
            {
                fileName = in.readLine( );
                if( fileName == null )
                    System.exit( 0 );
                theFile = new FileReader( fileName );
                fileIn  = new BufferedReader( theFile );
            }
            catch( IOException e )
            { System.err.println( "Cannot open " + fileName ); }
        } while( fileIn == null );

        System.out.println( "Opened " + fileName );
        return fileIn;
    }

    private void readWords() throws IOException {
        List<String> words = new ArrayList<>();
        String lastWord = null;
        String thisWord;

        while ((thisWord = wordStream.readLine()) != null){
            if (lastWord != null && thisWord.compareTo(lastWord) < 0 ) {
                System.err.println("Dictionary is not sorted... skipping");
                continue;
            }
            words.add(thisWord);
            lastWord = thisWord;
        }
        theWords = new String[words.size()];
        theWords = words.toArray(theWords);
    }

    private void readPuzzle() throws IOException {
        String oneLine;
        List<String> puzzleLines = new ArrayList<String>();

        if((oneLine = puzzleStream.readLine()) == null )
            throw new IOException("No lines in puzzle file");

        columns = oneLine.length();
        puzzleLines.add(oneLine);

        while ((oneLine = puzzleStream.readLine()) != null) {
            if(oneLine.length() != columns)
                System.err.println("Puzzle is not rectangular: skipping row");
            else {
                puzzleLines.add(oneLine);
            }
            rows = puzzleLines.size();
            theBoard = new char[rows][columns];

            int r = 0;
            for (String theLine : puzzleLines)
                theBoard[r++] = theLine.toCharArray();
        }
    }

    private int solveDirection(int baseRow, int baseCol, int rowDelta, int colDelta) {
        String charSequence = "";
        int numMatches = 0;
        int searchResult;

        charSequence += theBoard[baseRow][baseCol];
        System.out.println("Searching for: " + charSequence);

        for (int i = baseRow + rowDelta, j = baseCol + colDelta;
             i >= 0 && j >= 0 && i < rows && j < columns;
             i += rowDelta, j += colDelta) {
            charSequence += theBoard[i][j];
            System.out.println("Checking: " + charSequence);

            searchResult = prefixSearch(theWords, charSequence);

            if (searchResult == theWords.length) {
                System.out.println("Word not found in dictionary.");
                break;
            }
            if (!((String) theWords[searchResult]).startsWith(charSequence)) {
                System.out.println("No prefix match found in dictionary.");
                break;
            }

            if (theWords[searchResult].equals(charSequence)) {
                numMatches++;
                System.out.println("Found " + charSequence + " at " +
                        baseRow + " " + baseCol + " to " +
                        i + " " + j);
            }
        }

        return numMatches;
    }

    public static void main(String[] args) {
        WordSearch p = null;
        try {
            p = new WordSearch();
        } catch ( IOException e ) {
            System.out.println("IO Error: ");
            e.printStackTrace();
            return;
        }
        System.out.println("Solving...");
        System.out.println(p.solvePuzzle());
    }
}


