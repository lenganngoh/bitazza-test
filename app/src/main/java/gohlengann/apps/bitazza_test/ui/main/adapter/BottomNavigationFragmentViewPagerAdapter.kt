package gohlengann.apps.bitazza_test.ui.main.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class BottomNavigationFragmentViewPagerAdapter(
    private val callback: Callback,
    supportFragmentManager: FragmentManager
) :
    FragmentPagerAdapter(
        supportFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getCount(): Int {
        return 5
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> callback.getMarketFragment()
            1 -> callback.getWalletFragment()
            2 -> callback.getNewsFragment()
            3 -> callback.getActivityFragment()
            4 -> callback.getSettingsFragment()
            else -> callback.getWalletFragment()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

    interface Callback {
        fun getMarketFragment(): Fragment
        fun getWalletFragment(): Fragment
        fun getNewsFragment(): Fragment
        fun getActivityFragment(): Fragment
        fun getSettingsFragment(): Fragment
    }
}