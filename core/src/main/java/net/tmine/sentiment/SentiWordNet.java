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
package net.tmine.sentiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.tmine.entities.Sentence;
import net.tmine.utils.Serialization;
import net.tmine.entities.Word;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class SentiWordNet {

    private Map<String, Double> dictionary;

    @SuppressWarnings("unchecked")
    public SentiWordNet(File file) throws Exception {
        dictionary = (HashMap<String, Double>) Serialization.deserialize(file.getPath());
    }

    public SentiWordNet(String pathToSWN) throws Exception {
        dictionary = new HashMap<>();
        initializeDictionary(pathToSWN);
        Serialization.serialize(dictionary, "model/sentiwordnet/dictionary.model");
    }

    public void initializeDictionary(String pathToSWN) throws IOException {
        HashMap<String, HashMap<Integer, Double>> tempDictionary = new HashMap<String, HashMap<Integer, Double>>();
        BufferedReader csv = null;
        try {
            csv = new BufferedReader(new FileReader(pathToSWN));
            int lineNumber = 0;

            String line;
            while ((line = csv.readLine()) != null) {
                lineNumber++;

                // If it's a comment, skip this line.
                if (!line.trim().startsWith("#")) {
                    // We use tab separation
                    String[] data = line.split("\t");
                    String wordTypeMarker = data[0];

					// Example line:
                    // POS ID PosS NegS SynsetTerm#sensenumber Desc
                    // a 00009618 0.5 0.25 spartan#4 austere#3 ascetical#2
                    // ascetic#2 practicing great self-denial;...etc
                    // Is it a valid line? Otherwise, through exception.
                    if (data.length != 6)
                        throw new IllegalArgumentException(
                                "Incorrect tabulation format in file, line: "
                                + lineNumber);

                    // Calculate synset score as score = PosS - NegS
                    Double synsetScore = Double.parseDouble(data[2])
                            - Double.parseDouble(data[3]);

                    // Get all Synset terms
                    String[] synTermsSplit = data[4].split(" ");

                    // Go through all terms of current synset.
                    for (String synTermSplit : synTermsSplit) {
                        // Get synterm and synterm rank
                        String[] synTermAndRank = synTermSplit.split("#");
                        String synTerm = synTermAndRank[0] + "#" + wordTypeMarker;
                        int synTermRank = Integer.parseInt(synTermAndRank[1]);
			// What we get here is a map of the type:
                        // term -> {score of synset#1, score of synset#2...}

                        // Add map to term if it doesn't have one
                        if (!tempDictionary.containsKey(synTerm))
                            tempDictionary.put(synTerm, new HashMap<Integer, Double>());

                        // Add synset link to synterm
                        tempDictionary.get(synTerm).put(synTermRank, synsetScore);
                    }
                }
            }

            // Go through all the terms.
            for (Map.Entry<String, HashMap<Integer, Double>> entry : tempDictionary
                    .entrySet()) {
                String word = entry.getKey();
                Map<Integer, Double> synSetScoreMap = entry.getValue();

				// Calculate weighted average. Weigh the synsets according to
                // their rank.
                // Score= 1/2*first + 1/3*second + 1/4*third ..... etc.
                // Sum = 1/1 + 1/2 + 1/3 ...
                double score = 0.0;
                double sum = 0.0;
                for (Map.Entry<Integer, Double> setScore : synSetScoreMap
                        .entrySet()) {
                    score += setScore.getValue() / (double) setScore.getKey();
                    sum += 1.0 / (double) setScore.getKey();
                }
                score /= sum;

                dictionary.put(word, score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csv != null)
                csv.close();
        }
    }

    public double getSentiment(Sentence sentence) {
        double sentiment = 0;
        for (int i = 0; i < sentence.size(); i++) {
            Word t = sentence.get(i);
            String lemma = t.getLemma().toLowerCase();
            String pos = t.getPOS();
            if (pos.startsWith("JJ"))
                pos = "a";
            else if (pos.startsWith("NN"))
                pos = "n";
            else if (pos.startsWith("RB"))
                pos = "r";
            else if (pos.startsWith("VB"))
                pos = "v";
            if (dictionary.containsKey(lemma + "#" + pos))
                sentiment += dictionary.get(lemma + "#" + pos);
        }
        return sentiment;
    }

}
