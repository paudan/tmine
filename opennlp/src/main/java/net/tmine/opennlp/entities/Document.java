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
package net.tmine.opennlp.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.tmine.opennlp.processing.Toolkit;
import opennlp.tools.coref.DiscourseEntity;
import opennlp.tools.coref.Linker;
import opennlp.tools.coref.mention.DefaultParse;
import opennlp.tools.coref.mention.Mention;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Paulius Danenas, 2016
 */
public class Document extends net.tmine.entities.Document {

    public Document(String d) throws Exception {
        super(d);
    }

    public Document(String d, String c) throws Exception {
        super(d, c);
    }

    public Document() {
        super();
    }
    
    @Override
    public void preprocess() {
        return;
    }

    static Mention[] findEntityMentions(Linker _linker, Sentence sent, int index) throws IOException {
        // generate the sentence parse tree
        final Parse parse = sent.parseSentence();
        final DefaultParse parseWrapper = new DefaultParse(parse, index);
        final Mention[] extents = _linker.getMentionFinder().getMentions(parseWrapper);
        //Note: taken from TreebankParser source...
        for (int ei = 0, en = extents.length; ei < en; ei++)
            // construct parses for mentions which don't have constituents
            if (extents[ei].getParse() == null) {
                // not sure how to get head index, but it doesn't seem to be used at this point
                final Parse snp = new Parse(parse.getText(), extents[ei].getSpan(), "NML", 1.0, 0);
                parse.insert(snp);
                // setting a new Parse for the current extent
                extents[ei].setParse(new DefaultParse(snp, index));
            }
        return extents;
    }

    public DiscourseEntity[] findEntityMentions() throws IOException {
        Linker _linker = Toolkit.getInstance().getLinker();
        if (_linker == null)
            return null;
        // list of document mentions
        final List<Mention> mentions = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            Mention[] sent_mentions = findEntityMentions(_linker, (Sentence) get(i), i);
            mentions.addAll(Arrays.asList(sent_mentions));
        }
        if (!mentions.isEmpty())
            return _linker.getEntities(mentions.toArray(new Mention[] {}));
        return new DiscourseEntity[0];
    }

}
