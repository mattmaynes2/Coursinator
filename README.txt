1. Cameron Blanchard 100886476
   Matthew Maynes
   Kevin Cox

2. SE

3. Mr.Abaza

4. Cameron Blanchard - html/javascript views, course selection and scheduling
   Matthew Maynes - Java client, database design
   Kevin Kox - Database backend functions and prerequisites

5. database/ - Contains the course data, prerequisites, and program patterns to be loaded into the database
   server/ - Contains index.html, install.php, and some style sheets required for views
   server/api - Contains API endpoints
   server/api/lib - Contains db.php for configuration of database username and password
   server/js - Contains Javascript required for Student Views 1 and 2

6.

7. The code does not implement the year status prerequisites.

   Concurrent Courses
   The structure of the prerequisites is as follows: Each course may have one or more "course groups" 
   specified as prerequisites.Each of these course groups may have a "concurrent" flag set, which indicates 
   that they may be taken concurrently. The SQL queries for prerequisites check this flag.
   
