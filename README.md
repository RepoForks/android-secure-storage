# Android Secure Storage

[ ![Library](https://api.bintray.com/packages/drummer-aidan/maven/android-secure-storage/images/download.svg) ](https://bintray.com/drummer-aidan/maven/android-secure-storage/_latestVersion)
[![Build Status](https://travis-ci.org/afollestad/android-secure-storage.svg?branch=master)](https://travis-ci.org/afollestad/android-secure-storage)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ace68cb79e6243a79d00da6be87bdb21)](https://www.codacy.com/app/drummeraidan_50/android-secure-storage?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=afollestad/android-secure-storage&amp;utm_campaign=Badge_Grade)

This is a simple library that lets you encrypt and decrypt data/text. Android's
[keystore system](https://developer.android.com/training/articles/keystore.html) is used to store
cryptographic keys in a container to make it more difficult to extract from the device. The key
material is non-exportable. The keystore system is used *instead* of the `KeyChain` API, because
this library does not intend to allow system-wide credential storage, allowing multiple apps to
access the keys. This library is single-app use only.

*You should have a relatively good understanding of RxJava before using this library.*

---

## Instance Creation

```java
RxSecureStorage secureStorage =
    RxSecureStorage.create(this, "alias_name")
```

The create method takes a `Context` and an alias. An alias can encrypt and decrypt data, another
alias cannot encrypt/decrypt the same data successfully.

---

## Encryption

```java
secureStorage
    .encryptString("string to encrypt")
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(result -> {
        // Use the resulting string
    },
    error -> {
        // Handle error
    });
```

You can replace `.encryptString(String)` with `.encrypt(byte[])` also.

It's not recommended, but you can perform thread-blocking encryption also:

```java
String result = secureStorage
    .encryptString("string to encrypt")
    .blockingGet();
```

---

## Decryption

```java
secureStorage
    .decryptString("9yIfhiwf3eDENxI1XG/XWYZOPc5RH6B9ez9y7I7BtEsig==")
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(result -> {
        // Use the resulting string
    },
    error -> {
        // Handle error
    });
```

You can replace `.decryptString(String)` with `.decrypt(byte[])` also.

It's not recommended, but you can perform thread-blocking decryption also:

```java
String result = secureStorage
    .decryptString("9yIfhiwf3eDENxI1XG/XWYZOPc5RH6B9ez9y7I7BtEsig==")
    .blockingGet();
```

---

## Value Persistence

You can save encrypted data in local secure storage:

```java
secureStorage.putString("key", "hello, world!").subscribe();
```

And retrieve it later:

```java
secureStorage
    .getString("key")
    .subscribe(
        latest -> {
          // preference was changed, here's the latest decryped value
        });
```
