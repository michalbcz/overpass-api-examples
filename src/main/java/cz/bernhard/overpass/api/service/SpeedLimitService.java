package cz.bernhard.overpass.api.service;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.methods.HttpGet;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;

public class SpeedLimitService {



    public int speedLimit(double latitude, double longitude) {

        // http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=72.998251,33.670049,72.998451,33.670249]
        String url = " http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=" + createBoundBox(latitude, longitude) + "]";
        System.out.println(url);

//        HttpGet overpass = new HttpGet("http://www.overpass-api.de/api/xapi?*[maxspeed=*][bbox=72.998251,33.670049,72.998451,33.670249]");
//        overpass.get

        try {
            HttpResponse<String> response = Unirest.get(url).asString();
            String xmlResponse = response.getBody();

            System.out.println(xmlResponse);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new ByteArrayInputStream(xmlResponse.getBytes("utf-8"))));

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("//tag[@k='maxspeed']/@v");
            String speedLimit = expr.evaluate(doc);

            return Integer.valueOf(speedLimit);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String createBoundBox(double latitude, double longitude) {
        final double offset = 0.0002;

        double lowestLatitude = latitude - offset;
        double lowestLongitude = longitude - offset;

        double highestLatitude = latitude + offset;
        double highestLongitude = longitude + offset;

        /* according to
            http://wiki.openstreetmap.org/wiki/Overpass_API/Language_Guide#Bounding_box_clauses_.28.22bbox_query.22.2C_.22bounding_box_filter.22.29
           bouncing box should be lowestLatitude, lowestLongitude but overpass api have it in fact swapped (bug)
         */
        return lowestLongitude + "," + lowestLatitude + "," + highestLongitude + "," + highestLatitude;

    }

}
