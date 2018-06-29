/*
 * SkyTube
 * Copyright (C) 2016  Ramon Mifsud
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation (version 3 of the License).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package free.rm.skytube.gui.activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import free.rm.skytube.R;
import free.rm.skytube.app.SkyTubeApp;
import free.rm.skytube.businessobjects.interfaces.YouTubePlayerFragmentInterface;
import free.rm.skytube.gui.businessobjects.BackButtonActivity;
import free.rm.skytube.gui.businessobjects.fragments.FragmentEx;
import free.rm.skytube.gui.fragments.YouTubePlayerFragment;
import free.rm.skytube.gui.fragments.YouTubePlayerV2Fragment;

/**
 * An {@link Activity} that contains an instance of
 * {@link free.rm.skytube.gui.fragments.YouTubePlayerFragment}.
 */
public class YouTubePlayerActivity extends BackButtonActivity {

	private FragmentEx videoPlayerFragment;
	private YouTubePlayerFragmentInterface fragmentListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final boolean useDefaultPlayer = useDefaultPlayer();

		// if the user wants to use the default player, then ensure that the activity does not
		// have a toolbar (actionbar) -- this is as the fragment is taking care of the toolbar
		if (useDefaultPlayer) {
			setTheme(R.style.NoActionBarActivityTheme);
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_holder);

		// either use the SkyTube's default video player or the legacy one
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		videoPlayerFragment = useDefaultPlayer ? new YouTubePlayerV2Fragment() : new YouTubePlayerFragment();
		try {
			fragmentListener = (YouTubePlayerFragmentInterface) videoPlayerFragment;
		} catch(ClassCastException e) {
			throw new ClassCastException(videoPlayerFragment.toString()
							+ " must implement YouTubePlayerFragmentInterface");
		}
		fragmentTransaction.add(R.id.fragment_container, videoPlayerFragment);
		fragmentTransaction.commit();
	}


	/**
	 * @return True if the user wants to use SkyTube's default video player;  false if the user wants
	 * to use the legacy player.
	 */
	private boolean useDefaultPlayer() {
		final String defaultPlayerValue = getString(R.string.pref_default_player_value);
		final String str = SkyTubeApp.getPreferenceManager().getString(getString(R.string.pref_key_choose_player), defaultPlayerValue);

		return str.equals(defaultPlayerValue);
	}


	@Override
	protected void onStart() {
		super.onStart();

		// set the video player's orientation as what the user wants
		String  str = SkyTubeApp.getPreferenceManager().getString(getString(R.string.pref_key_screen_orientation), getString(R.string.pref_screen_auto_value));
		int     orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

		if (str.equals(getString(R.string.pref_screen_landscape_value)))
			orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
		if (str.equals(getString(R.string.pref_screen_portrait_value)))
			orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
		if (str.equals(getString(R.string.pref_screen_sensor_value)))
			orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;

		setRequestedOrientation(orientation);
	}


	@Override
	protected void onStop() {
		super.onStop();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	@Override
	protected void onPause() {

		super.onPause();
		if (videoPlayerFragment instanceof YouTubePlayerV2Fragment) {
			((YouTubePlayerV2Fragment) videoPlayerFragment).pause();
		}
	}

	@Override
	public void onPanelClosed(int featureId, Menu menu) {
		super.onPanelClosed(featureId, menu);

		// notify the player that the menu is no longer visible
		if (videoPlayerFragment instanceof YouTubePlayerV2Fragment) {
			((YouTubePlayerV2Fragment) videoPlayerFragment).onMenuClosed();
		}
	}

	@Override
	public void onBackPressed() {
		fragmentListener.videoPlaybackStopped();
		super.onBackPressed();
	}

	// If the back button in the toolbar is hit, save the video's progress (if playback history is not disabled)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			fragmentListener.videoPlaybackStopped();
		}
		return super.onOptionsItemSelected(item);
	}
}
