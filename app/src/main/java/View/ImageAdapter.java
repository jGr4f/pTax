package View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.auditarrm.taxscan.R;
import java.util.ArrayList;
import Model.Image;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    ArrayList<Image> pdfList;
    OnItemClickListener onItemClickListener;

    public ImageAdapter(Context context, ArrayList<Image> pdfList) {
        this.context = context;
        this.pdfList = pdfList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pdf_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Image pdf = pdfList.get(position);
        holder.title.setText(pdf.getName());

        holder.imageView.setImageResource(R.drawable.baseline_insert_drive_file_24);

        // Abrir pdf cuando se le de click
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("pdfUri", pdf.getUrl()); // Pasar la URI del PDF al Intent
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    //Inicia los elementos para el recyclerview
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.list_item_title);
            imageView = itemView.findViewById(R.id.list_item_image);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Image pdf);
    }

}
