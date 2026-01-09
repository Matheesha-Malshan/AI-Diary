CREATE TABLE user_chat (
       id INT AUTO_INCREMENT PRIMARY KEY,
       user_id INT NOT NULL,
       createDate DATE,
       sentimentScore DOUBLE PRECISION,
       CONSTRAINT fk_user
           FOREIGN KEY (user_id) REFERENCES user(userId)
);
