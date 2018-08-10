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
import net.tmine.entities.Entity;
import net.tmine.entities.Entity.EntityType;
import static net.tmine.entities.Word.UNDEFINED;
import net.tmine.stanfordnlp.processing.MaxEntropyPOSTagger;
import net.tmine.stanfordnlp.processing.NamedEntityFinder;

public class Word extends net.tmine.entities.Word {

    private Morphology morphology;

    public Word(String t, String l, String p, String n, String s, boolean i) {
        super(t, l, p, n, s, i);
        init();
    }

    public Word(String t) throws Exception {
        super(t);
        init();
    }

    public Word(String t, String pos) throws Exception {
        super(t, pos);
        init();
    }

    private void init() {
        morphology = new Morphology();
    }

    @Override
    public String getPOS() {
        if (pos == null) {
            MaxEntropyPOSTagger tagger = MaxEntropyPOSTagger.getInstance();
            if (tagger == null) {
                pos = UNDEFINED;
                return pos;
            }
            String[] tags = tagger.tagSentence(getToken());
            if (tags != null && tags.length > 0)
                setPOS(tags[0]);
            else
                pos = UNDEFINED;
        }
        return pos;
    }

    @Override
    public String getLemma() {
        if (lemma == null) {
            String pos_ = getPOS();
            if (pos_ != null)
                setLemma(morphology.lemma(getToken(), pos_));
            else
                setLemma(UNDEFINED);
        }
        return lemma;
    }

    @Override
    public String getStem() {
        if (stem == null)
            stem = morphology.stem(getToken());
        return stem;
    }

    @Override
    protected void searchNER() {
        List<Entity> entityList = NamedEntityFinder.getInstance().findAllEntities(getToken());
        nerType = EntityType.GENERAL;
        if (!entityList.isEmpty())
            nerType = entityList.get(0).getEntityType();
        if (nerType != EntityType.GENERAL)
            setNER(getToken());
    }
}
