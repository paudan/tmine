/*
 * Copyright 2017 Paulius.
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

import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import net.tmine.utils.PosUtils;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;


public class VerbWord extends Word {

    public VerbWord(String lemma, String pos, String ner, String stem, boolean isStop) {
        super("VB", lemma, pos, ner, stem, isStop);
    }

    public VerbWord(String token, String pos) throws Exception {
        super(token, pos);
    }

    public VerbWord(String token) throws Exception {
        super(token);
    }
    
    public String getTenseForm(Tense tense) {
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        String lemma = getLemma();
        String verbForm = (lemma != null && lemma.length() > 0) ? lemma : token;
        WordElement word = lexicon.getWord(verbForm, LexicalCategory.VERB);
        InflectedWordElement infl = new InflectedWordElement(word);
        infl.setFeature(Feature.TENSE, tense);
        Realiser realiser = new Realiser(lexicon);
        return realiser.realise(infl).getRealisation();
    }

    public static boolean isVerb(String word) {
        Dictionary dict = PosUtils.getWordNetInstance();
        if (dict == null)
            return false;
        try {
            IndexWord indexWord = dict.lookupIndexWord(POS.VERB, separateParticle(word));
            return indexWord != null;
        } catch (JWNLException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static String separateParticle(String verb) {
        if (verb.contains(" "))
            return verb;
        return verb.replaceAll("^(.*)down$", "$1 down")
                .replaceAll("^(.*)in$", "$1 in")
                .replaceAll("^(.*)out$", "$1 out")
                .replaceAll("^(.*)up$", "$1 up");
    }

    @Override
    public String getPOS() {
        return "VB";
    }
}
