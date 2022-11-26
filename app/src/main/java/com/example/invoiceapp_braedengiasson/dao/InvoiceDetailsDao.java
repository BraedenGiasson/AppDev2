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
import com.example.invoiceapp_braedengiasson.tables.Invoice;
import com.example.invoiceapp_braedengiasson.tables.InvoiceDetails;

import java.util.List;

@Dao
public abstract class InvoiceDetailsDao extends GenericDao<InvoiceDetails> {

    private static final String TABLE_NAME = TableName.InvoiceDetails;

    public InvoiceDetailsDao() {
        super(TABLE_NAME);
    }

    @Query("SELECT * from invoice_details_table ORDER BY id ASC")
    public abstract LiveData<List<InvoiceDetails>> getAll();

    @Query("SELECT * from invoice_details_table LIMIT 1")
    public abstract InvoiceDetails[] getAny();

    @Query("SELECT * from invoice_details_table WHERE id = :id")
    public abstract InvoiceDetails get(int id);

    @Query("SELECT * from invoice_details_table WHERE invoice_id = :id")
    public abstract LiveData<List<InvoiceDetails>> getInvoicesDetailsForInvoice(int id);

    @Query("SELECT SUM(line_total) from invoice_details_table WHERE invoice_id = :id")
    public abstract double getInvoiceDetailsTotalForInvoice(int id);

    @Query("DELETE FROM invoice_details_table WHERE invoice_id = :id")
    public abstract void deleteAllInvoicesDetailsForInvoice(int id);
}
