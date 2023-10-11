package com.application.bit_time;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;




public class WrapperFragment extends Fragment {

    int FRAGMAX = 3;
    FragmentContainerView[] fragmentsViews;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentsViews= new FragmentContainerView[FRAGMAX];

        for(int i = 0;i< FRAGMAX; i++) {
            fragmentsViews[i]= new FragmentContainerView(this.getContext());
            fragmentsViews[i].setId(i+1);
            Log.i("FragmentsViews id",Integer.toString(fragmentsViews[i].getId()));
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(fragmentsViews[0],MATCH_PARENT,WRAP_CONTENT);
        linearLayout.addView(fragmentsViews[1],MATCH_PARENT,WRAP_CONTENT);
        linearLayout.addView(fragmentsViews[2],MATCH_PARENT,WRAP_CONTENT);

        //View view = inflater.inflate(R.layout.activity_main_wrapper_layout,container,false);





        return linearLayout;
    }


    @Override
    public void onStart() {
        super.onStart();


        Fragment firstChild = new HomeFragment();
        Fragment secondChild = new HomeBottomFragment();
        Fragment thirdChild = new HomeBottomSecondaryFragment();

        FragmentManager childrenFragmentManager = this.getChildFragmentManager();

        childrenFragmentManager
                .beginTransaction()
                .add(fragmentsViews[0].getId(),firstChild)
                .add(fragmentsViews[1].getId(),secondChild)
                .add(fragmentsViews[2].getId(),thirdChild)
                .commit();
    }
}
