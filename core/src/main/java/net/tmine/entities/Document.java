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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

abstract public class Document implements Preprocessable, Iterable<Sentence> {

    protected String document, classification;
    protected Vector<Double> features;
    protected List<Sentence> sentences;

    public Document(String d) throws Exception {
        document = d;
        classification = null;
        features = new Vector<>();
        sentences = new ArrayList<>();
    }

    public Document(String d, String c) throws Exception {
        this(d);
        classification = c;
    }

    public Document() {
        document = null;
        classification = null;
        features = new Vector<>();
        sentences = new ArrayList<>();
    }

    public Iterator<Sentence> iterator() {
        return sentences.iterator();
    }

    public int size() {
        return sentences.size();
    }

    public boolean addSentence(Sentence sent) {
        return sentences.add(sent);
    }
    
    public boolean addAll(Collection<Sentence> sentences) {
        return sentences.addAll(sentences);
    }

    public Sentence get(int index) {
        return sentences.get(index);
    }

    public Vector<Double> getFeatures() {
        return features;
    }

    public void setClassification(String c) {
        classification = c;
    }

    public String getClassification() {
        return classification;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String d) {
        document = d;
    }

    public void setFeatures(Vector<Double> f) {
        features = f;
    }

}
