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
package net.tmine.opennlp.entities;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.tmine.entities.VerbWord;

public class WordFactory implements net.tmine.entities.WordFactory {
    
    private static WordFactory INSTANCE;
    
    private WordFactory() {}
    
    public static WordFactory getInstance() {
        if (INSTANCE == null)
            INSTANCE = new WordFactory();
        return INSTANCE;
    }

    @Override
    public Word createWord(String t, String l, String p, String n, String s, boolean i) {
        try {
            return new Word(t, l, p, n, s, i);
        } catch (Exception ex) {
            Logger.getLogger(WordFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Word createWord(String t) {
        try {
            return new Word(t);
        } catch (Exception ex) {
            Logger.getLogger(WordFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Word createWord(String t, String pos) {
        try {
            return new Word(t, pos);
        } catch (Exception ex) {
            Logger.getLogger(WordFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public VerbWord createVerbWord(String t, String l, String p, String n, String s, boolean i) {
        try {
            return new VerbWord(new Word(t, l, p, n, s, i));
        } catch (Exception ex) {
            Logger.getLogger(WordFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public VerbWord createVerbWord(String t) {
        try {
            return new VerbWord(new Word(t));
        } catch (Exception ex) {
            Logger.getLogger(WordFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
