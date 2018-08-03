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
package net.tmine.stanfordnlp.entities;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import net.tmine.processing.POSTagger;
import net.tmine.processing.Porter;
import net.tmine.stanfordnlp.processing.MaxEntropyPOSTagger;
import net.tmine.stanfordnlp.processing.Toolkit;
import net.tmine.stanfordnlp.sentiment.CoreNLPSentiment;
import net.tmine.utils.PosUtils;

public class Sentence extends net.tmine.entities.Sentence {

    protected double sentimentCore;
    protected SemanticGraph basic, ccProcessed;

    public Sentence(String s) {
        super(s, MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), false, null);
        sentimentCore = -2000000000;
    }
    
    public Sentence(String s, boolean preprocess) {
        super(s, MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), preprocess, null);
        sentimentCore = -2000000000;
    }

    public Sentence(String s, POSTagger posTagger, boolean preprocess) {
        super(s, posTagger, WordFactory.getInstance(), preprocess, null);
    }

    public Sentence(String s, boolean preprocess, String defaultTag) {
        super(s, MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), preprocess, defaultTag);
    }

    public Sentence(String s, POSTagger tagger, boolean preprocess, String defaultTag) {
        super(s, tagger != null ? tagger: MaxEntropyPOSTagger.getInstance(), WordFactory.getInstance(), preprocess, defaultTag);
    }


    /* public void preprocess() {
        edu.stanford.nlp.simple.Sentence sent = new edu.stanford.nlp.simple.Sentence(getSentence());
        TreeSet<String> stopSet = PosUtils.getStopSet();
        Porter porter = new Porter();
        setParseTree(sent.parse().toString());
        setDependencies(sent.dependencyGraph().toList().split("\n"));
        basic = sent.dependencyGraph(SemanticGraphFactory.Mode.BASIC);
        ccProcessed = sent.dependencyGraph(SemanticGraphFactory.Mode.CCPROCESSED);
        for (int i = 0; i < sent.length(); i++) {
            boolean stop = stopSet.contains(sent.lemma(i)) || stopSet.contains(sent.originalText(i));
            String stem = porter.stripAffixes(sent.originalText(i));
            Word t = new Word(sent.originalText(i), sent.lemma(i), sent.posTag(i), sent.nerTag(i), stem, stop);
            add(t);
        }
    }*/

    public double getSentimentCoreNLP() {
        if (sentimentCore == -2000000000)
            sentimentCore = new CoreNLPSentiment().getSentiment(this);
        return sentimentCore;
    }

    public String[] tokenize() {
        TokenizerFactory<CoreLabel> tokenizerFactory = Toolkit.getTokenizerFactory();
        List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sentence)).tokenize();
        List<String> words = new ArrayList<>();
        for (CoreLabel label: rawWords)
            words.add(label.value());
        return words.toArray(new String[]{});
    }
}
