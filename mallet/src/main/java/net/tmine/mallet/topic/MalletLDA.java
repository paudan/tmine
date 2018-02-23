package net.tmine.mallet.topic;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.StringArrayIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Pattern;
import net.tmine.entities.Document;
import net.tmine.entities.DocumentSet;
import net.tmine.entities.Pair;
import net.tmine.mallet.core.Topic;
import net.tmine.utils.Serialization;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class MalletLDA {

    private ParallelTopicModel model;
    private InstanceList instances;

    public MalletLDA(DocumentSet collection, int noTopics, int iteration) throws Exception {
        createLDA(collection, noTopics, iteration);
        model = ParallelTopicModel.read(new File("model/mallet/lda.model"));
        instances = (InstanceList) Serialization.deserialize("model/mallet/instances.model");
    }

    public MalletLDA(String folder) throws Exception {
        model = ParallelTopicModel.read(new File(folder + "/lda.model"));
        instances = (InstanceList) Serialization.deserialize(folder + "/instances.model");
    }

    public Topic getTopic(Document d) {
        String document = d.getDocument();
        Alphabet dataAlphabet = model.getAlphabet();
        TopicInferencer inferencer = model.getInferencer();
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(document, null, "test instance", null));

        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        Vector<Pair<Double, Integer>> list = new Vector<>();
        for (int i = 0; i < testProbabilities.length; i++) {
            Pair<Double, Integer> p = new Pair<>(testProbabilities[i], i);
            list.add(p);
        }
        Collections.sort(list);

        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
        int topicId = list.get(list.size() - 1).right;
        return new Topic(topicId, dataAlphabet, topicSortedWords.get(topicId).iterator());
    }
    
    public Topic[] createLDA(DocumentSet collection, int noTopics, int iteration) throws Exception {
        ArrayList<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")));
        pipeList.add(new TokenSequenceRemoveStopwords(new File("data/util/stopwords.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());
        InstanceList instances = new InstanceList(new SerialPipes(pipeList));
        String[] array = new String[collection.size()];
        for (int i = 0; i < collection.size(); i++)
            array[i] = collection.get(i).getDocument();
        instances.addThruPipe(new StringArrayIterator(array));
        ParallelTopicModel model = new ParallelTopicModel(noTopics, 1.0, 0.01);
        model.addInstances(instances);
        model.setNumThreads(2);
        model.setNumIterations(iteration);
        model.estimate();
        model.write(new File("model/mallet/lda.model"));
        Serialization.serialize(instances, "model/mallet/instances.model");
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
        Topic[] topics = new Topic[noTopics];
        for (int i = 0; i < noTopics; i++)
            topics[i] = new Topic(i, model.getAlphabet(), topicSortedWords.get(i).iterator());
        return topics;
    }

}
