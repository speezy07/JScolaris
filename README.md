# JScolaris

JScolaris is a JavaFX desktop app for a school's academic records. It was built as a third-year computer science project and uses MySQL to store students, teachers, modules, subjects, and grades.

## What it does

- View record counts from a dashboard and manage students, teachers, modules, and subjects.
- Record grades out of 20 and calculate each student's weighted average.
- Import student records from CSV or Excel (`.xlsx`) and review invalid or duplicate entries.
- Export PDF report cards with subject grades, coefficients, and the weighted average.

## Run locally

You’ll need JDK 17 or newer, Maven, and a local MySQL server. JavaFX and the other Java dependencies are declared in `pom.xml`.

1. From the project root, open the MySQL client with an administrator account using `mysql -u root -p`. Run these commands in that client. The sample data is optional. Replace `<choose-a-local-password>` with your own password.

   ```sql
   SOURCE database/schema.sql;
   SOURCE database/sample_data.sql;
   CREATE USER 'jscolaris_app'@'localhost' IDENTIFIED BY '<choose-a-local-password>';
   GRANT SELECT, INSERT, UPDATE, DELETE ON jscolaris.* TO 'jscolaris_app'@'localhost';
   ```

2. Open a terminal from the project root and run the app with the new database account.

   **PowerShell 7.2+**

   ```powershell
   $env:JSCOLARIS_DB_USER = 'jscolaris_app'
   $env:JSCOLARIS_DB_PASSWORD = Read-Host 'MySQL app password' -MaskInput
   mvn javafx:run
   ```

   **Bash**

   ```bash
   export JSCOLARIS_DB_USER=jscolaris_app
   read -rs -p 'MySQL app password: ' JSCOLARIS_DB_PASSWORD
   printf '\n'
   export JSCOLARIS_DB_PASSWORD
   mvn javafx:run
   ```

   The default database address is `localhost:3306/jscolaris`. Set `JSCOLARIS_DB_URL` to your JDBC URL if your local setup differs. Existing installations can still use an untracked `src/main/resources/database.properties` file with `db.url`, `db.user`, and `db.password`. Environment variables take precedence.

To build the project without opening the UI, run `mvn verify`.

## Try with demo data

1. Open **Grades** and select Yasmine Benali. Her two seeded grades are 16.50 and 14.75. The app weights each grade by the subject coefficient multiplied by its module coefficient.
2. Open **Import** and select [`sample-data/students.csv`](sample-data/students.csv). The same five column headers are used for Excel imports.
3. Open **Export PDF**, select a student with grades, and choose where to save their PDF report card.

## How it’s organized

| Path | Role |
| --- | --- |
| `src/main/java/view` and `controller` | JavaFX screens and user workflows |
| `src/main/java/model` | Student, teacher, module, subject, and grade objects |
| `src/main/java/dao` and `database` | JDBC queries and connection settings |
| `src/main/java/util` | Validation, CSV and Excel import, and PDF export |
| `database/` | MySQL schema and optional demo records |

The schema keeps emails unique, links each subject to a module, and allows one grade per student and subject. The UI is built in Java with a shared CSS stylesheet.
