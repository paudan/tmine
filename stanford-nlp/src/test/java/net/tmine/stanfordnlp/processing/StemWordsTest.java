/*
 * Copyright 2017 Paulius Danenas
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

import net.tmine.stanfordnlp.entities.Word;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class StemWordsTest {

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
    public void testStemming4() throws Exception {
        Word token = new Word("creations", "NN");
        assertEquals("creation", token.getStem());
    }

    @Test
    public void testStemming5() throws Exception {
        Word token = new Word("booking", "NN");
        assertEquals("book", token.getStem());
    }
    
    @Test
    public void testStemming6() throws Exception {
        Word token = new Word("valuation", "VB");
        assertEquals("valuation", token.getStem());
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
}
