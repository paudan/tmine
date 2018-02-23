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
package net.tmine.stanfordnlp.processing;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.NERClassifierCombiner;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.pipeline.POSTaggerAnnotator;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.TokenizerAnnotator;
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.util.PropertiesUtils;
import java.util.Properties;

public class Toolkit {

    private static AbstractSequenceClassifier nerFinder;
    private static AnnotationPipeline pipeline, posPipeline;
    private static TokenizerFactory<CoreLabel> tokFactory;
    private static MaxentTagger tagger;

    private Toolkit() {
    }

    public static AbstractSequenceClassifier getNERFinder() {
        if (nerFinder == null)
            nerFinder = NERClassifierCombiner.createNERClassifierCombiner(null,
                    PropertiesUtils.asProperties("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz,"
                            + "edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz,"
                            + "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz"));
        return nerFinder;
    }

    public static AnnotationPipeline getProcessingPipeline() {
        if (pipeline == null)
            pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                    "annotators", "tokenize,ssplit,pos,lemma,ner,parse",
                    "tokenize.language", "en"));
        return pipeline;
    }
    
    public static TokenizerFactory<CoreLabel> getTokenizerFactory() {
        if (tokFactory == null)
            tokFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "normalizeCurrency=true");
        return tokFactory;
    }
    
    static MaxentTagger getMaxEntropyPOSTagger() {
        if (tagger == null)
            tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words/english-left3words-distsim.tagger");
        return tagger;
    }
    
    public static AnnotationPipeline getPOSTaggerPipeline() {
        if (posPipeline == null) {
            Properties props = new Properties();
            posPipeline = new AnnotationPipeline();
            posPipeline.addAnnotator(new TokenizerAnnotator(false));
            posPipeline.addAnnotator(new WordsToSentencesAnnotator(false));
            posPipeline.addAnnotator(new POSTaggerAnnotator(false));
            posPipeline.addAnnotator(new TimeAnnotator("sutime", props));
        }
        return posPipeline;
    }

}
