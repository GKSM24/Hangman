import java.io.*;
import java.util.*;

public class HangmanGame implements Comparable<HangmanGame>{

    String availableLetters = "abcdefghijklmnopqrstuvwxyz", guessedLetters="";
    private final String secretWord;
    Player player;

    {
        Scanner sc = new Scanner(System.in);
        int randomNumber = 0;
        ArrayList<String> movies = null;
        ArrayList<Integer> easyLevelIndex = null, mediumLevelIndex=null, hardLevelIndex=null;
        try {
            movies = this.getMoviesList();
            easyLevelIndex = this.getEasyLevelIndices();
            mediumLevelIndex = this.getMediumLevelIndices();
            hardLevelIndex = this.getHardLevelIndices();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Enter the level of game\n"+"1.Easy\n"+"2.Medium\n"+"3.Hard\n"+"Any Number:Random\n"+"Enter your choice:");
        int choice;
        try {
            choice = sc.nextInt();
            sc.nextLine();
        }catch(RuntimeException re) {choice = 0; re.printStackTrace();}
        switch(choice){
            case 1:
                randomNumber = this.getRandomValue(easyLevelIndex.size());
                secretWord = this.generateWord(movies, easyLevelIndex.get(randomNumber)).toLowerCase();
                break;
            case 2:
                randomNumber = this.getRandomValue(mediumLevelIndex.size());
                secretWord = this.generateWord(movies, mediumLevelIndex.get(randomNumber)).toLowerCase();
                break;
            case 3:
                randomNumber = this.getRandomValue(hardLevelIndex.size());
                secretWord = this.generateWord(movies, hardLevelIndex.get(randomNumber)).toLowerCase();
                break;
            default:
                randomNumber = this.getRandomValue(movies.size());
                secretWord = this.generateWord(movies, randomNumber).toLowerCase();
                break;
        }
    }

    public HangmanGame(){
        availableLetters = "abcdefghijklmnopqrstuvwxyz";
        guessedLetters="";
        player = new Player();
    }

    public HangmanGame(String playerName){
        availableLetters = "abcdefghijklmnopqrstuvwxyz";
        guessedLetters="";
        player = new Player(playerName);
    }

    public static void main(String args[]) throws IOException{
        Scanner sc = new Scanner(System.in);
        HangmanGame[] games;
        System.out.println("Enter number of players: ");
        int numberOfPlayers;
        try {
            numberOfPlayers = sc.nextInt();
            sc.nextLine();
        }catch(RuntimeException re) {numberOfPlayers = 1; re.printStackTrace();}
        if (numberOfPlayers > 1){
            games = new HangmanGame[numberOfPlayers];
            for(int index = 0;index < numberOfPlayers; index++){
                System.out.println("Enter the name of the player"+(index+1)+":");
                games[index] = new HangmanGame(sc.nextLine());
                games[index].player.playerScore = games[index].startGame();
                System.out.println("The answer is: " + games[index].getAnswer());
            }
            getLeaderBoard(games);
        }
        else
        {
            games = new HangmanGame[1];
            games[0].player = new Player();
            games[0].player.playerScore = games[0].startGame();
            System.out.println("Game Over! Your Final Score: " + games[0].player.playerScore + "\nThe answer is: " + games[0].getAnswer());
        }
    }

    public int startGame(){
        Scanner sc= new Scanner(System.in);
        HashMap<Character, Integer> charFrequency = getCharFrequency(secretWord);
        int countSpaces = 0;
        for (int index=0; index<secretWord.length(); index++)
            if (secretWord.charAt(index) == ' ') {
                System.out.print(" ");
                countSpaces++;
            }
            else
                System.out.print("-");
        System.out.print("\n");
        System.out.println("YOUR WORD LENGTH: "+(secretWord.length() - countSpaces));
        String word = "";
        int guesses = secretWord.length()/2, score = 0;
        do{
            System.out.println("\n");
            System.out.println("Number of guesses you have: "+ guesses);
            showGuessedLetters();
            showAvailableLetters();
            System.out.println("Enter a character to guess:");
            char guessChar = sc.nextLine().charAt(0);
            if(checkGuessedCharacters(guessChar))
                System.out.println("You have already guessed it.Try another.");
            else if (charFrequency.containsKey(guessChar)) {
                word = buildWord(secretWord, guessChar);
                score += charFrequency.get(guessChar);
                charFrequency.remove(guessChar);
            }
            else{
                System.out.println("Sorry you have guessed wrong!Try again!");
                guesses--;
            }
            guessedLetters += guessChar;
            availableLetters = updateAvailableChars(guessChar);
            printWord(word);
            System.out.println("Your Score: "+score);
        }while(guesses > 0 && !(word.equals(secretWord)));
        return score;
    }

    private ArrayList<String> getMoviesList() throws IOException{
        //Finding movie, level, hints
        ArrayList<String> movies = new ArrayList<>();
        File fileObj = new File("C:\\Users\\Gopi Venkat Krishna\\IdeaProjects\\Hangman\\src\\movies.txt");
        Scanner scan = new Scanner(new FileInputStream(fileObj));
        String s ="";
        while (scan.hasNextLine()){
            String temp = scan.nextLine();
            if (temp.length() == 0)
                s += "\n";
            else
                s += (temp+"\t");
        }
        for (String str:s.split("\n")) {
            //String words[] = str.split("\t");
            movies.add(str.split("\t")[0]);
        }
        scan.close();
        return movies;
    }

    private ArrayList<Integer> getEasyLevelIndices() throws IOException{
        ArrayList<Integer> easyLevels = new ArrayList<>();
        File fileObj = new File("C:\\Users\\Gopi Venkat Krishna\\IdeaProjects\\Hangman\\src\\movies.txt");
        Scanner scan = new Scanner(new FileInputStream(fileObj));
        String s ="";
        while (scan.hasNextLine()){
            String temp = scan.nextLine();
            if (temp.length() == 0)
                s += "\n";
            else
                s += (temp+"\t");
        }
        String[] line = s.split("\n");
        for(int index = 0; index < line.length; index++)
            if (line[index].split("\t")[1].toLowerCase().equals("easy"))
                easyLevels.add(index);
        scan.close();
        return easyLevels;
    }

    private ArrayList<Integer> getMediumLevelIndices() throws IOException{
        ArrayList<Integer> medLevels = new ArrayList<>();
        File fileObj = new File("C:\\Users\\Gopi Venkat Krishna\\IdeaProjects\\Hangman\\src\\movies.txt");
        Scanner scan = new Scanner(new FileInputStream(fileObj));
        String s ="";
        while (scan.hasNextLine()){
            String temp = scan.nextLine();
            if (temp.length() == 0)
                s += "\n";
            else
                s += (temp+"\t");
        }
        String[] line = s.split("\n");
        for(int index = 0; index < line.length; index++)
            if (line[index].split("\t")[1].toLowerCase().equals("medium"))
                medLevels.add(index);
        scan.close();
        return medLevels;
    }

    private ArrayList<Integer> getHardLevelIndices() throws IOException{
        ArrayList<Integer> hardLevels = new ArrayList<>();
        File fileObj = new File("C:\\Users\\Gopi Venkat Krishna\\IdeaProjects\\Hangman\\src\\movies.txt");
        Scanner scan = new Scanner(new FileInputStream(fileObj));
        String s ="";
        while (scan.hasNextLine()){
            String temp = scan.nextLine();
            if (temp.length() == 0)
                s += "\n";
            else
                s += (temp+"\t");
        }
        String[] line = s.split("\n");
        for(int index = 0; index < line.length; index++)
            if (line[index].split("\t")[1].toLowerCase().equals("hard"))
                hardLevels.add(index);
        scan.close();
        return hardLevels;
    }

    private int getRandomValue(int value){
        Random rand = new Random();
        return rand.nextInt(value);
    }

    private String generateWord(ArrayList<String> list, int randomNumber){
        return list.get(randomNumber);
    }

    private void showGuessedLetters(){
        System.out.println("Guessed letters so far.");
        for (int index = 0; index < guessedLetters.length(); index++)
            System.out.print(guessedLetters.charAt(index)+" ");
        System.out.print("\n");
    }

    private void showAvailableLetters(){
        System.out.println("Available letters for you to try.");
        for (int index = 0; index < availableLetters.length(); index++)
            System.out.print(availableLetters.charAt(index)+" ");
        System.out.print("\n");
    }

    private HashMap<Character, Integer> getCharFrequency(String word){
        HashMap<Character, Integer> charFreqDict= new HashMap<>();
        for (int index = 0; index < word.length(); index++) {
            if (charFreqDict.containsKey(word.charAt(index)))
                charFreqDict.put(word.charAt(index), charFreqDict.get(word.charAt(index))+1);
            else
                charFreqDict.put(word.charAt(index),1);
        }
        return charFreqDict;
    }

    private String buildWord(String word, char guessChar){
        String temp = "";
        for (int index = 0;index < word.length(); index++)
            if (word.charAt(index) == ' ')
                temp += ' ';
            else if(checkGuessedCharacters(word.charAt(index)) || word.charAt(index) == guessChar )
                temp += word.charAt(index);
            else
                temp += '-';
        return temp;
    }

    private boolean checkGuessedCharacters(char guessChar){
        for (int index = 0; index < guessedLetters.length(); index++)
            if (guessChar == guessedLetters.charAt(index))
                return true;
        return false;
    }

    private String getAnswer(){
        return secretWord;
    }

    private void printWord(String word){
        for (int index = 0; index < word.length(); index++)
            System.out.print(word.charAt(index));
        System.out.print("\n");
    }

    private String updateAvailableChars(char guessChar){
        for(int index = 0; index < availableLetters.length(); index++)
            if (availableLetters.charAt(index) == guessChar) {
                return availableLetters.substring(0, index) + availableLetters.substring(index+1);
            }
        return availableLetters;
    }

    @Override
    public int compareTo(HangmanGame p2){
        if (this.player.playerScore < p2.player.playerScore)
            return 1;
        else if (this.player.playerScore > p2.player.playerScore)
            return -1;
        else
            return 0;
    }

    static void getLeaderBoard(HangmanGame[] playersGame){
        Arrays.sort(playersGame);
        System.out.println("\n****Game Leader Board****");
        for (int index = 0; index < playersGame.length; index++)
            System.out.println(playersGame[index].player.name+" "+playersGame[index].player.playerScore);
    }
}
