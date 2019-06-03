/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;

import org.json.JSONException;
import org.junit.Test;

import edu.uw.tacoma.group7.brewme.model.Brewery;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BreweryTest {

    /**
     * Test the Brewery constructor with working inputs.
     */
    @Test
    public void testBreweryConstructor() {
        assertNotNull(new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId));
=======
                "http://www.eclipticbrewing.com/", 5739));
>>>>>>> Stashed changes
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullName() {
        Brewery brewery = new Brewery(null, "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getName());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullBreweryType() {
        Brewery brewery = new Brewery("Ecliptic Brewing", null, "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getBreweryType());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullStreet() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", null,
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getStreet());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullCity() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                null, "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getCity());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullState() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", null, "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getState());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullPostalCode() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", null, "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getPostalCode());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullPhone() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", null,
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getPhone());
    }

    /**
     * Test the Brewery constructor with null input.
     */
    @Test
    public void testBreweryConstructorNullWebsite() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                null, mBrewId);
=======
                null, 5739);
>>>>>>> Stashed changes
        assertNull(brewery.getWebsite());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyName() {
        Brewery brewery = new Brewery("", "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getName());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyBreweryType() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getBreweryType());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyStreet() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getStreet());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyCity() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getCity());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyState() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getState());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyPostalCode() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", "", "5032658002",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getPostalCode());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyPhone() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "",
<<<<<<< Updated upstream
                "http://www.eclipticbrewing.com/", mBrewId);
=======
                "http://www.eclipticbrewing.com/", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getPhone());
    }

    /**
     * Test the Brewery constructor with an empty string.
     */
    @Test
    public void testBreweryConstructorEmptyWebsite() {
        Brewery brewery = new Brewery("Ecliptic Brewing", "micro", "825 N Cook St",
                "Portland", "Oregon", "97227-1503", "5032658002",
<<<<<<< Updated upstream
                "", mBrewId);
=======
                "", 5739);
>>>>>>> Stashed changes
        assertEquals("", brewery.getWebsite());
    }

    /**
     * Test the Brewery parse a JSON correctly.
     */
    @Test
    public void testBreweryParseJSON() {
        try {
            assertNotNull(Brewery.parseBreweryJson("[{\"id\": 5793, \"name\": \"Ecliptic Brewing\","
                    + "\"brewery_type\": \"micro\", \"street\": \"825 N Cook St\", \"city\": \"Portland\","
                    + "\"state\": \"Oregon\", \"postal_code\": \"97227-1503\", \"country\": \"United States\","
                    + "\"longitude\": \"-122.675118549414\", \"latitude\": \"45.54737005\", \"phone\": \"5032658002\","
                    + "\"website_url\": \"http://www.eclipticbrewing.com/\", \"updated_at\": \"2018-08-24T16:01:29.190Z\","
                    + "\"tag_list\": []}]"));
        } catch (JSONException e) {
            e.printStackTrace();
            fail("JSON did not parse properly.");
        }
    }

    /**
     * Test the Brewery parse a null.
     */
    @Test
    public void testBreweryParseJSONNull() {
        try {
            assertEquals(0, Brewery.parseBreweryJson(null).size());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("JSON parsed a null.");
        }
    }

    /**
     * Test the Brewery parse an empty string.
     */
    @Test
    public void testBreweryParseJSONEmpty() {
        try {
            assertEquals(0, Brewery.parseBreweryJson("").size());
            fail("parsed an empty JSON.");
        } catch (JSONException e) {
//            e.printStackTrace();

        }
    }

    /**
     * Test the Brewery with a garbage string.
     */
    @Test
    public void testBreweryParseJSONGarbage() {
        try {
            Brewery.parseBreweryJson("FDSKfdi{{[[sfanfolel");
            fail("Parsed a JSON from garbage.");
        } catch (JSONException e) {

        }
    }

}
