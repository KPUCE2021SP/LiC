package com.example.stacklounge.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.stacklounge.R
import kotlinx.android.synthetic.main.fragment_main_favorite.view.*

class FragmentMainFavorite : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main_favorite, null)


        return inflater.inflate(R.layout.fragment_main_favorite, container, false)
    }

    //appbar 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_navi_menu, menu)
    }

    //appbar 메뉴 클릭 시
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tbmenu -> {
                //Toast.makeText(activity,"메뉴",Toast.LENGTH_SHORT).show()
                true
            }

            R.id.tbsearch -> {
                // navigate to settings screen
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}