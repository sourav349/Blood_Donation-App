package com.example.blooddonation.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.blooddonation.models.Donor;

@Database(entities = {Donor.class}, version = 1)
public abstract class DonorDatabase extends RoomDatabase {
    private static DonorDatabase instance;
    public abstract DonorDao donorDao();

    public static synchronized DonorDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    DonorDatabase.class,
                    "donor_database"
            ).build();
        }
        return instance;
    }
} 