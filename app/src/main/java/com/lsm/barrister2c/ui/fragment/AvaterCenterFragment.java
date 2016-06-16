package com.lsm.barrister2c.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lsm.barrister2c.R;
import com.lsm.barrister2c.data.entity.User;
import com.lsm.barrister2c.ui.UIHelper;
import com.lsm.barrister2c.ui.activity.AvatarDetailActivity;
import com.lsm.barrister2c.ui.activity.LoginActivity;
import com.lsm.barrister2c.ui.activity.MsgsActivity;
import com.lsm.barrister2c.ui.activity.MyAccountActivity;

public class AvaterCenterFragment extends Fragment {

    public AvaterCenterFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(User user) {
        AvaterCenterFragment fragment = new AvaterCenterFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }


    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user");
        }
    }

    AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avater_center, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        aq = new AQuery(view);

        aq.id(R.id.btn_user_layout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    UIHelper.goLoginActivity(getActivity());
                } else {
                    Intent intent = new Intent(getActivity(), AvatarDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        SimpleDraweeView usrIconView = (SimpleDraweeView) view.findViewById(R.id.image_avatar);

        if (user != null) {

            if (!TextUtils.isEmpty(user.getUserIcon()))
                usrIconView.setImageURI(Uri.parse(user.getUserIcon()));

            aq.id(R.id.tv_nickname).text(user.getName());

        } else {

            usrIconView.setImageURI(Uri.parse("res://com.lsm.barrister2c/"+R.drawable.umeng_socialize_default_avatar));

            aq.id(R.id.tv_nickname).text(getString(R.string.not_login));

        }

        aq.id(R.id.btn_user_layout).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AvatarDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

        aq.id(R.id.btn_my_favorite).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    UIHelper.goLoginActivity(getActivity());
                } else {
                    UIHelper.goMyFavorite(getActivity());
                }
            }
        });

        aq.id(R.id.btn_my_account).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                    startActivity(intent);
                }
            }
        });

        aq.id(R.id.btn_my_orders).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    UIHelper.goLoginActivity(getActivity());
                } else {
                    UIHelper.goMyOrdersActivity(getActivity());
                }
            }
        });

        aq.id(R.id.btn_my_msg).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MsgsActivity.class);
                startActivity(intent);
            }
        });

        aq.id(R.id.btn_contact_us).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4009889595"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        aq.id(R.id.btn_settings).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UIHelper.goSettingsActivity(getActivity());
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


}
