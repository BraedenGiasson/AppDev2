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
import com.example.invoiceapp_braedengiasson.tables.Invoice;

import java.util.List;

@Dao
public abstract class AddressDao extends GenericDao<Address> {

    private static final String TABLE_NAME = TableName.Address;

    public AddressDao() {
        super(TABLE_NAME);
    }

    @Query("SELECT * from address_table ORDER BY id ASC")
    public abstract LiveData<List<Address>> getAll();

    @Query("SELECT * from address_table LIMIT 1")
    public abstract Address[] getAny();

    @Query("SELECT * from address_table WHERE id = :id")
    public abstract Address get(int id);

    @Query("SELECT * from address_table WHERE customer_id = :id")
    public abstract Address getFromCustomerId(int id);

    @Query("SELECT * from address_table WHERE customer_id = :id")
    public abstract LiveData<List<Address>> getAddressesForCustomer(int id);

    @Query("SELECT * from address_table WHERE customer_id = :id ORDER BY id DESC")
    public abstract LiveData<List<Address>> getAddressesForCustomerNewest(int id);

    @Query("SELECT * from address_table WHERE id = :id")
    public abstract Address getCustomerDefaultAddress(int id);

    @Query("SELECT * FROM address_table ORDER BY id DESC LIMIT 1")
    public abstract Address getNewlyAdded();

    @Query("DELETE FROM address_table WHERE customer_id = :id")
    public abstract void deleteAllAddressForCustomer(int id);
}
