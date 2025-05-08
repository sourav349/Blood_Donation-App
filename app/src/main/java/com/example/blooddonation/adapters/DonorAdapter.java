package com.example.blooddonation.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.blooddonation.R;
import com.example.blooddonation.models.User;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {
    private Context context;
    private List<User> donors;
    private OnDonorClickListener listener;

    public interface OnDonorClickListener {
        void onDonorClick(User donor);
    }

    public DonorAdapter(Context context, List<User> donors, OnDonorClickListener listener) {
        this.context = context;
        this.donors = donors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donor, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonorViewHolder holder, int position) {
        User donor = donors.get(position);
        holder.bind(donor);
    }

    @Override
    public int getItemCount() {
        return donors.size();
    }

    public void updateDonors(List<User> newDonors) {
        this.donors = newDonors;
        notifyDataSetChanged();
    }

    class DonorViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView bloodGroupTextView;
        private final TextView locationTextView;
        private final TextView ageTextView;
        private final TextView genderTextView;
        private final TextView phoneTextView;
        private final CircleImageView profileImageView;
        private final ImageView callButton;
        private final ImageView messageButton;

        DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            bloodGroupTextView = itemView.findViewById(R.id.bloodGroupTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            callButton = itemView.findViewById(R.id.callButton);
            messageButton = itemView.findViewById(R.id.messageButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDonorClick(donors.get(position));
                }
            });

            // Set click listeners for call and message buttons
            callButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + donors.get(position).getPhoneNumber()));
                    context.startActivity(intent);
                }
            });

            messageButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("smsto:" + donors.get(position).getPhoneNumber()));
                    context.startActivity(intent);
                }
            });
        }

        void bind(User donor) {
            nameTextView.setText(donor.getName());
            bloodGroupTextView.setText(donor.getBloodGroup());
            locationTextView.setText(donor.getLocation());
            ageTextView.setText("Age: " + donor.getAge());
            genderTextView.setText("Gender: " + donor.getGender());
            phoneTextView.setText(donor.getPhoneNumber());

            // Load profile image using Glide
            if (donor.getProfileImagePath() != null && !donor.getProfileImagePath().isEmpty()) {
                File imageFile = new File(donor.getProfileImagePath());
                if (imageFile.exists()) {
                    Glide.with(context)
                            .load(imageFile)
                            .placeholder(R.drawable.default_profile)
                            .error(R.drawable.default_profile)
                            .into(profileImageView);
                } else {
                    profileImageView.setImageResource(R.drawable.default_profile);
                }
            } else {
                profileImageView.setImageResource(R.drawable.default_profile);
            }
        }
    }
} 