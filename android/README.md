# EOSREACH
An open source EOS wallet developed in Kotlin using the [eos-jvm](https://github.com/memtrip/eos-jvm) SDK.

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
- Filter duplicate actions
- Explore accounts without importing a private key
- use https://github.com/ThreeTen/threetenbp rather than Date for times
- pull to refresh on block producer screen
- Add a loading indicator to the action lazy loading
- Add an error indicator to the action lazy loading
- Handle all airdrop token decimal formats, currently only supporting `0.0000`
- stop the lazy loading from triggering multiple network requests
- Implement collapsing header for AccountActivity
- Returning to the vote screen after an error is resetting the form
- All EOS values in the app should be formatted with 0.0000