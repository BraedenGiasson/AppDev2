package com.example.invoiceapp_braedengiasson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.invoiceapp_braedengiasson.dao.CustomerDao;
import com.example.invoiceapp_braedengiasson.repositories.CustomerRepository;
import com.example.invoiceapp_braedengiasson.roomDatabase.InvoiceAppRoomDatabase;
import com.example.invoiceapp_braedengiasson.tables.Customer;
import com.example.invoiceapp_braedengiasson.viewModel.CustomerViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleUnitTest {
    private CustomerRepository customerRepository;
    private CustomerViewModel customerViewModel;
    private CustomerDao customerDao;
    private InvoiceAppRoomDatabase db;

    @Before
    public void doBefore(){
//        Context context = ApplicationProvider.getApplicationContext();
//        db = Room.inMemoryDatabaseBuilder(context, InvoiceAppRoomDatabase.class).build();
//        customerDao = db.customerDao();
    }

    @After
    public void closeDb() throws IOException {
//        db.close();
    }

//    @Test
//    public void testCustomerInsertionWorks() {
//        int customerId = (int) customerDao.insert(new Customer("new name"));
//
//        assertEquals(1, customerId);
//    }

//    public static <T> int getOrAwaitValue(final LiveData<List<Customer>> liveData) throws InterruptedException {
//        final Object[] data = new Object[1];
////        final List<Customer> data = new ArrayList<>();
//        final CountDownLatch latch = new CountDownLatch(1);
//        Observer<List<Customer>> observer = new Observer<List<Customer>>() {
//            @Override
//            public void onChanged(List<Customer> customers) {
//                data[0] = customers.size();
//                latch.countDown();
//                liveData.removeObserver(this);
//            }
//        };
//        liveData.observeForever(observer);
//        latch.await(2, TimeUnit.SECONDS);
//        //noinspection unchecked
//        return (int) data[0];
//    }
}