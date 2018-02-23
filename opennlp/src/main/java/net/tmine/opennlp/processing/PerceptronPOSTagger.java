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

import eu.crydee.uima.opennlp.resources.EnPosPerceptronModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;

public class PerceptronPOSTagger implements net.tmine.processing.POSTagger {
    
    private static PerceptronPOSTagger  INSTANCE;
    
    private PerceptronPOSTagger () {}
    
    public static PerceptronPOSTagger  getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PerceptronPOSTagger ();
        return INSTANCE;
    }
    
    @Override
    public String[] tagSentence(String sentence) {
        POSTagger tagger = Toolkit.getPOSTagger(EnPosPerceptronModel.url);
        Tokenizer tokenizer = Toolkit.getTokenizer();
        if (tagger == null || sentence == null)
            return null;
        return tagger.tag(tokenizer.tokenize(sentence));
    }

    @Override
    public String[] tagSentence(net.tmine.entities.Sentence sentence) {
        POSTagger tagger = Toolkit.getPOSTagger(EnPosPerceptronModel.url);
        if (tagger == null || sentence == null)
            return null;
        return tagger.tag(sentence.tokenize());
    }

    @Override
    public String[] tagSentence(String[] sentence) {
        POSTagger tagger = Toolkit.getPOSTagger(EnPosPerceptronModel.url);
        if (tagger == null || sentence == null)
            return null;
        return tagger.tag(sentence);
    }
    
}
