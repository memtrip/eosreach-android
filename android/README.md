### TODO:
- Create account api 
    - Implement the create account api 
    - Integrate with create account api with the app
    - Integrate the create account api with Google play store items
- transaction pagination
- airdropped tokens?
- stake resources

- Add an invalidate shared pref that causes the account list to reload after a transfer transaction occurs
- import key / create account should trigger an account list refresh
- Add a 'purchased' label to the button in create account if the product has been purchased but not used because of technical error
- Populate the cast vote screen with the existing votes

### Clean up
- Ensure every rx call is handling error, easily verified by unit tests
- sanity check for unused resources and files
- write tests for all the ViewModels
- write integration tests

### Backlog
- Support all action types
- Stake resources
- use https://github.com/ThreeTen/threetenbp for time throughout eos-jvm project
- View action screen
- Transaction receipts 
    - Store transaction receipts in a database
    - View a list of transaction receipts in settings
- pull to refresh on block producer screen
AccountBundle -> AccountContext
ContractAccountBalance -> AccountContractContext