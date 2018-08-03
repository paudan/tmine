/*
 * Copyright 2018 Paulius Danenas
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

import net.tmine.entities.Sentence;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.tokenize.Tokenizer;


public class CustomOpenNLPTagger implements net.tmine.processing.POSTagger {
    
    private String modelPath;
    
    public CustomOpenNLPTagger(String modelPath) {
        this.modelPath = modelPath;
    }

    @Override
    public String[] tagSentence(String string) {
        POSTagger tagger = Toolkit.getCustomTagger(modelPath);
        Tokenizer tokenizer = Toolkit.getTokenizer();
        if (tagger == null || string == null)
            return null;
        return tagger.tag(tokenizer.tokenize(string));
    }

    @Override
    public String[] tagSentence(Sentence sntnc) {
        POSTagger tagger = Toolkit.getCustomTagger(modelPath);
        Tokenizer tokenizer = Toolkit.getTokenizer();
        if (tagger == null || sntnc == null)
            return null;
        return tagger.tag(sntnc.tokenize());
    }

    @Override
    public String[] tagSentence(String[] strings) {
        POSTagger tagger = Toolkit.getCustomTagger(modelPath);
        if (tagger == null || strings == null)
            return null;
        return tagger.tag(strings);
    }
    
}
