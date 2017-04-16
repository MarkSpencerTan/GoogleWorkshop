/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private HashSet<String> wordList = new HashSet<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetters(word);
            if(lettersToWord.containsKey(sortedWord)){
                lettersToWord.get(sortedWord).add(word);
            }
            else {
                lettersToWord.put(sortedWord, new ArrayList<>(Collections.singletonList(word)));
            }
            wordList.add(word);
            if(sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }
            else
                sizeToWords.put(word.length(), new ArrayList<>(Collections.singletonList(word)));
        }
    }

    public boolean isGoodWord(String word, String base) {
        return (wordList.contains(word) && !word.contains(base));
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();

        for(String word : wordList){
            if (sortLetters(word).equals(sortLetters(targetWord))){
                result.add(word);
            }
        }
        return result;
    }

    public String sortLetters(String word){
        char[] char_array = word.toCharArray();
        Arrays.sort(char_array);
        return new String(char_array);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        // an arraylist containing all the anagrams of the word + a character
        ArrayList<String> result = new ArrayList<String>();

        for(char c = 'a'; c <= 'z'; c++ ){
            String sortedWord = sortLetters(word+c);
            if(lettersToWord.containsKey(sortedWord)){
                for(String s : lettersToWord.get(sortedWord)){
                    result.add(s);
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithTwoLetters(String word) {
        // an arraylist containing all the anagrams of the word + a character
        ArrayList<String> result = new ArrayList<String>();

        for(char c = 'a'; c <= 'z'; c++ ){
            for( char c2 = c; c2 <= 'z'; c2++){
                String sortedWord = sortLetters(word+c+c2);
                if(lettersToWord.containsKey(sortedWord)){
                    for(String s : lettersToWord.get(sortedWord)){
                        result.add(s);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> wordArray = sizeToWords.get(wordLength);

        Random rand = new Random();
        int r = rand.nextInt(wordArray.size());

        String word = "";
        boolean wrappedAround = false;
        for(int i = r; i < wordArray.size(); i++){
            String sortedWord = sortLetters(wordArray.get(i));
            if(lettersToWord.get(sortedWord).size() >= MIN_NUM_ANAGRAMS){
                word = wordArray.get(i);
                if(wordLength < MAX_WORD_LENGTH){
                    wordLength++;
                }
                return word;
            }
            // wrap around the list
            if (i == wordArray.size()-1){
                i = 0;
                wrappedAround = true;
            }
            // if you wrapped fully around to your initial value,
            // increment word length and update the wordArray
            if (i == r && wrappedAround){
                wordLength++;
                wordArray = sizeToWords.get(wordLength);
            }
        }
        return word;
    }

    public String pickDoubleStarterWord() {
        ArrayList<String> wordArray = sizeToWords.get(wordLength);

        Random rand = new Random();
        int r = rand.nextInt(wordArray.size());

        String word = "";
        boolean wrappedAround = false;

        for(int i = r; i < wordArray.size(); i++){
            String sortedWord = sortLetters(wordArray.get(i));
            if(lettersToWord.get(sortedWord).size() >= MIN_NUM_ANAGRAMS){
                word = wordArray.get(i);
                if(wordLength < MAX_WORD_LENGTH){
                    wordLength++;
                }
                return word;
            }
            // wrap around the list
            if (i == wordArray.size()-1){
                i = 0;
                wrappedAround = true;
            }
            // if you wrapped fully around to your initial value,
            // increment word length and update the wordArray
            if (i == r && wrappedAround){
                wordLength++;
                wordArray = sizeToWords.get(wordLength);
            }
        }
        return word;
    }
}
