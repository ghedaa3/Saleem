package sa.ksu.gpa.saleem


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter(fragmentManager: FragmentManager,date:String) :
    FragmentStatePagerAdapter(fragmentManager) {
    var date = date

    // 2
    override fun getItem(position: Int): Fragment {
        return CounterFragment(position,date)
    }

    // 3
    override fun getCount(): Int {
        return 2
    }
}