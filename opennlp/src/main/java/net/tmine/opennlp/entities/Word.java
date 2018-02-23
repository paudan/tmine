package net.tmine.opennlp.entities;

import java.util.List;
import java.util.TreeSet;
import net.tmine.entities.Entity;
import net.tmine.entities.Entity.EntityType;
import net.tmine.entities.InitializationException;
import net.tmine.opennlp.processing.NamedEntityFinder;
import net.tmine.opennlp.processing.Toolkit;
import net.tmine.processing.POSTagger;
import net.tmine.utils.PosUtils;
import opennlp.tools.lemmatizer.SimpleLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.stemmer.PorterStemmerWrapper;

/**
 @author Paulius Danenas, 2016
 */
public class Word extends net.tmine.entities.Word {
    
    protected EntityType ner_type;

    public Word(String token, String lemma, String pos, String ner, String stem, boolean isStop) {
        super(token, lemma, pos, ner, stem, isStop);
    }

    public Word(String t) throws Exception {
        super(t);
    }

    public Word(String t, String pos) throws Exception {
        super(t, pos);
    }

    @Override
    public void preprocess() throws Exception {
        PorterStemmerWrapper stemmer = new PorterStemmerWrapper();
        stem = stemmer.stem(token);
        POSTagger tagger = Toolkit.getMaxEntropyPOSTagger();
        if (tagger == null)
            throw new InitializationException(POSTagger.class);

        String tag[] = tagger.tagSentence(new String[]{token});
        pos = tag[0];
        SimpleLemmatizer lemmatizer = Toolkit.getLemmatizer();
        if (lemmatizer == null)
            throw new InitializationException(SimpleLemmatizer.class, 
                    "Could not initialize lemmatizer. Check if en-lemmatizer.dict is present in data directory");
        lemma = lemmatizer.lemmatize(token, pos);
        NameFinderME finder = Toolkit.getNameFinder();
        checkNamedEntity(finder, EntityType.PERSON);
        if (ner == null) {
            finder = Toolkit.getOrganizationFinder();
            checkNamedEntity(finder, EntityType.ORGANIZATION);
        }
        if (ner == null) {
            finder = Toolkit.getLocationFinder();
            checkNamedEntity(finder, EntityType.LOCATION);
        }
        if (ner == null) {
            finder = Toolkit.getDateFinder();
            checkNamedEntity(finder, EntityType.DATE);
        }
        if (ner == null) {
            finder = Toolkit.getTimeFinder();
            checkNamedEntity(finder, EntityType.TIME);
        }
        if (ner == null) {
            finder = Toolkit.getMoneyFinder();
            checkNamedEntity(finder, EntityType.MONEY);
        }
        if (ner == null) {
            finder = Toolkit.getPercentageFinder();
            checkNamedEntity(finder, EntityType.PERCENTAGE);
        }
        if (ner == null)
            this.ner_type = EntityType.GENERAL;
        TreeSet<String> stopSet = PosUtils.getStopSet();
        setStop(stopSet.contains(lemma) || stopSet.contains(token));
    }

    private void checkNamedEntity(NameFinderME finder, EntityType type) {
        if (finder != null) {
            String[] tok = new String[]{token};
            List<Entity> tags = NamedEntityFinder.findEntities(finder, tok, type);
            if (tags.size() > 0) {
                ner = tags.get(0).getExpression();
                ner_type = type;
            }
        }
    }

    public EntityType getNamedEntityType() {
        return ner_type;
    }
    
}
