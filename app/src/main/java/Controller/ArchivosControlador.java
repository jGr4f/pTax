package Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import Model.Image;
import View.ImageAdapter;

public class ArchivosControlador{
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public ArchivosControlador() {
        this.mAuth = FirebaseAuth.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference();
        this.currentUser = mAuth.getCurrentUser();
    }

    public void cargarFacturas(RecyclerView recyclerView, Context context) {
        FirebaseStorage.getInstance().getReference().child("facturas/" + currentUser.getUid()).listAll().addOnSuccessListener(listResult -> {
            ArrayList<Image> arrayList = new ArrayList<>();
            ImageAdapter adapter = new ImageAdapter(context, arrayList);
            adapter.setOnItemClickListener(image -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(image.getUrl()));
                intent.setDataAndType(Uri.parse(image.getUrl()), "image/*");
                context.startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
            listResult.getItems().forEach(storageReference -> {
                Image image = new Image();
                image.setName(storageReference.getName());
                storageReference.getDownloadUrl().addOnCompleteListener(task -> {
                    String url = "https://" + task.getResult().getEncodedAuthority() + task.getResult().getEncodedPath() + "?alt=media&token=" + task.getResult().getQueryParameters("token").get(0);
                    image.setUrl(url);
                    arrayList.add(image);
                    adapter.notifyDataSetChanged();
                });
            });
        }).addOnFailureListener(e -> Toast.makeText(context, "Failed to retrieve images", Toast.LENGTH_SHORT).show());
    }

    public void subirFactura(Uri image_url) {
        Uri file = image_url;
        StorageReference riversRef = storageRef.child("facturas/" + currentUser.getUid() + "/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask.addOnFailureListener(exception -> {
            // Manejar carga fallida
        }).addOnSuccessListener(taskSnapshot -> {
            // Manejar carga exitosa
        });
    }
}
