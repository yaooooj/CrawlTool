package com.example.coustomtoolbar.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "SignFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Person mPerson;
    private EditText name;
    private EditText mail;
    private EditText password;
    public SignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignFragment newInstance(String param1, String param2) {
        SignFragment fragment = new SignFragment();
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
        View view  =  inflater.inflate(R.layout.fragment_sign, container, false);

        //init();
        Button button = (Button) view.findViewById(R.id.btn_signup);
        TextView textView = (TextView)view.findViewById(R.id.link_login);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setOnClickListener(this);
        button.setOnClickListener(this);
        name = (EditText) view.findViewById(R.id.input_name);
        mail = (EditText) view.findViewById(R.id.input_email);
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
            case R.id.btn_signup:
                if(TextUtils.isEmpty(name.getText())){
                    toast("input name" + name.getText().toString());
                    break;
                }

                if(TextUtils.isEmpty(mail.getText())){
                    toast("input E-mail");
                    break;
                }

                if(TextUtils.isEmpty(password.getText())){
                    toast("input password");
                    break;
                }else {
                    if (password.length() < 6){
                        toast("密码需要大于6位");
                        break;
                    }
                }


                BmobUser bu =new BmobUser();
                bu.setUsername(mail.getText().toString());
                bu.setEmail(mail.getText().toString());
                bu.setPassword(password.getText().toString());
                bu.signUp(new SaveListener<Person>() {
                    @Override
                    public void done(Person person, BmobException e) {
                        if(e==null){
                            toast("注册成功:");

                        }else{
                            switch (e.getErrorCode()){
                                case 202:
                                    toast("用户名已经存在");
                                    break;
                                case 203:
                                    toast("邮箱已经存在");
                                    break;
                                case 204:
                                    toast("必须提供一个邮箱地址");
                                    break;
                                case 301:
                                    toast("邮箱格式不正确");
                                default:
                                    break;
                            }
                            Log.e(TAG, "done: " +e );
                        }
                    }
                });
                break;
            case R.id.link_login:
                mListener.onFragmentInteraction(R.id.login_fragment);

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
        void onFragmentInteraction(int  resId);
    }
}
