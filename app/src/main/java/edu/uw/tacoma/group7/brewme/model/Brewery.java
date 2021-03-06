/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Brewery class that defines custom data structure for holding brewery information.
 */
public class Brewery implements Serializable {

    public static final String NAME = "name";
    public static final String BREWERY_TYPE = "brewery_type";
    public static final String STREET = "street";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String POSTAL_CODE = "postal_code";
    public static final String PHONE = "phone";
    public static final String WEBSITE = "website_url";
    public static final String ID = "id";

    private String mName;
    private String mBreweryType;
    private String mStreet;
    private String mCity;
    private String mState;
    private String mPostalCode;
    private String mPhone;
    private String mWebsite;
    private String mBrewId;

    public Brewery(String name, String breweryType, String street, String city, String state,
                   String postalCode, String phone, String website, String brewId) {
        this.mName = name;
        this.mBreweryType = breweryType;
        this.mStreet = street;
        this.mCity = city;
        this.mState = state;
        this.mPostalCode = postalCode;
        this.mPhone = phone;
        this.mWebsite = website;
        this.mBrewId = brewId;
    }

    public String getName() {
        return mName;
    }
    public void setName(String mName) {
        this.mName = mName;
    }
    public String getBreweryType() {
        return mBreweryType;
    }
    public void setBreweryType(String mBrewerType) {
        this.mBreweryType = mBrewerType;
    }
    public String getStreet() {
        return mStreet;
    }
    public void setStreet(String mStreet) {
        this.mStreet = mStreet;
    }
    public String getCity() {
        return mCity;
    }
    public void setCity(String mCity) {
        this.mCity = mCity;
    }
    public String getState() {
        return mState;
    }
    public void setState(String mState) {
        this.mState = mState;
    }
    public String getPostalCode() {
        return mPostalCode;
    }
    public void setPostalCode(String mPostalCode) {
        this.mPostalCode = mPostalCode;
    }
    public String getPhone() {
        return mPhone;
    }
    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }
    public String getWebsite() {
        return mWebsite;
    }
    public void setWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }
    public String getBreweryId() {
        return mBrewId;
    }
    public void setBreweryId(String brewId) {
        this.mBrewId = brewId;
    }

    /**
     * Parses JSON array into a List data structure containing custom Brewery objects.
     * @param breweryJson JSON string search results returned from web service.
     * @return List containing Brewery objects.
     * @throws JSONException if the JSON string does not contain an 'id' field
     */
    public static List<Brewery> parseBreweryJson(String breweryJson) throws JSONException {
        ArrayList<Brewery> breweryList = new ArrayList<>();
        if(breweryJson != null){
            if(!breweryJson.contains("\"id\":"))
                throw new JSONException("Could not parse breweryJSON due to formatting.");
            JSONArray arr = new JSONArray(breweryJson);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Brewery brewery = new Brewery(obj.getString(Brewery.NAME),
                        obj.getString(Brewery.BREWERY_TYPE), obj.getString(Brewery.STREET),
                        obj.getString(Brewery.CITY), obj.getString(Brewery.STATE),
                        obj.getString(Brewery.POSTAL_CODE), obj.getString(Brewery.PHONE),
                        obj.getString(Brewery.WEBSITE), obj.getString(Brewery.ID));
                breweryList.add(brewery);
            }
        }
        return breweryList;
    }//end parseBreweryJson

}//end Brewery