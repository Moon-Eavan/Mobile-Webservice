package com.example.photoviewer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.photoviewer.model.PostItem;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<PostItem> postItemList;

    public ImageAdapter(List<PostItem> postItemList) { // 생성자에서 게시물 목록 입력
        this.postItemList = postItemList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 이미지 항목을 나타낼 뷰 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // 해당 위치 게시물의 이미지를 뷰에 설정 (이번 Phase에서는 이미지만 표시)
        PostItem postItem = postItemList.get(position);
        holder.imageView.setImageBitmap(postItem.getBitmap());
    }

    @Override
    public int getItemCount() {
        return postItemList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewItem); // item_image.xml에 있는 ImageView
        }
    }
}
