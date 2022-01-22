package api;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FindPlace {

    
    /**
     * searches for a place and returns the response as JSON
     * uses the Search in Places API
     * @pre query has no spaces in it. they have been replaced with +
     * @param query a string like you'd type into the search bar on Google Maps
     */
    public static String getPlaceID(String query) throws IOException
    {

        // replace any whitespaces in the query with a plus sign
        String querySanitized = sanitize(query);

        URL url = new URL("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + querySanitized + "&inputtype=textquery&key=AIzaSyDpY2tpFV0aZD3gRWLJRl23dSLd3d9ENIo");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        // now handling what was returned
        String contentString = content.toString();
        JSONObject contentJSON = new JSONObject(contentString);

        // we now have the response as JSON, and we want to get the place_id from it
        // it is actually nested JSON (so we have to get candidates from content and then get place_id from candidates)
        // we will assume that our search query was good and just take the first place_id that comes from the search

        String candidates = contentJSON.get("candidates").toString();
        JSONObject candidatesJSON = new JSONObject(candidates);
        String firstCandidatePlaceID = candidatesJSON.get("place_id").toString();

        return firstCandidatePlaceID;
    }

    /**
     * used to prepare strings for input as parameters to API calls
     * @param withSpaces a String from which to remove spaces
     * @return the String with plus signs instead of spaces
     */
    public static String sanitize(String withSpaces)
    {
        String sanitized = withSpaces.replace(" ", "+");
        return sanitized;
    }







}
