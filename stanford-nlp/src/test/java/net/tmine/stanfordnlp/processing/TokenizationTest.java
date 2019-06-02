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
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class TokenizationTest {

    @Test
    public void testTokenizeSentence() {
        Sentence sent = new Sentence("An input sample sentence.");
        String[] tokens = sent.tokenize();
        assertArrayEquals(tokens, new String[]{"An", "input", "sample", "sentence", "."});
    }
    
}
