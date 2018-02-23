/*
 * Copyright 2017 Paulius.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import simplenlg.features.Tense;

/**
 *
 * @author Paulius
 */
public class TestWord {

    @Test
    public void testVerbForm1() throws Exception {
        VerbWord token = new VerbWord("startup");
        assertEquals("startups", token.getTenseForm(Tense.PRESENT));
    }

    @Test
    public void testVerbForm2() throws Exception {
        VerbWord token = new VerbWord("log");
        assertEquals("logs", token.getTenseForm(Tense.PRESENT));
    }
    
    @Test
    public void testVerbForm3() throws Exception {
        VerbWord token = new VerbWord("log-in");
        assertEquals("log ins", token.getTenseForm(Tense.PRESENT));
    }

    @Test
    public void testIsVerb1() throws Exception {
        assertTrue(VerbWord.isVerb("log"));
    }

    @Test
    public void testIsVerb2() throws Exception {
        assertFalse(VerbWord.isVerb("car"));
    }

    @Test
    public void testIsVerb3() throws Exception {
        assertTrue(VerbWord.isVerb("shutdown"));
    }

    @Test
    public void testIsVerb4() throws Exception {
        assertTrue(VerbWord.isVerb("shut down"));
    }

    @Test
    public void testIsVerb5() throws Exception {
        assertTrue(VerbWord.isVerb("log-in"));
    }
    
    @Test
    public void testIsVerb6() throws Exception {
        assertTrue(VerbWord.isVerb("log in"));
    }
    
    @Test
    public void testIsVerb7() throws Exception {
        assertTrue(VerbWord.isVerb("query"));
    }
    
    @Test
    public void testSeparateParticle() throws Exception {
        assertEquals("shut down", VerbWord.separateParticle("shutdown"));
    }
    
    @Test
    public void testSeparateParticle2() throws Exception {
        assertEquals("shut down", VerbWord.separateParticle("shut down"));
    }
    
    @Test
    public void testSeparateParticle3() throws Exception {
        assertEquals("log in", VerbWord.separateParticle("login"));
    }
    
}
