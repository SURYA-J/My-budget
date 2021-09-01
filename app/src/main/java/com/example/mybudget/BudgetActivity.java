package com.example.mybudget;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class BudgetActivity extends AppCompatActivity {

    private ProgressDialog loader;
    private TextView totalAmountSpentOn;
    private RecyclerView recyclerView;

    private DatabaseReference expenseRef;
//    private String post_key = "";
////    private String item = "";
////    private int amount = 0;
////    private String note="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        totalAmountSpentOn = findViewById(R.id.totalBudgetAmountTextView);

        FloatingActionButton fab = findViewById(R.id.fab);
        loader = new ProgressDialog(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String onlineUserId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        expenseRef = FirebaseDatabase.getInstance().getReference("Expenses").child(onlineUserId);


        totalAmountSpentOn = findViewById(R.id.totalBudgetAmountTextView);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        expenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalAmount = 0;

                for (DataSnapshot snap: snapshot.getChildren()){
                    Data data = snap.getValue(Data.class);
                    assert data != null;
                    totalAmount += data.getAmount();
                    String sTotal = "Total Budget:₹" + totalAmount;
                    totalAmountSpentOn.setText(sTotal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(v -> additem());


    }
    private void additem(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout,null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final Spinner itemSpinner = myView.findViewById(R.id.itemsspinner);
        final EditText amount = myView.findViewById(R.id.amount);
        final EditText note = myView.findViewById(R.id.note);
        final Button cancel = myView.findViewById(R.id.cancel);
        final Button save = myView.findViewById(R.id.save);
        note.setVisibility(View.VISIBLE);

        save.setOnClickListener(v -> {
            String budgetAmount = amount.getText().toString();
            String budgetItem = itemSpinner.getSelectedItem().toString();
            String notes = note.getText().toString();

            if(TextUtils.isEmpty(budgetAmount) || TextUtils.isEmpty(notes) ){
                amount.setError("Amount is required...");
                note.setError("Note is required...");
                return;
            }
            if(budgetItem.equals("Category is required...")){
                Toast.makeText(BudgetActivity.this, "Select a valid Category", Toast.LENGTH_SHORT).show();
            }
            else{
                loader.setMessage("Adding a Budget Item");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                String id = expenseRef.push().getKey();
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());



                Data data = new Data(budgetItem, date, id, notes, Integer.parseInt(budgetAmount));
                assert id != null;
                expenseRef.child(id).setValue(data).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(BudgetActivity.this, "Budget Item added Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(BudgetActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    }
                    loader.dismiss();
                });
            }
            dialog.dismiss();
        });
        cancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(expenseRef, Data.class)
                .build();

        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Data model) {

                holder.setItemAmount("Allocated amount: ₹"+ model.getAmount());
                holder.setDate("On: "+ model.getDate());
                holder.setNotes("Note: " + model.getNotes());
                holder.setItemName("Category: "+model.getItem());


                holder.notes.setVisibility(View.VISIBLE);

                switch (model.getItem()){
                    case "Food":
                        holder.imageView.setImageResource(R.drawable.food);
                        break;
                    case "Transport":
                        holder.imageView.setImageResource(R.drawable.transport);
                        break;
                    case "House":
                        holder.imageView.setImageResource(R.drawable.house);
                        break;
                    case "Entertainment":
                        holder.imageView.setImageResource(R.drawable.entertainment);
                        break;
                    case "Education":
                        holder.imageView.setImageResource(R.drawable.education);
                        break;
                    case "Health":
                        holder.imageView.setImageResource(R.drawable.health);
                        break;
                    case "Charity":
                        holder.imageView.setImageResource(R.drawable.budget1);
                        break;
                    case "Apparel":
                        holder.imageView.setImageResource(R.drawable.apparel);
                        break;
                    case "Insurance":
                        holder.imageView.setImageResource(R.drawable.insurance);
                        break;
                    case "Recharge":
                        holder.imageView.setImageResource(R.drawable.recharge);
                        break;
                    case "Other":
                        holder.imageView.setImageResource(R.drawable.other);
                        break;
                }

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String post_key = getRef(position).getKey();
                        String item = model.getItem();
                        int amount = model.getAmount();
                        String note = model.getNotes();
                        new AlertDialog.Builder(BudgetActivity.this)
                                .setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",(dialog,id) ->{
                                    expenseRef.child(post_key).removeValue().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            Toast.makeText(BudgetActivity.this, "Deleted Succesfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(BudgetActivity.this, "Deleted failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieve_layout,parent,false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView,delete;
        public TextView item, amount,notes, date;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            delete = itemView.findViewById(R.id.delete);
            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            imageView = itemView.findViewById(R.id.imageView);
            notes = itemView.findViewById(R.id.note);
            date = itemView.findViewById(R.id.date);
        }

        public void setItemName (String itemName){
            TextView item = itemView.findViewById(R.id.item);
            item.setText(itemName);
        }
        public void setNotes (String notes){
            TextView item = itemView.findViewById(R.id.note);
            item.setText(notes);
        }
        public void setItemAmount (String itemAmount){
            TextView item = itemView.findViewById(R.id.amount);
            item.setText(itemAmount);
        }
        public void setDate (String itemDate){
            TextView date = itemView.findViewById(R.id.date);
            date.setText(itemDate);
        }
    }
    //---Logout----//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                startActivity(new Intent(BudgetActivity.this,AccountActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //-----Logout----//

}