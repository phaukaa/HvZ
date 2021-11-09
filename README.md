# Humans versus zombies

## Team members
* Patrick Haukaa
* Eirik Joneid
* Jakob Walmann
* Fredrik Arne Rikheim

## Deployment urls
Frontend: https://humans-versus-zombies.herokuapp.com  
Backend : https://humans-versus-zombies-api.herokuapp.com

## Documentation
API Swagger docs: https://humans-versus-zombies-api.herokuapp.com/api-docs/ui  
Frontend user manual: https://humans-versus-zombies.herokuapp.com/assets/HvZ-usermanual.pdf

## How to run locally

### External dependencies
<table>
    <tr>
        <th>Name</th>
        <th>Version</th>
        <th>Link</th>
        <th>Required</th>
    </tr>
    <tr>
        <td>Postgres</td>
        <td>13</td>
        <td>https://www.postgresql.org/</td>
        <td>Yes</td>
    </tr>
    <tr>
        <td>Java JDK</td>
        <td>17</td>
        <td>https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html</td>
        <td>Yes</td>
    </tr>
    <tr>
        <td>Node.js</td>
        <td>14</td>
        <td>https://nodejs.org/en/</td>
        <td>Yes</td>
    </tr>
    <tr>
        <td>NPM</td>
        <td>6 <=</td>
        <td>https://nodejs.org/en/</td>
        <td>Yes</td>
    </tr>
    <tr>
        <td>pgAdmin</td>
        <td>4</td>
        <td>https://www.pgadmin.org/</td>
        <td>No</td>
    </tr>
</table>

### Instuctions

1. Install required external [dependencies](#-external-dependencies).

2. Create a postgres database named `HvZ` by running ```psql -U postgres -c "CREATE DATABASE HvZ"```.  
Alternatively create the database using pgAdmin.

2. Clone the project  
``git clone https://gitlab.com/EirikJoneid/hvz_case.git`` or ``git clone git@gitlab.com:EirikJoneid/hvz_case.git``

3. Open `HvZ/backend` in Intellij, let Intellij install maven dependencies and run the project from the main method in `HvZ/backend/src/main/java/no/noroff/hvz/HvzApplication.java`.

4. cd into `HvZ/frontend`.

5. Install project dependencies by running ```npm install```.

6. Start frontend by running ```npm serve``` or ```ng serve```.

7. Visit frontend at http://localhost:4200/ and backend at http://localhost:8080/.
