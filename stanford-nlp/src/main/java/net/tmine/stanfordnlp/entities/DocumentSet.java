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

import java.util.List;

public class DocumentSet extends net.tmine.entities.DocumentSet {

    public DocumentSet(List<String> list) throws Exception {
        super(list);
    }

    public DocumentSet(List<String> docs, List<String> classes) throws Exception {
        super(docs, classes);
    }
    
     public void preprocess() throws Exception {
        for (int i = 0; i < size(); i++) {
            System.out.println("Preprocessing document " + i);
            Document d = (Document) get(i);
            d.preprocess();
        }
    }

    @Override
    protected Document createDocument(String d) throws Exception {
        return new Document(d);
    }

    @Override
    protected Document createDocument(String d, String c) throws Exception {
        return new Document(d, c);
    }
    
}
