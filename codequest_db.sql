CREATE DATABASE IF NOT EXISTS codequest_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE codequest_db;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(40) NOT NULL UNIQUE,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL,
  xp INT NOT NULL DEFAULT 0,
  level INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admins (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(40) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS missions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(120) NOT NULL,
  description TEXT NOT NULL,
  category VARCHAR(30) NOT NULL,
  difficulty ENUM('Easy','Medium','Hard') NOT NULL,
  option_a VARCHAR(255) NOT NULL,
  option_b VARCHAR(255) NOT NULL,
  option_c VARCHAR(255) NOT NULL,
  option_d VARCHAR(255) NOT NULL,
  correct_answer CHAR(1) NOT NULL,
  xp_reward INT NOT NULL DEFAULT 10,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT chk_correct_answer CHECK (correct_answer IN ('A','B','C','D')),
  CONSTRAINT chk_xp_reward CHECK (xp_reward >= 0)
);

CREATE TABLE IF NOT EXISTS mission_submissions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  mission_id BIGINT NOT NULL,
  selected_answer CHAR(1) NOT NULL,
  is_correct BOOLEAN NOT NULL DEFAULT FALSE,
  xp_awarded INT NOT NULL DEFAULT 0,
  submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_submission_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_submission_mission FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE,
  CONSTRAINT uq_one_attempt UNIQUE (user_id, mission_id),
  CONSTRAINT chk_selected_answer CHECK (selected_answer IN ('A','B','C','D'))
);

-- Demo admin: username admin, password ChangeMe123!
-- SHA-256 hash is used by the included coursework demo implementation.
INSERT IGNORE INTO admins(username, password_hash)
VALUES ('admin', SHA2('ChangeMe123!', 256));

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Java: Primitive integer type', 'Which Java primitive type is commonly used to store a whole number?', 'Java', 'Easy',
       'boolean', 'int', 'char', 'float', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Java: Primitive integer type');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'SQL: Read records', 'Which SQL command retrieves rows from a table?', 'SQL', 'Easy',
       'INSERT', 'UPDATE', 'SELECT', 'DELETE', 'C', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='SQL: Read records');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'DSA: FIFO structure', 'Which data structure follows First In, First Out?', 'DSA', 'Easy',
       'Stack', 'Queue', 'Tree', 'Graph', 'B', 15
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='DSA: FIFO structure');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Python: list indexing', 'What is the index of the first element in a Python list?', 'Python', 'Easy',
       '1', '0', '-1', 'None', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Python: list indexing');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Java: method entry', 'Which method is the usual entry point of a Java application?', 'Java', 'Easy',
       'start()', 'run()', 'main()', 'init()', 'C', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Java: method entry');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'HTML: hyperlink', 'Which HTML element creates a hyperlink?', 'Web', 'Easy',
       '<link>', '<a>', '<href>', '<url>', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='HTML: hyperlink');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'CSS: text color', 'Which CSS property changes text color?', 'Web', 'Easy',
       'font-style', 'background', 'color', 'text-decoration', 'C', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='CSS: text color');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'JavaScript: strict equality', 'Which operator checks equality without type conversion?', 'JavaScript', 'Medium',
       '==', '===', '=', '!=', 'B', 15
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='JavaScript: strict equality');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'SQL: filter rows', 'Which SQL clause filters rows before grouping?', 'SQL', 'Medium',
       'ORDER BY', 'WHERE', 'HAVING', 'LIMIT', 'B', 15
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='SQL: filter rows');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'DSA: binary search', 'What is binary search's time complexity on a sorted array?', 'DSA', 'Medium',
       'O(n)', 'O(log n)', 'O(n log n)', 'O(1)', 'B', 15
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='DSA: binary search');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'OOP: inheritance', 'Which OOP concept lets a class acquire features from another class?', 'OOP', 'Easy',
       'Encapsulation', 'Inheritance', 'Abstraction', 'Polymorphism', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='OOP: inheritance');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Networks: HTTPS', 'Which protocol secures HTTP using TLS?', 'Networking', 'Easy',
       'FTP', 'HTTPS', 'SMTP', 'DHCP', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Networks: HTTPS');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Git: commit', 'Which Git command records staged changes?', 'Tools', 'Easy',
       'git push', 'git add', 'git commit', 'git pull', 'C', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Git: commit');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Java: exception handling', 'Which block handles an exception in Java?', 'Java', 'Medium',
       'catch', 'throw', 'finally', 'try', 'A', 15
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Java: exception handling');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'SQL: primary key', 'What does a primary key ensure in a table?', 'SQL', 'Easy',
       'Duplicate rows', 'Unique row identification', 'Sorted rows', 'Encrypted values', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='SQL: primary key');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Python: dictionary', 'Which syntax creates an empty Python dictionary?', 'Python', 'Easy',
       '[]', '()', '{}', 'set()', 'C', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Python: dictionary');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'DSA: stack operation', 'Which stack operation removes the top item?', 'DSA', 'Easy',
       'push', 'pop', 'peek', 'enqueue', 'B', 10
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='DSA: stack operation');

INSERT INTO missions
(title, description, category, difficulty, option_a, option_b, option_c, option_d, correct_answer, xp_reward)
SELECT 'Web: responsive design', 'Which CSS feature commonly adapts layout to screen width?', 'Web', 'Medium',
       '@keyframes', '@media', '@font-face', '@import', 'B', 15
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Web: responsive design');
