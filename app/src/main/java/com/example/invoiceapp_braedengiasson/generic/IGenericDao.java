package com.example.invoiceapp_braedengiasson.generic;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

public interface IGenericDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(T object);

    void deleteAll();

    @Delete
    void delete(T object);

    @Update
    void update(T object);
}
