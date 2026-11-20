-- ============================================================
-- ResumeCraft Database Schema
-- Database: resumecraft_db
-- ============================================================

CREATE DATABASE IF NOT EXISTS resumecraft_db;
USE resumecraft_db;

-- ==================== USERS TABLE ====================
CREATE TABLE users (
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100)  NOT NULL,
    email         VARCHAR(150)  NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    phone         VARCHAR(20),
    address       TEXT,
    linkedin_url  VARCHAR(255),
    github_url    VARCHAR(255),
    portfolio_url VARCHAR(255),
    summary       TEXT,
    profile_photo VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==================== EDUCATION TABLE ====================
CREATE TABLE education (
    education_id  INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT           NOT NULL,
    institution   VARCHAR(200)  NOT NULL,
    degree        VARCHAR(150)  NOT NULL,
    field_of_study VARCHAR(150),
    start_date    DATE          NOT NULL,
    end_date      DATE,
    grade         VARCHAR(20),
    description   TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==================== EXPERIENCE TABLE ====================
CREATE TABLE experience (
    experience_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT           NOT NULL,
    company_name  VARCHAR(200)  NOT NULL,
    job_title     VARCHAR(150)  NOT NULL,
    location      VARCHAR(150),
    start_date    DATE          NOT NULL,
    end_date      DATE,
    is_current    BOOLEAN       DEFAULT FALSE,
    description   TEXT,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==================== SKILLS TABLE ====================
CREATE TABLE skills (
    skill_id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT           NOT NULL,
    skill_name    VARCHAR(100)  NOT NULL,
    proficiency   ENUM('Beginner', 'Intermediate', 'Advanced', 'Expert') DEFAULT 'Intermediate',
    category      ENUM('Technical', 'Soft Skill', 'Language', 'Tool', 'Other') DEFAULT 'Technical',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==================== PROJECTS TABLE ====================
CREATE TABLE projects (
    project_id    INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT           NOT NULL,
    project_name  VARCHAR(200)  NOT NULL,
    description   TEXT,
    technologies  VARCHAR(500),
    project_url   VARCHAR(255),
    start_date    DATE,
    end_date      DATE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==================== CERTIFICATIONS TABLE ====================
CREATE TABLE certifications (
    certification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT           NOT NULL,
    cert_name        VARCHAR(200)  NOT NULL,
    issuing_org      VARCHAR(200)  NOT NULL,
    issue_date       DATE,
    expiry_date      DATE,
    credential_url   VARCHAR(255),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==================== RESUMES TABLE ====================
-- Tracks each resume a user builds (template choice + metadata)
CREATE TABLE resumes (
    resume_id     INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT           NOT NULL,
    resume_title  VARCHAR(200)  NOT NULL DEFAULT 'My Resume',
    template_name VARCHAR(50)   NOT NULL DEFAULT 'classic',
    is_public     BOOLEAN       DEFAULT FALSE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==================== RESUME SECTIONS TABLE ====================
-- Controls which sections are included in a specific resume and their order
CREATE TABLE resume_sections (
    section_id    INT AUTO_INCREMENT PRIMARY KEY,
    resume_id     INT           NOT NULL,
    section_type  ENUM('education', 'experience', 'skills', 'projects', 'certifications', 'summary') NOT NULL,
    section_order INT           NOT NULL DEFAULT 0,
    is_visible    BOOLEAN       DEFAULT TRUE,
    FOREIGN KEY (resume_id) REFERENCES resumes(resume_id) ON DELETE CASCADE
);

-- ==================== INDEXES ====================
CREATE INDEX idx_education_user    ON education(user_id);
CREATE INDEX idx_experience_user   ON experience(user_id);
CREATE INDEX idx_skills_user       ON skills(user_id);
CREATE INDEX idx_projects_user     ON projects(user_id);
CREATE INDEX idx_certifications_user ON certifications(user_id);
CREATE INDEX idx_resumes_user      ON resumes(user_id);
