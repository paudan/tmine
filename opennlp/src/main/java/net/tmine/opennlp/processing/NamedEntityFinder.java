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
package net.tmine.opennlp.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.tmine.entities.Entity;
import net.tmine.entities.Entity.EntityType;
import net.tmine.entities.Sentence;
import net.tmine.opennlp.entities.SentenceFactory;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.util.Span;

public class NamedEntityFinder implements net.tmine.processing.NamedEntityFinder {

    
    public static List<Entity> findEntities(NameFinderME finder, String[] words, EntityType type) {
        Span nameSpans[] = finder.find(words);
        String names[] = new String[nameSpans.length];
        List<Entity> entities = new ArrayList<>();
        for (int i = 0; i < nameSpans.length; i++) {
            String[] subarray = Arrays.copyOfRange(words, nameSpans[i].getStart(), nameSpans[i].getEnd());
            names[i] = String.join(" ", subarray);
            entities.add(new Entity(type, names[i], nameSpans[i].getStart(), nameSpans[i].getEnd(), SentenceFactory.getInstance()));
        }
        return entities;
    }

    private List<Entity> findEntities(NameFinderME finder, EntityType type, Sentence sentence) {
        return findEntities(finder, sentence.tokenize(), type);
    }

    @Override
    public List<Entity> findNameEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getNameFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.PERSON, sentence);
    }

    @Override
    public List<Entity> findDateEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getDateFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.DATE, sentence);
    }

    @Override
    public List<Entity> findTimeEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getTimeFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.TIME, sentence);
    }

    @Override
    public List<Entity> findLocationEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getLocationFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.LOCATION, sentence);
    }

    @Override
    public List<Entity> findMoneyEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getMoneyFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.MONEY, sentence);
    }

    @Override
    public List<Entity> findPercentageEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getPercentageFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.PERCENTAGE, sentence);
    }

    @Override
    public List<Entity> findOrganizationEntities(Sentence sentence) {
        NameFinderME finder = Toolkit.getOrganizationFinder();
        if (finder == null)
            return null;
        return findEntities(finder, Entity.EntityType.ORGANIZATION, sentence);
    }

    @Override
    public List<Entity> findAllEntities(Sentence sentence) {
        List<Entity> entities = new ArrayList<>();
        entities.addAll(findNameEntities(sentence));
        entities.addAll(findDateEntities(sentence));
        entities.addAll(findTimeEntities(sentence));
        entities.addAll(findLocationEntities(sentence));
        entities.addAll(findOrganizationEntities(sentence));
        entities.addAll(findMoneyEntities(sentence));
        entities.addAll(findPercentageEntities(sentence));
        return entities;
    }

    @Override
    public List<Entity> findAllEntities(String sentence) {
        return findAllEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findDateEntities(String sentence) {
        return findDateEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findLocationEntities(String sentence) {
        return findLocationEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findMoneyEntities(String sentence) {
        return findMoneyEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findNameEntities(String sentence) {
        return findNameEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findOrganizationEntities(String sentence) {
        return findOrganizationEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findPercentageEntities(String sentence) {
        return findPercentageEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }

    @Override
    public List<Entity> findTimeEntities(String sentence) {
        return findTimeEntities(SentenceFactory.getInstance().createSentence(sentence, false));
    }
    
}
