# Running the Server

This is an Apache/PHP application.  It requires no special apache configuration and a default config file is provided.

For the PHP API the only thing that is required is the database connection info.  This info is read from the environment variables shown below.

- `COURSINATOR_DB`:   The DSN describing the database to connect to.
- `COURSINATOR_USER`: The username of the database if required.
- `COURSINATOR_PASS`: The password if required.
