CREATE TABLE conversations (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE messages (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(255) NOT NULL,
    conversation_id INT,
    from_user_id INT,
    from_user_name VARCHAR(255) NOT NULL,
    text TEXT,
    timestamp TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    FOREIGN KEY (from_user_id) REFERENCES users(id) ON DELETE CASCADE
);
