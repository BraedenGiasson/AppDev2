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
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;

import java.util.List;

@Dao
public abstract class InvoiceDao extends GenericDao<Invoice> {

    private static final String TABLE_NAME = TableName.Invoice;

    public InvoiceDao() {
        super(TABLE_NAME);
    }

    @Query("SELECT * from invoice_table ORDER BY id ASC")
    public abstract LiveData<List<Invoice>> getAll();

    @Query("SELECT * from invoice_table LIMIT 1")
    public abstract Invoice[] getAny();

    @Query("SELECT * from invoice_table WHERE id = :id")
    public abstract Invoice get(int id);

    @Query("SELECT * from invoice_table WHERE customer_id = :id")
    public abstract LiveData<List<Invoice>> getInvoicesForCustomer(int id);

    @Query("SELECT SUM(line_total) as total from invoice_details_table WHERE invoice_id = :id")
    public abstract double totalOfInvoicesDetailsByInvoiceId(int id);

    @Query("SELECT SUM(total) as total from invoice_table WHERE customer_id = :id")
    public abstract double totalOfInvoicesByCustomerId(int id);

    @Query("SELECT * FROM invoice_table ORDER BY id DESC LIMIT 1")
    public abstract Invoice getNewlyAdded();

    @Query("DELETE FROM invoice_table WHERE customer_id = :id")
    public abstract void deleteAllInvoicesForCustomer(int id);
}
