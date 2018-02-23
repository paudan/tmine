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
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.DocDateAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.AnnotationPipeline;
import edu.stanford.nlp.time.TimeAnnotations.TimexAnnotations;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Triple;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import net.tmine.entities.DateEntity;
import net.tmine.entities.Entity;
import net.tmine.entities.Entity.EntityType;
import net.tmine.entities.Sentence;
import net.tmine.stanfordnlp.entities.SentenceFactory;

public class NamedEntityFinder implements net.tmine.processing.NamedEntityFinder {
    
    private static NamedEntityFinder INSTANCE;
    
    private NamedEntityFinder() {}
    
    public static NamedEntityFinder getInstance() {
        if (INSTANCE == null)
            INSTANCE = new NamedEntityFinder();
        return INSTANCE;
    }

    @Override
    public List<Entity> findAllEntities(String sent) {
        AbstractSequenceClassifier classifier = Toolkit.getNERFinder();
        List<Triple<String, Integer, Integer>> offsets = classifier.classifyToCharacterOffsets(sent);
        List<Entity> entities = new ArrayList<>();
        for (Triple<String, Integer, Integer> o : offsets)
            entities.add(new Entity(getEntityType(o.first), sent.substring(o.second, o.third), 
                    o.second, o.third, SentenceFactory.getInstance()));
        return entities;
    }

    public static EntityType getEntityType(String nerType) {
        switch (nerType) {
            case ("LOCATION"):
                return EntityType.LOCATION;
            case ("MONEY"):
                return EntityType.MONEY;
            case ("DATE"):
                return EntityType.DATE;
            case ("TIME"):
                return EntityType.TIME;
            case ("PERSON"):
                return EntityType.PERSON;
            case ("ORGANIZATION"):
                return EntityType.ORGANIZATION;
            case ("PERCENT"):
                return EntityType.PERCENTAGE;
            default:
                return EntityType.GENERAL;
        }
    }

    private List<Entity> findEntities(EntityType type, String sentence) {
        List<Entity> entities = new ArrayList<>();
        AbstractSequenceClassifier classifier = Toolkit.getNERFinder();
        if (classifier == null)
            return entities;
        List<Triple<String, Integer, Integer>> offsets = classifier.classifyToCharacterOffsets(sentence);
        for (Triple<String, Integer, Integer> o : offsets)
            if (getEntityType(o.first) == type)
                entities.add(new Entity(type, sentence.substring(o.second, o.third), o.second, o.third, SentenceFactory.getInstance()));
        return entities;
    }

    @Override
    public List<Entity> findNameEntities(Sentence sentence) {
        return findNameEntities(sentence.getSentence());
    }

    @Override
    public List<Entity> findDateEntities(Sentence sentence) {
        return findEntities(EntityType.DATE, sentence.getSentence());
    }

    @Override
    public List<Entity> findTimeEntities(Sentence sentence) {
        return findEntities(EntityType.TIME, sentence.getSentence());
    }

    @Override
    public List<Entity> findLocationEntities(Sentence sentence) {
        return findEntities(EntityType.LOCATION, sentence.getSentence());
    }

    @Override
    public List<Entity> findMoneyEntities(Sentence sentence) {
        return findEntities(EntityType.MONEY, sentence.getSentence());
    }

    @Override
    public List<Entity> findPercentageEntities(Sentence sentence) {
        return findEntities(EntityType.PERCENTAGE, sentence.getSentence());
    }

    @Override
    public List<Entity> findOrganizationEntities(Sentence sentence) {
        return findEntities(EntityType.ORGANIZATION, sentence.getSentence());
    }

    public List<Entity> findSUTimeEntities(String sent, LocalDate date) {
        List<Entity> entities = new ArrayList<>();
        AnnotationPipeline pipeline = Toolkit.getPOSTaggerPipeline();
        Annotation annotation = new Annotation(sent);
        if (date != null)
            annotation.set(DocDateAnnotation.class, date.format(DateTimeFormatter.ISO_DATE));
        pipeline.annotate(annotation);
        List<CoreMap> timexAnnsAll = annotation.get(TimexAnnotations.class);
        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(TokensAnnotation.class);
            entities.add(new DateEntity(cm.toString(),
                    tokens.get(0).get(CharacterOffsetBeginAnnotation.class),
                    tokens.get(tokens.size() - 1).get(CharacterOffsetEndAnnotation.class),
                    cm.get(TimeExpression.Annotation.class).getTemporal().toISOString(),
                    SentenceFactory.getInstance()));
        }
        return entities;
    }

    public List<Entity> findSUTimeEntities(Sentence sentence, LocalDate date) {
        return findSUTimeEntities(sentence.getSentence(), date);
    }
    
    @Override
    public List<Entity> findAllEntities(Sentence sentence) {
        return findAllEntities(sentence.getSentence());
    }

    @Override
    public List<Entity> findDateEntities(String sentence) {
        return findEntities(EntityType.DATE, sentence);
    }

    @Override
    public List<Entity> findLocationEntities(String sentence) {
        return findEntities(EntityType.LOCATION, sentence);
    }

    @Override
    public List<Entity> findMoneyEntities(String sentence) {
        return findEntities(EntityType.MONEY, sentence);
    }

    @Override
    public List<Entity> findNameEntities(String sentence) {
        return findEntities(EntityType.PERSON, sentence);
    }

    @Override
    public List<Entity> findOrganizationEntities(String sentence) {
        return findEntities(EntityType.ORGANIZATION, sentence);
    }

    @Override
    public List<Entity> findPercentageEntities(String sentence) {
        return findEntities(EntityType.PERCENTAGE, sentence);
    }

    @Override
    public List<Entity> findTimeEntities(String sentence) {
        return findEntities(EntityType.TIME, sentence);
    }

}
