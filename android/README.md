### TODO:
- pull to refresh on each data screen
    - all three account tabs should trigger a refresh of AccountsActivity
    - all three account tabs should trigger a refresh of AccountsActivity
- Vote for block producer screen
    - a request for the user to vote for memtrip should be at the top
- Create account api 
    - Implement the create account api 
    - Integrate with create account api with the app
    - Integrate the create account api with Google play store items
- airdropped tokens?
- Transaction receipts 
    - Store transaction receipts in a database
    - View a list of transaction receipts in settings
    - View a list of google play account purchases
- Fix the duplicates in the view private key screen
- Add an invalidate shared pref that causes the account list to reload after a transaction occurs

### Clean up
- remove the homeUp finish from mxandroid
- remove the navigation components and args plugin
- Ensure every rx call is handling error
- sanity check for unused resources and files

### Backlog
- Buy resources
- Stake resources
- use https://github.com/ThreeTen/threetenbp for time
- View action screen

AccountBundle -> AccountContext

ContractAccountBalance -> AccountContractContext