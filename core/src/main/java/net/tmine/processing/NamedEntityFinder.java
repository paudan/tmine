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
package net.tmine.processing;

import java.util.List;
import net.tmine.entities.Entity;
import net.tmine.entities.Sentence;

public interface NamedEntityFinder {

    public List<Entity> findAllEntities(Sentence sentence);

    public List<Entity> findDateEntities(Sentence sentence);

    public List<Entity> findLocationEntities(Sentence sentence);

    public List<Entity> findMoneyEntities(Sentence sentence);

    public List<Entity> findNameEntities(Sentence sentence);

    public List<Entity> findOrganizationEntities(Sentence sentence);

    public List<Entity> findPercentageEntities(Sentence sentence);

    public List<Entity> findTimeEntities(Sentence sentence);

    public List<Entity> findAllEntities(String sentence);

    public List<Entity> findDateEntities(String sentence);

    public List<Entity> findLocationEntities(String sentence);

    public List<Entity> findMoneyEntities(String sentence);

    public List<Entity> findNameEntities(String sentence);

    public List<Entity> findOrganizationEntities(String sentence);

    public List<Entity> findPercentageEntities(String sentence);

    public List<Entity> findTimeEntities(String sentence);

}
