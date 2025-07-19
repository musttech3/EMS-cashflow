package com.alphasoft.EMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class InvitationCleanupService {

    private final FamilyInvitationService familyInvitationService;

    @Autowired
    public InvitationCleanupService(FamilyInvitationService familyInvitationService) {
        this.familyInvitationService = familyInvitationService;
    }

    /**
     * تنظيف الدعوات المنتهية الصلاحية كل ساعة
     * يتم تشغيل هذه المهمة تلقائياً كل ساعة
     */
    @Scheduled(fixedRate = 3600000) // 3600000 ms = 1 hour
    public void cleanupExpiredInvitations() {
        try {
            familyInvitationService.cleanupExpiredInvitations();
            System.out.println("Expired invitations cleanup completed successfully");
        } catch (Exception e) {
            System.err.println("Error during expired invitations cleanup: " + e.getMessage());
        }
    }
} 