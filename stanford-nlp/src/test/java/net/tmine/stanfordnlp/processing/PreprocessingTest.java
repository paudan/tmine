/*
 * Copyright 2016 Paulius Danenas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tmine.stanfordnlp.processing;

import net.tmine.stanfordnlp.entities.Sentence;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import net.tmine.entities.Word;
import org.junit.Test;

public class PreprocessingTest extends TestCase {
    
    @Test
    public void testLemmas() {
        try {
            Scanner s = new Scanner(new FileReader("../data/twitter_stream.txt"));
            for (int i = 0; i < 10; i++) {
                String text = s.nextLine();
                Sentence sent = new Sentence(text);
                sent.preprocess();
                System.out.println("Sentence # " + (i+1));
                System.out.println(sent.getSentence());
                System.out.println("After tokenization:");
                System.out.println("Tokens: ");
                for (Word token: sent)
                    System.out.print("| " + token.getToken());
                System.out.println();
                System.out.println("Lemmas with part of speech: ");
                for (Word token: sent)
                    System.out.print("| " + token.getLemma() + "/" + token.getPOS());
                System.out.println();

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PreprocessingTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PreprocessingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testNGrams() throws FileNotFoundException, Exception {
        Scanner s = new Scanner(new FileReader("../data/twitter_stream.txt"));
        s.nextLine();
        s.nextLine();
        String text = s.nextLine();
        Sentence sent = new Sentence(text);
        sent.preprocess();
        int n = 2;
        System.out.println("Sentence");
        System.out.println(sent.getSentence());
        System.out.println(n + "-grams");
        System.out.println(sent.getNGrams(n));
        System.out.println();
        s.close();
    }

}
