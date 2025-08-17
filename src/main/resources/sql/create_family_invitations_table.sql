-- Create family_invitations table
CREATE TABLE IF NOT EXISTS family_invitations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inviter_id BIGINT NOT NULL,
    invitee_id BIGINT NOT NULL,
    family_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    invitation_code VARCHAR(8) NOT NULL UNIQUE,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    responded_at TIMESTAMP NULL,
    
    FOREIGN KEY (inviter_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (invitee_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (family_id) REFERENCES family(id) ON DELETE CASCADE,
    
    INDEX idx_invitee_status (invitee_id, status),
    INDEX idx_family_id (family_id),
    INDEX idx_invitation_code (invitation_code),
    INDEX idx_expires_at (expires_at)
);

-- Add comments to explain the table structure
ALTER TABLE family_invitations COMMENT = 'Table for storing family invitations with status tracking'; 