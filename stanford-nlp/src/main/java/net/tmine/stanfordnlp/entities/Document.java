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

public class Document extends net.tmine.entities.Document {

    public Document(String d) throws Exception {
        super(d);
    }

    public Document(String d, String c) throws Exception {
        super(d, c);
    }

    @Override
    public void preprocess() throws Exception {
        edu.stanford.nlp.simple.Document doc = new edu.stanford.nlp.simple.Document(getDocument());
        for (edu.stanford.nlp.simple.Sentence sent : doc.sentences()) {
            net.tmine.entities.Sentence s = new Sentence(sent.text());
            s.preprocess();
            addSentence(s);
        }
    }

}
