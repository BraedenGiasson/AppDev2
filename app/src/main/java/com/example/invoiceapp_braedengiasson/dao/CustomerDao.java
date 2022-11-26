/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.invoiceapp_braedengiasson.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.invoiceapp_braedengiasson.generic.GenericDao;
import com.example.invoiceapp_braedengiasson.helpers.TableName;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;

import java.util.List;

/**
 * Data Access Object (DAO) for a word.
 * Each method performs a database operation, such as inserting or deleting a word,
 * running a DB query, or deleting all words.
 */

@Dao
public abstract class CustomerDao extends GenericDao<Customer> {

    private static final String TABLE_NAME = TableName.Customer;

    public CustomerDao() {
        super(TABLE_NAME);
    }

    @Query("SELECT * from customer_table ORDER BY name ASC")
    public abstract LiveData<List<Customer>> getAll();

    @Query("SELECT * from customer_table LIMIT 3")
    public abstract Customer[] getAny();

    @Query("SELECT * from customer_table WHERE id = :id")
    public abstract Customer get(int id);

    @Query("SELECT * FROM customer_table ORDER BY id DESC LIMIT 1")
    public abstract Customer getNewlyAdded();
}
