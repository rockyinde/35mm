package free.rm.skytube.gui.fragments;

import free.rm.skytube.R;
import free.rm.skytube.app.SkyTubeApp;
import free.rm.skytube.businessobjects.VideoCategory;

public class MMSOldFragment extends VideosGridFragment {

    @Override
    protected VideoCategory getVideoCategory() {
        return VideoCategory.OLD;
    }

    @Override
    public String getFragmentName() {
        return SkyTubeApp.getStr(R.string.old);
    }
}
