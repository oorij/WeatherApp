package bsu.bsit3d.weathermap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class VPAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentarrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitle = new ArrayList<>();
    public VPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentarrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentarrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentarrayList.add(fragment);
        fragmentTitle.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return fragmentTitle.get(position);
    }
}
