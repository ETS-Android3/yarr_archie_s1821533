//<!--Archie Yarr S1821533 -->
package com.example.yarr_archie_s1821533;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PullTsParser {
    private static final String TAG = "ParseIncidents";
    private ArrayList<TsItem> applications;




    public PullTsParser() {
        this.applications = new ArrayList<>();
    }

    private Date[] getDates(String[] parts){

        Date newformatStartDate = null;
        Date newformatEndDate = null;

        try{
            //string dates
        String sDate = parts[0].substring(12);
        String eDate = parts[1].substring(10);

        //first format
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMMMM yyyy - HH:mm", Locale.ENGLISH);
        Date sd = sdf.parse(sDate);
        Date ed = sdf.parse(eDate);

        //reformat
            sdf.applyPattern("dd/MM/yy HH:mm");
        String startDate = sdf.format(sd);
        String endDate = sdf.format(ed);
        newformatStartDate = sdf.parse(startDate);
        newformatEndDate = sdf.parse(endDate);

        } catch (ParseException e){
            e.printStackTrace();
        }
//        Log.d(TAG, "DATES: " + startDate);
//        Log.d(TAG, "DATES: " + endDate);

        return new Date[]{newformatStartDate, newformatEndDate};
    };

    private long getDur (Date[] dates){
        Date startDate = dates[0];
        Date endDate = dates[1];

        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

//        Log.d(TAG, "DATES, duration: " +diff);

        return diff;
    }

    public ArrayList<TsItem> getApplications() {
        return applications;
    }

    public boolean parse(String xmlData) {

        boolean status = true;
        TsItem currentTsItem = null;
        boolean inEntry = false;
        String textValue = "";
        Log.d(TAG, "parse: xmldata: " + xmlData);

        try {
                Log.d(TAG, "parse: Try");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
//            eventType = xpp.next();
//
            Log.d(TAG, "parse: Event type: " + eventType);


            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if("item".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentTsItem = new TsItem();
                            Log.d(TAG, "parse: NEW ITEM");
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:



                        if(inEntry) {
                            if("item".equalsIgnoreCase(tagName)) {
                                applications.add(currentTsItem);
                                inEntry = false;
                            } else if("title".equalsIgnoreCase(tagName)) {
                                currentTsItem.setTitle(textValue);
                            }else if("description".equalsIgnoreCase(tagName)) {
                                Log.d("MyTag", "Desc0: "+textValue);
                                currentTsItem.setDesc0(textValue);
                                if (textValue.contains("Start Date:")) {
                                    String[] parts = textValue.split("<br />");
                                    Date dates[] = getDates(parts);
                                    long duration = getDur(dates);
                                    Log.d("MyTag", ""+parts[0]);
                                    Log.d("MyTag", ""+parts[1]);
                                    Log.d("MyTag", "DURATION: "+duration);

                                    String period = dates[0].toString().substring(0, 10) + " - " + dates[1].toString().substring(0, 10) ;
                                    String period2 = dates[0].toString().substring(0, 23) + " - " + dates[1].toString().substring(0, 23) ;
//                                    String period = dates[0].toString();

                                    currentTsItem.setDesc(parts[2]);
                                    currentTsItem.setStartDate(dates[0]);
                                    currentTsItem.setEndDate(dates[1]);
                                    currentTsItem.setPeriod(period);
                                    currentTsItem.setPeriod2(period2);
                                    currentTsItem.setDuration(duration);
                                    currentTsItem.setInc(false);
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getStartDate());
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getEndDate());
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getDuration());
//                                    Log.d(TAG, "DATES: " + currentTrafficItem.getPeriod());
                                } else {
                                    currentTsItem.setInc(true); currentTsItem.setDesc(textValue);}
                            }else if("link".equalsIgnoreCase(tagName)) {
                                currentTsItem.setLink(textValue);
                            }else if("point".equalsIgnoreCase(tagName)) {
                                Log.d("MyTag", "GEO TAG: "+textValue);
                                currentTsItem.setGeoRss(textValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();

            }
            Log.d("ParseIncidents", "new items: ");
            for (TsItem app: applications) {
//                Log.d(TAG, "******************");
//                Log.d(TAG, app.toString());
            }

        } catch(Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }



}