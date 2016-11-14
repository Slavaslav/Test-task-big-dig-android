package com.myapplication.testtaskbigdig1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TestFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text_image_uri);
        Button buttonOk = (Button) view.findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTaskBigDig2(editText.getText().toString());
            }
        });
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

    private void startTaskBigDig2(String editText) {
        Context ctx = getActivity();
        try {
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(getString(R.string.name_package_application_b));
            if (i == null)
                throw new PackageManager.NameNotFoundException();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_TEXT, editText);
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast toast = Toast.makeText(ctx, R.string.application_b_not_found, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
