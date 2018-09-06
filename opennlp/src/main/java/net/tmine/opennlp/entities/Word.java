package net.tmine.opennlp.entities;

import java.util.List;
import net.tmine.entities.Entity;
import net.tmine.entities.Entity.EntityType;
import net.tmine.opennlp.processing.NamedEntityFinder;
import net.tmine.opennlp.processing.Toolkit;
import net.tmine.processing.POSTagger;
import opennlp.tools.lemmatizer.SimpleLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.stemmer.PorterStemmerWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Paulius Danenas, 2016
 */
public class Word extends net.tmine.entities.Word {

    protected SimpleLemmatizer lemmatizer;

    public Word(String token, String lemma, String pos, String ner, String stem, boolean isStop) throws Exception {
        super(token, lemma, pos, ner, stem, isStop);
    }

    public Word(String t) throws Exception {
        super(t);
    }

    public Word(String t, String pos) throws Exception {
        super(t, pos);
    }

    @Override
    public String getStem() {
        if (stem == null)
            stem = new PorterStemmerWrapper().stem(token);
        return stem;
    }

    @Override
    public String getPOS() {
        if (pos == null) {
            POSTagger tagger = Toolkit.getInstance().getMaxEntropyPOSTagger();
            if (tagger == null) {
                pos = UNDEFINED;
                return pos;
            }
            String tag[] = tagger.tagSentence(new String[]{token});
            pos = tag[0] != null ? tag[0] : UNDEFINED;
        }
        return pos;
    }

    @Override
    public String getLemma() {
        if (lemma == null) {
            lemmatizer = Toolkit.getInstance().getLemmatizer();
            if (lemmatizer == null) {
                Logger logger = LoggerFactory.getLogger(getClass().getName());
                logger.error("Could not initialize lemmatizer. Check if en-lemmatizer.dict is present");
                lemma = UNDEFINED;
            }
            pos = getPOS();
            if (token != null && pos != null && pos.compareTo(UNDEFINED) != 0) {
                lemma = lemmatizer.lemmatize(token, pos);
                if (lemma == null)
                    lemma = UNDEFINED;
            }
        }
        return lemma;
    }

    @Override
    protected void searchNER() {
        NameFinderME finder = Toolkit.getInstance().getNameFinder();
        checkNamedEntity(finder, EntityType.PERSON);
        if (ner == null) {
            finder = Toolkit.getInstance().getOrganizationFinder();
            checkNamedEntity(finder, EntityType.ORGANIZATION);
        }
        if (ner == null) {
            finder = Toolkit.getInstance().getLocationFinder();
            checkNamedEntity(finder, EntityType.LOCATION);
        }
        if (ner == null) {
            finder = Toolkit.getInstance().getDateFinder();
            checkNamedEntity(finder, EntityType.DATE);
        }
        if (ner == null) {
            finder = Toolkit.getInstance().getTimeFinder();
            checkNamedEntity(finder, EntityType.TIME);
        }
        if (ner == null) {
            finder = Toolkit.getInstance().getMoneyFinder();
            checkNamedEntity(finder, EntityType.MONEY);
        }
        if (ner == null) {
            finder = Toolkit.getInstance().getPercentageFinder();
            checkNamedEntity(finder, EntityType.PERCENTAGE);
        }
        if (ner == null)
            this.nerType = EntityType.GENERAL;
    }

    private void checkNamedEntity(NameFinderME finder, EntityType type) {
        if (finder != null) {
            String[] tok = new String[]{token};
            List<Entity> tags = NamedEntityFinder.findEntities(finder, tok, type);
            if (tags.size() > 0) {
                ner = tags.get(0).getExpression();
                nerType = type;
            }
        }
    }
}
