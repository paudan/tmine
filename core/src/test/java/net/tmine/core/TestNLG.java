/*
 * Copyright 2019 Paulius Danenas
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
package net.tmine.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.InflectedWordElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGFactory;
import simplenlg.framework.WordElement;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class TestNLG {
    
    private Realiser realiser = null;
    private NLGFactory factory = null;
    private Lexicon lexicon = Lexicon.getDefaultLexicon();

    @Before
    public void setup() {
       factory = new NLGFactory(lexicon);
       realiser = new Realiser(lexicon);    
    }   
    
    @Test
    public void testVerbConjugation() {
        SPhraseSpec clause = factory.createClause("we", "be");
        clause.setFeature(Feature.TENSE, Tense.PAST);
        clause.setFeature(Feature.PERSON, Person.SECOND);
        clause.setFeature(Feature.NUMBER, NumberAgreement.PLURAL);
        Assert.assertEquals("We were.", realiser.realiseSentence(clause));
    }
    
    @Test
    public void testNounPhrase() {
        WordElement word = lexicon.getWord("creation", LexicalCategory.NOUN);
        InflectedWordElement pluralWord = new InflectedWordElement(word);
        Assert.assertEquals("creation", pluralWord.getBaseWord());
    }
    
}
