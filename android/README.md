### Stability
- Ensure all screens support screen rotation correctly 
- Ensure every rx call is handling errors properly, easily verified by unit tests
- sanity check for unused resources and files (lint)
- write unit tests for all the ViewModels
- write UI integration tests
- Ensure that all OnError states include a unique ID (required by mx for repated states)
- Ensure Idle model is published after a dialog is shown to stop the dialog being shown on return 

### Backlog
- Implement buy / sell ram
- Import key / create account should trigger an account list refresh
- Support a generic action type in the action list
- Explore accounts without importing a private key
- use https://github.com/ThreeTen/threetenbp rather than Date for times
- pull to refresh on block producer screen
- Add a loading indicator to the action lazy loading
- Add an error indicator to the action lazy loading
- Handle all airdrop token decimal formats, currently only supporting `0.0000`
- stop the lazy loading from triggering multiple network requests
- fix the initial jank on pressing the import private key button
- Implement collapsing header for AccountActivity
- Move the settings menu from import key to the welcome activity
- update balances after staking resources
- Returning to the vote screen after an error is resetting the form
- All EOS values in the app should be formatted with 0.0000 