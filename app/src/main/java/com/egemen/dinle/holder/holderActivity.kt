package com.egemen.dinle.holder



import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.egemen.dinle.models.Users
import com.egemen.dinle.R
class holderActivity : Fragment() {
    var isConnected:Boolean=true
    lateinit var fragmentListesi:ArrayList<Fragment>
    lateinit var tabBaslikListesi:ArrayList<String>
    lateinit var fragmentView: View
    lateinit var kullanicilar:ArrayList<Users>
    lateinit var tumDoktorlar:ArrayList<String>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentView = inflater?.inflate(R.layout.kitaplar_layout, container, false)
        fragmentListesi = ArrayList<Fragment>()
        tabBaslikListesi = ArrayList<String>()
        var viewPager=fragmentView.findViewById<View>(R.id.diyetisyenler_view_pager) as ViewPager
        var tabLayout=fragmentView.findViewById<View>(R.id.tabs) as TabLayout


        fragmentListesi.add(kitaplar_fragment())
        tabBaslikListesi.add("Kitaplar")
        fragmentListesi.add(begeni_fragment())
        tabBaslikListesi.add("Beğenilenler")
        var adapter: TextTabAdapter = TextTabAdapter(fragmentManager, fragmentListesi, tabBaslikListesi)
        viewPager.adapter=adapter
        tabLayout.setupWithViewPager(viewPager)


        return fragmentView
    }

}


