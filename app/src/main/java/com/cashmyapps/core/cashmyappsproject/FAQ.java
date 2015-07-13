package com.cashmyapps.core.cashmyappsproject;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class FAQ extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private WebView wb;


    public FAQ() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();



        wb =(WebView)getActivity().findViewById(R.id.webViewFAQ);
        wb.setWebViewClient(new WebViewClient());
        wb.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0");
        wb.getSettings().setLoadWithOverviewMode(false);
        wb.getSettings().setUseWideViewPort(false);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.loadUrl(getActivity().getResources().getString(R.string.html_faq));



    }

    public static FAQ newInstance(int sectionnumber){

        FAQ frag = new FAQ();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionnumber);
        frag.setArguments(args);
        return frag;

    }




}
