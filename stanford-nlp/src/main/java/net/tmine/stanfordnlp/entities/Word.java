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

import edu.stanford.nlp.process.Morphology;
import java.util.List;
import java.util.TreeSet;
import net.tmine.entities.Entity;
import net.tmine.entities.Entity.EntityType;
import net.tmine.stanfordnlp.processing.MaxEntropyPOSTagger;
import net.tmine.stanfordnlp.processing.NamedEntityFinder;
import net.tmine.utils.PosUtils;

public class Word extends net.tmine.entities.Word {

    public Word(String t, String l, String p, String n, String s, boolean i) {
        super(t, l, p, n, s, i);
    }

    public Word(String t) throws Exception {
        super(t);
    }
    
    public Word(String t, String pos) throws Exception {
        super(t, pos);
    }
    
    public void preprocess() {
        MaxEntropyPOSTagger tagger = MaxEntropyPOSTagger.getInstance();
        String [] tags = tagger.tagSentence(token);
        if (tags != null && tags.length > 0)
            setPOS(tags[0]);
        Morphology morphology = new Morphology();
        setLemma(morphology.lemma(getToken(), pos));
        stem = morphology.stem(getToken());
        List<Entity> entityList = NamedEntityFinder.getInstance().findAllEntities(token);
        nerType = EntityType.GENERAL;
        if (!entityList.isEmpty())
            nerType = entityList.get(0).getEntityType();
        if (nerType != EntityType.GENERAL)
            setNER(token);
        TreeSet<String> stopSet = PosUtils.getStopSet();
        setStop(stopSet.contains(lemma) || stopSet.contains(token));
    }

    public EntityType getNamedEntityType() {
        return nerType;
    }
}
