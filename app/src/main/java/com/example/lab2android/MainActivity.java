package com.example.lab2android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final List<ShoppingItem> shoppingList = new ArrayList<>();
    private ShoppingAdapter shoppingAdapter;
    private int selectedItemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingAdapter = new ShoppingAdapter(shoppingList);
        recyclerView.setAdapter(shoppingAdapter);

        // Добавление элемента в список
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showAddItemDialog());

        // Установка слушателя кликов
        shoppingAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Реализуйте обработку клика на элементе списка (например, редактирование)
                selectedItemPosition = position;
                showEditItemDialog();
            }

            @Override
            public void onItemLongClick(int position) {
                // Реализуйте обработку долгого клика на элементе списка (например, удаление)
                removeItem(position);
            }
        });
    }

    // Отображение диалога добавления элемента
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Новый товар");

        // Настройка макета диалога
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        final EditText input = viewInflated.findViewById(R.id.inputItemName);
        builder.setView(viewInflated);

        // Настройка кнопок диалога
        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String itemName = input.getText().toString().trim();
            if (!itemName.isEmpty()) {
                shoppingList.add(new ShoppingItem(itemName));
                shoppingAdapter.notifyItemInserted(shoppingList.size() - 1);
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Название товара не может быть пустым", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        // Установка цвета кнопок
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialogInterface -> {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
        });

        // Установка цвета диалогового окна
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.dark_blue);
        alertDialog.show();
    }

    // Отображение диалога редактирования элемента
    private void showEditItemDialog() {
        if (selectedItemPosition >= 0 && selectedItemPosition < shoppingList.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Редактировать товар");

            // Настройка макета диалога
            View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
            final EditText input = viewInflated.findViewById(R.id.inputItemName);
            input.setText(shoppingList.get(selectedItemPosition).getName());
            builder.setView(viewInflated);

            // Настройка кнопок диалога
            builder.setPositiveButton("Сохранить", (dialog, which) -> {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty()) {
                    ShoppingItem editedItem = shoppingList.get(selectedItemPosition);
                    editedItem.setName(newName);
                    shoppingAdapter.notifyItemChanged(selectedItemPosition);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Название товара не может быть пустым", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

            // Установка цвета кнопок
            AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(dialogInterface -> {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
            });

            // Установка цвета диалогового окна
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(R.color.dark_blue);
            alertDialog.show();
        }
    }

    // Удаление элемента по позиции
    private void removeItem(int position) {
        if (position >= 0 && position < shoppingList.size()) {
            shoppingList.remove(position);
            shoppingAdapter.notifyItemRemoved(position);
            Toast.makeText(this, "Товар удален", Toast.LENGTH_SHORT).show();
        }
    }
}
