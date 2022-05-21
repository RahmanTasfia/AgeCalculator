package com.example.agecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button fromDateBTN, toDateBTN, calculateBTN, insertBTN, viewBTN;
    private TextView resultTV;
    BottomNavigationView bottomNavigationView;
    DBHelper DB;


    DatePickerDialog.OnDateSetListener dateSetListenerFromDate, dateSetListenerToDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.faq:
                        startActivity(new Intent(getApplicationContext(), faq.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), about.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        fromDateBTN = findViewById(R.id.fromAgeBTN);
        toDateBTN= findViewById(R.id.toAgeButton);
        calculateBTN= findViewById(R.id.CalculateAgeBTN);
        insertBTN= findViewById(R.id.insertBTN);
        resultTV= findViewById(R.id.resultTV);
        viewBTN= findViewById(R.id.viewBTN);
        DB = new DBHelper(this);

        Calendar calendar = Calendar.getInstance();

        // for year
        int year = calendar.get(Calendar.YEAR);

        // for month
        int month = calendar.get(Calendar.MONTH);

        // for date
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        toDateBTN.setText(todayDate);

        fromDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerFromDate, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListenerFromDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String birthDate = dayOfMonth+ "/" + month + "/" + year;
                fromDateBTN.setText(birthDate);
            }
        };

        toDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerToDate, year, month, day);

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListenerToDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String todayDate = dayOfMonth+ "/" + month + "/" + year;
                toDateBTN.setText(todayDate);
            }
        };

        calculateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthDate = fromDateBTN.getText().toString();
                String todayDate = toDateBTN.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1 = simpleDateFormat.parse(birthDate);
                    Date date2 = simpleDateFormat.parse(todayDate);

                    long startdate = date1.getTime();
                    long endDate = date2.getTime();

                    if (startdate <= endDate) {
                        org.joda.time.Period period = new Period(startdate, endDate, PeriodType.yearMonthDay());
                        int years = period.getYears();
                        int months = period.getMonths();
                        int days = period.getDays();


                        // show the final output
                        resultTV.setText(years + " Years |" + months + "Months |" + days + "Days");

                    }

                    else {
                        // show message
                        Toast.makeText(MainActivity.this, "BirthDate should not be larger then today's date!", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

       insertBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String birthDate = fromDateBTN.getText().toString();
               String todayDate = toDateBTN.getText().toString();

               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
               try {
                   Date date1 = simpleDateFormat.parse(birthDate);
                   Date date2 = simpleDateFormat.parse(todayDate);

                   long startdate = date1.getTime();
                   long endDate = date2.getTime();

                   if (startdate <= endDate) {
                       org.joda.time.Period period = new Period(startdate, endDate, PeriodType.yearMonthDay());
                       int years = period.getYears();
                       int months = period.getMonths();
                       int days = period.getDays();


                       // show the final output
                       resultTV.setText(years + " Years |" + months + "Months |" + days + "Days");

                   }

                   else {
                       // show message
                       Toast.makeText(MainActivity.this, "BirthDate should not be larger then today's date!", Toast.LENGTH_SHORT).show();
                   }

               } catch (ParseException e) {
                   e.printStackTrace();
               }

               String resultTXT =   resultTV.getText().toString();
               boolean checkinsertdata= DB.insertuserdata(resultTXT);
               if (checkinsertdata)
                   Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
               else
                   Toast.makeText(MainActivity.this, "Entry Not Inserted", Toast.LENGTH_SHORT).show();

           }
       });

       viewBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Cursor res= DB.getdata();
               if(res.getCount()==0)
               {
                   Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                   return;
               }

               StringBuffer buffer = new StringBuffer();
               while (res.moveToNext())
               {
                   buffer.append("Name: "+res.getString(0)+"\n");
               }

               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setCancelable(true);
               builder.setTitle("User Entries");
               builder.setMessage(buffer.toString());
               builder.show();
           }
       });


    }
}