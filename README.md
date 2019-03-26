# EOSREACH
An EOS wallet developed in Kotlin using the [eos-jvm](https://github.com/memtrip/eos-jvm) SDK.

## Foreword
The current generation of browser plugin dependant DApps are not fit for mass market consumption,
the average consumer has little interest in the fact that the products they use are running on top
of a blockchain technology. We do not see a future for generic wallet apps as a gateway to Dapps running in web views,
in comparison to native mobile experiences, the UX is slow and cumbersome.

This wallet serves as a blueprint for how other developers might want to utilise [eos-jvm](https://github.com/memtrip/eos-jvm)
to develop native Android apps that consume the EOS blockchain.
[App on Google play](https://play.google.com/store/apps/details?id=com.memtrip.eosreach)

## Design pattern
The app uses the model view intent (MVI) design pattern, a unidirectional data flow driven by a single
Rx PublishSubject.

## Key management
### Import private key
When an EOS private key is imported, the EOS public key counterpart is resolved and a new RSA KeyPair is created in the AndroidKeyStore, the base58 EOS public key is used as the alias of this new RSA KeyPair. The KeyPairs contained in the AndroidKeyStore can only be accessed by the app process that created them.
[Read more about the Android keystore system](https://developer.android.com/training/articles/keystore)

The RSA KeyPair created previously is used to encrypt the EOS private key bytes, the encrypted bytes are stored in the Android shared preferences using the base58 EOS public key as the storage key.
e.g; `sharedPref.edit().putString(eosPublicKeyBase58String, encryptedPrivateKey)`

[See EosKeyManagerImpl for implementation details](https://github.com/memtrip/eosreach/blob/master/android/app/src/main/java/com/memtrip/eosreach/wallet/EosKeyManagerImpl.kt)

### Create account
The memtripissue service is used to create an account on behalf of the user, in this scenario a new EOS key pair is generated on the device and the private key is encrypted using the aforementioned mechanism. The EOS public key counterpart as base58 is sent to the memtripissue service to be assigned as both the owner and active authority of the new account.

### Accounts and public keys
The EOS public key counterparts to any imported private keys are stored in a local SQLite database, along with the accounts associated with each public key.

### Transaction signing
When an EOS private key is required to sign a transaction, the base58 EOS public key counterpart is used to fetch the RSA encrypted private key bytes from the Android shared preferences. The RSA KeyPair used to encrypted the private key was imported using the base58 EOS public key as the alias, as such the public key fetches the RSA KeyPair and uses it to decrypt the private key bytes. At this point the EOS private key can be used to sign the transaction.

### Disabling screen shots
The import key, show private keys and create account activities have the window `FLAG_SECURE` set. This window option disables the ability to take screenshots, blocking any screen scraping malware from viewing sensitive information.
```
window.setFlags(
    WindowManager.LayoutParams.FLAG_SECURE,
    WindowManager.LayoutParams.FLAG_SECURE)
```

## Tests
The ui tests in `androidTestStub` run against the offline stubs defined in the `stub` product flavour,
these are the quickest way to verify the core functionality. The ui tests in `androidTestDev` run against
a local nodeos instance, they are to verify transaction signing and they assume the [eos-jvm dev setup script](https://github.com/memtrip/eos-jvm/tree/master/eos-dev-env)
has been ran.

## Vote for memtripblock
If you find this app useful, please consider voting for [memtripblock](https://www.memtrip.com/code_of_conduct.html)
as a block producer. We are committed to open sourcing all the software we develop, let’s build the future of EOS on mobile together!

## TODO
- Support for hardware generated private keys

### Stability
- Ensure all screens support screen rotation correctly
- Ensure every rx call is handling errors properly, easily verified by unit tests
- sanity check for unused resources and files (lint)
- write unit tests for all the ViewModels
- expand the UI integration tests
- Ensure that all OnError states or repeatable intents include a unique ID (MxViewModel filters
intents by distinctUntilChanged)
- Publish Idle model in a more elegant way and ensure Idle is published after all dialogs

### Backlog
- Import key / create account should trigger an account list refresh
- Support a generic action type in the action list
- use https://github.com/ThreeTen/threetenbp rather than Date for times
- pull to refresh on block producer screen
- Add a loading indicator to the action lazy loading
- Add an error indicator to the action lazy loading
- Handle all airdrop token decimal formats, currently only supporting `0.0000`
- stop the lazy loading from triggering multiple network requests
- All EOS values in the app should be formatted with 0.0000

### Credits
- [Join us on telegram](http://t.me/joinchat/JcIXl0x7wC9cRI5uF_EiQA)
- [Developed by memtrip.com](http://memtrip.com)
