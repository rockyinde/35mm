package free.rm.skytube.gui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import free.rm.skytube.R;
import free.rm.skytube.businessobjects.Logger;
import free.rm.skytube.businessobjects.db.BookmarksDb;
import free.rm.skytube.businessobjects.db.DownloadedVideosDb;
import free.rm.skytube.gui.businessobjects.adapters.SubsAdapter;
import free.rm.skytube.gui.businessobjects.fragments.FragmentEx;
import free.rm.skytube.gui.fragments.mms.MMSComedyFragment;
import free.rm.skytube.gui.fragments.mms.MMSHitsFragment;
import free.rm.skytube.gui.fragments.mms.MMSOldFragment;
import free.rm.skytube.gui.fragments.mms.MMSPastFragment;
import free.rm.skytube.gui.fragments.mms.MMSPopularFragment;
import free.rm.skytube.gui.fragments.mms.MMSRecentFragment;

public class MainFragment extends FragmentEx {

	private RecyclerView				subsListView = null;
	private SubsAdapter					subsAdapter  = null;
	private ActionBarDrawerToggle		subsDrawerToggle;
	private TabLayout                   tabLayout = null;
	private DrawerLayout 				subsDrawerLayout = null;

	/** List of fragments that will be displayed as tabs. */
	private List<VideosGridFragment>	videoGridFragmentsList = new ArrayList<>();
	private FeaturedVideosFragment		featuredVideosFragment = null;

	private MMSRecentFragment recentFragment = null;
	private MMSHitsFragment hitsFragment = null;
	private MMSComedyFragment comedyFragment = null;
	private MMSPastFragment pastFragment = null;
	private MMSOldFragment oldFragment = null;
	private MMSPopularFragment popularFragment = null;

	private MostPopularVideosFragment	mostPopularVideosFragment = null;
	private SubscriptionsFeedFragment   subscriptionsFeedFragment = null;
	private BookmarksFragment			bookmarksFragment = null;
	private DownloadedVideosFragment    downloadedVideosFragment = null;

	// Constants for saving the state of this Fragment's child Fragments
	public static final String RECENT_FRAGMENT = "MainFragment.recentFragment";
	public static final String HITS_FRAGMENT = "MainFragment.hitsFragment";
	public static final String POPULAR_FRAGMENT = "MainFragment.popularFragment";
	public static final String BOOKMARKS_FRAGMENT = "MainFragment.bookmarksFragment";

	private VideosPagerAdapter			videosPagerAdapter = null;
	private ViewPager					viewPager;

	public static final String SHOULD_SELECTED_FEED_TAB = "MainFragment.SHOULD_SELECTED_FEED_TAB";

	private AdView mAdView;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(savedInstanceState != null) {

			recentFragment = (MMSRecentFragment) getChildFragmentManager().getFragment(savedInstanceState, RECENT_FRAGMENT);
			hitsFragment = (MMSHitsFragment) getChildFragmentManager().getFragment(savedInstanceState, HITS_FRAGMENT);
			popularFragment = (MMSPopularFragment) getChildFragmentManager().getFragment(savedInstanceState, POPULAR_FRAGMENT);
			bookmarksFragment = (BookmarksFragment) getChildFragmentManager().getFragment(savedInstanceState, BOOKMARKS_FRAGMENT);
		}
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		// setup the toolbar / actionbar
		Toolbar toolbar = view.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// indicate that this fragment has an action bar menu
		setHasOptionsMenu(true);

