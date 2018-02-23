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
package net.tmine.entities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tmine.processing.POSTagger;
import net.tmine.sentiment.SentiWordNet;

abstract public class Sentence extends ArrayList<Word> implements Preprocessable {

    protected String sentence, parseTree;
    protected String[] dependencies;
    protected double sentimentWord;
    protected POSTagger tagger;
    protected WordFactory wordsFactory;
    protected String defaultTag = null;

    public Sentence(String s, POSTagger tagger, WordFactory factory, boolean preprocess, String defaultTag) {
        sentence = s;
        parseTree = null;
        dependencies = null;
        sentimentWord = -2000000000;
        this.tagger = tagger;
        this.wordsFactory = factory;
        this.defaultTag = defaultTag;
        if (preprocess)
            preprocess();
    }

    public Sentence(String s, POSTagger tagger, WordFactory factory) {
        this(s, tagger, factory, true, null);
    }

    public void setParseTree(String p) {
        parseTree = p;
    }

    public String getParseTree() {
        return parseTree;
    }

    public void setDependencies(String[] dep) {
        dependencies = dep;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String s) {
        sentence = s;
    }

    abstract public String[] tokenize();

    public double getSentimentWordNet() throws Exception {
        if (sentimentWord == -2000000000) {
            File serFile = new File("model/sentiwordnet/dictionary.model");
            if (serFile.exists()) {
                SentiWordNet swn = new SentiWordNet(serFile);
                sentimentWord = swn.getSentiment(this);
            } else {
                SentiWordNet swn = new SentiWordNet("model/sentiwordnet/SentiWordNet_3.0.0_20130122.txt");
                sentimentWord = swn.getSentiment(this);
            }
        }
        return sentimentWord;
    }

    public List<String> getNGrams(int n) {
        List<String> ret = new ArrayList<>();
        for (int i = 0; i < size() - n; i++) {
            String ngram = "";
            for (int j = i; j < i + n; j++)
                ngram += get(j).getLemma() + " ";
            ngram = ngram.trim();
            ret.add(ngram);
        }
        return ret;
    }

    @Override
    public void preprocess() {
        if (tagger == null || sentence == null)
            return;
        String[] tokens = tokenize();
        for (int i = 0; i < tokens.length; i++) {
            Word word = wordsFactory.createWord(tokens[i]);
            try {
                word.preprocess();
            } catch (Exception ex) {
                Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (defaultTag != null)
                word.setPOS(defaultTag);
            this.add(word);
        }
        // Set POS using tagger set in constructor
        String[] tags = tagger.tagSentence(sentence);
        if (tags.length != size())
            return;
        for (int i = 0; i < size(); i++)
            get(i).setPOS(tags[i]);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Word word : this)
            builder.append(word.getToken()).append(" ");
        return builder.toString().trim();
    }

    public String toSummaryString() {
        StringBuilder builder = new StringBuilder();
        builder.append(sentence).append("\n");
        builder.append("Token\tLemma\tStem\tPOS\tNER\tStopword\tIOB").append("\n");
        for (Word word : this)
            builder.append(String.format("%s\t%s\t%s\t%s\t%s\t%b\t%s\n", 
                    checkNull(word.token), checkNull(word.lemma), checkNull(word.stem), 
                    checkNull(word.pos), checkNull(word.ner), word.isStop, checkNull(word.iob)));
        return builder.toString();
    }

    private String checkNull(String str) {
        return str != null ? str : "null";
    }

    public String getTagString() {
        StringBuilder builder = new StringBuilder();
        for (Word word : this)
            builder.append("[").append(word.getPOS()).append("]");
        return builder.toString();
    }

    public WordFactory getWordsFactory() {
        return wordsFactory;
    }

}
