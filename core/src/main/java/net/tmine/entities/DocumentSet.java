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

import net.tmine.utils.Serialization;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.tmine.feature.TFIDF;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LibSVM;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

abstract public class DocumentSet implements Preprocessable, Iterable<Document> {

    private static final long serialVersionUID = -283788963082278261L;
    private Topic[] topics;
    protected ArrayList<Document> documents;

    public DocumentSet(List<String> list) throws Exception {
        topics = null;
        for (String str : list)
            add(createDocument(str));
    }

    public DocumentSet(List<String> docs, List<String> classes) throws Exception {
        for (int i = 0; i < docs.size(); i++)
            add(createDocument(docs.get(i), classes.get(i)));
    }

    protected abstract Document createDocument(String d) throws Exception;

    protected abstract Document createDocument(String d, String c) throws Exception;

    public Iterator<Document> iterator() {
        return documents.iterator();
    }

    public int size() {
        return documents.size();
    }

    public boolean add(Document sent) {
        return documents.add(sent);
    }

    public Document get(int index) {
        return documents.get(index);
    }

    public TFIDF getTfIdf(int noOfFeatures) {
        TFIDF tfidf = new TFIDF(this, noOfFeatures);
        return tfidf;
    }

    public TFIDF getTfIdf() {
        TFIDF tfidf = new TFIDF(this, -1);
        return tfidf;
    }

    public String getClassLabel(int index) {
        Document doc = get(index);
        return doc.getClassification();
    }

    public void svm(String modelFile) throws IOException, Exception {
        ArrayList<Attribute> attributes = new ArrayList<>();
        int numAttributes = get(0).getFeatures().size();
        for (int i = 0; i < numAttributes; i++)
            attributes.add(new Attribute("F" + i));
        attributes.add(new Attribute("class"));
        Instances data = new Instances("", attributes, 0);
        for (int i = 0; i < size(); i++) {
            Vector<Double> featureList = get(i).getFeatures();
            Instance inst = new DenseInstance(4);
            for (int j = 0; j < numAttributes; j++)
                inst.setValue(attributes.get(j), featureList.get(j));
            inst.setValue(attributes.get(numAttributes), get(i).getClassification());
            data.add(inst);
        }
        Classifier classifier = new LibSVM();
        classifier.buildClassifier(data);
        Serialization.serialize(classifier, modelFile);
    }

    public void divideTrainTest(DocumentSet train, DocumentSet test, double ratio) {
        int noTrain = (int) (size() * ratio);
        for (int i = 0; i < noTrain; i++)
            train.add(get(i));
        for (int i = noTrain; i < size(); i++)
            test.add(get(i));
    }

    public Topic[] getTopics() {
        return topics;
    }

}
