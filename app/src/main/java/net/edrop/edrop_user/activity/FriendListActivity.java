package net.edrop.edrop_user.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.fragment.FriendListFragment;
import net.edrop.edrop_user.utils.SystemTransUtil;

public class FriendListActivity extends AppCompatActivity{
    private FriendListFragment friendListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(FriendListActivity.this);
        super.onCreate(savedInstanceState);
        friendListFragment=new FriendListFragment();
        setContentView(R.layout.activity_friend_list);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,friendListFragment)
                .show(friendListFragment)
                .commit();
    }
}
