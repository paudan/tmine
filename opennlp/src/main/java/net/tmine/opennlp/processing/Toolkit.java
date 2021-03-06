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
package net.tmine.opennlp.processing;

import eu.crydee.uima.opennlp.resources.EnChunkerModel;
import eu.crydee.uima.opennlp.resources.EnNerDateModel;
import eu.crydee.uima.opennlp.resources.EnNerLocationModel;
import eu.crydee.uima.opennlp.resources.EnNerMoneyModel;
import eu.crydee.uima.opennlp.resources.EnNerOrganizationModel;
import eu.crydee.uima.opennlp.resources.EnNerPercentageModel;
import eu.crydee.uima.opennlp.resources.EnNerPersonModel;
import eu.crydee.uima.opennlp.resources.EnNerTimeModel;
import eu.crydee.uima.opennlp.resources.EnParserChunkingModel;
import eu.crydee.uima.opennlp.resources.EnTokenModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.tmine.opennlp.entities.Sentence;
import net.tmine.processing.POSTagger;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.coref.DefaultLinker;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.LinkerMode;
import opennlp.tools.lemmatizer.SimpleLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Toolkit {

    private Tokenizer tokenizer;
    private Linker linker;
    private POSTagger maxEntTagger, perceptronTagger;
    private Chunker chunker;
    private Parser parser;
    private NameFinderME nameF, dateF, timeF, locationF, organizationF, moneyF, percentageF;
    private SimpleLemmatizer lemmatizer;
    
    private static Toolkit INSTANCE;

    private Toolkit() {}
    
    public static Toolkit getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Toolkit();
        return INSTANCE;
    }

    public Tokenizer getTokenizer() {
        if (tokenizer == null)
            try (InputStream modelIn = EnTokenModel.url.openStream()) {
                TokenizerModel model = new TokenizerModel(modelIn);
                tokenizer = new TokenizerME(model);
            } catch (IOException ex) {
                Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
            }
        return tokenizer;
    }

    public opennlp.tools.postag.POSTagger getCustomTagger(String path) {
        URL modelFile = Toolkit.class.getClassLoader().getResource(path);
        return getPOSTagger(modelFile);
    }

    public opennlp.tools.postag.POSTagger getPOSTagger(URL modelURL) {
        try (InputStream modelIn = modelURL.openStream()) {
            POSModel model = new POSModel(modelIn);
            return new POSTaggerME(model);
        } catch (IOException ex) {
            Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public POSTagger getMaxEntropyPOSTagger() {
        if (maxEntTagger == null)
            maxEntTagger = MaxEntropyPOSTagger.getInstance();
        return maxEntTagger;
    }

    public POSTagger getPerceptronPOSTagger() {
        if (perceptronTagger == null)
            perceptronTagger = PerceptronPOSTagger.getInstance();
        return perceptronTagger;
    }

    public Chunker getChunker() {
        if (chunker == null)
            try (InputStream modelIn = EnChunkerModel.url.openStream()) {
                ChunkerModel model = new ChunkerModel(modelIn);
                chunker = new ChunkerME(model);
            } catch (IOException ex) {
                Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
            }
        return chunker;
    }

    public Parser getParser() {
        if (parser == null)
            try (InputStream modelIn = EnParserChunkingModel.url.openStream()) {
                ParserModel model = new ParserModel(modelIn);
                parser = ParserFactory.create(model);
            } catch (IOException ex) {
                Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
            }
        return parser;
    }

    public Linker getLinker() {
        if (linker == null)
            try {
                linker = new DefaultLinker("coref", LinkerMode.TEST);
            } catch (final FileNotFoundException fnfe) {
                Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE,
                        "This exception is usually thrown when the coreference data files "
                        + "do not exist at the coref location", fnfe);
            } catch (IOException ex) {
                Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
            }
        return linker;
    }

    private NameFinderME initNameFinder(URL modelURL) {
        try (InputStream modelIn = modelURL.openStream()) {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            return new NameFinderME(model);
        } catch (IOException ex) {
            Logger.getLogger(Sentence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public NameFinderME getNameFinder() {
        if (nameF == null)
            nameF = initNameFinder(EnNerPersonModel.url);
        return nameF;
    }

    public NameFinderME getDateFinder() {
        if (dateF == null)
            dateF = initNameFinder(EnNerDateModel.url);
        return dateF;
    }

    public NameFinderME getTimeFinder() {
        if (timeF == null)
            timeF = initNameFinder(EnNerTimeModel.url);
        return timeF;
    }

    public NameFinderME getLocationFinder() {
        if (locationF == null)
            locationF = initNameFinder(EnNerLocationModel.url);
        return locationF;
    }

    public NameFinderME getMoneyFinder() {
        if (moneyF == null)
            moneyF = initNameFinder(EnNerMoneyModel.url);
        return moneyF;
    }

    public NameFinderME getPercentageFinder() {
        if (percentageF == null)
            percentageF = initNameFinder(EnNerPercentageModel.url);
        return percentageF;
    }

    public NameFinderME getOrganizationFinder() {
        if (organizationF == null)
            organizationF = initNameFinder(EnNerOrganizationModel.url);
        return organizationF;
    }

    public SimpleLemmatizer getLemmatizer() {
        if (lemmatizer == null) {
            ClassLoader classLoader = getClass().getClassLoader();
            URL resource = classLoader.getResource("en-lemmatizer.dict");
            File f = new File(resource.getFile());
            try {
                lemmatizer = new SimpleLemmatizer(new FileInputStream(f));
            } catch (FileNotFoundException ex) {
                try {
                    InputStream stream = new FileInputStream(new File("../data/en-lemmatizer.dict"));
                    lemmatizer = new SimpleLemmatizer(stream);
                } catch (FileNotFoundException ex2) {
                    Logger.getLogger(Toolkit.class.getName()).log(Level.SEVERE, null, ex2);
                    lemmatizer = null;
                }
            }
        }
        return lemmatizer;
    }
}
