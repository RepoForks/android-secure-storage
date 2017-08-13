package com.afollestad.androidsecurestorage;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MainTest {

  @Test
  public void useAppContext() throws Exception {
    Context appContext = getTargetContext();
    assertEquals("com.afollestad.androidsecurestorage.test", appContext.getPackageName());
  }

  @Test
  public void canEncryptAndDecryptData() {
    RxSecureStorage storage = RxSecureStorage.create(getTargetContext(), "hello_world");
    byte[] plain = "Hello, world! Testing byte encryption.".getBytes();
    byte[] encrypted = storage.encrypt(plain).blockingGet();
    byte[] decrypted = storage.decrypt(encrypted).blockingGet();
    assertTrue("Decrypted data didn't match the original data.", Arrays.equals(plain, decrypted));
  }

  @Test
  public void canEncryptAndDecryptText() {
    RxSecureStorage storage = RxSecureStorage.create(getTargetContext(), "hello_world");
    String plain = "Hello, world! Testing string encryption.";
    String encrypted = storage.encryptString(plain).blockingGet();
    String decrypted = storage.decryptString(encrypted).blockingGet();
    assertEquals("Decrypted text didn't match the original text.", plain, decrypted);
  }

  @Test
  public void canSaveDataToStorage() {
    RxSecureStorage storage = RxSecureStorage.create(getTargetContext(), "hello_world");
    byte[] data = "Hello, world!".getBytes();
    storage.putBytes("testing", data).blockingGet();
    byte[] retrieved = storage.getBytes("testing").blockingFirst();
    assertTrue(
        "Retrieved preference didn't match the original saved value.",
        Arrays.equals(data, retrieved));
  }

  @Test
  public void canSaveTextToStorage() {
    RxSecureStorage storage = RxSecureStorage.create(getTargetContext(), "hello_world");
    String text = "Hello, world!";
    storage.putString("testing2", text).blockingGet();
    String retrieved = storage.getString("testing2").blockingFirst();
    assertEquals("Retrieved preference didn't match the original saved value.", text, retrieved);
  }
}
