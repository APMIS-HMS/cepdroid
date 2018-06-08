package ng.apmis.apmismobile.ui.dashboard;

/**
 * Created by Thadeus-APMIS on 5/16/2018.
 */

public class ModuleListModel {

    private String mOption;
    private int mOptionImage;

    public ModuleListModel (String option, int optionImage) {
        mOption = option;
        mOptionImage = optionImage;
    }

    public String getmOption() {
        return mOption;
    }

    public int getmOptionImage() {
        return mOptionImage;
    }

}
