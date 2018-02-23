/*
 * Copyright 2017 Paulius Danenas
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

import edu.stanford.nlp.hcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.hcoref.data.CorefChain.CorefMention;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Maps;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.Test;

public class CoreferenceTest {

    public void runTest(String text) {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        // run all Annotators on this text
        pipeline.annotate(document);
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            System.out.println(sentence);
            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeAnnotation.class);
            System.out.println("parse tree:\n" + tree);
            // this is the Stanford dependency graph of the current sentence
            //SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
            //System.out.println("dependency graph:\n" + dependencies);
        }
        // This is the coreference link graph
        Map<Integer, CorefChain> graph = document.get(CorefChainAnnotation.class);
        System.out.println(Maps.toStringSorted(graph));
        // Extraction adopted from http://stackoverflow.com/questions/6572207/stanford-core-nlp-understanding-coreference-resolution
        for (Map.Entry<Integer, CorefChain> entry : graph.entrySet()) {
            CorefChain c = entry.getValue();
            //skip self-references
            if (c.getMentionsInTextualOrder().size() <= 1)
                continue;
            CorefMention cm = c.getRepresentativeMention();
            String clust = "";
            List<CoreLabel> tks = document.get(SentencesAnnotation.class).get(cm.sentNum - 1).get(TokensAnnotation.class);
            for (int i = cm.startIndex - 1; i < cm.endIndex - 1; i++)
                clust += tks.get(i).get(TextAnnotation.class) + " ";
            clust = clust.trim();
            System.out.println("representative mention: \"" + clust + "\" is mentioned by:");
            for (CorefMention m : c.getMentionsInTextualOrder()) {
                String clust2 = "";
                tks = document.get(SentencesAnnotation.class).get(m.sentNum - 1).get(TokensAnnotation.class);
                for (int i = m.startIndex - 1; i < m.endIndex - 1; i++)
                    clust2 += tks.get(i).get(TextAnnotation.class) + " ";
                clust2 = clust2.trim();
                //don't need the self mention
                if (clust.equals(clust2))
                    continue;

                System.out.println("\t" + clust2);
            }
        }
    }

    @Test
    public void testStanfordCoreference() {
        String text = "Rental manager’s assistant confirms a car reservation if he creates rental contract";
        runTest(text);
    }

    @Test
    public void testStanfordCoreference2() {
        String text = "Car reservation is confirmed by rental manager’s assistant if it is created by rental car manager";
        runTest(text);
    }

    @Test
    public void testStanfordCoreference3() {
        String text = "Rental manager’s assistant confirm a car reservation if rental contract is created by him";
        runTest(text);
    }

    @Test
    public void testStanfordCoreference4() {
        String text = "It is obligatory that car reservation is confirmed by rental manager’s assistant if it is created by rental car manager";
        runTest(text);
    }

}
