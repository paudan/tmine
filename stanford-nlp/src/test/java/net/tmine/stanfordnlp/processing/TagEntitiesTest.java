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
package net.tmine.stanfordnlp.processing;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import net.tmine.entities.Entity;
import net.tmine.stanfordnlp.entities.Sentence;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;


public class TagEntitiesTest {

    public String[] getEntityList(List<Entity> entities) {
        List<String> list = new ArrayList<>();
        entities.forEach(e -> list.add(e.getExpression()));
        return list.toArray(new String[]{});
    }

    @Test
    public void testFindNames() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old, will join the board as a nonexecutive director November 29.");
        List<Entity> entities = NamedEntityFinder.getInstance().findNameEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"Pierre Vinken"});
    }

    @Test
    public void testFindDates() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old, will join the board as a nonexecutive director November 29.");
        List<Entity> entities = NamedEntityFinder.getInstance().findDateEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"November 29"});
    }

    @Test
    public void testFindTimes() {
        Sentence sent = new Sentence("Pierre Vinken, 61 years old, will join the board in the morning at 11:30 AM.");
        List<Entity> entities = NamedEntityFinder.getInstance().findTimeEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"morning", "11:30 AM"});
    }

    @Test
    public void testFindLocations() {
        Sentence sent = new Sentence("John Haddock, 32 years old male, moved to Cambridge, England in October 20.");
        List<Entity> entities = NamedEntityFinder.getInstance().findLocationEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"Cambridge", "England"});
    }

    @Test
    public void testFindLocations2() {
        Sentence sent = new Sentence("John Haddock, 32 years old male, moved to Cambridge, Great Britain in October 20.");
        List<Entity> entities = NamedEntityFinder.getInstance().findLocationEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"Cambridge", "Great Britain"});
    }

    @Test
    public void testFindOrganizations() {
        Sentence sent = new Sentence("Mr. Gates is chairman of Microsoft, the international computer science company.");
        List<Entity> entities = NamedEntityFinder.getInstance().findOrganizationEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"Microsoft"});
    }

    @Test
    public void testFindPercentages() {
        Sentence sent = new Sentence("Only 30 percent (or 30%) of the current population has not been abroad");
        List<Entity> entities = NamedEntityFinder.getInstance().findPercentageEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"30 percent", "30%"});
    }

    @Test
    public void testFindMoney() {
        Sentence sent = new Sentence("The meat cost $6.50 (or 6.50 dollars) a kilogram");
        List<Entity> entities = NamedEntityFinder.getInstance().findMoneyEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{"$6.50", "6.50 dollars"});
    }

    @Test
    public void testAllEntities() {
        Sentence sent = new Sentence("John Haddock, 32 years old male, moved to Cambridge, Great Britain on a 11:30 train in October 20");
        List<Entity> entities = NamedEntityFinder.getInstance().findAllEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{
            "John Haddock", "32 years old", "Cambridge", "Great Britain", "11:30", "October 20"
        });
    }

    @Test
    public void testAllEntities2() {
        Sentence sent = new Sentence("John Haddock, 32 years old male, moved to Cambridge, England in October 20 at 11:30");
        List<Entity> entities = NamedEntityFinder.getInstance().findAllEntities(sent);
        assertArrayEquals(getEntityList(entities), new String[]{
            "John Haddock", "32 years old", "Cambridge", "England", "October 20", "at 11:30"
        });
    }
    
    @Test
    public void testSUTimeEntities() {
        String sent = "Three interesting dates are 18 Feb 1997, the 20th of july and 4 days from today.";
        List<Entity> entities = NamedEntityFinder.getInstance().findSUTimeEntities(sent, LocalDate.of(2013, 7, 14));
        assertArrayEquals(getEntityList(entities), new String[]{"18 Feb 1997", "the 20th of july", "4 days from today"});
    }
}
