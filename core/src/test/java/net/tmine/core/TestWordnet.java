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
package net.tmine.core;

import net.tmine.entities.Word;
import net.tmine.utils.PosUtils;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author Paulius Danenas, 2016
 */
public class TestWordnet {

    @Test
    public void testGetAllSynonyms() throws Exception {
        Word token = new Word("interesting", "JJS");
        Map<String, Set<String>> syns = token.getAllSynonyms();
        System.out.println("All synonyms for " + token.getToken());
        for (String synset : syns.keySet())
            System.out.println(synset + ": {" + Arrays.toString(syns.get(synset).toArray(new String[]{})) + "}");
    }

    @Test
    public void testGetLemma() throws JWNLException {
        Dictionary instance = PosUtils.getWordNetInstance();
        String word = instance.lookupIndexWord(POS.NOUN, "accomplishment").getLemma();
        assertEquals(word, "accomplishment");
    }
    
    @Test
    public void testGetLemma2() throws JWNLException {
        Dictionary instance = PosUtils.getWordNetInstance();
        String word = instance.lookupIndexWord(POS.VERB, "accomplished").getLemma();
        assertEquals(word, "accomplish");
    }
    
    @Test
    public void testGetLemma3() throws JWNLException {
        Dictionary instance = PosUtils.getWordNetInstance();
        String word = instance.lookupIndexWord(POS.VERB, "creating").getLemma();
        assertEquals(word, "create");
    }
    
    @Test(expected=NullPointerException.class)
    public void testGetLemma4() throws JWNLException {
        Dictionary instance = PosUtils.getWordNetInstance();
        String word = instance.lookupIndexWord(POS.VERB, "creation").getLemma();
        assertNull(word);  // "creation" is not recognized as verb!
    }
    
    @Test
    public void testGetPOS1() throws Exception {
        Dictionary instance = PosUtils.getWordNetInstance();
        String word = instance.lookupIndexWord(POS.NOUN, "John").getPOS().toString();
        System.out.println(word);
    }

    @Test
    public void testGetHypernyms() throws JWNLException, Exception {
        Word token = new Word("accomplish", "VB");
        assertArrayEquals(token.getHypernyms().toArray(new String[] {}), 
                new String[]{"complete", "effect", "effectuate", "finish", "set_up"});
    }
    
    @Test
    public void testGetHypernyms2() throws JWNLException, Exception {
        Word token = new Word("interesting", "JJS");
        // it happens that synonyms are returned only for nouns and verb; synonyms are returned for adjectives
        assertEquals(0, token.getHypernyms().size());
    }
    
    @Test
    public void testGetSynonyms2() throws JWNLException, Exception {
        Word token = new Word("house", "NN");
        // it happens that synonyms are returned only for adjectives; nouns and verbs have hypernyms
        assertEquals(0, token.getSynonyms().size());
    }
    
    @Test
    public void testGetSynonyms3() throws JWNLException, Exception {
        Word token = new Word("interesting", "JJS");
        System.out.println(Arrays.toString(token.getSynonyms().toArray(new String[] {})));
        assertArrayEquals(token.getSynonyms().toArray(new String[] {}), 
                new String[]{"absorbing", "amusing", "amusive", "diverting", "engrossing", "entertaining", "fascinating", 
                    "gripping", "intriguing", "newsworthy", "riveting"});
    }
    
    

}
