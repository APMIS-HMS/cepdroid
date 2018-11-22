package ng.apmis.apmismobile.ui.dashboard.healthProfile;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.SharedPreferencesManager;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.documentationModel.DoubleTypeAdapter;
import ng.apmis.apmismobile.data.database.documentationModel.IntegerTypeAdapter;
import ng.apmis.apmismobile.data.database.documentationModel.Vitals;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.AppUtils;
import ng.apmis.apmismobile.utilities.Constants;
import ng.apmis.apmismobile.utilities.InjectorUtils;

/**
 * Created by mofeejegi-apmis.<br/>
 * A view for showing hhe Person's health profile
 */
public class HealthProfileFragment extends Fragment {

    @BindView(R.id.blood_pressure_text)
    TextView bloodPressureTextView;
    @BindView(R.id.bmi_text)
    TextView bmiText;
    @BindView(R.id.temperature_text)
    TextView temperatureTextView;
    @BindView(R.id.graph)
    LineChart chart;

    //List of Vitals from which the health profile detail would be based on
    List<Vitals> vitalsList;

    private HealthProfileViewModel healthProfileViewModel;
    SharedPreferencesManager preferencesManager;

    public HealthProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_profile, container, false);
        ButterKnife.bind(this, view);

        vitalsList = new ArrayList<>();

        preferencesManager = new SharedPreferencesManager(getContext());


        //and initialize the view model to observe these records
        initViewModel();

        return view;
    }

    private void initViewModel() {
        HealthProfileViewModelFactory factory = InjectorUtils.provideHealthProfileViewModelFactory(getActivity().getApplicationContext());
        healthProfileViewModel = ViewModelProviders.of(this, factory).get(HealthProfileViewModel.class);

        final Observer<List<Documentation>> vitalsObserver = documentations -> {

            if (documentations == null) return;

            this.vitalsList.clear();
            //graphView.removeAllSeries();
            try {
                this.vitalsList = populateVitalsFromDocumentations(documentations);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!vitalsList.isEmpty()){

                //get the newest (most recent) vitals object and use
                // it for the main views in the health profile
                Vitals bodyVitals = vitalsList.get(0);

                bloodPressureTextView.setText(bodyVitals.getBloodPressure().getSystolic() + " mmHg");
                bmiText.setText(bodyVitals.getBodyMass().getBmi() + "");
                temperatureTextView.setText(Html.fromHtml(bodyVitals.getTemperature() + "&deg;C" ));
                //Plot the graph. Vitals are ordered from newest to oldest
                plotGraph(vitalsList);

            }
        };

        healthProfileViewModel.getRecordsForPerson(preferencesManager.getPersonId()).observe(this, vitalsObserver);
    }

    /**
     * Plot a graph using the last five vital records taken from this person
     * @param vitalsList the list containing all the Vital records
     */
    private void plotGraph(List<Vitals> vitalsList){
        //Max size is 5 or the size of the list (if less than 5)
        int maxSize = vitalsList.size()>5 ? 5 : vitalsList.size();
        List<Vitals> graphItems = vitalsList.subList(0, maxSize); //sublist of recent (maxsize)

        //Reverse the list to show the oldest out of the (maxsize) first
        Collections.reverse(graphItems);

        //Prepare entry lists for the chart
        List<Entry> temperatureEntries = new ArrayList<>();
        List<Entry> bloodPressureEntries = new ArrayList<>();
        List<Entry> heightEntries = new ArrayList<>();
        List<Entry> weightEntries = new ArrayList<>();

        for (int i=0; i < graphItems.size(); ++i){
            Vitals item = graphItems.get(i);
            temperatureEntries.add(new Entry((float) i, (AppUtils.doubleToFloat(item.getTemperature()))));
            bloodPressureEntries.add(new Entry((float) i, (item.getBloodPressure().getSystolic().floatValue())));
//            if (item.getBodyMass().getBmi() != null)
//                bmiEntries.add(new Entry((float) i, (AppUtils.doubleToFloat(item.getBodyMass().getBmi()))));
//            else
//                bmiEntries.add(new Entry((float) i, 0f));
            weightEntries.add(new Entry((float) i, (AppUtils.doubleToFloat(item.getBodyMass().getWeight()))));
            heightEntries.add(new Entry((float) i, (AppUtils.doubleToFloat(item.getBodyMass().getHeight()))));

        }

        Typeface appFont = ResourcesCompat.getFont(getActivity(), R.font.montserrat);

        LineDataSet temperatureDataSet = new LineDataSet(temperatureEntries, getActivity().getResources().getString(R.string.temp_label)); // add entries to dataSet
        temperatureDataSet.setValueTypeface(appFont);
        temperatureDataSet.setColor(Color.BLUE);
        temperatureDataSet.setLineWidth(3f);
        temperatureDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        temperatureDataSet.setDrawFilled(true);
        temperatureDataSet.setFillColor(Color.BLUE);
        temperatureDataSet.setFillAlpha(40);


        LineDataSet bloodPressureDataSet = new LineDataSet(bloodPressureEntries, "B.P (mmHg) ."); // add entries to dataSet
        bloodPressureDataSet.setValueTypeface(appFont);
        bloodPressureDataSet.setColor(Color.GRAY);
        bloodPressureDataSet.setLineWidth(3f);
        bloodPressureDataSet.setDrawFilled(true);
        bloodPressureDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        bloodPressureDataSet.setFillColor(Color.GRAY);
        bloodPressureDataSet.setFillAlpha(40);

        LineDataSet heightDataSet = new LineDataSet(heightEntries, "Hgt. (m) ."); // add entries to dataSet
        heightDataSet.setValueTypeface(appFont);
        heightDataSet.setColor(Color.GREEN);
        heightDataSet.setLineWidth(3f);
        heightDataSet.setDrawFilled(true);
        heightDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        heightDataSet.setFillColor(Color.GREEN);
        heightDataSet.setFillAlpha(40);

        LineDataSet weightDataSet = new LineDataSet(weightEntries, "Wgt. (kg) ."); // add entries to dataSet
        weightDataSet.setValueTypeface(appFont);
        weightDataSet.setColor(Color.rgb(238, 128, 51));
        weightDataSet.setLineWidth(3f);
        weightDataSet.setDrawFilled(true);
        weightDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        weightDataSet.setFillColor(Color.rgb(238, 128, 51));
        weightDataSet.setFillAlpha(40);


        LineData lineData = new LineData(bloodPressureDataSet, temperatureDataSet, heightDataSet, weightDataSet);
        lineData.setValueTypeface(appFont);
        chart.setData(lineData);

        //X-Axis formatting
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setValueFormatter(new DateXAxisValueFormatter(graphItems));
        chart.getXAxis().setGranularity(1f); //Make intervals 1
        chart.getXAxis().setTypeface(appFont);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setAxisMinimum((maxSize > 1) ? 0 : -1); //set -1 ... 0 ... 1(max size) if it's <=1
        chart.getXAxis().setAxisMaximum((maxSize > 1) ? maxSize-1 : 1); //set -1 ... 0 ... 1(max size) if it's <=1
        chart.getXAxis().setLabelCount((maxSize > 1) ? maxSize : 3, true); //set count to 3 if it's <=1

        //Left Y-Axis formatting
        chart.getAxisLeft().setTypeface(appFont);
        chart.getAxisLeft().setDrawGridLines(false);

        //Right Y-Axis formatting
        chart.getAxisRight().setTypeface(appFont);
        chart.getAxisRight().setAxisLineColor(getActivity().getResources().getColor(android.R.color.transparent));
        chart.getAxisRight().setTextColor(getActivity().getResources().getColor(android.R.color.transparent));
        chart.getAxisRight().setDrawGridLines(false);

        //Chart Description Formatting
        chart.getDescription().setTypeface(appFont);
        chart.getDescription().setText("Date taken");

        //Chart Legend
        chart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        chart.getLegend().setTypeface(appFont);

        // animate both axes with easing
        chart.animateY(3000, Easing.EasingOption.EaseOutBack);
        chart.invalidate(); // refresh
    }

    /**
     * Get the Vitals list from the documentation objects
     * @param documentations The Medical Records
     * @return The Vitals List
     * @throws JSONException Exception
     */
    private List<Vitals> populateVitalsFromDocumentations(List<Documentation> documentations) throws JSONException {

        //Iterate through all documentations
        for (Documentation documentation : documentations) {
            //attempt parsing the Body Json object for a Vitals list
            List<Vitals> list = parseJSON(documentation.getDocument().getBody());

            //if found, return it
            if (!list.isEmpty())
                return list;
        }

        //otherwise return an empty list
        return new ArrayList<>();
    }

    /**
     * Attempt to find a Vitals object and return it's List
     * @param json The JSONObject containing the data
     * @return The Vitals list or an empty list if Vitals aren't found
     * @throws JSONException Exception
     */
    private List<Vitals> parseJSON(JSONObject json) throws JSONException {

        Iterator<String> iter = json.keys();

        while (iter.hasNext()) {
            String key = iter.next();

            //Check if it's a Vitals array
            if (key.equalsIgnoreCase("vitals")) {
                //do some vitals stuff
                return parseVitals(json.getJSONArray(key));
            }
        }

        return new ArrayList<>();
    }

    /**
     * Parse the {@link Vitals} gotten from the JSONObject and return the list of it
     * @param json The JSONArray containing the vitals
     * @return The Vitals List or an empty List if the JSON Array is empty
     */
    private List<Vitals> parseVitals(JSONArray json){

        if (json.length() > 0) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(int.class, new IntegerTypeAdapter());
            builder.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
            builder.registerTypeAdapter(double.class, new DoubleTypeAdapter());
            builder.registerTypeAdapter(Double.class, new DoubleTypeAdapter());
            Gson gson = builder.create();

            //Get the vitals and sort them in descending order from newest to oldest
            List<Vitals> vitals = new ArrayList<>(Arrays.asList(gson.fromJson(json.toString(), Vitals[].class)));
            Comparator<Vitals> comparator = (o1, o2) -> o2.compareTo(o1);
            Collections.sort(vitals, comparator);

            return vitals;
        }

        return new ArrayList<>();
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            ((DashboardActivity)getActivity()).setToolBarTitleAndBottomNavVisibility(Constants.VITALS, false);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        healthProfileViewModel.getRecordsForPerson(preferencesManager.getPersonId()).removeObservers(this);
        super.onStop();
    }


    /**
     * Axis value formatting for the Date X Axis using converting indices
     * to dates by getting the {@link Vitals#updatedAt} field from a Vitals list
     */
    public class DateXAxisValueFormatter implements IAxisValueFormatter {

        private List<Vitals> mVitals;

        public DateXAxisValueFormatter(List<Vitals> values) {
            this.mVitals = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            try {
                return AppUtils.dateToVeryShortDateString(AppUtils.dbStringToLocalDate(mVitals.get((int) value).getUpdatedAt()));
            } catch (Exception e) {
                return "";
            }

        }


    }


}
