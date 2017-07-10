# SPARR
#### (SPA)ced (R)epetition (R)eminder

Spaced repetition is a learning technique that incorporates increasing intervals of time between subsequent review of previously learned material in order to exploit the psychological spacing effect.

##### About SPARR (Still Under Development)

SPARR is an android application to facilitate learning using spaced reminders. This application allows user to enter tasks, and remind him about to review the tasks at spaced intervals. Right now revision interval is set to 1,3,5,10,20 days.
A task added with start date as 10 July 2017 will be repeated on 12 July, 14 July, 19 July, 29 July 2017.
After last repetition task will be removed from list.

##### SPARR Features
1. ###### Implemented
  * Add tasks. Mark task as completed to update the task date to next repetition date
  * Delete tasks
  * Show today's, week's tasks, and all tasks
2. ###### Partially Implemented
  * Add patterns with custom repetition intervals. UI Pending.
3. ###### Planned
  * Reschedule tasks
  * Tags/Labels feature
  * Search feature
  * Settings
  * Task history
  * Google analytics
  * Push notification
4. ###### Wished
  * Learning/completion percentage stats/graphs
  * Login feature
  * Data backup to local/remote

##### Note
Lots of potential bugs. Input text Validation missing. UI to be fixed. Dummy buttons/ stubs present. Missing error handling/logging. Incomplete documentation. Missing icons.

##### Help Required / Contribution
I am looking for someone who can design the icons/ backgrounds. Any help on UI is also welcome.

##### Third party libraries
* [butterknife](https://github.com/JakeWharton/butterknife)
* [material-dialogs](https://github.com/afollestad/material-dialogs)
* [DBFlow](https://github.com/Raizlabs/DBFlow)
* [SectionedRecyclerViewAdapter](https://github.com/luizgrp/SectionedRecyclerViewAdapter)