/*
 * Copyright 2018 paulius.
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

public class VerbWord extends net.tmine.entities.VerbWord {

    public VerbWord(String lemma, String pos, String ner, String stem, boolean isStop) {
        super(lemma, pos, ner, stem, isStop);
    }

    public VerbWord(Word word) {
        super(word);
        this.pos = word.getPOS();
    }

    @Override
    public String getPOS() {
        return pos;
    }

}
