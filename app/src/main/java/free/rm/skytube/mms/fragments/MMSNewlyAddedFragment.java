package free.rm.skytube.mms.fragments;

import free.rm.skytube.R;
import free.rm.skytube.app.SkyTubeApp;
import free.rm.skytube.businessobjects.VideoCategory;
import free.rm.skytube.gui.fragments.VideosGridFragment;

public class MMSNewlyAddedFragment extends VideosGridFragment {

    @Override
    protected VideoCategory getVideoCategory() {
        return VideoCategory.NEWLY_ADDED;
    }

    @Override
    public String getFragmentName() {
        return SkyTubeApp.getStr(R.string.newly);
    }
}
