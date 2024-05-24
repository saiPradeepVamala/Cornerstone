package com.cornerstonehospice.android.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.cornerstonehospice.R
import com.cornerstonehospice.android.activities.MainDashboardActivity
import com.newsstand.ScrollTabHolderFragment

class ContactUsFragment : ScrollTabHolderFragment(), AbsListView.OnScrollListener {
    private var mListView: ListView? = null
    private var mListItems: ArrayList<String>? = null
    private var mPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPosition = requireArguments().getInt(ARG_POSITION)
        mListItems = ArrayList()
        for (i in 1..100) {
            mListItems!!.add(i.toString() + ". item - currnet page: " + (mPosition + 1))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.headered_viewpager_fragment_list, null)
        mListView = v.findViewById<View>(R.id.listView) as ListView
        val placeHolderView = inflater.inflate(R.layout.headered_viewpager_header_placeholder, mListView, false)
        mListView!!.addHeaderView(placeHolderView)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mListView!!.setOnScrollListener(this)
        //	 	mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.headered_viewpager_list_item, android.R.id.text1, mListItems));
        mListView!!.adapter = activity?.let { listAdapter(it) }
    }

    override fun adjustScroll(scrollHeight: Int) {
        if (scrollHeight == 0 && mListView!!.firstVisiblePosition >= 1) {
            return
        }
        mListView!!.setSelectionFromTop(1, scrollHeight)
    }

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (mScrollTabHolder != null) mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition)
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        // nothing
    }

    inner class listAdapter(a: Activity) : BaseAdapter() {
        var inflater: LayoutInflater? = null

        init {
            inflater = a.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        override fun getCount(): Int {
            return 1
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var vi = convertView
            //			ViewHolder holder;
            if (convertView == null) {
                /****** Inflate tabitem.xml file for each row ( Defined below )  */
                vi = inflater!!.inflate(R.layout.fragment_contact_us, null)
                /****** View Holder Object to contain tabitem.xml file elements  */
                vi.findViewById<View>(R.id.email_btn).setOnClickListener(mViewClicksListener)
                vi.findViewById<View>(R.id.phone_btn).setOnClickListener(mViewClicksListener)
                //				holder = new ViewHolder();
//				holder.text = (TextView) vi.findViewById(R.id.text);
//				holder.text1=(TextView)vi.findViewById(R.id.text1);
//				holder.image=(ImageView)vi.findViewById(R.id.image);
                /************  Set holder with LayoutInflater  */
//				vi.setTag( holder );
            }
            return vi
        }

        private inner class ViewHolder
    }

    var mViewClicksListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.email_btn -> (activity as MainDashboardActivity?)!!.sendEmail()
            R.id.phone_btn -> (activity as MainDashboardActivity?)!!.call()
        }
    }

    companion object {
        private const val ARG_POSITION = "position"
        @JvmStatic
		fun newInstance(position: Int): Fragment {
            val f = ContactUsFragment()
            val b = Bundle()
            b.putInt(ARG_POSITION, position)
            f.arguments = b
            return f
        }
    }
}