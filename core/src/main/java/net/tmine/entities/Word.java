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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.dictionary.MorphologicalProcessor;
import net.tmine.entities.Entity.EntityType;
import net.tmine.processing.Porter;
import net.tmine.utils.PosUtils;

abstract public class Word {

    protected String token, lemma, pos, ner, stem, iob;
    protected boolean isStop;
    protected EntityType nerType;

    public static final String UNDEFINED = "";
    /**
     * The entities which the word object is part of
     */
    protected List<Entity> entities = new ArrayList<>();

    private Dictionary dict = PosUtils.getWordNetInstance();

    public Word(String token, String lemma, String pos, String ner, String stem, boolean isStop) {
        this.token = token;
        this.lemma = lemma;
        this.pos = pos;
        this.ner = ner;
        this.stem = stem;
        this.isStop = isStop;
        this.setNamedEntityType(EntityType.UNDEFINED);
    }

    public Word(String token) throws Exception {
        this.token = token;
    }

    public Word(String token, String pos) throws Exception {
        this.token = token;
        this.pos = pos;
    }

    public Word(Word word) {
        this(word.token, word.lemma, word.pos, word.ner, word.stem, word.isStopword());
        this.setNamedEntityType(word.getNamedEntityType());
        this.entities = word.entities;
    }

    public String getStem() {
        if (stem == null)
            stem = new Porter().stripAffixes(token);
        return stem;
    }

    public String getToken() {
        return token;
    }

    public String getLemma() {
        if (lemma == null) {
            pos = getPOS();
            if (pos != null)
                try {
                    lemma = getWordnetLemma();
                } catch (Exception ex) {
                    Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
                    lemma = UNDEFINED;
                }
        }
        return lemma;
    }

    abstract public String getPOS();

    public boolean isStopword() {
        TreeSet<String> stopSet = PosUtils.getStopSet();
        lemma = getLemma();
        if (lemma != null && lemma.compareTo(UNDEFINED) != 0)
            setStop(stopSet.contains(lemma) || stopSet.contains(token));
        else
            setStop(stopSet.contains(token));
        return isStop;
    }

    public void setLemma(String l) {
        lemma = l;
    }

    public void setPOS(String p) {
        pos = p;
    }

    public void setNER(String n) {
        ner = n;
    }

    public void setStop(boolean s) {
        isStop = s;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIOB() {
        return iob;
    }

    public void setIOB(String iob) {
        this.iob = iob;
    }

    /**
     * Return synonyms for all senses
     */
    public Map<String, Set<String>> getAllSynonyms() {
        Map<String, Set<String>> set = new HashMap<>();
        if (getPOS() == null)
            return set;
        POS wnPos = PosUtils.getWordNetPOS(pos);
        if (dict == null || wnPos == null)
            return set;
        try {
            String lemma_ = getLemma();
            lemma_ = lemma_ != null ? lemma_ : this.token;
            final IndexWord indexWord = dict.lookupIndexWord(wnPos, lemma_);
            final Synset[] senses = indexWord.getSenses();
            for (Synset sense : senses)
                try {
                    PointerTargetNodeList syns = PointerUtils.getInstance().getSynonyms(sense);
                    Set<String> synset = new TreeSet<>();
                    for (Object o : syns) {
                        PointerTargetNode node = (PointerTargetNode) o;
                        for (net.didion.jwnl.data.Word hword : node.getSynset().getWords())
                            synset.add(hword.getLemma());
                    }
                    set.put(sense.getGloss(), synset);
                } catch (NullPointerException ex) {
                    //Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
                }
        } catch (JWNLException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
        return set;
    }

    /**
     * Return synonyms for first sense
     */
    public Set<String> getSynonyms() {
        Set<String> synonyms = new TreeSet<>();
        if (getPOS() == null)
            return synonyms;
        POS wnPos = PosUtils.getWordNetPOS(pos);
        if (dict == null || wnPos == null)
            return synonyms;
        try {
            String lemma_ = getLemma();
            lemma_ = lemma_ != null ? lemma_ : this.token;
            final IndexWord word = dict.lookupIndexWord(wnPos, lemma_);
            PointerTargetNodeList synList = PointerUtils.getInstance().getSynonyms(word.getSense(1));
            for (Object o : synList) {
                PointerTargetNode node = (PointerTargetNode) o;
                for (net.didion.jwnl.data.Word hword : node.getSynset().getWords())
                    synonyms.add(hword.getLemma());
            }
        } catch (JWNLException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
        return synonyms;
    }

    public Set<String> getHypernyms() {
        POS wnPos = PosUtils.getWordNetPOS(pos);
        Dictionary dict = PosUtils.getWordNetInstance();
        Set<String> hypernyms = new TreeSet<>();
        if (dict == null || wnPos == null)
            return hypernyms;
        try {
            String lemma_ = getLemma();
            lemma_ = lemma_ != null ? lemma_ : this.token;
            final IndexWord word = dict.lookupIndexWord(wnPos, lemma_);
            PointerTargetNodeList hypernymList = PointerUtils.getInstance().getDirectHypernyms(word.getSense(1));
            for (Object o : hypernymList) {
                PointerTargetNode node = (PointerTargetNode) o;
                for (net.didion.jwnl.data.Word hword : node.getSynset().getWords())
                    hypernyms.add(hword.getLemma());
            }
        } catch (JWNLException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hypernyms;
    }

    protected void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    protected void searchNER() {
        if (ner == null)
            ner = checkNER() ? token : UNDEFINED;
    }

    public String getNER() {
        if (ner == null) {
            searchNER();
            if (ner == null)
                ner = UNDEFINED;
        }
        return ner;
    }

    public EntityType getNamedEntityType() {
        if (nerType == null)
            searchNER();
        return nerType;
    }

    public void setNamedEntityType(EntityType nerType) {
        this.nerType = nerType;
    }

    private String getWordnetLemma() {
        POS wnPos = PosUtils.getWordNetPOS(getPOS());
        MorphologicalProcessor morph = dict.getMorphologicalProcessor();
        try {
            IndexWord res = morph.lookupBaseForm(wnPos, token);
            return res != null ? res.getLemma() : token;
        } catch (JWNLException ex) {
            return null;
        }
    }

    /**
     * Check proper noun (if word can represent a named entity)
     */
    private boolean checkNER() {
        if (Character.isUpperCase(token.charAt(0)))
            try {
                return dict.lookupIndexWord(POS.NOUN, token).getSenses().length > 0;
            } catch (JWNLException ex) {
                return false;
            }
        return false;
    }

}
