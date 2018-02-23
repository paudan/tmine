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
package net.tmine.opennlp.entities;

import net.tmine.entities.Sentence;
import net.tmine.entities.WordFactory;
import net.tmine.processing.POSTagger;

public class SentenceFactory implements net.tmine.entities.SentenceFactory {

    private static SentenceFactory INSTANCE;

    private SentenceFactory() {
    }

    public static SentenceFactory getInstance() {
        if (INSTANCE == null)
            INSTANCE = new SentenceFactory();
        return INSTANCE;
    }

    @Override
    public Sentence createSentence(String sentence) {
        return new net.tmine.opennlp.entities.Sentence(sentence, true);
    }

    @Override
    public WordFactory getWordFactory() {
        return net.tmine.opennlp.entities.WordFactory.getInstance();
    }

    @Override
    public Sentence createSentence(String sentence, boolean preprocess) {
        return new net.tmine.opennlp.entities.Sentence(sentence, preprocess);
    }

    @Override
    public Sentence createSentence(String sentence, boolean preprocess, String defaultTag) {
        return new net.tmine.opennlp.entities.Sentence(sentence, preprocess, defaultTag);
    }

    @Override
    public Sentence createSentence(String sentence, POSTagger tagger, boolean preprocess, String defaultTag) {
        return new net.tmine.opennlp.entities.Sentence(sentence, tagger, preprocess, defaultTag);
    }

}
