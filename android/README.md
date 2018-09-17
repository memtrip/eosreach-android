### TODO:
- Create account api 
    - Implement the create account api 
    - Integrate with create account api with the app
    - Integrate the create account api with Google play store items
- airdropped tokens?

- Remove the price from the button in create account if the product has been purchased but not used because of technical error
- Populate the cast vote screen with the existing votes
- stop the lazy loading from triggering multiple network requests
- split the account strings into seperate files
- fix the initial lag on pressing the import private key button
- Implement collapsing header for AccountActivity
- update balances after staking resources

### Clean up
- Ensure all screens support screen rotation correctly 
- Ensure every rx call is handling error, easily verified by unit tests
- sanity check for unused resources and files
- write tests for all the ViewModels
- write integration tests

### Backlog
- implement buy / sell ram
- import key / create account should trigger an account list refresh
- Support all action types
- Explore accounts without importing a private key
- use https://github.com/ThreeTen/threetenbp for time throughout eos-jvm project
- pull to refresh on block producer screen
AccountBundle -> AccountContext
- Add a loading indicator to the action lazy loading
ContractAccountBalance -> AccountContractConteCxt