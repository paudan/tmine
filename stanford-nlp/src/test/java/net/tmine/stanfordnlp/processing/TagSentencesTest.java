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
package net.tmine.stanfordnlp.processing;

import java.util.Arrays;
import net.tmine.stanfordnlp.entities.Sentence;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class TagSentencesTest {
    
    
    @Test
    public void testTagSentence1() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old man, will join the board as a nonexecutive director November 29.");
        String[] tags = MaxEntropyPOSTagger.getInstance().tagSentence(sent);
        System.out.println(Arrays.toString(tags));
    }
    
    @Test
    public void testTagSentence2() {
        Sentence sent = new Sentence("User preset Telescope to Catalogue Object");
        String[] tags = MaxEntropyPOSTagger.getInstance().tagSentence(sent);
        assertArrayEquals(tags, new String[]{"NN", "JJ", "NNP", "TO", "NNP", "NNP"});
    }

    @Test
    public void testTagSentence3() {
        Sentence sent = new Sentence("User preset telescope to catalogue object");
        String[] tags = MaxEntropyPOSTagger.getInstance().tagSentence(sent);
        assertArrayEquals(tags, new String[]{"NN", "JJ", "NN", "TO", "NN", "NN"});
    }    
}
