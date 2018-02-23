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
package net.tmine.entities;

import java.util.Map;

public class Topic {

    protected int id;
    protected Map<String, Double> wordProbPair;

    public String toString() {
        String print = id + ": ";
        int i = 0;
        for (String topic: wordProbPair.keySet()) {
            i += 1;
            print += String.format("%s (%.2f) ", topic, wordProbPair.get(topic));
            if (i == 10)
                break;
        }
        return print;
    }
}
