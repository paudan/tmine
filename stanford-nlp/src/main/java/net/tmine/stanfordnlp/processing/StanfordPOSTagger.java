/*
 * Copyright 2018 Paulius Danenas.
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

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.util.List;
import net.tmine.processing.POSTagger;
import net.tmine.stanfordnlp.entities.SentenceFactory;

public class StanfordPOSTagger implements POSTagger {

    protected MaxentTagger tagger;

    protected StanfordPOSTagger() {}
    
    public StanfordPOSTagger(MaxentTagger tagger) {
        this.tagger = tagger;
    }
    
    private String[] getTags(MaxentTagger tagger, List<HasWord> sent) {
        List<TaggedWord> taggedSent = tagger.tagSentence(sent);
        String[] tags = new String[taggedSent.size()];
        for (int i = 0; i < taggedSent.size(); i++)
            tags[i] = taggedSent.get(i).tag();
        return tags;
    }

    @Override
    public String[] tagSentence(String sentence) {
        if (tagger == null)
            return null;
        List<HasWord> sent = Sentence.toWordList(SentenceFactory.getInstance().createSentence(sentence).tokenize());
        return getTags(tagger, sent);
    }

    @Override
    public String[] tagSentence(net.tmine.entities.Sentence sentence) {
        if (tagger == null)
            return null;
        List<HasWord> sent = Sentence.toWordList(sentence.tokenize());
        return getTags(tagger, sent);
    }

    @Override
    public String[] tagSentence(String[] sentence) {
        if (tagger == null)
            return null;
        List<HasWord> sent = Sentence.toWordList(sentence);
        return getTags(tagger, sent);
    }

}
