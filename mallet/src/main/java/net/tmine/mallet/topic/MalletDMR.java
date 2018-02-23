package net.tmine.mallet.topic;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TargetStringToFeatures;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.DMRTopicModel;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;
import net.tmine.entities.DocumentSet;
import net.tmine.mallet.core.Topic;
import net.tmine.utils.Serialization;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class MalletDMR {

    private DMRTopicModel model;

    public MalletDMR(DocumentSet collection, int noTopics, int iteration, int interval) throws Exception {
        createDMR(collection, noTopics, iteration, interval);
        model = (DMRTopicModel) DMRTopicModel.read(new File("model/mallet/dmr.model"));
    }

    public MalletDMR(String folder) throws Exception {
        model = (DMRTopicModel) DMRTopicModel.read(new File(folder + "/dmr.model"));
    }

    public void printParameters(String file) throws Exception {
        model.writeParameters(new File(file));
    }

    public void printTopics(String file) throws Exception {
        model.printTopWords(new File(file), 20, false);
    }

    public Topic[] createDMR(DocumentSet collection, int noTopics, int iteration, int interval) throws Exception {
        ArrayList<Pipe> pipeList = new ArrayList<>();

        pipeList.add(new Input2CharSequence("UTF-8"));
        pipeList.add(new TargetStringToFeatures());
        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")));
        pipeList.add(new TokenSequenceLowercase());
        pipeList.add(new TokenSequenceRemoveStopwords(new File("data/util/stopwords.txt"), "UTF-8", false, false, false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));
        ArrayList<cc.mallet.types.Instance> instanceBuffer = new ArrayList<>();
        for (int i = 0; i < collection.size(); i++) {
            String doc = collection.get(i).getDocument();
            String feature = collection.get(i).getClassification();

            instanceBuffer.add(new cc.mallet.types.Instance(doc, feature, i + "", null));
        }
        instances.addThruPipe(instanceBuffer.iterator());
        DMRTopicModel model = new DMRTopicModel(noTopics);
        model.setTopicDisplay(interval, 50);
        model.setNumIterations(iteration);
        model.setOptimizeInterval(iteration / 10);
        model.addInstances(instances);
        model.estimate();
        model.write(new File("model/mallet/dmr.model"));
        Serialization.serialize(instances, "model/mallet/instances.model");
        Topic[] topics = new Topic[noTopics];
        IDSorter[] topicSortedWords = model.getSortedTopicWords(0);
        ArrayList<IDSorter> tsw = new ArrayList<>();
        for (IDSorter ids : topicSortedWords)
            tsw.add(ids);
        for (int i = 0; i < noTopics; i++)
            topics[i] = new Topic(i, model.getAlphabet(), tsw.iterator());
        return topics;
    }

}
