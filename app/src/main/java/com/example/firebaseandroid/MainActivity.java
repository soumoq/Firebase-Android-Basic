package com.example.firebaseandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        TextView textView = findViewById(R.id.text_view);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", "Mistipuchu");
        userMap.put("Roll", "1");


        firebaseFirestore.collection("users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                try {
                    List<Stu> types = queryDocumentSnapshots.toObjects(Stu.class);
                    for (Stu i : types) {
                        textView.append(i.getName() + " " + i.getRoll() + "\n\n\n");
                    }

                    DocumentSnapshot documentReference = queryDocumentSnapshots.getDocuments().get(0);
                    collectionReference.document(documentReference.getId()).collection("play").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<Play> plays = queryDocumentSnapshots.toObjects(Play.class);
                            Toast.makeText(MainActivity.this, plays.get(0).getName(), Toast.LENGTH_LONG).show();
                        }
                    });

                    Toast.makeText(MainActivity.this, documentReference.getId() + "", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

class Stu {
    private String name;
    private String Roll;

    public String getName() {
        return name;
    }

    public String getRoll() {
        return Roll;
    }
}

class Play {
    private String name;

    public String getName() {
        return name;
    }
}