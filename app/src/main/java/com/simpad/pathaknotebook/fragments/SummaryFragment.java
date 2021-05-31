package com.simpad.pathaknotebook.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.simpad.pathaknotebook.R;
import com.simpad.pathaknotebook.adapters.SummaryAdapter;
import com.simpad.pathaknotebook.databinding.FragmentSummaryBinding;
import com.simpad.pathaknotebook.models.NotebookData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class SummaryFragment extends Fragment implements PaymentResultWithDataListener {

    FrameLayout personalInfo,summary,payment;
    View personalTosummary,summaryTopayment;
    ImageView tick;
    TextView detailsNumber;
    Button continueToPayment;
    List<NotebookData> cartProduct ;
    private FragmentSummaryBinding binding;
    private DatabaseReference mRef;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private Float sum;
    private RecyclerView summaryRecyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cartProduct = new ArrayList<>();
        personalInfo = getActivity().findViewById(R.id.personalInfo);
        summary = getActivity().findViewById(R.id.orderSummary);
        payment = getActivity().findViewById(R.id.payment);
        personalTosummary = getActivity().findViewById(R.id.linePersonal);
        summaryTopayment = getActivity().findViewById(R.id.lineOrder);
        continueToPayment = getActivity().findViewById(R.id.continueToPayment);
        tick = getActivity().findViewById(R.id.detailsStatus);
        detailsNumber = getActivity().findViewById(R.id.detailsNumber);
        tick.setVisibility(View.VISIBLE);
        detailsNumber.setVisibility(View.INVISIBLE);
        personalInfo.setBackground(getActivity().getDrawable(R.drawable.shape_completed));
        personalTosummary.setBackgroundColor(Color.parseColor("#11FF0A"));
        summary.setBackground(getActivity().getDrawable(R.drawable.shape_current));
        summaryTopayment.setBackgroundColor(Color.parseColor("#84a9ac"));
        payment.setBackground(getActivity().getDrawable(R.drawable.shape_incomplete));
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        continueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
        binding.showAddress.name.setText(getArguments().getString("name"));
        String address = getArguments().getString("houseName")+"\n"+getArguments().getString("area");
        binding.showAddress.address.setText(address);
        binding.showAddress.city.setText(getArguments().getString("city"));
        binding.showAddress.state.setText(getArguments().getString("state"));
        binding.showAddress.pincode.setText(getArguments().getString("pincode"));
        binding.showAddress.phoneNumber.setText(getArguments().getString("phoneNumber"));
        binding.showAddress.changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_summaryFragment_to_personalDetailsFragment2);

            }
        });

        mRef.child("user").child(user.getUid()).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartProduct.clear();
                sum =0.0f;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    NotebookData notebookData = (NotebookData) dataSnapshot.getValue(NotebookData.class);
                    Log.d(TAG, "onDataChange: "+ notebookData.getSerialNumber());
                    cartProduct.add(notebookData);
                    try {
                        sum = sum + (Float.parseFloat(notebookData.getQuantity())*Float.parseFloat(notebookData.getSalePerPcs()));
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                }
                String price = "Price ("+snapshot.getChildrenCount()+" items)";
                binding.priceDetails.priceText.setText(price);
                binding.priceDetails.price.setText("\u20B9"+sum);
                binding.priceDetails.finalPayment.setText("\u20B9"+String.valueOf(sum+50.0));
                binding.finalPaymentHead.setText("\u20B9"+String.valueOf(sum+50.0));
                SummaryAdapter summaryAdapter = new SummaryAdapter(cartProduct,getContext(),user);
                binding.summaryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.summaryRecyclerView.setAdapter(summaryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSummaryBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }



    public void startPayment() {

        final SummaryFragment activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Pathak Print");
            options.put("description", "NoteBook Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://firebasestorage.googleapis.com/v0/b/ecommerce-d6fcb.appspot.com/o/logo.jpg?alt=media&token=d9b208cf-2a54-4206-b7ae-9caf2328afc5");
            options.put("currency", "INR");
            String payment = String.valueOf(sum+50.0);
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", getArguments().getString("phoneNumber"));

            options.put("prefill", preFill);

            co.open(requireActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_summaryFragment_to_paymentFragment2);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}