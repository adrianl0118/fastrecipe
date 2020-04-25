## About
Lookup recipe URLs based on main ingredient and cook time preferences. Recipe data read into app from .csv file at run time. Recipe URLs stored in a local SQLite DB and pages accessed via hyperlink on Android. Note: at present, the database is only partially populated as the initial intent was just to get the app to function (a goal which has now been fulfilled).

<img src="https://github.com/adrianl0118/fastrecipe/blob/master/docs/Screenshot_20200425-153022.png" alt="" width="200">   <img src="https://github.com/adrianl0118/fastrecipe/blob/master/docs/Screenshot_20200425-153038.png" alt="" width="200">    <img src="https://github.com/adrianl0118/fastrecipe/blob/master/docs/Screenshot_20200425-153133.png" alt="" width="200">    <img src="https://github.com/adrianl0118/fastrecipe/blob/master/docs/Screenshot_20200425-153141.png" alt="" width="200">

<img src="https://github.com/adrianl0118/fastrecipe/blob/master/docs/Screenshot_20200425-153150.png" alt="" width="200">   <img src="https://github.com/adrianl0118/fastrecipe/blob/master/docs/Screenshot_20200425-153249.png" alt="" width="200">

## TODO
- Add recipes to the database
- Modify database reading functions: at current, app reads DB everytime app is created and destroys DB when app is destroyed; change to using a persistent DB
