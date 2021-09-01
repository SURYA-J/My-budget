//package com.example.mybudget;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.TextView;
//import android.widget.Toolbar;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Calendar;
//
//public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
//
//    private RecyclerView recyclerView;
//    private FirebaseAuth mAuth;
//    private String onlineUserId="";
//    private DatabaseReference expenseRef;
//
//    private Toolbar toolbar2;
//    private Button Search;
//    private TextView HistoryTotalAmount;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
//
//        Search = findViewById(R.id.search);
//        HistoryTotalAmount = findViewById(R.id.historTotalBudget);
//        recyclerView = findViewById(R.id.recyclerView_history);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        linearLayoutManager.setReverseLayout(true);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        expenseRef = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
//
//        Search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePickerDialog();
//            }
//        });
//    }
//
//    private void showDatePickerDialog() {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//          this,
//                 this,
//            Calendar.getInstance().get(Calendar.YEAR),
//            Calendar.getInstance().get(Calendar.MONTH),
//            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
//    );
//        datePickerDialog.show();
//    }
//
//    @Override
//    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
//
//        int months = month + 1;
//        String date = dayOfMonth + "-" + "0" + months + "-" + year;
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);
//        Query query = reference.orderByChild("date").equalTo(date);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int totalAmount = 0;
//
//                for (DataSnapshot snap: snapshot.getChildren()){
//                    Data data = snap.getValue(Data.class);
//                    assert data != null;
//                    totalAmount += data.getAmount();
//                    String sTotal = "Total budget:₹" + totalAmount;
//                    if(totalAmount > 0){
//                        HistoryTotalAmount.setVisibility(View.VISIBLE);
//                        HistoryTotalAmount.setText("This day you Spent on " + sTotal);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//}