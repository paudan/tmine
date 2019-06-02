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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import net.didion.jwnl.JWNLException;
import net.tmine.stanfordnlp.entities.Word;
import net.tmine.entities.Entity.EntityType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TagWordsTest {

    @Test
    public void testPOSTaging() throws Exception {
        Word token = new Word("creation");
        assertEquals("NN", token.getPOS());
    }

    @Test
    public void testPOSTaging2() throws Exception {
        Word token = new Word("creating");
        assertEquals("VBG", token.getPOS());
    }

    @Test
    public void testPOSTaging3() throws Exception {
        Word token = new Word("remote");
        assertEquals("JJ", token.getPOS());
    }

    @Test
    public void testPOSTaging4() throws Exception {
        Word token = new Word("created");
        assertEquals("VBN", token.getPOS());
    }

    @Test
    public void testPOSTaging5() throws Exception {
        Word token = new Word("is");
        assertEquals("VBZ", token.getPOS());
    }

    @Test
    public void testPOSTaging6() throws Exception {
        Word token = new Word("time");
        assertEquals("NN", token.getPOS());
    }

    @Test
    public void testPOSTaging7() throws Exception {
        Word token = new Word("Time");
        assertEquals("NNP", token.getPOS());
    }

    @Test
    public void testPOSTaging8() throws Exception {
        Word token = new Word("customer");
        assertEquals("NN", token.getPOS());
    }

    @Test
    public void testPOSTaging9() throws Exception {
        Word token = new Word("Customer");
        assertEquals("NN", token.getPOS());
    }
    
    @Test
    public void testPOSTaging10() throws Exception {
        Word token = new Word("john");
        assertEquals("NN", token.getPOS());
    }

    @Test
    public void testNER1() throws Exception {
        Word token = new Word("Actor");
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER2() throws Exception {
        Word token = new Word("England");
        assertEquals("England", token.getNER());
        assertEquals(EntityType.LOCATION, token.getNamedEntityType());
    }

    @Test
    public void testNER2_1() throws Exception {
        Word token = new Word("Lithuania");
        assertEquals("Lithuania", token.getNER());
        assertEquals(EntityType.LOCATION, token.getNamedEntityType());
    }
    
    @Test
    public void testNER2_2() throws Exception {
        Word token = new Word("lithuania");
        // Locations in small caps are not recognized either!
        assertNull(token.getNER());
        assertEquals(EntityType.GENERAL, token.getNamedEntityType());
    }

    @Test
    public void testNER3() throws Exception {
        Word token = new Word("Vilnius");
        assertEquals("Vilnius", token.getNER());
        assertEquals(EntityType.LOCATION, token.getNamedEntityType());
    }

    @Test
    public void testNER4() throws Exception {
        Word token = new Word("Microsoft");
        assertEquals("Microsoft", token.getNER());
        assertEquals(EntityType.ORGANIZATION, token.getNamedEntityType());
    }

    @Test
    public void testNER5() throws Exception {
        Word token = new Word("₤5");
        // Does not recognize currency expressions
        assertNull(token.getNER());
        assertEquals(EntityType.GENERAL, token.getNamedEntityType());
    }

    @Test
    public void testNER6() throws Exception {
        Word token = new Word("UNESCO");
        assertEquals("UNESCO", token.getNER());
        assertEquals(EntityType.ORGANIZATION, token.getNamedEntityType());
    }

    @Test
    public void testNER7() throws Exception {
        Word token = new Word("2016.09.25");
        // Fails to recognize numeric date expressions
        assertNull(token.getNER());
        assertEquals(EntityType.GENERAL, token.getNamedEntityType());
    }

    @Test
    public void testNER8() throws Exception {
        Word token = new Word("2016-09-25");
        assertEquals("2016-09-25", token.getNER());
        assertEquals(EntityType.DATE, token.getNamedEntityType());
    }

    @Test
    public void testNER9() throws Exception {
        Word token = new Word("23:00");
        assertEquals("23:00", token.getNER());
        assertEquals(EntityType.TIME, token.getNamedEntityType());
    }

    @Test
    public void testNER10() throws Exception {
        Word token = new Word("23:25:00");
        assertEquals("23:25:00", token.getNER());
        assertEquals(EntityType.TIME, token.getNamedEntityType());
    }

    @Test
    public void testNER11() throws Exception {
        Word token = new Word("John");
        assertEquals("John", token.getNER());
        assertEquals(EntityType.PERSON, token.getNamedEntityType());
    }

    @Test
    public void testNER12() throws Exception {
        Word token = new Word("Paulius");
        assertEquals("Paulius", token.getNER());
        assertEquals(EntityType.PERSON, token.getNamedEntityType());
    }

    @Test
    public void testNER13() throws Exception {
        Word token = new Word("CEO");
        // Fails to recognize popular abbreviations as named entities
        assertNull(token.getNER());
        assertEquals(EntityType.GENERAL, token.getNamedEntityType());
    }

    @Test
    public void testNER14() throws Exception {
        Word token = new Word("john");
        // Unfortunately, lowercase name is not recognized as named entity!
        assertNull(token.getNER());
        assertEquals(EntityType.GENERAL, token.getNamedEntityType());
    }

    @Test
    public void testNER15() throws Exception {
        Word token = new Word("Jennifer");
        assertEquals("Jennifer", token.getNER());
        assertEquals(EntityType.PERSON, token.getNamedEntityType());
    }
    
        
    @Test
    public void testGetSynonyms2() throws JWNLException, Exception {
        net.tmine.entities.Word token = new Word("house", "NN");
        // it happens that synonyms are returned only for adjectives; nouns and verbs have hypernyms
        assertEquals(0, token.getSynonyms().size());
    }
    
    @Test
    public void testGetSynonyms3() throws JWNLException, Exception {
        net.tmine.entities.Word token = new Word("interesting", "JJS");
        System.out.println(Arrays.toString(token.getSynonyms().toArray(new String[] {})));
        assertArrayEquals(token.getSynonyms().toArray(new String[] {}), 
                new String[]{"absorbing", "amusing", "amusive", "diverting", "engrossing", "entertaining", "fascinating", 
                    "gripping", "intriguing", "newsworthy", "riveting"});
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
    public void testGetAllSynonyms() throws Exception {
        net.tmine.entities.Word token = new Word("interesting", "JJS");
        Map<String, Set<String>> syns = token.getAllSynonyms();
        System.out.println("All synonyms for " + token.getToken());
        for (String synset : syns.keySet())
            System.out.println(synset + ": {" + Arrays.toString(syns.get(synset).toArray(new String[]{})) + "}");
    }
}
