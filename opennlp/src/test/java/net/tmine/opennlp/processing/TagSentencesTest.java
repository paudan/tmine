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

import java.util.Arrays;
import net.tmine.opennlp.entities.Sentence;
import org.junit.Test;

public class TagSentencesTest {
    
    @Test
    public void testTagMaxEntropy() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old, will join the board as a nonexecutive director November 29.");
        String[] tags = MaxEntropyPOSTagger.getInstance().tagSentence(sent);
        System.out.println(Arrays.toString(tags));
    }
    
    @Test
    public void testTagPerceptron() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old, will join the board as a nonexecutive director November 29.");
        String[] tags = PerceptronPOSTagger.getInstance().tagSentence(sent);
        System.out.println(Arrays.toString(tags));
    }
    
    @Test
    public void testChunkMaxEntropy() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old man, will join the board as a nonexecutive director November 29.");
        String[] tags = sent.chunkSentence();
        System.out.println(Arrays.toString(tags));
    }
    
    @Test
    public void testChunkPerceptron() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old man, will join the board as a nonexecutive director November 29.", 
                PerceptronPOSTagger.getInstance(), false, null);
        String[] tags = sent.chunkSentence();
        System.out.println(Arrays.toString(tags));
    }
}
