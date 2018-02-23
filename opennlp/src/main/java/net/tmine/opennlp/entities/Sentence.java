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

import java.io.IOException;
import net.tmine.opennlp.processing.MaxEntropyPOSTagger;
import net.tmine.opennlp.processing.Toolkit;
import net.tmine.processing.POSTagger;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.coref.DiscourseEntity;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.mention.Mention;
import opennlp.tools.parser.AbstractBottomUpParser;
import opennlp.tools.parser.Parse;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

public class Sentence extends net.tmine.entities.Sentence {

    public Sentence(String s, boolean preprocess) {
        super(s, MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), preprocess, null);
    }

    public Sentence(String s) {
        super(s, MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), false, null);
    }

    public Sentence(String s, boolean preprocess, String defaultTag) {
        super(s, MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), preprocess, defaultTag);
    }

    public Sentence(String s, POSTagger tagger, boolean preprocess, String defaultTag) {
        super(s, tagger, WordFactory.getInstance(), preprocess, defaultTag);
    }


    public String[] tokenize() {
        Tokenizer tokenizer = Toolkit.getTokenizer();
        if (tokenizer == null)
            return null;
        return tokenizer.tokenize(sentence);
    }

    public String[] chunkSentence() {
        Chunker chunker_ = Toolkit.getChunker();
        if (chunker_ == null || sentence == null)
            return null;
        return chunker_.chunk(tokenize(), tagger.tagSentence(sentence));
    }

    /**
     * Convert the provided sentence and corresponding tokens into a parse tree.
     */
    public Parse parseSentence() throws IOException {
        final Parse p = new Parse(sentence, new Span(0, sentence.length()),
                // the label for the top if an incomplete node
                AbstractBottomUpParser.INC_NODE, 1, 0);
        Tokenizer tokenizer = Toolkit.getTokenizer();
        if (tokenizer == null)
            return null;
        final Span[] spans = tokenizer.tokenizePos(sentence);
        for (int idx = 0; idx < spans.length; idx++)
            // flesh out the parse with token sub-parses
            p.insert(new Parse(sentence, spans[idx], AbstractBottomUpParser.TOK_NODE, 0, idx));
        return Toolkit.getParser().parse(p);
    }

    public DiscourseEntity[] findEntityMentions() throws IOException {
        Linker _linker = Toolkit.getLinker();
        if (_linker == null)
            return null;
        Mention[] mentions = Document.findEntityMentions(_linker, this, 0);
        if (mentions != null && mentions.length > 0)
            return _linker.getEntities(mentions);
        return new DiscourseEntity[0];
    }

}
