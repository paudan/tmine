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
package net.tmine.classification;

import java.util.ArrayList;
import java.util.Vector;
import net.tmine.entities.Document;
import net.tmine.utils.Serialization;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class LibLinearSVM {

    private Classifier model;

    public LibLinearSVM(String file) throws Exception {
        model = (Classifier) Serialization.deserialize(file);
    }

    public String getClassification(Document d) throws Exception {
        Vector<Double> featureList = d.getFeatures();
        ArrayList<Attribute> attributes = new ArrayList<>();
        int numAttributes = featureList.size();
        for (int i = 0; i < numAttributes; i++)
            attributes.add(new Attribute("F" + i)); 
        attributes.add(new Attribute("class"));
        Instances data = new Instances("", attributes, 0);
        Instance inst = new DenseInstance(4);
        for (int j = 0; j < numAttributes; j++)
            inst.setValue(attributes.get(j), featureList.get(j));
        inst.setValue(attributes.get(numAttributes), d.getClassification());
        data.add(inst);
        return Double.toString(model.classifyInstance(inst));
    }

}
