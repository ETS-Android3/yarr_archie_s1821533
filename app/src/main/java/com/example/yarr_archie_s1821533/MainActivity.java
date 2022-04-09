//<!--Archie Yarr S1821533-->
package com.example.yarr_archie_s1821533;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView itemsTitle;

    public ArrayAdapter adapter;

    private TextView roadworks;
    private TextView planned;
    private String result = "" ;
    private Button startIncidentsButton;
    private Button startPlannedButton;
    private Button startRoadworksButton;
    //private Button loadButton;
    private LinearLayout loadLayout;
    // Traffic Scotland URLs
    private String roadworksURL = "https://trafficscotland.org/rss/feeds/roadworks.aspx";
    private String plannedURL = "https://trafficscotland.org/rss/feeds/plannedroadworks.aspx";
    private String incidentsURL = "https://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private ArrayList<TsItem> itemArrayList;

    private ListView listView;
    private EditText search;

    private String plannedTitle[] = {};
    private String incidentTitle[] = {};
    private String roadWorkTitle[] = {};
    private ArrayList<TsItem> incidentItems;
    private ArrayList<TsItem> roadworksItems;
    private ArrayList<TsItem> plannedItems;

   private ProgressBar spinner;

    private String blankTitles[] = {};
    private ArrayList<TsItem> test = new ArrayList<>();
    TsItem tsItem = new TsItem();
    private CustomArrAdapter adapterino;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        test.add(tsItem);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemsTitle = (TextView) findViewById(R.id.itemsTitle);

        startIncidentsButton = (Button) findViewById(R.id.incidentsButton);
        startIncidentsButton.setOnClickListener(this);

        startRoadworksButton = (Button) findViewById(R.id.roadworksButton);
        startRoadworksButton.setOnClickListener(this);

        startPlannedButton = (Button) findViewById(R.id.plannedButton);
        startPlannedButton.setOnClickListener(this);


        spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);




        listView = (ListView) findViewById(R.id.itemsList);
            search = (EditText) findViewById(R.id.filter);
                search.setVisibility(View.INVISIBLE);




        adapterino = new CustomArrAdapter(this, test);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"] - Start ["+start+"] - Before ["+before+"] - Count ["+count+"]");
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    adapterino.resetData();
                }
                adapterino.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // Log.d("MyTag", "*** Search value changed: " + s.toString());

            }
        }
        );

    }


    public void clear(){
        Log.e("MyTag","clear i:" + incidentTitle.length);
        Log.e("MyTag","clear p:" + plannedTitle.length);
        Log.e("MyTag","clear r:" + roadWorkTitle.length);

        String blank[] = {};
        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                blank);
        listView.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);
        search.setVisibility(View.INVISIBLE);

    }


    public void setAdapter(String type){
        ArrayAdapter<String> adapter;
        CustomArrAdapter ad;

        switch(type) {
            case "incident":
                Log.e("MyTag","items i : "+ incidentItems);

                adapterino = new CustomArrAdapter(
                        MainActivity.this,

                        incidentItems);
                listView.setAdapter(adapterino);

                break;
            case "roadworks":
                Log.e("MyTag","items r : "+ roadworksItems);

                adapterino = new CustomArrAdapter(
                        MainActivity.this,

                        roadworksItems);
                listView.setAdapter(adapterino);

                break;
            case "planned":
                Log.e("MyTag","items P : "+ plannedItems);

                adapterino = new CustomArrAdapter(
                        MainActivity.this,

                        plannedItems);
                listView.setAdapter(adapterino);
                break;
        }
    }


    public void onClick(View view) {


    String type;
        Log.e("MyTag","Pressed a button...");

        switch(view.getId()){
            case R.id.incidentsButton:
                type = "incident";
                Log.e("MyTag","Button clicked:"+type);
                itemsTitle.setText("Incidents:");
//                Log.e("MyTag","TitlesI: "+titlesI[0]);
                if (incidentTitle.length != 0){
                    Log.e("MyTag","incident itles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(incidentsURL, type);
                }
                break;
            case R.id.roadworksButton:
                type = "roadworks";
                Log.e("MyTag","Button clicked:"+type);
                itemsTitle.setText("Roadworks:");
                Log.e("MyTag","TitlesI: "+roadWorkTitle.toString());
                if (roadWorkTitle.length != 0){
                    Log.e("MyTag","roadworks titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(roadworksURL, type);
                }
                break;
            case R.id.plannedButton:
                type = "planned";
                Log.e("MyTag","Button clicked:"+type);
                itemsTitle.setText("Planned Roadworks:");
                Log.e("MyTag","TitlesI: "+ plannedTitle.toString());
                if (plannedTitle.length != 0){
                    Log.e("MyTag","planned titles Not 0");
                    setAdapter(type);
                }else{
                    clear();
                    startProgress(plannedURL, type);
                    }
                break;


        }

//        int id = aview.getId();
//        Log.e("MyTag", aview.toString());
//        Log.e("MyTag", id);
//        Log.e("MyTag", Integer.toString(id));


    }

    public void startProgress(String url, String type)
    {
        // Run network access on a separate thread
        String[] urlAndType = {url, type};
        Log.e("MyTag","startprogress... url: "+url + " type: "+type);

        new downloadTask().execute(urlAndType);


    } //

    public ArrayList handleResult(String data){
        Log.e("MyTag", "handleresult data: " + data);

        PullTsParser p = new PullTsParser();
        p.parse(data);

//        Log.e("MyTag", "handelresult itemArraylist size: " + itemArrayList.size());
//        Log.e("MyTag", "handelresult itemArraylist size: " + itemArrayList.size());

        return p.getApplications();
    }

    private class downloadTask extends AsyncTask<String[], Void, ArrayList<TsItem>> {

        String tType;

        @Override
        public ArrayList<TsItem> doInBackground(String[]... params) {
            tType = params[0][1];

            Log.e("MyTag","In background, params: "+ params[0][0]+tType);
            return getData(params[0][0]);
        }

        @Override
        protected void onPostExecute(ArrayList<TsItem> result) {
            super.onPostExecute(result);

            spinner.setVisibility(View.GONE);
            if(result.get(0).getTitle() == "No Incidents Found") {
            }else{
                search.setVisibility(View.VISIBLE);
            }

            setAdapter(tType);
        }

        private ArrayList<TsItem> getData(String url)
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag","GETTING DATA FROM " + url);
            Log.e("MyTag","in run");

            try{
                result = "";
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","GETTING DATA FROMd " + url);

                Log.e("MyTag", "result:");
                while ((inputLine = in.readLine()) != null){

                    result = result + inputLine;

                }
                Log.d("MyTag", "result done : " + result);
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it

            ArrayList<TsItem> items= handleResult(result);
            Log.d("Post handle result ", "items: "+ items.size());


            String titles[];
            int i = 0;
            titles = new String [items.size()];
            for (TsItem o : items){

                titles[i] = o.getTitle();
                i++;
            }

            if (items.size() == 0) {
                TsItem neu = new TsItem();
                neu.setTitle("No Incidents Found");
                neu.setDesc("None");
                neu.setPubDate("Mon, 01 Jan 2020 00:00:00 GMT");
                neu.setLink("www yahoo");
                neu.setInc(true);
                items.add(neu);
            }

            switch(tType) {
                case "incident":
                    incidentTitle = titles;
                    incidentItems = items;
                    break;
                case "roadworks":
                    roadWorkTitle = titles;
                    roadworksItems = items;
                    break;
                case "planned":
                    plannedTitle = titles;
                    plannedItems = items;
                    break;
            }
            return items;
        }
    }

}