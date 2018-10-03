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

    private String parseJSON(JSONObject json) throws JSONException {

        StringBuffer bodyText = new StringBuffer();

        Iterator<String> iter = json.keys();

        while (iter.hasNext()) {
            String key = iter.next();

            //Check if it's a Vitals array
            if (key.equalsIgnoreCase("vitals")) {
                //do some vitals stuff
                parseVitals(json.getJSONArray(key));
                return "";
            }

            bodyText.append("<b><font color=\"#0071bc\">" + key + "</font></b><br/>");

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

}
