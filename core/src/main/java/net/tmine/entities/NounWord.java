/*
 * Copyright 2019 Paulius Danenas
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
package net.tmine.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;
import net.tmine.utils.PosUtils;

public class NounWord extends Word {

    public NounWord(String token) throws Exception {
        super(token);
    }

    public NounWord(String token, String lemma, String pos, String ner, String stem, boolean isStop) {
        super(token, lemma, pos, ner, stem, isStop);
    }

    public NounWord(Word word) {
        super(word);
    }

    @Override
    public String getPOS() {
        return "NN";
    }

    public static boolean isNoun(String word) {
        Dictionary dict = PosUtils.getWordNetInstance();
        if (dict == null)
            return false;
        try {
            return dict.getIndexWord(POS.NOUN, word) != null;
        } catch (JWNLException ex) {
            Logger.getLogger(NounWord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Set<String> getDerivedVerbs() {
        Set<String> results = new HashSet<>();
        Dictionary dict = PosUtils.getWordNetInstance();
        if (dict == null)
            return results;
        try {
            String lemma_ = getLemma();
            lemma_ = lemma_ != null ? lemma_ : this.token;
            final IndexWord indexWord = dict.lookupIndexWord(POS.NOUN, lemma_);
            final Synset[] senses = indexWord.getSenses();
            for (Synset synset : senses) {
                Pointer[] pointers = null;
                try {
                    pointers = synset.getPointers(PointerType.NOMINALIZATION);
                } catch (NullPointerException ex) {
                    pointers = null;
                }
                if (pointers == null)
                    continue;
                for (Pointer pointer : pointers)
                    for (net.didion.jwnl.data.Word hword : pointer.getTargetSynset().getWords()) {
                        if (hword.getPOS().equals(POS.VERB))
                            results.add(hword.getLemma());
                    }
            }
        } catch (JWNLException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

}
