<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a name="readme-top"></a>




<!-- PROJECT LOGO -->
<br />
<div align="center">


<h2 align="center">RecipeAPI</h2>

  <p align="center">
    Manage your favourite recipes. This API 
allows adding, updating, removing and fetching recipes.
Additionally you are able to filter the recipes.
    <br />
    <a href="https://github.com/github_username/repo_name"><strong>Explore the docs »</strong></a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#running-the-application">Running the application</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->




<!-- GETTING STARTED -->
## Getting Started

The API is running a Spring Boot backend and a PostgreSQL database. Easiest way to run is using docker-compose, but it's also possible to run it standalone (see configuration files in `src\main\resources`  and `src\main\docker`). A prebuilt jar is also included.

The application runs on port 8081, if this port is in use you can change the port by changing the config files in  `src\main\resources` and  `src\main\docker` before running.

### Prerequisites

Make sure you have docker installed

### Running the application

1. Clone this repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. (OPTIONAL) build jar from source:
    ```
    mvn clean package -DskipTests
    ```
3. Run docker-compose from the \docker directory
   ```
   cd src\main\docker
   docker-compose-up
   ```
4. Application should start


### Running tests

1. Clone this repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. (OPTIONAL) build jar from source:
    ```
    mvn test
    ```
3. Tests will run


<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

- After the application has started, Swagger documentation will be available at `http://localhost:8081/swagger-ui/`
- Additional documentation is available in the `/docs`  directory

<p align="right">(<a href="#readme-top">back to top</a>)</p>





