package com.example.coustomtoolbar.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Bean.Person;
import com.example.coustomtoolbar.R;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "LoginFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText username;
    private EditText password;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView textView = (TextView) view.findViewById(R.id.link_signup);
        textView.setClickable(true);
        textView.setFocusable(true);
        Button button = (Button) view.findViewById(R.id.btn_login);
        button.setOnClickListener(this);
        textView.setOnClickListener(this);

        username = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);


        return view;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int resId) {
        if (mListener != null) {
            mListener.onFragmentInteraction(resId);
        }
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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.link_signup:
                Log.e(TAG, "onClick: " + "to sign" );
                mListener.onFragmentInteraction(R.id.sign_fragment);
                break;

            case  R.id.btn_login:

                if (TextUtils.isEmpty(username.getText())){
                    toast("请输入账号");
                    break;
                }

                if (TextUtils.isEmpty(password.getText())){
                    toast("请输入密码");
                    break;
                }

                BmobQuery<BmobUser> query = new BmobQuery<>();
                query.getObject("36cff05721", new QueryListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){

                            if (!bmobUser.getEmailVerified()){

                                toast("账号未验证,请前往邮箱验证");
                            }
                            else {
                                BmobUser user = new BmobUser();
                                user.setUsername(username.getText().toString());
                                user.setPassword(password.getText().toString());
                                user.login(new SaveListener<BmobUser>() {
                                    @Override
                                    public void done(BmobUser bmobUser, BmobException e) {
                                        if (e == null){
                                            toast("登录成功:");
                                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                                        }else {
                                            if (e.getErrorCode() == 205){
                                                toast("没有找到此邮件的用户");
                                            }
                                            if (e.getErrorCode() == 101){
                                                toast("登录的用户名或密码不正确");
                                            }
                                            Log.e(TAG, "done: "+e );
                                        }
                                    }
                                });
                            }

                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

                break;
            default:
                break;
        }
    }


    public void toast(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int resId);
    }
}
