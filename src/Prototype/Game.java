package Prototype;

import java.io.*;
import java.util.*;
public class Game {
    String availableLetters = "abcdefghijklmnopqrstuvwxyz", guessedLetters="";
    /*private final String secretWord;

    {
        ArrayList<String> movies = null;
        try {
            movies = this.getMoviesList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int randomNumber = this.getRandomValue(movies.size());
        secretWord = this.generateWord(movies, randomNumber).toLowerCase();
    }*/

    public static void main(String args[]) throws IOException{
        //Finding movie, level, hints
        Scanner sc= new Scanner(System.in);
        Game game = new Game();
        /*File fileObj = new File("C:\\Users\\Gopi Venkat Krishna\\IdeaProjects\\Hangman\\src\\movies.txt");
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
            String words[] = str.split("\t");
            System.out.println(words[0]);
        }*/
        ArrayList<String> movies = game.getMoviesList();
        int randomNumber = game.getRandomValue(movies.size());
        String secretWord = game.generateWord(movies, randomNumber).toLowerCase(), word = "";
        HashMap<Character, Integer> charFrequency = game.getCharFrequency(secretWord);
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

        int guesses = secretWord.length()/2, score = 0;
        do{
            //System.out.println("xxxxxx");
            System.out.println("\n");
            System.out.println("Number of guesses you have: "+ guesses);
            game.showGuessedLetters();
            game.showAvailableLetters();
            System.out.println("Enter a character to guess:");
            char guessChar = sc.nextLine().charAt(0);
            if(game.checkGuessedCharacters(guessChar))
                System.out.println("You have already guessed it.Try another.");
            else if (charFrequency.containsKey(guessChar)) {
                word = game.buildWord(secretWord, guessChar);
                score += charFrequency.get(guessChar);
                charFrequency.remove(guessChar);
            }
            else{
                System.out.println("Sorry you have guessed wrong!Try again!");
                guesses--;
            }
            game.guessedLetters += guessChar;
            game.availableLetters = game.updateAvailableChars(guessChar);
            game.printWord(word);
            System.out.println("Your Score: "+score);
        }while(guesses > 0 && !(word.equals(secretWord)));
        System.out.println("Final Score: "+score);
    }

    ArrayList<String> getMoviesList() throws IOException{
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
        return movies;
    }

    int getRandomValue(int value){
        Random rand = new Random();
        return rand.nextInt(value);
    }

    String generateWord(ArrayList<String> list, int randomNumber){
        return list.get(randomNumber);
    }

   /*int calculateScore(){
        return -1;
    }
    */

    void showGuessedLetters(){
        System.out.println("Guessed letters so far.");
        for (int index = 0; index < guessedLetters.length(); index++)
            System.out.print(guessedLetters.charAt(index)+" ");
        System.out.print("\n");
    }

    void showAvailableLetters(){
        System.out.println("Available letters for you to try.");
        for (int index = 0; index < availableLetters.length(); index++)
            System.out.print(availableLetters.charAt(index)+" ");
        System.out.print("\n");
    }

    HashMap<Character, Integer> getCharFrequency(String word){
        HashMap<Character, Integer> charFreqDict= new HashMap<>();
        for (int index = 0; index < word.length(); index++) {
            if (charFreqDict.containsKey(word.charAt(index)))
                charFreqDict.put(word.charAt(index), charFreqDict.get(word.charAt(index))+1);
            else
                charFreqDict.put(word.charAt(index),1);
        }
        return charFreqDict;
    }

    String buildWord(String word, char guessChar){
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

    boolean checkGuessedCharacters(char guessChar){
        for (int index = 0; index < guessedLetters.length(); index++)
            if (guessChar == guessedLetters.charAt(index))
                return true;
        return false;
    }

    void printWord(String word){
        for (int index = 0; index < word.length(); index++)
            System.out.print(word.charAt(index));
        System.out.print("\n");
    }

    String updateAvailableChars(char guessChar){
        for(int index = 0; index < availableLetters.length(); index++)
            if (availableLetters.charAt(index) == guessChar) {
                return availableLetters.substring(0, index) + availableLetters.substring(index+1);
            }
        return availableLetters;
    }
}
