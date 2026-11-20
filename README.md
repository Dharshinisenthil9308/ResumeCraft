# 🎯 ResumeCraft — Smart Resume Builder for Students

A full-stack web application that helps college students build professional resumes using customisable templates.

## Tech Stack

| Layer        | Technology                    |
|-------------|-------------------------------|
| Language     | Java 17                       |
| Web          | JSP, Java Servlets, HTML/CSS/JS |
| Database     | MySQL 8                       |
| ORM / Data   | JDBC (DAO pattern)            |
| Build        | Maven                         |
| Server       | Apache Tomcat 9               |
| PDF          | iText 7                       |
| Auth         | BCrypt                        |
| Container    | Docker (multi-stage)          |
| Hosting      | Railway                       |

## Development Workflow

> **No Java/Maven needed locally.** All building and running happens inside Docker or on Railway.

1. Edit source files in Antigravity
2. Push to GitHub
3. Railway auto-builds using the Dockerfile
4. MySQL is provisioned as a Railway service

## Project Structure

```
ResumeCraft/
├── pom.xml                          # Maven build config
├── Dockerfile                       # Multi-stage Docker build
├── .gitignore
├── README.md
├── sql/
│   └── schema.sql                   # Database schema
├── docker/
│   └── docker-compose.yml           # Local Docker testing
└── src/main/
    ├── java/com/resumecraft/
    │   ├── model/                   # POJOs (User, Education, etc.)
    │   ├── dao/                     # Data Access Objects (JDBC)
    │   ├── servlet/                 # HTTP Servlets (Controllers)
    │   └── util/                    # Utilities (DBConnection)
    ├── resources/
    └── webapp/
        ├── index.jsp                # Landing page
        ├── css/                     # Stylesheets
        ├── js/                      # JavaScript
        ├── images/
        ├── WEB-INF/
        │   └── web.xml              # Servlet mappings
        └── jsp/
            ├── auth/                # login.jsp, register.jsp
            ├── dashboard/           # dashboard.jsp
            ├── resume/              # builder.jsp, preview.jsp
            ├── templates/           # classic.jsp, modern.jsp, minimal.jsp
            └── common/              # header.jsp, footer.jsp, error pages
```

## Deployment to Railway

1. Create a GitHub repository and push this project
2. Create a new project on [Railway](https://railway.app)
3. Add a **MySQL** service → copy the connection variables
4. Add a **GitHub Repo** service → select your repo
5. Set environment variables on the app service:
   - `MYSQL_URL` = `jdbc:mysql://<host>:<port>/resumecraft_db?useSSL=false&allowPublicKeyRetrieval=true`
   - `MYSQL_USER` = (from Railway MySQL)
   - `MYSQL_PASSWORD` = (from Railway MySQL)
6. Railway auto-detects the Dockerfile and deploys

## License

This project is for educational purposes.
