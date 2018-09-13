### TODO:
- Create account api 
    - Implement the create account api 
    - Integrate with create account api with the app
    - Integrate the create account api with Google play store items
- airdropped tokens?
- stake resources

- import key / create account should trigger an account list refresh
- Remove the price from the button in create account if the product has been purchased but not used because of technical error
- Populate the cast vote screen with the existing votes

- add bottom padding to final items in the list

### Clean up
- Ensure all screens support screen rotation correctly 
- Ensure every rx call is handling error, easily verified by unit tests
- sanity check for unused resources and files
- write tests for all the ViewModels
- write integration tests

### Backlog
- Support all action types
- Stake resources
- Explore accounts without importing a private key
- use https://github.com/ThreeTen/threetenbp for time throughout eos-jvm project
- View action screen
- Transaction receipts 
    - Store transaction receipts in a database
    - View a list of transaction receipts in settings
- pull to refresh on block producer screen
AccountBundle -> AccountContext
ContractAccountBalance -> AccountContractContext