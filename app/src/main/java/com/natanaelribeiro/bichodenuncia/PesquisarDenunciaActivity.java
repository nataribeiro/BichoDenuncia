package com.natanaelribeiro.bichodenuncia;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PesquisarDenunciaActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.date_from) public EditText date_from;
    @BindView(R.id.date_to) public EditText date_to;
    @BindView(R.id.edit_search_hashtags) public EditText edit_search_hashtags;
    @BindView(R.id.btn_pesquisar_denuncias) public ImageButton btn_pesquisar_denuncias;

    final Locale brasilLocale = new Locale("pt", "BR");

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_denuncia);

        setFullscreenActivity();
        setupToolbar(true);

        ButterKnife.bind(this);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", brasilLocale);

        setarCamposData();
    }

    private void setarCamposData() {
        date_from.setOnClickListener(this);
        date_to.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_from.setText(dateFormatter.format(newDate.getTime()));
                hideSoftKeyboard();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date_to.setText(dateFormatter.format(newDate.getTime()));
                hideSoftKeyboard();
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == date_from) {
            fromDatePickerDialog.show();
        } else if(view == date_to) {
            toDatePickerDialog.show();
        }
    }

    @OnClick(R.id.btn_pesquisar_denuncias)
    public void onClickPesquisar() {
        Intent intent = new Intent(this, ListaDenunciasActivity.class);
        if(!date_from.equals("") && !date_to.equals("")) {
            intent.putExtra("tipo_busca", "data");
            intent.putExtra("data_inicio", date_from.getText().toString());
            intent.putExtra("data_fim", date_to.getText().toString());
        } else if(!edit_search_hashtags.equals((""))){
            intent.putExtra("tipo_busca", "hashtag");
            intent.putExtra("filtro", edit_search_hashtags.getText().toString());
        } else {
            Toast.makeText(this, "Insira um filtro para pesquisa", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
