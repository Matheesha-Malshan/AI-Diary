CREATE TABLE user_voice (
               id INT AUTO_INCREMENT PRIMARY KEY,
               user_id INT NOT NULL,
               duration DOUBLE PRECISION,
               fileSize DOUBLE PRECISION,
               format VARCHAR(50),
               CONSTRAINT fk_user_voice
                   FOREIGN KEY (user_id) REFERENCES user(userId)
);
