package com.simpad.pathaknotebook.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.simpad.pathaknotebook.CheckOutActivity;
import com.simpad.pathaknotebook.R;
import com.simpad.pathaknotebook.databinding.FragmentPersonalDetailsBinding;


public class PersonalDetailsFragment extends Fragment {

   private Button doneContinue;
    FrameLayout personalInfo,summary,payment;
    View personalTosummary,summaryTopayment;
    ImageView tick;
    TextView detailsNumber;
    private FragmentPersonalDetailsBinding binding;
    CheckOutActivity checkOutActivity ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doneContinue = view.findViewById(R.id.submitContinue);
        personalInfo = getActivity().findViewById(R.id.personalInfo);
        summary = getActivity().findViewById(R.id.orderSummary);
        payment = getActivity().findViewById(R.id.payment);
        personalTosummary = getActivity().findViewById(R.id.linePersonal);
        summaryTopayment = getActivity().findViewById(R.id.lineOrder);
        tick = getActivity().findViewById(R.id.detailsStatus);
        detailsNumber = getActivity().findViewById(R.id.detailsNumber);
        tick.setVisibility(View.INVISIBLE);
        detailsNumber.setVisibility(View.VISIBLE);
        personalInfo.setBackground(getActivity().getDrawable(R.drawable.shape_current));
        personalTosummary.setBackgroundColor(Color.parseColor("#84a9ac"));
        summary.setBackground(getActivity().getDrawable(R.drawable.shape_incomplete));
        summaryTopayment.setBackgroundColor(Color.parseColor("#84a9ac"));
        payment.setBackground(getActivity().getDrawable(R.drawable.shape_incomplete));
        checkOutActivity = (CheckOutActivity) getActivity();
        doneContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNotNull(view);
            }
        });

    }

    private void checkNotNull(View view) {
        if (binding.pincode.getText().toString().isEmpty()){
            binding.pincode.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.pincode.getTop());
        }
        if (binding.houseName.getText().toString().isEmpty()){
            binding.houseName.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.houseName.getTop());
        }
        if (binding.area.getText().toString().isEmpty()){
            binding.area.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.area.getTop());
        }
        if (binding.city.getText().toString().isEmpty()){
            binding.city.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.city.getTop());
        }
        if (binding.state.getText().toString().isEmpty()){
            binding.state.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.state.getTop());
        }
        if (binding.name.getText().toString().isEmpty()){
            binding.name.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.name.getTop());
        }
        if (binding.phoneNumber.getText().toString().isEmpty()){
            binding.phoneNumber.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.phoneNumber.getTop());
        }
        if (binding.addressType.getCheckedRadioButtonId() == -1){
            binding.textAddressType.setError("Please Fill this Field");
            binding.scrollView.scrollTo(0,binding.addressType.getTop());
        }
        if (!binding.pincode.getText().toString().isEmpty()&&!binding.houseName.getText().toString().isEmpty()
                &&!binding.area.getText().toString().isEmpty()&&!binding.city.getText().toString().isEmpty()
                &&!binding.state.getText().toString().isEmpty()&&!binding.name.getText().toString().isEmpty()
                &&!binding.phoneNumber.getText().toString().isEmpty()&&!(binding.addressType.getCheckedRadioButtonId() == -1)){

            Bundle bundle = new Bundle();
            bundle.putString("pincode",binding.pincode.getText().toString());
            bundle.putString("houseName",binding.houseName.getText().toString());
            bundle.putString("area",binding.area.getText().toString());
            bundle.putString("city",binding.city.getText().toString());
            bundle.putString("state",binding.state.getText().toString());
            bundle.putString("name",binding.name.getText().toString());
            bundle.putString("phoneNumber",binding.phoneNumber.getText().toString());
            if (!binding.altPhoneNumber.getText().toString().isEmpty())
                bundle.putString("altPhoneNumber",binding.altPhoneNumber.getText().toString());
            if (!binding.landmark.getText().toString().isEmpty())
                bundle.putString("landmark",binding.landmark.getText().toString());

            Navigation.findNavController(view).navigate(R.id.action_personalDetailsFragment2_to_summaryFragment,bundle);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}