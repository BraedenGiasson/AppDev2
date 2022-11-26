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

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.invoiceapp_braedengiasson.repositories.AddressRepository;
import com.example.invoiceapp_braedengiasson.tables.Address;

import java.util.List;

/**
 * The WordViewModel provides the interface between the UI and the data layer of the app,
 * represented by the Repository
 */

public class AddressViewModel extends AndroidViewModel {

    private AddressRepository addressRepository;

    private LiveData<List<Address>> allAddresses;

    public AddressViewModel(Application application) {
        super(application);
        addressRepository = new AddressRepository(application);
        allAddresses = addressRepository.getAllAddresses();
    }

    public LiveData<List<Address>> getAllAddresses() {
        return allAddresses;
    }

    public Address getAddressByCustomerId(int id){
        return addressRepository.getAddressByCustomerId(id);
    }

    public LiveData<List<Address>> getAddressesForCustomer(int id){
        return addressRepository.getAddressesForCustomer(id);
    }

    public Address getCustomerDefaultAddress(int id){
        return addressRepository.getCustomerDefaultAddress(id);
    }

    public LiveData<List<Address>> getAddressesForCustomerNewest(int id){
        return addressRepository.getAddressesForCustomerNewest(id);
    }

    public Address getNewlyAdded(){
        return addressRepository.getNewlyAdded();
    }

    public void insert(Address address) {
        addressRepository.insert(address);
    }

    public void deleteAll() {
        addressRepository.deleteAll();
    }

    public void deleteAddress(Address address) {
        addressRepository.deleteAddress(address);
    }

    public void deleteAllAddressForCustomer(int customerId) {
        addressRepository.deleteAllAddressForCustomer(customerId);
    }

    public void update(Address address) {
        addressRepository.update(address);
    }
}
