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

package com.example.invoiceapp_braedengiasson.viewModel;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_braedengiasson.repositories.CustomerRepository;
import com.example.invoiceapp_braedengiasson.tables.Address;
import com.example.invoiceapp_braedengiasson.tables.Customer;

import java.util.List;

/**
 * The WordViewModel provides the interface between the UI and the data layer of the app,
 * represented by the Repository
 */

public class CustomerViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;

    private LiveData<List<Customer>> allCustomers;

    public CustomerViewModel(Application application) {
        super(application);
        customerRepository = new CustomerRepository(application);
        allCustomers = customerRepository.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public Customer getCustomerById(int id){
        return customerRepository.getCustomerById(id);
    }

    public Customer getNewlyAdded(){
        return customerRepository.getNewlyAdded();
    }

    public int getDefaultAddress(int customerId){
        return customerRepository.getDefaultAddress(customerId);
    }

    public void setDefaultAddress(int customerId, @Nullable int addressId){
        customerRepository.setDefaultAddress(customerId, addressId);
    }

    public void insert(Customer customer) {
        customerRepository.insert(customer);
    }

    public void deleteAll() {
        customerRepository.deleteAll();
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.deleteWord(customer);
    }

    public void update(Customer customer) {
        customerRepository.update(customer);
    }
}
