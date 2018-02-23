package net.tmine.mallet.core;

import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Paulius Danenas
 */
public class Topic extends net.tmine.entities.Topic {

    public Topic(int topic, Alphabet dataAlphabet, Iterator<IDSorter> iterator) {
        id = topic;
        wordProbPair = new HashMap<>();
        while (iterator.hasNext()) {
            IDSorter idCountPair = iterator.next();
            wordProbPair.put(dataAlphabet.lookupObject(idCountPair.getID()).toString(), idCountPair.getWeight());
        }
    }
    
}
