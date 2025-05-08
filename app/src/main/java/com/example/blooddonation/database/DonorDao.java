package com.example.blooddonation.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.blooddonation.models.Donor;

import java.util.List;

@Dao
public interface DonorDao {
    @Insert
    void insert(Donor donor);

    @Update
    void update(Donor donor);

    @Delete
    void delete(Donor donor);

    @Query("SELECT * FROM donors ORDER BY name ASC")
    LiveData<List<Donor>> getAllDonors();

    @Query("SELECT * FROM donors WHERE bloodGroup = :bloodGroup AND isAvailable = 1")
    LiveData<List<Donor>> getDonorsByBloodGroup(String bloodGroup);

    @Query("SELECT * FROM donors WHERE id = :id")
    LiveData<Donor> getDonorById(int id);
} 