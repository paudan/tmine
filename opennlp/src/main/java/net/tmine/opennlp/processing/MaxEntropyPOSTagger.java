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

import eu.crydee.uima.opennlp.resources.EnPosMaxentModel;
import net.tmine.entities.Sentence;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;

public class MaxEntropyPOSTagger implements net.tmine.processing.POSTagger {
    
    private static MaxEntropyPOSTagger INSTANCE;
    private POSTagger tagger;
    private Tokenizer tokenizer;
    
    private MaxEntropyPOSTagger() {
        tagger = Toolkit.getInstance().getPOSTagger(EnPosMaxentModel.url);
        tokenizer = Toolkit.getInstance().getTokenizer();
    }
    
    public static MaxEntropyPOSTagger getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MaxEntropyPOSTagger();
        return INSTANCE;
    }

    @Override
    public String[] tagSentence(String sentence) {
        if (tagger == null || sentence == null)
            return null;
        return tagger.tag(tokenizer.tokenize(sentence));
    }

    @Override
    public String[] tagSentence(Sentence sentence) {
        if (tagger == null || sentence == null)
            return null;
        return tagger.tag(sentence.tokenize());
    }

    @Override
    public String[] tagSentence(String[] sentence) {
        if (tagger == null || sentence == null)
            return null;
        return tagger.tag(sentence);
    }
    
}
