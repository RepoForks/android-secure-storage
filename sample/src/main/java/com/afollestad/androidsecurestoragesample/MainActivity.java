package com.afollestad.androidsecurestoragesample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.afollestad.androidsecurestorage.RxSecureStorage;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.alias_name)
  EditText inputAliasName;

  @BindView(R.id.initialize_button)
  Button initializeButton;

  @BindView(R.id.input_encrypt)
  EditText inputEncrypt;

  @BindView(R.id.button_encrypt)
  Button buttonEncrypt;

  @BindView(R.id.input_decrypt)
  EditText inputDecrypt;

  @BindView(R.id.button_decrypt)
  Button buttonDecrypt;

  private Unbinder unbinder;
  private RxSecureStorage secureStorage;

  private static class EnabledTextWatcher implements TextWatcher {

    private final View view;

    EnabledTextWatcher(View view) {
      this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
      view.setEnabled(!charSequence.toString().trim().isEmpty());
    }

    @Override
    public void afterTextChanged(Editable editable) {}
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    unbinder = ButterKnife.bind(this);

    inputAliasName.addTextChangedListener(new EnabledTextWatcher(initializeButton));
    inputEncrypt.addTextChangedListener(new EnabledTextWatcher(buttonEncrypt));
    inputDecrypt.addTextChangedListener(new EnabledTextWatcher(buttonDecrypt));
  }

  @Override
  protected void onDestroy() {
    unbinder.unbind();
    super.onDestroy();
  }

  @OnClick(R.id.initialize_button)
  void onClickInitialize() {
    secureStorage = RxSecureStorage.create(this, inputAliasName.getText().toString().trim());
    buttonEncrypt.setEnabled(true);
    buttonDecrypt.setEnabled(true);
    inputEncrypt.setEnabled(true);
    inputDecrypt.setEnabled(true);
  }

  @OnClick(R.id.button_encrypt)
  void onClickEncrypt() {
    secureStorage
        .encryptString(inputEncrypt.getText().toString())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new Consumer<String>() {
              @Override
              public void accept(String encrypted) throws Exception {
                inputEncrypt.setText("");
                inputDecrypt.setText(encrypted);
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                showError(throwable);
              }
            });
  }

  @OnClick(R.id.button_decrypt)
  void onClickDecrypt() {
    secureStorage
        .decryptString(inputDecrypt.getText().toString())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            new Consumer<String>() {
              @Override
              public void accept(String decrypted) throws Exception {
                inputDecrypt.setText("");
                inputEncrypt.setText(decrypted);
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                showError(throwable);
              }
            });
  }

  private void showError(Throwable throwable) {
    throwable.printStackTrace();
    new AlertDialog.Builder(MainActivity.this)
        .setMessage(throwable.getMessage())
        .setPositiveButton(
            android.R.string.ok,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              }
            })
        .show();
  }
}
