package com.example.invoiceapp_braedengiasson.generic;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

@Dao
public abstract class GenericDao<T> implements IGenericDao<T> {
    private final String TABLE_NAME;

    /**
     * Instantiates a new instance of the {{@link GenericDao<T>}} class.
     * @param TABLE_NAME The name of the table in the database.
     */
    public GenericDao(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    @RawQuery
    public abstract int performDeleteAll(SupportSQLiteQuery query);

    @Override
    public void deleteAll() {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("DELETE FROM " + TABLE_NAME);
        performDeleteAll(query);
    }
}
