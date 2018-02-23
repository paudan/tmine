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

import net.tmine.opennlp.entities.Sentence;
import net.tmine.opennlp.entities.Document;
import java.io.IOException;
import java.util.Iterator;
import opennlp.tools.coref.DiscourseEntity;
import opennlp.tools.coref.mention.MentionContext;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Paulius Danenas, 2016
 */
public class TestCoreferenceResolution {

    @Test
    // Runs successfully with WordNet 3.0 only (somehow use of WordNet 3.1 results in diferent results)
    public void testFindEntityMentions() throws IOException {
        // Example adopted from:
        // https://github.com/dpdearing/nlp/blob/master/src/test/java/com/dpdearing/nlp/opennlp/OpenNlpToolkitTest.java
        Document document = new Document();
        final String[] sentences = {
            "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29.",
            "Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group.",
            "Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, was named a director of this British industrial conglomerate."
        };
        for (String str: sentences)
            document.addSentence(new Sentence(str));

        final DiscourseEntity[] entities = document.findEntityMentions();

        // expected
        final String[][] expected = {
            new String[]{"this British industrial conglomerate"},
            new String[]{"a nonexecutive director", "chairman", "former chairman", "a director"},
            new String[]{"Consolidated Gold Fields PLC"},
            new String[]{"55 years"},
            new String[]{"Rudolph Agnew"},
            new String[]{"Elsevier N.V.", "the Dutch publishing group"},
            new String[]{"Pierre Vinken", "Mr. Vinken"},
            new String[]{"Nov. 29"},
            new String[]{"the board"},
            new String[]{"61 years"}
        };
        assertEquals("Unexpected number of entities", expected.length, entities.length);
        for (int i = 0; i < entities.length; i++) {
            final DiscourseEntity ent = entities[i];

            assertEquals("Unexpected number of mentions at index " + i,
                    expected[i].length, ent.getNumMentions());
            final Iterator<MentionContext> mentions = ent.getMentions();
            int j = 0;
            while (mentions.hasNext()) {
                final MentionContext mc = mentions.next();
                System.out.println("[" + mc.toString() + "]");
                assertEquals("Unexpected Entity Mention found", expected[i][j], mc.toString().trim());
                j++;
            }
        }
        
    }

}
