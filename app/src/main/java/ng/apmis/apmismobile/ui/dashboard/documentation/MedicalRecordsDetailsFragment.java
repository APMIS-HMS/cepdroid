package ng.apmis.apmismobile.ui.dashboard.documentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.apmis.apmismobile.R;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.documentationModel.DoubleTypeAdapter;
import ng.apmis.apmismobile.data.database.documentationModel.IntegerTypeAdapter;
import ng.apmis.apmismobile.data.database.documentationModel.Vitals;
import ng.apmis.apmismobile.ui.dashboard.DashboardActivity;
import ng.apmis.apmismobile.utilities.AnnotationExclusionStrategy;
import ng.apmis.apmismobile.utilities.Constants;

/**
 * View for displaying details about Patient Medical Records,
 * like Allergies, Vitals and Doctor's notes
 */
public class MedicalRecordsDetailsFragment extends Fragment {
    private static final String DOCUMENTATION_KEY = "documentation_key";
    private Documentation documentation;

    @BindView(R.id.details_scroll)
    ScrollView detailsScroll;

    @BindView(R.id.details_text)
    TextView detailsTextView;

    @BindView(R.id.vitals_recycler)
    RecyclerView vitalsRecycler;


    public MedicalRecordsDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param documentation Documentation Record Object.
     * @return A new instance of fragment MedicalRecordsDetailsFragment.
     */
    public static MedicalRecordsDetailsFragment newInstance(Documentation documentation) {
        MedicalRecordsDetailsFragment fragment = new MedicalRecordsDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(DOCUMENTATION_KEY, documentation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            documentation = (Documentation) getArguments().getSerializable(DOCUMENTATION_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_medical_records_details, container, false);
        ButterKnife.bind(this, view);

        //Sets the details text on the view after parsing
        try {
            detailsTextView.setText(Html.fromHtml(parseJSON(documentation.getDocument().getBody())));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        if (getActivity() != null) {
            String documentTitle = documentation.getDocument().getDocumentType().getTitle();
            ((DashboardActivity)getActivity()).setToolBarTitle(documentTitle.toUpperCase(), false);

        }
        super.onResume();
    }

    /**
     * Parse the Object to resemble a proper doctor's note or in some cases,
     * call another function to parse a Vitals object
     * @param json The JSONObject containing the data
     * @return The well parsed doctor's note HTML or an empty string,
     * depending on the nature of information in the JSONObject
     * @throws JSONException Exception
     */
    private String parseJSON(JSONObject json) throws JSONException {

        StringBuilder bodyText = new StringBuilder();

        Iterator<String> iter = json.keys();

        while (iter.hasNext()) {
            String key = iter.next();

            //Check if it's a Vitals array
            if (key.equalsIgnoreCase("vitals")) {
                //do some vitals stuff
                parseVitals(json.getJSONArray(key));
                //and return an empty string for the details
                return "";
            }

            //If not a Vitals object, then begin parsing the notes
            bodyText.append("<b><font color=\"#0071bc\">" + formatKeyTitle(key) + "</font></b><br/>");

            try {
                bodyText.append("<p>");
                Object value = json.get(key);

                if (value.toString().startsWith("[")){
                    JSONArray arr = new JSONArray(value.toString());
                    for (int i=0; i<arr.length(); ++i){
                        bodyText.append(" - "+arr.get(i).toString()+"<br/>");
                    }

                } else {
                    bodyText.append(value.toString());
                }

                bodyText.append("</p>");

            } catch (JSONException e) {
                Log.e("Document", e.getLocalizedMessage());
            }

            bodyText.append("<br/>");
        }

        return bodyText.toString();
    }

    /**
     * Parse the {@link Vitals} gotten from the JSONObject and populate a RecyclerView with the information
     * @param json The JSONArray containing the vitals
     */
    private void parseVitals(JSONArray json){

        if (json.length() > 0) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(int.class, new IntegerTypeAdapter());
            builder.registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
            builder.registerTypeAdapter(double.class, new DoubleTypeAdapter());
            builder.registerTypeAdapter(Double.class, new DoubleTypeAdapter());
            Gson gson = builder.create();

            List<Vitals> vitals = Arrays.asList(gson.fromJson(json.toString(), Vitals[].class));
            Log.v("DocumentationV", vitals.get(0).toString());

            detailsScroll.setVisibility(View.GONE);
            vitalsRecycler.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            vitalsRecycler.setLayoutManager(layoutManager);
            VitalsAdapter vitalsAdapter = new VitalsAdapter(getContext(), vitals);
            vitalsRecycler.setAdapter(vitalsAdapter);

            DividerItemDecoration divider = new DividerItemDecoration(
                    vitalsRecycler.getContext(), layoutManager.getOrientation());
            vitalsRecycler.addItemDecoration(divider);

        }
    }

    /**
     * Format the key title of clinical documentations
     * to resemble something readable and presentable,
     * e.h by converting "camelHump" to "Camel Hump".
     * @param keyTitle the documentation key title
     */
    private String formatKeyTitle(String keyTitle) {
        String formattedString = keyTitle;
        int offset = 0;

        //Iterate through all the characters in the String
        for (int count=0; count<keyTitle.length(); ++count) {

            //if the character is an upper case one, then add a space behind it
            if (Character.isUpperCase(keyTitle.charAt(count))) {
                formattedString = formattedString.replace("" + formattedString.charAt(count+offset),
                        " " + formattedString.charAt(count+offset));

                //increase the offset since the string is now longer
                ++offset;
            }
        }

        //Make the first letter upper case too
        formattedString = formattedString.replaceFirst(formattedString.charAt(0)+"",
                (formattedString.charAt(0)+"").toUpperCase()  );

        return formattedString;
    }

}
