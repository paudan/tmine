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
package net.tmine.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

/**
 *
 * @author Paulius
 */
public class PosUtils {

    private static TreeSet<String> stopSet;
    private static String stopFile = "../data/stopwords.txt";
    private static Dictionary wordnet;

    private PosUtils() {
    }

    private static FileReader getStopWordsFile() throws FileNotFoundException {
        ClassLoader classLoader = PosUtils.class.getClassLoader();
        URL resource = classLoader.getResource("stopwords.txt");
        try {
            return new FileReader(new File(resource.getFile()));
        } catch (FileNotFoundException ex) {
            return new FileReader(new File(stopFile));
        }
    }

    public static TreeSet<String> getStopSet() {
        return getStopSet(stopFile);
    }

    public static TreeSet<String> getStopSet(String stopFile) {
        if (stopSet == null) {
            stopSet = new TreeSet<>();
            try (Scanner s = new Scanner(PosUtils.getStopWordsFile())) {
                while (s.hasNext())
                    stopSet.add(s.next());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PosUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return stopSet;
    }

    public static Map<String, String> getPosDictionary() {
        Map<String, String> map = new HashMap<>();
        map.put("``", "opening quotation mark");
        map.put("''", "closing quotation mark");
        map.put("(", "opening parenthesis");
        map.put(")", "closing parenthesis");
        map.put(",", "comma");
        map.put("--", "dash");
        map.put(".", "sentence terminator");
        map.put(":", "colon or ellipsis");
        map.put("CC", "conjunction, coordinating");
        map.put("CD", "numeral, cardinal");
        map.put("DT", "determiner");
        map.put("EX", "existential there");
        map.put("FW", "foreign word");
        map.put("IN", "preposition or conjunction, subordinating");
        map.put("JJ", "adjective or numeral, ordinal");
        map.put("JJR", "adjective, comparative");
        map.put("JJS", "adjective, superlative");
        map.put("LS", "list item marker");
        map.put("MD", "modal auxiliary");
        map.put("NN", "noun, common, singular or mass");
        map.put("NNP", "noun, proper, singular");
        map.put("NNPS", "noun, proper, plural");
        map.put("NNS", "noun, common, plural");
        map.put("PDT", "pre-determiner");
        map.put("POS", "genitive marker");
        map.put("PRP", "pronoun, personal");
        map.put("PRP$", "pronoun, possessive");
        map.put("RB", "adverb");
        map.put("RBR", "adverb, comparative");
        map.put("RBS", "adverb, superlative");
        map.put("RP", "particle");
        map.put("SYM", "symbol");
        map.put("TO", "'to' as preposition or infinitive marker");
        map.put("UH", "interjection");
        map.put("VB", "verb, base form");
        map.put("VBD", "verb, past tense");
        map.put("VBG", "verb, present participle or gerund");
        map.put("VBN", "verb, past participle");
        map.put("VBP", "verb, present tense, not 3rd person singular");
        map.put("VBZ", "verb, present tense, 3rd person singular");
        map.put("WDT", "WH-determiner");
        map.put("WP", "WH-pronoun");
        map.put("WP$", "WH-pronoun, possessive");
        map.put("WRB", "Wh-adverb");
        return map;
    }

    public static Dictionary getWordNetInstance() {
        if (wordnet == null) {
            ClassLoader classLoader = PosUtils.class.getClassLoader();
            URL resource = classLoader.getResource("jwnl_properties.xml");
            File file;
            if (resource != null)
                file = new File(resource.getFile());
            else
                file = new File("src/main/resources/jwnl_properties.xml");
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                inputStream = new ByteArrayInputStream(PosUtils.DEFAULT_WORDNET_CONFIG.getBytes(StandardCharsets.UTF_8));
            }
            try {
                JWNL.initialize(inputStream);
                wordnet = Dictionary.getInstance();
            } catch (JWNLException ex) {
                Logger.getLogger(PosUtils.class.getName()).log(Level.SEVERE, null, ex);
                wordnet = null;
            }
        }
        return wordnet;
    }

    public static POS getWordNetPOS(String pos) {
        if (pos == null)
            return null;
        if (pos.startsWith("J"))
            return POS.ADJECTIVE;
        if (pos.startsWith("V"))
            return POS.VERB;
        if (pos.startsWith("N"))
            return POS.NOUN;
        if (pos.startsWith("R"))
            return POS.ADVERB;
        return null;
    }

    static String DEFAULT_WORDNET_CONFIG
            = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<jwnl_properties language=\"en\">\n"
            + "    <version publisher=\"Princeton\" number=\"3.0\" language=\"en\"/>\n"
            + "    <dictionary class=\"net.didion.jwnl.dictionary.FileBackedDictionary\">\n"
            + "        <param name=\"morphological_processor\" value=\"net.didion.jwnl.dictionary.morph.DefaultMorphologicalProcessor\">\n"
            + "            <param name=\"operations\">\n"
            + "                <param value=\"net.didion.jwnl.dictionary.morph.LookupExceptionsOperation\"/>\n"
            + "                <param value=\"net.didion.jwnl.dictionary.morph.DetachSuffixesOperation\">\n"
            + "                    <param name=\"noun\" value=\"|s=|ses=s|xes=x|zes=z|ches=ch|shes=sh|men=man|ies=y|\"/>\n"
            + "                    <param name=\"verb\" value=\"|s=|ies=y|es=e|es=|ed=e|ed=|ing=e|ing=|\"/>\n"
            + "                    <param name=\"adjective\" value=\"|er=|est=|er=e|est=e|\"/>\n"
            + "                    <param name=\"operations\">\n"
            + "                        <param value=\"net.didion.jwnl.dictionary.morph.LookupIndexWordOperation\"/>\n"
            + "                        <param value=\"net.didion.jwnl.dictionary.morph.LookupExceptionsOperation\"/>\n"
            + "                    </param>\n"
            + "                </param>\n"
            + "                <param value=\"net.didion.jwnl.dictionary.morph.TokenizerOperation\">\n"
            + "                    <param name=\"delimiters\">\n"
            + "                        <param value=\" \"/>\n"
            + "                        <param value=\"-\"/>\n"
            + "                    </param>\n"
            + "                    <param name=\"token_operations\">\n"
            + "                        <param value=\"net.didion.jwnl.dictionary.morph.LookupIndexWordOperation\"/>\n"
            + "                        <param value=\"net.didion.jwnl.dictionary.morph.LookupExceptionsOperation\"/>\n"
            + "                        <param value=\"net.didion.jwnl.dictionary.morph.DetachSuffixesOperation\">\n"
            + "                            <param name=\"noun\" value=\"|s=|ses=s|xes=x|zes=z|ches=ch|shes=sh|men=man|ies=y|\"/>\n"
            + "                            <param name=\"verb\" value=\"|s=|ies=y|es=e|es=|ed=e|ed=|ing=e|ing=|\"/>\n"
            + "                            <param name=\"adjective\" value=\"|er=|est=|er=e|est=e|\"/>\n"
            + "                            <param name=\"operations\">\n"
            + "                                <param value=\"net.didion.jwnl.dictionary.morph.LookupIndexWordOperation\"/>\n"
            + "                                <param value=\"net.didion.jwnl.dictionary.morph.LookupExceptionsOperation\"/>\n"
            + "                            </param>\n"
            + "                        </param>\n"
            + "                    </param>\n"
            + "                </param>\n"
            + "            </param>\n"
            + "        </param>\n"
            + "        <param name=\"dictionary_element_factory\" value=\"net.didion.jwnl.princeton.data.PrincetonWN17FileDictionaryElementFactory\"/>\n"
            + "        <param name=\"file_manager\" value=\"net.didion.jwnl.dictionary.file_manager.FileManagerImpl\">\n"
            + "            <param name=\"file_type\" value=\"net.didion.jwnl.princeton.file.PrincetonRandomAccessDictionaryFile\"/>\n"
            + "            <param name=\"dictionary_path\" value=\"../dict\"/>\n"
            + "        </param>\n"
            + "    </dictionary>\n"
            + "    <resource class=\"PrincetonResource\"/>\n"
            + "</jwnl_properties>";

}
