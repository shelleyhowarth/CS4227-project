package com.example.cs4227_project.misc;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentController {
    private static FragmentController instance;
    private FragmentManager currentFragmentManager;

    public static FragmentController getInstance() {
        if(instance == null) {
            instance = new FragmentController();
        }
        return instance;
    }

    public void startFragment(Fragment fragment, int replacedContentID) {
        FragmentTransaction transaction = currentFragmentManager.beginTransaction();
        transaction.replace(replacedContentID, fragment);
        transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public void startFragment(Fragment fragment, int replacedContentID,  String fragmentName) {
        FragmentTransaction transaction = currentFragmentManager.beginTransaction();
        transaction.replace(replacedContentID, fragment);
        transaction.addToBackStack(fragmentName);
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }

    public FragmentManager getCurrentFragmentManager() {
        return currentFragmentManager;
    }

    public void setCurrentFragmentManager(FragmentManager currentFragmentManager) {
        this.currentFragmentManager = currentFragmentManager;
    }
}
