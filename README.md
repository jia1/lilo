# lilo

Indeed Winter Hackathon 2020 project - Lilo (and stitch the databases)

### About Lilo

Lilo is a POC data architecture for multiple types of and multiple databases.

My team reads from several types of and several data sources to send various engagement emails to employers. Maintaining these multitude of connections in Java is tedious and fragile. Furthermore, read speed is slow.

Our current system of using GraphQL to stitch the schema is good (better than REST definitely!) but it requires my team to write interpreting logic. I want to avoid that.

As such, my approach is a data architecture to index relevant data into an index with an existing query engine for us i.e. Elasticsearch. Job data is expected to update regularly, so the indexing should also allow streaming-in of updates, hence the choice of using Kafka in between the databases and Elasticsearch.

### Requirement

- A daemon which can stream data and updates to data from relational databases to an index via a message queue.

### Concrete goals for this hackathon project

- Lilo daemon can stream data and updates to data from PostgreSQL and MySQL databases to Elasticsearch via Kafka.

### Wishlist

- Support for streaming data and updates to data from IQL (at a lower frequency of course!)
- Implementing clean-up in Elasticsearch (in our case, we do not need job data which are older than 90 days)
- Optimisations for maintainability and scalability with additional tools like Kafka Connect, multi-node Kafka, and multi-node Elasticsearch

### Setup

Setup is a PITA. Apologies for not having thought of containerisation. To run this project, do the following steps:

1. Have the following installed: Java 8, Gradle, PostgreSQL, MySQL, Kafka, Elasticsearch and Postman (Postman is for querying Elasticsearch with ease).
1. Set up a dummy table each in PostgreSQL (`postgres.jobs`) and MySQL (`mysql.employers`) using the script / text files containing the relevant SQL commands.
1. Remember to run the commands from `add_updated_at.txt` for both PostgreSQL and MySQL so that the timestamp for this column is updated when `ON UPDATE`.
1. Start PostgreSQL, MySQL, Kafka and Elasticsearch services.
1. Run `LiloApplication`.

Feel free to keep inserting records into the databases and see them being updated in the index!

Note: The `updated_at` column in the MySQL database does not seem to update when `ON UPDATE`. I probably got my SQL commands wrong...
