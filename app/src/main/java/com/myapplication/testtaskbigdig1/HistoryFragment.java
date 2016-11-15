package com.myapplication.testtaskbigdig1;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks {
    private HistoryAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new HistoryAdapter(getActivity(), null, false);
        listView.setAdapter(adapter);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_history, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sort_by_date:
                break;
            case R.id.sort_by_status:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BigDigProvider.IMAGE_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        adapter.swapCursor((Cursor) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    public interface OnFragmentInteractionListener {
    }

    private class HistoryAdapter extends CursorAdapter {

        public HistoryAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.textUri = (TextView) root.findViewById(R.id.uri_text);
            holder.uriContainer = (FrameLayout) root.findViewById(R.id.uri_image_container);
            root.setTag(holder);
            return root;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder != null) {
                String sUri = cursor.getString(cursor.getColumnIndex(BigDigProvider.IMAGE_COLUMN_URI));
                holder.textUri.setText(sUri);
                int status = cursor.getInt(cursor.getColumnIndex(BigDigProvider.IMAGE_COLUMN_STATUS));
                int color = 0;
                switch (status) {
                    case 1:
                        color = ContextCompat.getColor(getActivity(), R.color.colorGreen);
                        break;
                    case 2:
                        color = ContextCompat.getColor(getActivity(), R.color.colorRed);
                        break;
                    case 3:
                        color = ContextCompat.getColor(getActivity(), R.color.colorGrey);
                        break;
                }
                holder.uriContainer.setBackgroundColor(color);
            }
        }

        class ViewHolder {
            TextView textUri;
            FrameLayout uriContainer;
        }
    }
}
