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
package net.tmine.opennlp.processing;

import net.tmine.opennlp.entities.Word;
import net.tmine.entities.Entity.EntityType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TagWordsTest {

    @Test
    public void testStemming() throws Exception {
        Word token = new Word("interesting", "JJS");
        assertEquals("interest", token.getStem());
    }

    @Test
    public void testStemming2() throws Exception {
        Word token = new Word("creation", "NNP");
        assertEquals("creation", token.getStem());
    }

    @Test
    public void testStemming3() throws Exception {
        Word token = new Word("creation", "VB");
        assertEquals("creation", token.getStem());
    }

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
    public void testPOSTaging6() throws Exception {
        Word token = new Word("time");
        assertEquals("NN", token.getPOS());
    }

    @Test
    public void testPOSTaging7() throws Exception {
        Word token = new Word("Time");
        assertEquals("NN", token.getPOS());
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
    public void testLemma1() throws Exception {
        Word token = new Word("creating");
        assertEquals("create", token.getLemma());
    }

    @Test
    public void testLemma2() throws Exception {
        Word token = new Word("creation");
        assertEquals("creation", token.getLemma());
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
        assertEquals(token.getNamedEntityType(), EntityType.LOCATION);
    }

    @Test
    public void testNER2_1() throws Exception {
        Word token = new Word("Lithuania");
        // Fails to recognize some countries
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER3() throws Exception {
        Word token = new Word("Vilnius");
        // Does not recognize Lithuanian cities :(
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER4() throws Exception {
        Word token = new Word("Microsoft");
        assertEquals("Microsoft", token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.ORGANIZATION);
    }

    @Test
    public void testNER5() throws Exception {
        Word token = new Word("₤5");
        // Does not recognize currency symbols
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER6() throws Exception {
        Word token = new Word("UNESCO");
        // Fails to recognize even some famous organizations
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER7() throws Exception {
        Word token = new Word("2016.09.25");
        // Fails to recognize numeric date expressions
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER8() throws Exception {
        Word token = new Word("2016-09-25");
        // Fails to recognize numeric date expressions
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER9() throws Exception {
        Word token = new Word("23:00");
        // Fails to recognize numeric time expressions
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER10() throws Exception {
        Word token = new Word("23:25:00");
        // Fails to recognize numeric time expressions
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER11() throws Exception {
        Word token = new Word("John");
        assertEquals("John", token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.PERSON);
    }

    @Test
    public void testNER12() throws Exception {
        Word token = new Word("Paulius");
        // Fails to recognize my name :(
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER13() throws Exception {
        Word token = new Word("CEO");
        // Fails to recognize popular abbreviations as named entities
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER14() throws Exception {
        Word token = new Word("john");
        // Lowercase name is not recognized as named entity
        assertNull(token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.GENERAL);
    }

    @Test
    public void testNER15() throws Exception {
        Word token = new Word("Jennifer");
        assertEquals("Jennifer", token.getNER());
        assertEquals(token.getNamedEntityType(), EntityType.PERSON);
    }
}
