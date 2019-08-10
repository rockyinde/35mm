package free.rm.skytube.gui.fragments.mms;

import free.rm.skytube.R;
import free.rm.skytube.app.SkyTubeApp;
import free.rm.skytube.businessobjects.VideoCategory;
import free.rm.skytube.gui.fragments.VideosGridFragment;

public class MMSHitsFragment extends VideosGridFragment {

    @Override
    protected VideoCategory getVideoCategory() {
        return VideoCategory.HITS;
    }

    @Override
    public String getFragmentName() {
        return SkyTubeApp.getStr(R.string.hits);
    }
}