		subsDrawerLayout = view.findViewById(R.id.subs_drawer_layout);
		subsDrawerToggle = new ActionBarDrawerToggle(
						getActivity(),
						subsDrawerLayout,
						R.string.app_name,
						R.string.app_name
		);
//		subsDrawerToggle.setDrawerIndicatorEnabled(true);
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setHomeButtonEnabled(true);
		}

		videosPagerAdapter = new VideosPagerAdapter(getChildFragmentManager());
		viewPager = view.findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(videoGridFragmentsList.size() - 1);
		viewPager.setAdapter(videosPagerAdapter);

		tabLayout = view.findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
				videoGridFragmentsList.get(tab.getPosition()).onFragmentSelected();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				videoGridFragmentsList.get(tab.getPosition()).onFragmentUnselected();
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});

		// select the default tab:  the default tab is defined by the user through the Preferences
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

		// If the app is being opened via the Notification that new videos from Subscribed channels have been found, select the Subscriptions Feed Fragment
		Bundle args = getArguments();
		if(args != null && args.getBoolean(SHOULD_SELECTED_FEED_TAB, false)) {
			viewPager.setCurrentItem(videoGridFragmentsList.indexOf(subscriptionsFeedFragment));
		} else {
			viewPager.setCurrentItem(Integer.parseInt(sp.getString(getString(R.string.pref_key_default_tab), "0")));
		}

		// Set the current viewpager fragment as selected, as when the Activity is recreated, the Fragment
		// won't know that it's selected. When the Feeds fragment is the default tab, this will prevent the
		// refresh dialog from showing when an automatic refresh happens.
		videoGridFragmentsList.get(viewPager.getCurrentItem()).onFragmentSelected();

		// load banner ad
		initializeGoogleAdMob();
		loadAdView(view);

		return view;
	}

	/**
	 * initializes the admob library with the admob App ID
	 */
	private void initializeGoogleAdMob() {

		MobileAds.initialize(getContext(), "ca-app-pub-9258673963588821~1256325557");
	}

	/**
	 * loads an ad unit
	 *
	 * @param view
	 */
	private void loadAdView(View view) {

		mAdView = view.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
//				.addTestDevice("213208C863404A6829AF8EDFE1EF48DA")
				.build();
		mAdView.loadAd(adRequest);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		subsDrawerToggle.syncState();
	}


	@Override
	public void onResume() {
		super.onResume();

		// when the MainFragment is resumed (e.g. after Preferences is minimized), inform the
		// current fragment that it is selected.
		if (videoGridFragmentsList != null  &&  tabLayout != null) {
			Logger.d(this, "MAINFRAGMENT RESUMED " + tabLayout.getSelectedTabPosition());
			videoGridFragmentsList.get(tabLayout.getSelectedTabPosition()).onFragmentSelected();
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app
		// icon touch event
		if (subsDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}


	private class VideosPagerAdapter extends FragmentPagerAdapter {

		public VideosPagerAdapter(FragmentManager fm) {
			super(fm);

			// initialise fragments
			if (featuredVideosFragment == null)
				featuredVideosFragment = new FeaturedVideosFragment();

			if (recentFragment == null)
				recentFragment = new MMSRecentFragment();

			if (hitsFragment == null)
				hitsFragment = new MMSHitsFragment();

			if (popularFragment == null)
				popularFragment = new MMSPopularFragment();

			if (bookmarksFragment == null) {
				bookmarksFragment = new BookmarksFragment();
				BookmarksDb.getBookmarksDb().addListener(bookmarksFragment);
			}

			// add fragments to list:  do NOT forget to ***UPDATE*** @string/default_tab and @string/default_tab_values
			videoGridFragmentsList.clear();
			videoGridFragmentsList.add(recentFragment);
			videoGridFragmentsList.add(hitsFragment);
			videoGridFragmentsList.add(popularFragment);
			videoGridFragmentsList.add(bookmarksFragment);
		}

		@Override
		public int getCount() {
			return videoGridFragmentsList.size();
		}

		@Override
		public Fragment getItem(int position) {
			return videoGridFragmentsList.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return videoGridFragmentsList.get(position).getFragmentName();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

		if(recentFragment != null && recentFragment.isAdded())
			getChildFragmentManager().putFragment(outState, RECENT_FRAGMENT, recentFragment);
		if(hitsFragment != null && hitsFragment.isAdded())
			getChildFragmentManager().putFragment(outState, HITS_FRAGMENT, hitsFragment);
        if(popularFragment != null && popularFragment.isAdded())
            getChildFragmentManager().putFragment(outState, POPULAR_FRAGMENT, popularFragment);
		if(bookmarksFragment != null && bookmarksFragment.isAdded())
			getChildFragmentManager().putFragment(outState, BOOKMARKS_FRAGMENT, bookmarksFragment);

		super.onSaveInstanceState(outState);
	}


	/**
	 * Returns true if the subscriptions drawer is opened.
	 */
	public boolean isDrawerOpen() {
		return subsDrawerLayout.isDrawerOpen(GravityCompat.START);
	}

	
	/**
	 * Close the subscriptions drawer.
	 */
	public void closeDrawer() {
		subsDrawerLayout.closeDrawer(GravityCompat.START);
	}

}
