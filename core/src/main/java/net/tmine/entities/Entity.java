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

import java.util.List;

public class Entity {
    
    public enum EntityType {
        LOCATION, ORGANIZATION, MONEY, PERCENTAGE, DATE, TIME, GENERAL, PERSON, UNDEFINED
    }
    
    protected EntityType nerType = EntityType.GENERAL;
    /** Start and end in sentence **/
    protected int start, end;
    protected String expression;
    protected List<Word> words;

    public Entity(EntityType ner_type, String expresion, int start, int end, SentenceFactory factory) {
        this.nerType = ner_type;
        this.start = start;
        this.end = end;
        this.expression = expresion;
        this.words = factory.createSentence(expresion, false);
        for (Word word: words)
            word.addEntity(this);
    }

    public EntityType getEntityType() {
        return nerType == null ? EntityType.UNDEFINED : nerType;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getExpression() {
        return expression;
    }
    
    public String toString() {
        return String.format("(%s \"%s\" %d %d)", nerType.toString(), expression, start, end);
    }

    public List<Word> getWords() {
        return words;
    }
    
    
    
}
